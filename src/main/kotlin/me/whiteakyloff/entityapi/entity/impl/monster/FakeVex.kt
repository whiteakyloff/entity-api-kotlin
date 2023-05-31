package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeVex(location: Location) : FakeLivingEntity(EntityType.VEX, location)
{
    open var inAttack = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BYTE_SERIALIZER, this.generateBitMask())
        }

    private fun generateBitMask(): Byte = (if (inAttack) 0x01 else 0).toByte()
}