package me.whiteakyloff.entityapi.entity.impl

import me.whiteakyloff.entityapi.entity.FakeEntity
import me.whiteakyloff.entityapi.entity.FakeLivingEntity
import me.whiteakyloff.entityapi.entity.util.MojangUtil
import me.whiteakyloff.entityapi.packet.ProtocolPacketFactory
import me.whiteakyloff.entityapi.packet.scoreboard.WrapperPlayServerScoreboardTeam

import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.utility.MinecraftProtocolVersion
import com.comphenix.protocol.utility.MinecraftReflection
import com.comphenix.protocol.utility.MinecraftVersion
import com.comphenix.protocol.wrappers.*

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

import com.google.common.collect.ImmutableList

import java.util.*
import kotlin.random.Random

class FakePlayer : FakeLivingEntity
{
    private val uuid: UUID
    private val name: String

    private var mojangSkin: MojangUtil.Skin? = null
    private lateinit var wrappedGameProfile: WrappedGameProfile

    constructor(skin: String, location: Location) : this(MojangUtil.getSkinTextures(skin), location)

    constructor(skin: MojangUtil.Skin, location: Location) : super(EntityType.PLAYER, location) {
        this.mojangSkin = skin

        this.uuid = UUID.randomUUID()
        this.name = String.format("§8NPC [%s]", Random.nextInt(0, 9999999))

        this.updateSkinPart(PlayerSkinPart.TOTAL.bitMask)
    }

    fun setSkin(skinName: String?) {
        this.setSkin(MojangUtil.getSkinTextures(skinName!!))
    }

    private fun setSkin(mojangSkin: MojangUtil.Skin?) {
        this.receivers.forEach { this.setSkin(it, mojangSkin) }
    }

    private fun setSkin(player: Player?, mojangSkin: MojangUtil.Skin?) {
        this.mojangSkin = mojangSkin

        this.sendPlayerInfoPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER, player!!)
        Bukkit.getScheduler().runTaskLater(this.javaPlugin, {
            this.sendPlayerInfoPacket(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER, player)
        }, 30L)
    }

    override fun setGlowingColor(chatColor: ChatColor) {
        super.setGlowingColor(chatColor)

        this.receivers.forEach { this.sendTeamPacket(this.getTeamName(), it, WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED) }
    }

    private fun updateSkinPart(skinParts: Byte) {
        val beeVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.BEE_UPDATE)
        val currentVersion = MinecraftProtocolVersion.getVersion(ProtocolLibrary.getProtocolManager().minecraftVersion)

        this.sendDataWatcherObject(if (currentVersion >= beeVersion) 16 else 13, BYTE_SERIALIZER, skinParts)
    }

    private fun getTeamName(): String {
        var teamName = name + "_TEAM"

        if (teamName.length > 16) {
            teamName = teamName.substring(0, 16)
        }
        return teamName
    }

    override fun sendDestroyPacket(player: Player?) {
        super.sendDestroyPacket(player)

        this.sendTeamPacket(getTeamName(), player!!, WrapperPlayServerScoreboardTeam.Mode.TEAM_REMOVED)
    }

    override fun sendSpawnPacket(player: Player?) {
        this.sendPlayerInfoPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER, player!!)
        this.sendTeamPacket(this.getTeamName(), player, WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED)

        ProtocolPacketFactory.createSpawnNamedEntityPacket(entityId, uuid, location, dataWatcher).sendPacket(player)
        ProtocolPacketFactory.createEntityLookPacket(entityId, location).sendPacket(player)
        ProtocolPacketFactory.createEntityHeadRotationPacket(entityId, location).sendPacket(player)

        this.sendTeamPacket(this.getTeamName(), player, WrapperPlayServerScoreboardTeam.Mode.PLAYERS_ADDED)

        Bukkit.getScheduler().runTaskLater(this.javaPlugin, {
            this.sendDataWatcherPacket()
            this.sendPlayerInfoPacket(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER, player)
        }, 100L)
    }

    private fun sendPlayerInfoPacket(action: EnumWrappers.PlayerInfoAction, player: Player) {
        this.wrappedGameProfile = WrappedGameProfile(uuid, name).apply {
            this.properties.put("textures",
                WrappedSignedProperty("textures", mojangSkin!!.value, mojangSkin!!.signature)
            )
        }
        val playerInfoData = PlayerInfoData(wrappedGameProfile, 0,
            EnumWrappers.NativeGameMode.ADVENTURE, WrappedChatComponent.fromText(name))
        ProtocolPacketFactory.createPlayerInfoPacket(action, playerInfoData).sendPacket(player)
    }

    private fun sendTeamPacket(teamName: String, player: Player, mode: Int) {
        val scoreboardTeam = WrapperPlayServerScoreboardTeam()
        val version = ProtocolLibrary.getProtocolManager().getProtocolVersion(player)
        val aquaticVersion = MinecraftProtocolVersion.getVersion(MinecraftVersion.AQUATIC_UPDATE)

        scoreboardTeam.name = teamName

        if (version >= aquaticVersion) {
            scoreboardTeam.handle.integers.write(0, mode)
            scoreboardTeam.handle.strings.write(1, "never")
            scoreboardTeam.handle.strings.write(2, "never")

            if (mode == WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED || mode == WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED) {
                scoreboardTeam.handle.integers.write(1, 0)
                scoreboardTeam.handle.chatComponents.write(0, WrappedChatComponent.fromText(ChatColor.stripColor(teamName)))
                scoreboardTeam.handle.chatComponents.write(1, WrappedChatComponent.fromText(this.getGlowingColor()?.toString() ?: "§8"))
                scoreboardTeam.handle.getEnumModifier(
                    ChatColor::class.java,
                    MinecraftReflection.getMinecraftClass("EnumChatFormat")
                ).write(0, if (this.getGlowingColor() == null) ChatColor.RESET else this.getGlowingColor())
            } else {
                scoreboardTeam.players = ImmutableList.of(this.name)
            }
        } else {
            scoreboardTeam.mode = mode
            scoreboardTeam.collisionRule = "never"
            scoreboardTeam.nameTagVisibility = "never"

            if (mode == WrapperPlayServerScoreboardTeam.Mode.TEAM_CREATED || mode == WrapperPlayServerScoreboardTeam.Mode.TEAM_UPDATED) {
                scoreboardTeam.packOptionData = 0
                scoreboardTeam.displayName = teamName
                scoreboardTeam.color = this.getGlowingColor()?.ordinal ?: 0
                scoreboardTeam.prefix = this.getGlowingColor()?.toString() ?: "§8"
            } else {
                scoreboardTeam.players = ImmutableList.of(this.name)
            }
        }
        scoreboardTeam.sendPacket(player)
    }

    private val javaPlugin: JavaPlugin = JavaPlugin.getProvidingPlugin(FakeEntity::class.java)

    enum class PlayerSkinPart(val bitMask: Byte)
    {
        CAPE(0x01),
        JACKET(0x02),
        RIGHT_HAND(0x04),
        LEFT_HAND(0x08),
        RIGHT_LEG(0x10),
        LEFT_LEG(0x20),
        HAT(0x40),
        TOTAL((CAPE.bitMask + JACKET.bitMask + RIGHT_HAND.bitMask + LEFT_HAND.bitMask + RIGHT_LEG.bitMask + LEFT_LEG.bitMask + HAT.bitMask).toByte());
    }
}