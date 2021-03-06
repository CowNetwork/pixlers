package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.formatToComponent
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.plus
import network.cow.messages.adventure.translate
import network.cow.messages.adventure.translateToComponent
import network.cow.messages.core.Gradients
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.shape.ShapeType
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ShapeTool(toolBox: ToolBox, canvas: Canvas) : Tool(toolBox, canvas), WithLiveCursorUpdates {

    var type: ShapeType = ShapeType.LINE; private set

    private var firstPosition: Point? = null
    private var layer: Layer? = null

    override fun onPrimary() : Boolean {
        if (this.cursor.x < 0 || this.cursor.x >= this.canvas.width || this.cursor.y < 0 || this.cursor.y >= this.canvas.height) return false

        if (this.firstPosition == null) {
            this.firstPosition = Point(this.cursor.x, this.cursor.y)

            val layer = this.refreshLayer()
            this.layer = layer
            this.canvas.setOverlay(this.toolBox.player, layer)
            return true
        }

        val layer = this.refreshLayer()
        this.canvas.setOverlay(this.toolBox.player, layer)
        this.canvas.bakeOverlay(this.toolBox.player)

        this.layer = null
        this.firstPosition = null
        return true
    }

    override fun onCursorMoved() {
        if (this.cursor.x < 0 || this.cursor.x >= this.canvas.width || this.cursor.y < 0 || this.cursor.y >= this.canvas.height) return

        val currentLayer = this.layer ?: return
        val layer = this.refreshLayer()
        if (currentLayer == layer) return

        this.layer = layer
        this.canvas.setOverlay(this.toolBox.player, layer)

        currentLayer.clear()
    }

    private fun refreshLayer() : Layer {
        val layer = Layer(this.canvas)
        val from = this.firstPosition ?: return layer
        val to = Point(this.cursor.x, this.cursor.y)
        val pixels = this.type.shape.calculatePixels(from, to)
        pixels.forEach { layer.setColor(it.x, it.y, this.toolBox.color) }
        return layer
    }

    override fun onSecondary() : Boolean {
        this.cancel()
        val index = ShapeType.values().indexOf(this.type)
        val nextIndex = if (index == ShapeType.values().lastIndex) 0 else index + 1
        this.type = ShapeType.values()[nextIndex]
        this.onUpdateItem?.let { it() }
        return true
    }

    override fun onCancel() {
        this.firstPosition = null
        this.layer?.let { this.canvas.removeOverlay(this.toolBox.player) }
    }

    override fun getItemStack(player: Player): ItemStack {
        val material = when (this.type) {
            ShapeType.LINE -> Material.DIAMOND_HOE
            ShapeType.RECTANGLE -> Material.STONE_HOE
            ShapeType.ELLIPSE -> Material.GOLDEN_HOE
            ShapeType.DONGUS -> Material.NETHERITE_HOE
        }
        
        return ItemBuilder(material)
            .name(Translations.Tool.Shape.NAME.translate(player).gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.type.translationKey.translateToComponent(player)).info())
            .lore(
                Component.empty(),
                Translations.Tool.Shape.ACTION_LEFT.translateToComponent(player, Translations.Action.LEFT.translate(player).highlight()).info(),
                Translations.Tool.Shape.ACTION_RIGHT.translateToComponent(player, Translations.Action.RIGHT.translate(player).highlight()).info()
            )
            .build()
    }

}
