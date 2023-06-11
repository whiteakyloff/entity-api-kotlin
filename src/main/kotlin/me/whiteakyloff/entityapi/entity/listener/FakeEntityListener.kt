package me.whiteakyloff.entityapi.entity.listener

import me.whiteakyloff.entityapi.entity.FakeEntity
import me.whiteakyloff.entityapi.entity.listener.event.PlayerInteractEntityEvent

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.PacketType.Play.Client

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

class FakeEntityListener(
    javaPlugin: JavaPlugin
) : Listener, PacketAdapter(javaPlugin, Client.USE_ENTITY)
{
    init {
        ProtocolLibrary.getProtocolManager().addPacketListener(this)
    }

    override fun onPacketReceiving(event: PacketEvent) {
        val player = event.player
        val fakeEntity = FakeEntity.getEntityById(event.packet.integers.read(0)) ?: return

        PlayerInteractEntityEvent(player, fakeEntity).callEvent()
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        for (fakeEntity in FakeEntity.ENTITIES) {
            if (fakeEntity.public) {
                fakeEntity.addReceiver(event.player)
            }
        }
    }

    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        for (fakeEntity in FakeEntity.ENTITIES) {
            if (fakeEntity.hasReceiver(event.player)) {
                fakeEntity.removeReceiver(event.player)
            }
        }
    }
}