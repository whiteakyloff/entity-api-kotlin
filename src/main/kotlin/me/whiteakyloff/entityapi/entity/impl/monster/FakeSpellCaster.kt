package me.whiteakyloff.entityapi.entity.impl.monster

import org.bukkit.*
import org.bukkit.entity.EntityType

abstract class FakeSpellCaster(entityType: EntityType, location: Location) : FakeIllager(entityType, location)
{
    enum class Spell {
        NONE, SUMMON, ATTACK, WOLOLO
    }

    open var spell: Spell? = null
        set(value) {
            if (value == null) {
                return
            }
            field = value
            this.sendDataWatcherObject(13, BYTE_SERIALIZER, value.ordinal)
        }
}