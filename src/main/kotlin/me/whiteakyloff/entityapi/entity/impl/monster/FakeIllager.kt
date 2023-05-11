package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

abstract class FakeIllager(entityType: EntityType, location: Location) : FakeLivingEntity(entityType, location)
{
    var aggressive = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte {
        return (if (aggressive) 0x01 else 0).toByte()
    }
}