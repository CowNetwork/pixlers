package network.cow.minigame.pixlers.tool

import network.cow.messages.adventure.component
import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.plus
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.canvas.CompoundCanvas
import network.cow.minigame.pixlers.getCanvasColor
import org.bukkit.Bukkit
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
class ToolBox(private val player: Player, val canvas: Canvas) : Listener {

    private val tools = listOf(
            ColorPickerTool(this.canvas),
            PaintTool(this.canvas),
            FillTool(this.canvas),
            SprayCanTool(this.canvas),
            EraseTool(this.canvas),
            ShapeTool(this.canvas),
            StateTool(this.canvas),
            ClearTool(this.canvas)
    )

    private var currentTool: Tool? = null
    private var cursor = Point(-1, -1)

    private lateinit var task: BukkitTask
    private var tick: Long = 0

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
    }

    fun remove() {
        HandlerList.unregisterAll(this)
        this.task.cancel()
        this.tools.forEach { it.onUpdateItem = null }
        this.player.inventory.clear()
    }

    @EventHandler
    private fun onSwitchTool(event: PlayerItemHeldEvent) {
        if (event.previousSlot == event.newSlot) return
        this.currentTool?.cancel()
        this.currentTool = this.tools.getOrNull(event.newSlot)
    }

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
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
            val color = block.getCanvasColor() ?: return
            this.canvas.currentColor = color
        }
    }

    private fun updateTools() {
        if (this.currentTool is WithLiveCursorUpdates) {
            this.updateCursor()
        }

        if (this.tick % 4L == 0L) {
            // TODO: translate
            this.player.sendActionBar("Farbe: ".corporate() + "████".component(this.canvas.currentColor.color))
        }

        this.tick += 1
    }

    private fun updateCursor() {
        val point = when (this.canvas) {
            is BlockCanvas -> this.canvas.calculatePointOnCanvas(this.player)
            is CompoundCanvas -> (this.canvas.canvases.firstOrNull { it is BlockCanvas } as? BlockCanvas)?.calculatePointOnCanvas(this.player)
            else -> null
        } ?: Point(-1, -1)

        this.tools.forEach { it.updateCursor(point.x, point.y) }
    }

}
