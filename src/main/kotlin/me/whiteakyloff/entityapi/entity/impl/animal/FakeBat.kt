package me.whiteakyloff.entityapi.entity.impl.animal

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeBat(location: Location) : FakeLivingEntity(EntityType.BAT, location)
{
    open var hanging = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte = (if (hanging) 0x01 else 0).toByte()
}