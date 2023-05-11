package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.WrappedWatchableObject

import org.bukkit.World
import org.bukkit.entity.Entity

class WrapperPlayServerEntityMetadata : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var metadata: List<WrappedWatchableObject>
        get() = handle.watchableCollectionModifier.read(0)
        set(value) {
            handle.watchableCollectionModifier.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.ENTITY_METADATA
    }
}