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

    fun setEquipment(itemSlot: EnumWrappers.ItemSlot, itemStack: ItemStack) {
        this.equipmentMap[itemSlot] = itemStack

        this.fakeEntity.receivers.forEach { this.sendEquipmentPacket(itemSlot, itemStack, it) }
    }

    fun getEquipment(itemSlot: EnumWrappers.ItemSlot): ItemStack? = this.equipmentMap[itemSlot]

    fun updateEquipmentPacket(player: Player) = this.equipmentMap.forEach {
        (itemSlot, itemStack) -> this.sendEquipmentPacket(itemSlot, itemStack, player)
    }

    private fun sendEquipmentPacket(itemSlot: EnumWrappers.ItemSlot, itemStack: ItemStack, player: Player) {
        ProtocolPacketFactory.createEntityEquipmentPacket(this.fakeEntity.entityId, itemStack, itemSlot).sendPacket(player)
    }
}
