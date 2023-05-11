package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent

import org.bukkit.World
import org.bukkit.entity.Entity

class WrapperPlayServerEntityVelocity : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var velocityX: Double
        get() = handle.integers.read(1) / 8000.0
        set(value) {
            handle.integers.write(1, (value * 8000.0).toInt())
        }
    var velocityY: Double
        get() = handle.integers.read(2) / 8000.0
        set(value) {
            handle.integers.write(2, (value * 8000.0).toInt())
        }
    var velocityZ: Double
        get() = handle.integers.read(3) / 8000.0
        set(value) {
            handle.integers.write(3, (value * 8000.0).toInt())
        }


    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.ENTITY_VELOCITY
    }
}