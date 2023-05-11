package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeLivingEntity

import org.bukkit.Location
import org.bukkit.entity.EntityType

class FakeGiant(location: Location) : FakeLivingEntity(EntityType.GIANT, location)