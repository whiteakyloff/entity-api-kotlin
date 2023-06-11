package me.whiteakyloff.entityapi.entity.listener.event

import me.whiteakyloff.entityapi.entity.FakeEntity

import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class PlayerInteractEntityEvent(
    val player: Player, val fakeEntity: FakeEntity
) : Event() {
    override fun getHandlers(): HandlerList = handlerList

    companion object { val handlerList = HandlerList() }
}