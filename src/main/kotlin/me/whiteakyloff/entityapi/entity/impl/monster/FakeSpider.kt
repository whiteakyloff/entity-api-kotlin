package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeSpider(location: Location) : FakeLivingEntity(EntityType.SPIDER, location)
{
    open var climbing = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte = (if (climbing) 0x01 else 0).toByte()
}