package me.whiteakyloff.entityapi.packet.entity

import me.whiteakyloff.entityapi.packet.AbstractPacket

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.injector.PacketConstructor
import com.comphenix.protocol.reflect.IntEnum

import org.bukkit.World
import org.bukkit.entity.Entity

import java.util.*

class WrapperPlayServerSpawnEntity : AbstractPacket(PacketContainer(TYPE), TYPE)
{
    init {
        handle.modifier.writeDefaults()
    }

    var entityID: Int
        get() = handle.integers.read(0)
        set(value) {
            handle.integers.write(0, value)
        }
    var uniqueId: UUID?
        get() = handle.uuiDs.read(0)
        set(value) {
            handle.uuiDs.write(0, value)
        }
    var x: Double
        get() = handle.doubles.read(0)
        set(value) {
            handle.doubles.write(0, value)
        }
    var y: Double
        get() = handle.doubles.read(1)
        set(value) {
            handle.doubles.write(1, value)
        }
    var z: Double
        get() = handle.doubles.read(2)
        set(value) {
            handle.doubles.write(2, value)
        }
    var optionalSpeedX: Double
        get() = handle.integers.read(1) / 8000.0
        set(value) {
            handle.integers.write(1, (value * 8000.0).toInt())
        }
    var optionalSpeedY: Double
        get() = handle.integers.read(2) / 8000.0
        set(value) {
            handle.integers.write(2, (value * 8000.0).toInt())
        }
    var optionalSpeedZ: Double
        get() = handle.integers.read(3) / 8000.0
        set(value) {
            handle.integers.write(3, (value * 8000.0).toInt())
        }
    var pitch: Float
        get() = handle.integers.read(4) * 360f / 256.0f
        set(value) {
            handle.integers.write(4, (value * 256.0f / 360.0f).toInt())
        }
    var yaw: Float
        get() = handle.integers.read(5) * 360f / 256.0f
        set(value) {
            handle.integers.write(5, (value * 256.0f / 360.0f).toInt())
        }
    var spawnTypeID: Int
        get() = handle.integers.read(6)
        set(value) {
            handle.integers.write(6, value)
        }

    fun getEntity(world: World?): Entity {
        return handle.getEntityModifier(world!!).read(0)
    }

    fun getEntity(event: PacketEvent): Entity {
        return this.getEntity(event.player.world)
    }

    object ObjectTypes : IntEnum()
    {
        const val BOAT = 1
        const val ITEM_STACK = 2
        const val AREA_EFFECT_CLOUD = 3
        const val MINECART = 10
        const val ACTIVATED_TNT = 50
        const val ENDER_CRYSTAL = 51
        const val TIPPED_ARROW_PROJECTILE = 60
        const val SNOWBALL_PROJECTILE = 61
        const val EGG_PROJECTILE = 62
        const val GHAST_FIREBALL = 63
        const val BLAZE_FIREBALL = 64
        const val THROWN_ENDERPEARL = 65
        const val WITHER_SKULL_PROJECTILE = 66
        const val SHULKER_BULLET = 67
        const val FALLING_BLOCK = 70
        const val ITEM_FRAME = 71
        const val EYE_OF_ENDER = 72
        const val THROWN_POTION = 73
        const val THROWN_EXP_BOTTLE = 75
        const val FIREWORK_ROCKET = 76
        const val LEASH_KNOT = 77
        const val ARMORSTAND = 78
        const val FISHING_FLOAT = 90
        const val SPECTRAL_ARROW = 91
        const val DRAGON_FIREBALL = 93
    }

    companion object {
        val TYPE: PacketType = PacketType.Play.Server.SPAWN_ENTITY

        private var entityConstructor: PacketConstructor? = null

        private fun fromEntity(entity: Entity, type: Int, objectData: Int): PacketContainer {
            if (entityConstructor == null) {
                entityConstructor = ProtocolLibrary.getProtocolManager()
                    .createPacketConstructor(TYPE, entity, type, objectData)
            }
            return entityConstructor!!.createPacket(entity, type, objectData)
        }
    }
}