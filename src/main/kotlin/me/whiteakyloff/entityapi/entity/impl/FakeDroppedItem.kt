package me.whiteakyloff.entityapi.entity.impl

import me.whiteakyloff.entityapi.entity.FakeEntity
import me.whiteakyloff.entityapi.packet.entity.WrapperPlayServerSpawnEntity

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack

class FakeDroppedItem(location: Location, itemStack: ItemStack = ItemStack(Material.AIR)) : FakeEntity(EntityType.DROPPED_ITEM, location)
{
    var itemStack: ItemStack = itemStack
        set(value) {
            this.sendDataWatcherObject(6, ITEMSTACK_SERIALIZER, value)
                .also { this.dataWatcher.watchableObjects.forEach { it.dirtyState = false } }
            field = value
        }

    override fun getSpawnTypeId(): Int = WrapperPlayServerSpawnEntity.ObjectTypes.ITEM_STACK
}
