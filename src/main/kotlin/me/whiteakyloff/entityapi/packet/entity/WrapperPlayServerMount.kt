package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent

import org.bukkit.World
import org.bukkit.entity.Entity

class WrapperPlayServerMount : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var passengerIDs: IntArray?
        get() = handle.integerArrays.read(0)
        set(value) {
            handle.integerArrays.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return getEntity(event.player.world)
    }

    fun getPassengers(event: PacketEvent): List<Entity> {
        return this.getPassengers(event.player.world)
    }

    fun setPassengers(value: List<Entity>) {
        passengerIDs = value.map { it.entityId }.toIntArray()
    }

    private fun getPassengers(world: World?): List<Entity> {
        val ids = passengerIDs ?: return emptyList()
        val protocolManager = ProtocolLibrary.getProtocolManager()

        return ids.map { id ->
            protocolManager.getEntityFromID(world, id)
        }
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.MOUNT
    }
}