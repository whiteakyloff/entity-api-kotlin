package me.whiteakyloff.entityapi.entity.impl

import me.whiteakyloff.entityapi.entity.FakeAgeableEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

open class FakeVillager(location: Location) : FakeAgeableEntity(EntityType.VILLAGER, location)
{
    open var profession: Profession?
        get() = Profession.fromId(dataWatcher.getInteger(13))
        set(value) {
            this.sendDataWatcherObject(13, INT_SERIALIZER, value?.ordinal)
        }

    enum class Profession(private val id: Int) {
        FARMER(0), LIBRARIAN(1),
        PRIEST(2), BACKSMITH(3), BUTCHER(4), NITWIT(4);

        companion object {
            fun fromId(id: Int): Profession? {
                return values().find { it.id == id }
            }
        }
    }
}