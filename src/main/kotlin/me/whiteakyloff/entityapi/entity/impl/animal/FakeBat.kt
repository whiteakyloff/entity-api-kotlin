package me.whiteakyloff.entityapi.entity.impl.animal

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

class FakeBat(location: Location) : FakeLivingEntity(EntityType.BAT, location)
{
    var hanging = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte {
        return (if (hanging) 0x01 else 0).toByte()
    }
}