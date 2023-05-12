package me.whiteakyloff.entityapi.entity

import me.whiteakyloff.entityapi.entity.equipment.FakeEntityEquipment
import me.whiteakyloff.entityapi.packet.ProtocolPacketFactory
import me.whiteakyloff.entityapi.entity.listener.FakeEntityListener

import com.comphenix.protocol.reflect.accessors.Accessors
import com.comphenix.protocol.reflect.accessors.FieldAccessor
import com.comphenix.protocol.utility.MinecraftProtocolVersion
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.Vector3F
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.Consumer
import org.bukkit.util.Vector

import java.util.concurrent.atomic.AtomicInteger

abstract class FakeEntity(entityType: EntityType, location: Location)
{
    val receivers: MutableList<Player>

    val dataWatcher: WrappedDataWatcher
    private val entityEquipment: FakeEntityEquipment

    var location: Location

    private val entityType: EntityType

    private var customName: String? = null

    private var glowingColor: ChatColor? = null

    var clickAction: Consumer<Player>? = null

    private var silent = false
    private var burning = false
    private var sneaking = false
    private var sprinting = false
    private var invisible = false
    private var noGravity = false
    private var elytraFlying = false
    private var customNameVisible = false

    val entityId: Int = if (ENTITY_ID.get(null) is AtomicInteger) {
        (ENTITY_ID.get(null) as AtomicInteger).incrementAndGet()
    } else {
        ENTITY_ID.get(null) as Int
    }

    init {
        this.location = location
        this.entityType = entityType

        if (LISTENER == null) {
            LISTENER = FakeEntityListener(JavaPlugin.getProvidingPlugin(this::class.java))
        }
        if (ENTITY_ID.get(null) !is AtomicInteger) {
            ENTITY_ID.set(null, entityId + 1)
        }
        ENTITIES.add(this)

        this.receivers = ArrayList()
        this.dataWatcher = WrappedDataWatcher()
        this.entityEquipment = FakeEntityEquipment(this)
    }

    fun look(location: Location) {
        this.receivers.forEach { this.look(it, location) }
    }

    fun look(yaw: Float, pitch: Float) {
        this.receivers.forEach { this.look(it, yaw, pitch) }
    }

    open fun look(player: Player?, yaw: Float, pitch: Float) {
        this.location.apply {
            this.yaw = yaw
            this.pitch = pitch
        }
        ProtocolPacketFactory.createEntityLookPacket(this.entityId, this.location).sendPacket(player)
        ProtocolPacketFactory.createEntityHeadRotationPacket(this.entityId, this.location).sendPacket(player)
    }

    open fun look(player: Player?, location: Location?) {
        this.location.apply {
            this.yaw = location!!.yaw
            this.pitch = location.pitch
            this.direction = location.clone().subtract(location).toVector().normalize()
        }
        ProtocolPacketFactory.createEntityLookPacket(this.entityId, this.location).sendPacket(player)
        ProtocolPacketFactory.createEntityHeadRotationPacket(this.entityId, this.location).sendPacket(player)
    }

    fun teleport(location: Location) {
        this.location = location

        this.receivers.forEach {
            ProtocolPacketFactory.createEntityTeleportPacket(this.entityId, this.location).sendPacket(it)
        }
    }

    fun setVelocity(vector: Vector?) {
        this.receivers.forEach { this.setVelocity(it, vector) }
    }

    open fun setVelocity(player: Player?, vector: Vector?) {
        ProtocolPacketFactory.createEntityVelocityPacket(entityId, vector!!).sendPacket(player)
    }

    fun setSneaking(sneaking: Boolean) {
        if (this.sneaking == sneaking) {
            return
        }
        this.sneaking = sneaking
        this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
    }

    fun setSprinting(sprinting: Boolean) {
        if (this.sprinting == sprinting) {
            return
        }
        this.sprinting = sprinting
        this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
    }

    fun setBurning(burning: Boolean) {
        if (this.burning == burning) {
            return
        }
        this.burning = burning
        this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
    }

    fun setInvisible(invisible: Boolean) {
        if (this.invisible == invisible) {
            return
        }
        this.invisible = invisible
        this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
    }

    fun setSilent(silent: Boolean) {
        if (this.silent == silent) {
            return
        }
        this.silent = silent
        this.sendDataWatcherObject(4, BOOLEAN_SERIALIZER, this.silent)
    }

    fun setNoGravity(noGravity: Boolean) {
        if (this.noGravity == noGravity) {
            return
        }
        this.noGravity = noGravity
        this.sendDataWatcherObject(5, BOOLEAN_SERIALIZER, this.noGravity)
    }

    fun setElytraFlying(elytraFlying: Boolean) {
        if (this.elytraFlying == elytraFlying) {
            return
        }
        this.elytraFlying = elytraFlying
        this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
    }

    fun getGlowingColor() : ChatColor? {
        return this.glowingColor
    }

    protected open fun setGlowingColor(chatColor: ChatColor) {
        if (this.glowingColor == chatColor) {
            return
        }
        this.glowingColor = chatColor
        this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
    }

    fun setCustomNameVisible(customNameVisible: Boolean) {
        if (this.customNameVisible == customNameVisible) {
            return
        }
        this.customNameVisible = customNameVisible
        this.sendDataWatcherObject(3, BOOLEAN_SERIALIZER, this.customNameVisible)
    }

    fun setCustomName(customName: String) {
        if (this.customName != null && this.customName == customName) {
            return
        }
        this.customName = customName
        val currentVersion = MinecraftProtocolVersion.getCurrentVersion()
        val aquaticVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.AQUATIC_UPDATE)
        if (currentVersion < aquaticVersion) {
            this.sendDataWatcherObject(2, STRING_SERIALIZER, this.customName)
        } else {
            this.sendDataWatcherObject(2, CHAT_COMPONENT_SERIALIZER, WrappedChatComponent.fromText(this.customName))
        }
    }

    fun addReceiver(player: Player?) {
        this.receivers.add(player!!)

        this.sendSpawnPacket(player)
        this.entityEquipment.updateEquipmentPacket(player)
    }

    fun hasReceiver(player: Player?): Boolean {
        return this.receivers.contains(player)
    }

    fun removeReceiver(player: Player?) {
        this.receivers.remove(player)

        this.sendDestroyPacket(player)
    }

    open fun setPassengers(entityIds: IntArray) {
        this.receivers.forEach { this.setPassengers(it, entityIds) }
    }

    fun setPassengers(vararg fakeEntities: FakeEntity?) {
        this.setPassengers(fakeEntities.map { it!!.entityId }.toIntArray())
    }

    open fun setPassengers(player: Player?, entityIds: IntArray) {
        ProtocolPacketFactory.createMountPacket(this.entityId, entityIds).sendPacket(player)
    }

    fun sendDataWatcherPacket() {
        this.receivers.forEach { ProtocolPacketFactory.createEntityMetadataPacket(entityId, dataWatcher).sendPacket(it) }
    }

    fun sendDataWatcherObject(dataWatcherIndex: Int, serializer: WrappedDataWatcher.Serializer?, value: Any?) {
        WrappedDataWatcherObject(dataWatcherIndex, serializer).apply {
            dataWatcher.setObject(this, value)
        }.also { this.sendDataWatcherPacket() }
    }

    protected open fun sendDestroyPacket(player: Player?) {
        ProtocolPacketFactory.createDestroyEntityPacket(entityId).sendPacket(player)
    }

    protected open fun sendSpawnPacket(player: Player?) {
        if (!this.entityType.isAlive) {
            ProtocolPacketFactory.createSpawnEntityPacket(this.entityId, this.getSpawnTypeId(), this.location, this.entityType).sendPacket(player)
        }
        else {
            ProtocolPacketFactory.createSpawnLivingEntityPacket(this.entityId, this.location, this.entityType, this.dataWatcher).sendPacket(player)
        }
        this.sendDataWatcherPacket()
    }

    private fun generateBitMask(): Byte {
        var bitMask: Byte = if (burning) 0x01 else 0x00

        bitMask = bitMask.plus(if (sneaking) 0x02 else 0x00).toByte()
        bitMask = bitMask.plus(if (sprinting) 0x08 else 0x00).toByte()
        bitMask = bitMask.plus(if (invisible) 0x20 else 0x00).toByte()
        bitMask = bitMask.plus(if (glowingColor != null) 0x40 else 0x00).toByte()
        bitMask = bitMask.plus(if (elytraFlying) 0x80 else 0x00).toByte()
        return bitMask
    }

    companion object {
        private val ENTITIES: MutableList<FakeEntity> = ArrayList()
        private val ENTITY_ID: FieldAccessor = Accessors.getFieldAccessor(
            MinecraftReflection.getEntityClass(), "entityCount", true
        )

        fun getEntityById(id: Int): FakeEntity? {
            return ENTITIES.firstOrNull { it.entityId == id }
        }

        var LISTENER: FakeEntityListener? = null
        val BYTE_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Byte::class.java)
        val STRING_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.String::class.java)
        val INT_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Integer::class.java)
        val FLOAT_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Float::class.java)
        val BOOLEAN_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Boolean::class.java)
        val CHAT_COMPONENT_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.getChatComponentSerializer()
        val ROTATION_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(Vector3F.getMinecraftClass())
        val ITEMSTACK_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(MinecraftReflection.getItemStackClass())
    }

    protected open fun getSpawnTypeId(): Int = 0
}