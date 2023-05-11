package me.whiteakyloff.entityapi.entity.impl.animal

import me.whiteakyloff.entityapi.entity.FakeAgeableEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

class FakePig(location: Location) : FakeAgeableEntity(EntityType.PIG, location)
{
    var hasSaddle = false
        set(value) {
            field = value
            this.sendDataWatcherObject(13, BOOLEAN_SERIALIZER, value)
        }
}