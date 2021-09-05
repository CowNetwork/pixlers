package network.cow.minigame.pixlers.tool

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLib
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.ListeningWhitelist
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.events.PacketListener
import network.cow.messages.adventure.component
import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.translateToComponent
import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.protocol.wrappers.WrapperPlayServerSpawnEntity
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ToolBox(val player: Player, val canvas: Canvas, private val palette: ColorPalette) : Listener {

    private val tools = listOf(
        PaintTool(this, this.canvas),
        ColorPickerTool(this, this.canvas),
        FillTool(this, this.canvas),
        SprayCanTool(this, this.canvas),
        EraseTool(this, this.canvas),
        ShapeTool(this, this.canvas),
        StateTool(this, this.canvas),
        ClearTool(this, this.canvas),
        SpeedTool(this, this.canvas)
    )

    private var currentTool: Tool? = null
    private var cursor = Point(-1, -1)

    private lateinit var task: BukkitTask
    private var tick: Long = 0

    private var shulker: Shulker? = null
    private lateinit var packetListener: PacketListener

    internal var color = this.palette.initialColor

    fun apply() {
        val plugin = JavaPlugin.getPlugin(PixlersPlugin::class.java)
        Bukkit.getPluginManager().registerEvents(this, plugin)

        this.player.inventory.clear()
        this.tools.forEach {
            var itemStack = it.getItemStack(this.player)
            this.player.inventory.addItem(itemStack)

            it.onUpdateItem = {
                this.player.inventory.removeItem(itemStack)
                itemStack = it.getItemStack(this.player)
                this.player.inventory.addItem(itemStack)
            }
        }

        this.currentTool = this.tools.getOrNull(this.player.inventory.heldItemSlot)
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, this::updateTools, 1L, 1L)

        this.packetListener = object : PacketAdapter(JavaPlugin.getPlugin(PixlersPlugin::class.java), ListenerPriority.HIGH, PacketType.Play.Server.SPAWN_ENTITY) {
            override fun onPacketSending(event: PacketEvent) {
                val entity = shulker ?: return

                val container = PacketContainer.fromPacket(event)
                val packet = WrapperPlayServerSpawnEntity(container)

                if (packet.entityID == entity.entityId && player != event.player) {
                    event.isCancelled = true
                }

                super.onPacketSending(event)
            }
        }

        ProtocolLibrary.getProtocolManager().addPacketListener(this.packetListener)
    }

    fun remove() {
        HandlerList.unregisterAll(this)
        this.task.cancel()
        this.tools.forEach { it.onUpdateItem = null }
        this.player.inventory.clear()

        ProtocolLibrary.getProtocolManager().removePacketListener(this.packetListener)
    }

    @EventHandler
    private fun onSwitchTool(event: PlayerItemHeldEvent) {
        if (this.player != event.player) return
        if (event.previousSlot == event.newSlot) return
        this.currentTool?.cancel()
        this.currentTool = this.tools.getOrNull(event.newSlot)
    }

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (this.player != event.player) return
        val tool = this.currentTool ?: return

        this.updateCursor()
        val executed = when (event.action) {
            Action.RIGHT_CLICK_AIR, Action.RIGHT_CLICK_BLOCK -> tool.executePrimary()
            Action.LEFT_CLICK_AIR, Action.LEFT_CLICK_BLOCK -> tool.executeSecondary()
            else -> false
        }

        if (!executed && (this.cursor.x < 0 || this.cursor.x >= this.canvas.width || this.cursor.y < 0 || this.cursor.y >= this.canvas.height)) {
            val result = this.player.rayTraceBlocks(200.0)
            val block = result?.hitBlock ?: return

            if (block.type != Material.NOTE_BLOCK) return
            this.color = this.palette.getIndex(block.blockData as NoteBlock)
        }
    }

    private fun updateTools() {
        if (this.currentTool is WithLiveCursorUpdates) {
            this.updateCursor()
        }

        if (this.tick % 4L == 0L) {
            this.player.sendActionBar(
                Translations.COLOR.translateToComponent(
                    this.player,
                    "████".component(this.palette.getColor(this.color))
                ).corporate()
            )
        }

        this.tick += 1
    }

    private fun updateCursor() {
        val canvas = this.canvas.castOrFindBlockCanvas()!!
        val point = canvas.calculatePointOnCanvas(this.player) ?: Point(-1, -1)
        this.tools.forEach { it.updateCursor(point.x, point.y) }

        if (point.x < 0 || point.y < 0) {
            if (this.shulker != null) {
                this.shulker?.remove()
                this.shulker = null
            }
        } else {
            val block = canvas.getBlockAt(point)
            if (this.shulker == null) {
                val entity = block.world.spawnEntity(block.location, EntityType.SHULKER) as Shulker
                entity.isInvisible = true
                entity.isSilent = true
                entity.isGlowing = true
                entity.setAI(false)
                this.shulker = entity
            } else {
                this.shulker!!.teleport(block.location)
            }
        }
    }

}
