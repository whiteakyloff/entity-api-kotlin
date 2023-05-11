package me.whiteakyloff.entityapi.packet.entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.injector.PacketConstructor
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import me.whiteakyloff.entityapi.packet.AbstractPacket
import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import java.util.*

class WrapperPlayServerSpawnLivingEntity : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var uniqueId: UUID?
        get() = handle.uuiDs.read(0)
        set(value) {
            handle.uuiDs.write(0, value)
        }
    var entityType: EntityType?
        get() = EntityType.fromId(handle.integers.read(1))
        set(value) {
            handle.integers.write(1, value!!.typeId.toInt())
        }
    var x: Double
        get() = handle.doubles.read(0)
        set(value) {
            handle.doubles.write(0, value)
        }
    var y: Double
        get() = handle.doubles.read(1)
        set(value) {
            handle.doubles.write(1, value)
        }
    var z: Double
        get() = handle.doubles.read(2)
        set(value) {
            handle.doubles.write(2, value)
        }
    var yaw: Float
        get() = handle.bytes.read(0) * 360f / 256.0f
        set(value) {
            handle.bytes.write(0, (value * 256.0f / 360.0f).toInt().toByte())
        }
    var pitch: Float
        get() = handle.bytes.read(1) * 360f / 256.0f
        set(value) {
            handle.bytes.write(1, (value * 256.0f / 360.0f).toInt().toByte())
        }
    var headPitch: Float
        get() = handle.bytes.read(2) * 360f / 256.0f
        set(value) {
            handle.bytes.write(2, (value * 256.0f / 360.0f).toInt().toByte())
        }
    var velocityX: Double
        get() = handle.integers.read(2) / 8000.0
        set(value) {
            handle.integers.write(2, (value * 8000.0).toInt())
        }
    var velocityY: Double
        get() = handle.integers.read(3) / 8000.0
        set(value) {
            handle.integers.write(3, (value * 8000.0).toInt())
        }
    var velocityZ: Double
        get() = handle.integers.read(4) / 8000.0
        set(value) {
            handle.integers.write(4, (value * 8000.0).toInt())
        }
    var metadata: WrappedDataWatcher?
        get() = handle.dataWatcherModifier.read(0)
        set(value) {
            handle.dataWatcherModifier.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    companion object {
        var TYPE : PacketType = PacketType.Play.Server.SPAWN_ENTITY_LIVING

        private var entityConstructor: PacketConstructor? = null

        private fun fromEntity(entity: Entity): PacketContainer {
            if (entityConstructor == null) {
                entityConstructor = ProtocolLibrary.getProtocolManager()
                    .createPacketConstructor(TYPE, entity)
            }
            return entityConstructor!!.createPacket(entity)
        }
    }
}