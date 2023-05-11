package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

class FakeSpider(location: Location) : FakeLivingEntity(EntityType.SPIDER, location)
{
    var climbing = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte {
        return (if (climbing) 0x01 else 0).toByte()
    }
}