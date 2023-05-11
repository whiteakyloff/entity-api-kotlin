package me.whiteakyloff.entityapi.entity.impl.arrow

import org.bukkit.*
import org.bukkit.entity.EntityType

class FakeTippedArrow(location: Location) : FakeArrow(EntityType.TIPPED_ARROW, location)
{
    var color: Color? = null
        set(value) {
            field = value
            this.sendDataWatcherObject(7, INT_SERIALIZER, value?.asBGR())
        }
}