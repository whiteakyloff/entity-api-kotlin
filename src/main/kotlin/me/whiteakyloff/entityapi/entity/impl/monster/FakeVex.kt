package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

class FakeVex(location: Location) : FakeLivingEntity(EntityType.VEX, location)
{
    var inAttack = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte {
        return (if (inAttack) 0x01 else 0).toByte()
    }
}