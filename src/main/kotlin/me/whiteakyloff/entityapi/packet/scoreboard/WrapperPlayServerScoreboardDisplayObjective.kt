package me.whiteakyloff.entityapi.packet.scoreboard

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

class WrapperPlayServerScoreboardDisplayObjective : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var position: Int
        get() = handle.integers.read(0);
        set(value) {
            handle.integers.write(0, value)
        }
    var scoreName: String?
        get() = handle.strings.read(0)
        set(value) {
            handle.strings.write(0, value)
        }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.SCOREBOARD_DISPLAY_OBJECTIVE
    }
}