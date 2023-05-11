package me.whiteakyloff.entityapi.entity.impl.monster

import me.whiteakyloff.entityapi.entity.FakeEntity

import org.bukkit.*
import org.bukkit.entity.EntityType

class FakeZombieVillager(location: Location) : FakeZombie(EntityType.ZOMBIE_VILLAGER, location)
{
    var converting = false
        set(value) {
            field = value
            this.sendDataWatcherObject(15, BOOLEAN_SERIALIZER, value)

        }
    var profession: Profession?
        get() = Profession.fromId(dataWatcher.getInteger(16))
        set(value) {
            this.sendDataWatcherObject(16, INT_SERIALIZER, value?.ordinal)
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