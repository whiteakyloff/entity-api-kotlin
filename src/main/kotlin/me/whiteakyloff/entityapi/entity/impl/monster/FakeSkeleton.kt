package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeSkeleton(entityType: EntityType, location: Location) : FakeLivingEntity(entityType, location)
{
    constructor(location: Location) : this(EntityType.SKELETON, location)

    open var swingingArms = false
        set(value) {
            field = value
            this.sendDataWatcherObject(12, BOOLEAN_SERIALIZER, value)
        }
}