package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeAgeableEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeZombie(entityType: EntityType, location: Location) : FakeAgeableEntity(entityType, location)
{
    constructor(location: Location) : this(EntityType.ZOMBIE, location)

    var handsUp = false
        set(value) {
            field = value;
            this.sendDataWatcherObject(14, BOOLEAN_SERIALIZER, value)
        }
}