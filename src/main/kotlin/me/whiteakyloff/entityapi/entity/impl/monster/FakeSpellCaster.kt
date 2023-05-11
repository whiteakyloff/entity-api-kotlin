package me.whiteakyloff.entityapi.entity.impl.monster

import org.bukkit.*
import org.bukkit.entity.EntityType

abstract class FakeSpellCaster(entityType: EntityType, location: Location) : FakeIllager(entityType, location)
{
    var spell: Spell? = null
        set(value) {
            field = value
            if (value != null) {
                this.sendDataWatcherObject(13, BYTE_SERIALIZER, value.ordinal)
            }
        }

    enum class Spell {
        NONE, SUMMON, ATTACK, WOLOLO
    }
}