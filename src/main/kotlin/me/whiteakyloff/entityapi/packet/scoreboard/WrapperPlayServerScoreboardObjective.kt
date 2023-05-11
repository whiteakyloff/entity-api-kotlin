package me.whiteakyloff.entityapi.packet.scoreboard

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.IntEnum

class WrapperPlayServerScoreboardObjective : AbstractPacket(PacketContainer(TYPE), TYPE) {
    init {
        handle.modifier.writeDefaults()
    }

    var mode: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var name: String?
        get() = handle.strings.read(0)
        set(value) {
            handle.strings.write(0, value)
        }
    var displayName: String?
        get() = handle.strings.read(1)
        set(value) {
            handle.strings.write(1, value)
        }
    var healthDisplay: HealthDisplay
        get() = handle.getEnumModifier(HealthDisplay::class.java, 2).read(0)
        set(value) {
            handle.getEnumModifier(HealthDisplay::class.java, 2).write(0, value)
        }

    enum class HealthDisplay
    {
        INTEGER, HEARTS
    }

    class Mode : IntEnum()
    {
        companion object {
            const val ADD_OBJECTIVE = 0
            const val UPDATE_VALUE = 2
            const val REMOVE_OBJECTIVE = 1;
        }
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.SCOREBOARD_OBJECTIVE
    }
}