package me.whiteakyloff.entityapi.entity.impl.arrow

import me.whiteakyloff.entityapi.entity.FakeEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeArrow(entityType: EntityType, location: Location) : FakeEntity(entityType, location)
{
    constructor(location: Location) : this(EntityType.ARROW, location)

    var critical = false
        set(value) {
            field = value
            this.sendDataWatcherObject(6, BYTE_SERIALIZER, this.generateBitMask())
        }
    var noClip = false
        set(value) {
            field = value
            this.sendDataWatcherObject(6, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte {
        return ((if (critical) 0x01 else 0) + (if (noClip) 0x02 else 0)).toByte()
    }
}