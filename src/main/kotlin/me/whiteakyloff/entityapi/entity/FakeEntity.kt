package me.whiteakyloff.entityapi.entity

import me.whiteakyloff.entityapi.packet.ProtocolPacketFactory
import me.whiteakyloff.entityapi.entity.equipment.FakeEntityEquipment

import com.comphenix.protocol.reflect.accessors.Accessors
import com.comphenix.protocol.reflect.accessors.FieldAccessor
import com.comphenix.protocol.utility.MinecraftProtocolVersion
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.Vector3F
import com.comphenix.protocol.wrappers.WrappedChatComponent
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.util.Vector

import java.util.concurrent.atomic.AtomicInteger

abstract class FakeEntity(val entityType: EntityType, var location: Location)
{
    val receivers: MutableList<Player>
    val dataWatcher: WrappedDataWatcher

    open lateinit var entityEquipment: FakeEntityEquipment

    val entityId: Int = if (ENTITY_ID.get(null) !is AtomicInteger) {
        ENTITY_ID.get(null) as Int
    } else {
        (ENTITY_ID.get(null) as AtomicInteger).incrementAndGet()
    }

    init {
        this.initialize()

        this.receivers = ArrayList()
        this.dataWatcher = WrappedDataWatcher()
    }

    open var public: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            if (!value) { this.receivers.forEach { this.removeReceiver(it) } }
        }
    open var sneaking: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var sprinting: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var glowing: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var burning: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var invisible: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var silent: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var noGravity: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var elytraFlying: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(0, BYTE_SERIALIZER, this.generateBitMask())
        }
    open var customName: String? = null
        set(value) {
            if (field != null && field == value) {
                return
            }
            field = value
            val currentVersion = MinecraftProtocolVersion.getCurrentVersion()
            val aquaticVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.AQUATIC_UPDATE)
            if (currentVersion < aquaticVersion) {
                this.sendDataWatcherObject(2, STRING_SERIALIZER, value)
            } else {
                this.sendDataWatcherObject(2, CHAT_COMPONENT_SERIALIZER, WrappedChatComponent.fromText(value))
            }
        }
    open var customNameVisible: Boolean = false
        set(value) {
            if (field == value) {
                return
            }
            field = value
            this.sendDataWatcherObject(3, BOOLEAN_SERIALIZER, value)
        }

    open fun look(location: Location) {
        this.receivers.forEach { this.look(it, location) }
    }

    open fun look(yaw: Float, pitch: Float) {
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

    open fun look(player: Player?, location: Location) {
        this.location.apply {
            this.yaw = location.yaw
            this.pitch = location.pitch
            this.direction = location.clone().subtract(location).toVector().normalize()
        }
        ProtocolPacketFactory.createEntityLookPacket(this.entityId, this.location).sendPacket(player)
        ProtocolPacketFactory.createEntityHeadRotationPacket(this.entityId, this.location).sendPacket(player)
    }

    open fun teleport(location: Location) {
        this.location = location

        this.receivers.forEach {
            ProtocolPacketFactory.createEntityTeleportPacket(this.entityId, this.location).sendPacket(it)
        }
    }

    open fun setVelocity(vector: Vector?) {
        this.receivers.forEach { this.setVelocity(it, vector) }
    }

    open fun setVelocity(player: Player?, vector: Vector?) {
        ProtocolPacketFactory.createEntityVelocityPacket(entityId, vector!!).sendPacket(player)
    }

    open fun addReceiver(player: Player?) {
        this.receivers.add(player!!)

        this.sendSpawnPacket(player)
        this.entityEquipment.updateEquipmentPacket(player)
    }

    open fun hasReceiver(player: Player?): Boolean {
        return this.receivers.contains(player)
    }

    open fun removeReceiver(player: Player?) {
        this.receivers.remove(player)

        this.sendDestroyPacket(player)
    }

    open fun setPassengers(entityIds: IntArray) {
        this.receivers.forEach { this.setPassengers(it, entityIds) }
    }

    open fun setPassengers(vararg fakeEntities: FakeEntity?) {
        this.setPassengers(fakeEntities.map { it!!.entityId }.toIntArray())
    }

    open fun setPassengers(player: Player?, entityIds: IntArray) {
        ProtocolPacketFactory.createMountPacket(this.entityId, entityIds).sendPacket(player)
    }

    open fun sendDataWatcherPacket() {
        this.receivers.forEach { ProtocolPacketFactory.createEntityMetadataPacket(entityId, dataWatcher).sendPacket(it) }
    }

    open fun sendDataWatcherObject(dataWatcherIndex: Int, serializer: WrappedDataWatcher.Serializer?, value: Any?) {
        WrappedDataWatcherObject(dataWatcherIndex, serializer).apply {
            dataWatcher.setObject(this, value)
        }.also { this.sendDataWatcherPacket() }
    }

    protected open fun getSpawnTypeId(): Int = 0

    protected open fun initialize() {
        if (ENTITY_ID.get(null) !is AtomicInteger) {
            ENTITY_ID.set(null, entityId + 1)
        }
        ENTITIES.add(this)

        this.entityEquipment = FakeEntityEquipment(this)
    }

    protected open fun sendDestroyPacket(player: Player?) {
        ProtocolPacketFactory.createDestroyEntityPacket(entityId).sendPacket(player)
    }

    protected open fun sendSpawnPacket(player: Player?) {
        if (!this.entityType.isAlive) {
            ProtocolPacketFactory.createSpawnEntityPacket(entityId, this.getSpawnTypeId(), this.location, this.entityType).sendPacket(player)
        }
        else {
            ProtocolPacketFactory.createSpawnLivingEntityPacket(entityId, location, entityType, dataWatcher).sendPacket(player)
        }
        this.sendDataWatcherPacket()
    }

    private fun generateBitMask(): Byte {
        var bitMask: Byte = if (burning) 0x01 else 0x00

        bitMask = bitMask.plus(if (sneaking) 0x02 else 0x00).toByte()
        bitMask = bitMask.plus(if (sprinting) 0x08 else 0x00).toByte()
        bitMask = bitMask.plus(if (invisible) 0x20 else 0x00).toByte()
        bitMask = bitMask.plus(if (glowing) 0x40 else 0x00).toByte()
        bitMask = bitMask.plus(if (elytraFlying) 0x80 else 0x00).toByte()
        return bitMask
    }

    companion object {
        val ENTITIES: MutableList<FakeEntity> = ArrayList()
        private val ENTITY_ID: FieldAccessor = Accessors.getFieldAccessor(
            MinecraftReflection.getEntityClass(), "entityCount", true
        )

        fun getEntityById(id: Int): FakeEntity? {
            return ENTITIES.firstOrNull { it.entityId == id }
        }

        val BYTE_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Byte::class.java)
        val STRING_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.String::class.java)
        val INT_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Integer::class.java)
        val FLOAT_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Float::class.java)
        val BOOLEAN_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(java.lang.Boolean::class.java)
        val CHAT_COMPONENT_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.getChatComponentSerializer()
        val ROTATION_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(Vector3F.getMinecraftClass())
        val ITEMSTACK_SERIALIZER: WrappedDataWatcher.Serializer = WrappedDataWatcher.Registry.get(MinecraftReflection.getItemStackClass())
    }
}