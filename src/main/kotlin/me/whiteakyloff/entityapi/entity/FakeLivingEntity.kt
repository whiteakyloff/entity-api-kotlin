package me.whiteakyloff.entityapi.entity

import me.whiteakyloff.entityapi.packet.ProtocolPacketFactory
import me.whiteakyloff.entityapi.entity.animation.FakeEntityAnimation

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

abstract class FakeLivingEntity(entityType: EntityType, location: Location) : FakeEntity(entityType, location)
{
    var health: Float = 0f
        set(value) {
            field = value
            this.sendDataWatcherObject(7, FLOAT_SERIALIZER, value)
        }
    var arrowCount: Int = 0
        set(value) {
            field = value
            this.sendDataWatcherObject(10, INT_SERIALIZER, value)
        }
    var potionColorEffect: ChatColor? = null
        set(value) {
            field = value
            this.sendDataWatcherObject(8, INT_SERIALIZER, value?.ordinal ?: 0)
        }
    var ambientPotionEffect: Boolean = false
        set(value) {
            field = value
            this.sendDataWatcherObject(9, BOOLEAN_SERIALIZER, value)
        }
    var intelligence: Boolean = false
        set(value) {
            field = value
            this.sendDataWatcherObject(11, BYTE_SERIALIZER, this.generateBitMask())
        }

    fun playAnimation(entityAnimation: FakeEntityAnimation) {
        this.receivers.forEach { playAnimation(it, entityAnimation) }
    }

    private fun playAnimation(player: Player, entityAnimation: FakeEntityAnimation) {
        ProtocolPacketFactory.createAnimationPacket(entityId, entityAnimation).sendPacket(player)
    }

    private fun generateBitMask(): Byte {
        return if (this.intelligence) 0.toByte() else 0x01
    }
}
