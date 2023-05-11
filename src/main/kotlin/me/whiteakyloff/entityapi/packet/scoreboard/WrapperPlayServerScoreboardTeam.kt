package me.whiteakyloff.entityapi.packet.scoreboard

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.reflect.IntEnum
import me.whiteakyloff.entityapi.packet.AbstractPacket

@Suppress("UNCHECKED_CAST")
class WrapperPlayServerScoreboardTeam : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
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
    var prefix: String?
        get() = handle.strings.read(2)
        set(value) {
            handle.strings.write(2, value)
        }
    var suffix: String?
        get() = handle.strings.read(3)
        set(value) {
            handle.strings.write(3, value)
        }
    var nameTagVisibility: String?
        get() = handle.strings.read(4)
        set(value) {
            handle.strings.write(4, value)
        }
    var collisionRule: String?
        get() = handle.strings.read(5)
        set(value) {
            handle.strings.write(5, value)
        }
    var color: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var mode: Int
        get() = handle.integers.read(1)
        set(value) {
            handle.integers.write(1, value)
        }
    var packOptionData: Int
        get() = handle.integers.read(2)
        set(value) {
            handle.integers.write(2, value)
        }
    var players: List<String>
        get() = handle.getSpecificModifier(Collection::class.java).read(0) as List<String>
        set(value) {
            handle.getSpecificModifier(Collection::class.java).write(0, value)
        }

    class Mode : IntEnum()
    {
        companion object {
            const val TEAM_CREATED = 0
            const val TEAM_REMOVED = 1
            const val TEAM_UPDATED = 2
            const val PLAYERS_ADDED = 3
            const val PLAYERS_REMOVED = 4
        }
    }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.SCOREBOARD_TEAM
    }
}