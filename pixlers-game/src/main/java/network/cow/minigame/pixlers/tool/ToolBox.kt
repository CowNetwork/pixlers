package network.cow.minigame.pixlers.tool

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.events.PacketEvent
import com.comphenix.protocol.events.PacketListener
import com.comphenix.protocol.wrappers.EnumWrappers
import network.cow.messages.adventure.component
import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.translateToComponent
import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.protocol.wrappers.WrapperPlayClientDig
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemHeldEvent
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

    private var currentTool: Tool? = this.tools.first()
    private var cursor = Point(-1, -1)

    private lateinit var task: BukkitTask
    private var tick: Long = 0

    internal var color = this.palette.initialColor

    private lateinit var packetAdapter: PacketListener

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

        this.packetAdapter = object : PacketAdapter(JavaPlugin.getPlugin(PixlersPlugin::class.java), PacketType.Play.Client.BLOCK_DIG) {
            override fun onPacketReceiving(event: PacketEvent) {
                if (event.player != player) {
                    super.onPacketReceiving(event)
                    return
                }

                val wrapper = WrapperPlayClientDig(event.packet)
                if (wrapper.status == EnumWrappers.PlayerDigType.RELEASE_USE_ITEM) {
                    currentTool?.onRelease()
                }

                super.onPacketReceiving(event)
            }
        }

        ProtocolLibrary.getProtocolManager().addPacketListener(this.packetAdapter)
    }

    fun remove() {
        HandlerList.unregisterAll(this)
        this.task.cancel()
        this.tools.forEach { it.onUpdateItem = null }
        this.player.inventory.clear()

        if (!this::packetAdapter.isInitialized) return
        ProtocolLibrary.getProtocolManager().removePacketListener(this.packetAdapter)
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
        this.tools.forEach { it.tick = this.tick }
    }

    private fun updateCursor() {
        val canvas = this.canvas.castOrFindBlockCanvas()!!
        val point = canvas.calculatePointOnCanvas(this.player) ?: Point(-1, -1)
        this.tools.forEach { it.updateCursor(point.x, point.y) }
    }

}
