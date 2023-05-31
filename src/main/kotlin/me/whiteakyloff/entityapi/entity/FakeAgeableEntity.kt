package me.whiteakyloff.entityapi.entity

import org.bukkit.Location
import org.bukkit.entity.EntityType

abstract class FakeAgeableEntity(entityType: EntityType, location: Location) : FakeLivingEntity(entityType, location)
{
    open var isBaby: Boolean = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BOOLEAN_SERIALIZER, value)
        }
}
