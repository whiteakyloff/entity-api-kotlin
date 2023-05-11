package me.whiteakyloff.entityapi.packet.entity

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.PacketContainer

import me.whiteakyloff.entityapi.packet.AbstractPacket

class WrapperPlayServerEntityDestroy : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityIDs: IntArray
        get() = handle.integerArrays.read(0)
        set(value) {
            handle.integerArrays.write(0, value)
        }

    fun getCount(): Int {
        return handle.integerArrays.read(0).size
    }

    companion object {
        val TYPE: PacketType = PacketType.Play.Server.ENTITY_DESTROY
    }
}