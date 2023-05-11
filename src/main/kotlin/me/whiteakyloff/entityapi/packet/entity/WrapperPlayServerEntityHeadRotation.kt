package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent

import org.bukkit.World
import org.bukkit.entity.Entity

class WrapperPlayServerEntityHeadRotation : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var headYaw: Byte
        get() = handle.bytes.read(0)
        set(value) {
            handle.bytes.write(0, value)
        }
    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.ENTITY_HEAD_ROTATION
    }
}