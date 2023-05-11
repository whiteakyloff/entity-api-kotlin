package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot

import org.bukkit.World
import org.bukkit.entity.Entity
import org.bukkit.inventory.ItemStack

class WrapperPlayServerEntityEquipment : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var itemSlot: ItemSlot?
        get() = handle.itemSlots.read(0)
        set(value) {
            handle.itemSlots.write(0, value)
        }
    var itemStack: ItemStack?
        get() = handle.itemModifier.read(0)
        set(value) {
            handle.itemModifier.write(0, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.ENTITY_EQUIPMENT
    }
}