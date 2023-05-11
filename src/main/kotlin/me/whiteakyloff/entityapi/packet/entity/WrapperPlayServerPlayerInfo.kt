package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction
import com.comphenix.protocol.wrappers.PlayerInfoData

class WrapperPlayServerPlayerInfo : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var action: PlayerInfoAction?
        get() = handle.playerInfoAction.read(0)
        set(value) {
            handle.playerInfoAction.write(0, value)
        }
    var data: List<PlayerInfoData>?
        get() = handle.playerInfoDataLists.read(0)
        set(value) {
            handle.playerInfoDataLists.write(0, value)
        }

    companion object {
        val TYPE : PacketType = PacketType.Play.Server.PLAYER_INFO
    }
}