package me.whiteakyloff.entityapi.entity.impl.animal

import me.whiteakyloff.entityapi.entity.FakeAgeableEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeSheep(location: Location) : FakeAgeableEntity(EntityType.SHEEP, location)
{
    open var color: DyeColor? = null
        set(value) {
            field = value
            this.sendDataWatcherObject(13, BYTE_SERIALIZER, this.generateBitMask())
        }

    open var sheared = false
        set(value) {
            field = value
            this.sendDataWatcherObject(13, BYTE_SERIALIZER, this.generateBitMask())

        }

    @Suppress("DEPRECATION")
    private fun generateBitMask(): Byte = ((if (sheared) 0x10 else 0) + (if (color != null) color!!.woolData else 0)).toByte()
}