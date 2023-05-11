package me.whiteakyloff.entityapi.entity.equipment

import me.whiteakyloff.entityapi.entity.FakeEntity
import me.whiteakyloff.entityapi.packet.ProtocolPacketFactory

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

import com.comphenix.protocol.wrappers.EnumWrappers

import java.util.EnumMap

class FakeEntityEquipment(private val fakeEntity: FakeEntity)
{
    private val equipmentMap = EnumMap<EnumWrappers.ItemSlot, ItemStack>(EnumWrappers.ItemSlot::class.java)

    fun getEquipment(itemSlot: EnumWrappers.ItemSlot): ItemStack? {
        return this.equipmentMap[itemSlot]
    }

    fun setEquipment(itemSlot: EnumWrappers.ItemSlot, itemStack: ItemStack) {
        this.equipmentMap[itemSlot] = itemStack
        this.fakeEntity.receivers.forEach { receiver ->
            sendEquipmentPacket(itemSlot, itemStack, receiver)
        }
    }

    fun updateEquipmentPacket(player: Player) {
        this.equipmentMap.forEach { (itemSlot, itemStack) ->
            sendEquipmentPacket(itemSlot, itemStack, player)
        }
    }

    private fun sendEquipmentPacket(itemSlot: EnumWrappers.ItemSlot, itemStack: ItemStack, player: Player) {
        ProtocolPacketFactory.createEntityEquipmentPacket(this.fakeEntity.entityId, itemStack, itemSlot).sendPacket(player)
    }
}
