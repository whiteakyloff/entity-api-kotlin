package me.whiteakyloff.entityapi.packet

import me.whiteakyloff.entityapi.packet.entity.*
import me.whiteakyloff.entityapi.entity.animation.FakeEntityAnimation

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

import com.comphenix.protocol.utility.MinecraftProtocolVersion
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction
import com.comphenix.protocol.wrappers.PlayerInfoData
import com.comphenix.protocol.wrappers.WrappedDataWatcher

import java.util.*
import com.google.common.collect.ImmutableList


object ProtocolPacketFactory
{
    fun createDestroyEntityPacket(vararg entityIDs: Int): WrapperPlayServerEntityDestroy {
        return WrapperPlayServerEntityDestroy().apply { this.entityIDs = entityIDs }
    }

    fun createMountPacket(entityID: Int, passengerIDs: IntArray): WrapperPlayServerMount {
        return WrapperPlayServerMount().apply { this.entityID = entityID; this.passengerIDs = passengerIDs }
    }

    fun createPlayerInfoPacket(action: PlayerInfoAction?, playerInfoData: PlayerInfoData): WrapperPlayServerPlayerInfo {
        return WrapperPlayServerPlayerInfo().apply { this.action = action; this.data = ImmutableList.of(playerInfoData) }
    }

    fun createEntityMetadataPacket(entityID: Int, dataWatcher: WrappedDataWatcher): WrapperPlayServerEntityMetadata {
        return WrapperPlayServerEntityMetadata().apply { this.entityID = entityID; this.metadata = dataWatcher.watchableObjects }
    }

    fun createAnimationPacket(entityID: Int, entityAnimation: FakeEntityAnimation): WrapperPlayServerAnimation {
        return WrapperPlayServerAnimation().apply { this.entityID = entityID; this.animation = entityAnimation.ordinal  }
    }

    fun createEntityEquipmentPacket(entityID: Int, itemStack: ItemStack?, itemSlot: ItemSlot?): WrapperPlayServerEntityEquipment {
        return WrapperPlayServerEntityEquipment().apply {
            this.entityID = entityID
            this.itemSlot = itemSlot; this.itemStack = itemStack
        }
    }

    fun createEntityLookPacket(entityID: Int, location: Location): WrapperPlayServerEntityLook {
        return WrapperPlayServerEntityLook().apply {
            this.entityID = entityID
            this.yaw = location.yaw; this.pitch = location.pitch
        }
    }

    fun createEntityVelocityPacket(entityID: Int, vector: Vector): WrapperPlayServerEntityVelocity {
        return WrapperPlayServerEntityVelocity().apply {
            this.entityID = entityID
            this.velocityX = vector.x; this.velocityY = vector.y; this.velocityZ = vector.z
        }
    }

    fun createEntityHeadRotationPacket(entityID: Int, location: Location): WrapperPlayServerEntityHeadRotation {
        return WrapperPlayServerEntityHeadRotation().apply {
            this.entityID = entityID
            this.headYaw = (location.yaw * 256.0f / 360.0f).toInt().toByte()
        }
    }

    fun createEntityTeleportPacket(entityID: Int, location: Location): WrapperPlayServerEntityTeleport {
        return WrapperPlayServerEntityTeleport().apply {
            this.entityID = entityID
            this.yaw = location.yaw; this.pitch = location.pitch
            this.x = location.x; this.y = location.y; this.z = location.z
        }
    }

    fun createSpawnNamedEntityPacket(entityID: Int, playerUUID: UUID?, location: Location, dataWatcher: WrappedDataWatcher?): WrapperPlayServerNamedEntitySpawn {
        return WrapperPlayServerNamedEntitySpawn().apply {
            val currentVersion = MinecraftProtocolVersion.getCurrentVersion()
            val beeVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.BEE_UPDATE)

            this.entityID = entityID; this.playerUUID = playerUUID
            if (currentVersion < beeVersion) {
                this.metadata = dataWatcher
            }
            this.yaw = location.yaw; this.pitch = location.pitch; this.position = location.toVector()
        }
    }

    fun createSpawnLivingEntityPacket(entityID: Int, location: Location, entityType: EntityType, dataWatcher: WrappedDataWatcher?): WrapperPlayServerSpawnLivingEntity {
        return WrapperPlayServerSpawnLivingEntity().apply {
            val currentVersion = MinecraftProtocolVersion.getCurrentVersion()
            val beeVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.BEE_UPDATE)

            this.entityID = entityID; this.entityType = entityType
            if (currentVersion < beeVersion) {
                this.metadata = dataWatcher
            }
            this.x = location.x; this.y = location.y; this.z = location.z; this.yaw = location.yaw; this.pitch = location.pitch
        }
    }

    @Suppress("DEPRECATION")
    fun createSpawnEntityPacket(entityID: Int, spawnTypeID: Int, location: Location, entityType: EntityType): WrapperPlayServerSpawnEntity {
        return WrapperPlayServerSpawnEntity().apply {
            val currentVersion = MinecraftProtocolVersion.getCurrentVersion()
            val beeVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.BEE_UPDATE)

            this.entityID = entityID; this.spawnTypeID = spawnTypeID
            if (currentVersion > beeVersion) {
                this.handle.entityTypeModifier.write(0, entityType)
            } else {
                this.handle.integers.write(7, entityType.typeId.toInt())
            }
            this.x = location.x; this.y = location.y; this.z = location.z; this.yaw = location.yaw; this.pitch = location.pitch
        }
    }
}