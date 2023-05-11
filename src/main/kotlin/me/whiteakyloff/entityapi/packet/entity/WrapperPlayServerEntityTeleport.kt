package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent

import org.bukkit.World
import org.bukkit.entity.Entity

class WrapperPlayServerEntityTeleport : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
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
    var onGround: Boolean
        get() = handle.booleans.read(0)
        set(value) {
            handle.booleans.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.ENTITY_TELEPORT
    }
}