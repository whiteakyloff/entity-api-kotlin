package me.whiteakyloff.entityapi.entity.listener

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import me.whiteakyloff.entityapi.entity.FakeEntity

import org.bukkit.plugin.java.JavaPlugin

class FakeEntityListener(javaPlugin: JavaPlugin) : PacketAdapter(javaPlugin, PacketType.Play.Client.USE_ENTITY)
{
    init {
        ProtocolLibrary.getProtocolManager().addPacketListener(this)
    }

    override fun onPacketReceiving(event: PacketEvent) {
        val player = event.player
        val fakeEntity = FakeEntity.getEntityById(event.packet.integers.read(0))

        if (fakeEntity?.clickAction == null) {
            return
        }
        fakeEntity.clickAction!!.accept(player)
    }
}