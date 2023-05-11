package me.whiteakyloff.entityapi.entity.impl

import com.comphenix.protocol.wrappers.Vector3F

import me.whiteakyloff.entityapi.entity.FakeEntity

import org.bukkit.Location
import org.bukkit.entity.EntityType

class FakeArmorStand(location: Location) : FakeEntity(EntityType.ARMOR_STAND, location)
{
    var marker = false
        set(value) {
            field = value
            this.sendDataWatcherObject(11, BYTE_SERIALIZER, this.generateBitMask())
        }
    var small = false
        set(value) {
            field = value
            this.sendDataWatcherObject(11, BYTE_SERIALIZER, this.generateBitMask())
        }
    var basePlate = false
        set(value) {
            field = value
            this.sendDataWatcherObject(11, BYTE_SERIALIZER, this.generateBitMask())
        }
    var arms = false
        set(value) {
            field = value
            this.sendDataWatcherObject(11, BYTE_SERIALIZER, this.generateBitMask())
        }

    fun setHeadRotation(vector3F: Vector3F) {
        this.sendDataWatcherObject(12, ROTATION_SERIALIZER, vector3F)
    }

    fun setBodyRotation(vector3F: Vector3F) {
        this.sendDataWatcherObject(13, ROTATION_SERIALIZER, vector3F)
    }

    fun setLeftArmRotation(vector3F: Vector3F) {
        this.sendDataWatcherObject(14, ROTATION_SERIALIZER, vector3F)
    }

    fun setRightArmRotation(vector3F: Vector3F) {
        this.sendDataWatcherObject(15, ROTATION_SERIALIZER, vector3F)
    }

    fun setLeftLegRotation(vector3F: Vector3F) {
        this.sendDataWatcherObject(16, ROTATION_SERIALIZER, vector3F)
    }

    fun setRightLegRotation(vector3F: Vector3F) {
        this.sendDataWatcherObject(17, ROTATION_SERIALIZER, vector3F)
    }

    private fun generateBitMask(): Byte {
        return ((if (small) 0x01 else 0) + (if (arms) 0x04 else 0) + (if (!basePlate) 0x08 else 0) + (if (marker) 0x10 else 0)).toByte()
    }
}
