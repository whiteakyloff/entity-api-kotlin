package me.whiteakyloff.entityapi.entity

import me.whiteakyloff.entityapi.packet.ProtocolPacketFactory
import me.whiteakyloff.entityapi.entity.animation.FakeEntityAnimation

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

abstract class FakeLivingEntity(entityType: EntityType, location: Location) : FakeEntity(entityType, location)
{
    open var health: Float = 0f
        set(value) {
            field = value
            this.sendDataWatcherObject(7, FLOAT_SERIALIZER, value)
        }
    open var arrowCount: Int = 0
        set(value) {
            field = value
            this.sendDataWatcherObject(10, INT_SERIALIZER, value)
        }
    open var potionColorEffect: ChatColor? = null
        set(value) {
            field = value
            this.sendDataWatcherObject(8, INT_SERIALIZER, value?.ordinal ?: 0)
        }
    open var ambientPotionEffect: Boolean = false
        set(value) {
            field = value
            this.sendDataWatcherObject(9, BOOLEAN_SERIALIZER, value)
        }
    open var intelligence: Boolean = false
        set(value) {
            field = value
            this.sendDataWatcherObject(11, BYTE_SERIALIZER, this.generateBitMask())
        }

    open fun playAnimation(entityAnimation: FakeEntityAnimation) = this.receivers.forEach {
        this.playAnimation(it, entityAnimation)
    }

    open fun playAnimation(player: Player, entityAnimation: FakeEntityAnimation) {
        ProtocolPacketFactory.createAnimationPacket(entityId, entityAnimation).sendPacket(player)
    }

    private fun generateBitMask(): Byte = if (this.intelligence) 0.toByte() else 0x01
}
