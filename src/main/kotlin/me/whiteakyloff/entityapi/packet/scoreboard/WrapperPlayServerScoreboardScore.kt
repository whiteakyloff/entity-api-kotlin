package me.whiteakyloff.entityapi.packet.scoreboard

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers.ScoreboardAction

class WrapperPlayServerScoreboardScore : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var value: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var scoreName: String
        get() = handle.strings.read(0)
        set(value) {
            handle.strings.write(0, value)
        }
    var objectiveName: String
        get() = handle.strings.read(1)
        set(value) {
            handle.strings.write(1, value)
        }
    var action: ScoreboardAction?
        get() = handle.scoreboardActions.read(0)
        set(value) {
            handle.scoreboardActions.write(0, value)
        }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.SCOREBOARD_SCORE
    }
}