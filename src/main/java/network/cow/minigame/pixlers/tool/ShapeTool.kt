package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.formatToComponent
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.plus
import network.cow.messages.core.Gradients
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
class ShapeTool(canvas: Canvas) : Tool(canvas) {

    var type: ShapeType = ShapeType.LINE; private set

    private var firstPosition: Point? = null
    private var layer: Layer? = null

    override fun onPrimary() : Boolean {
        if (this.cursor.x < 0 || this.cursor.x >= this.canvas.width || this.cursor.y < 0 || this.cursor.y >= this.canvas.height) return false

        if (this.firstPosition == null) {
            this.firstPosition = Point(this.cursor.x, this.cursor.y)

            val layer = Layer(this.canvas)
            this.layer = layer
            this.refreshLayer(layer)
            this.canvas.addWithoutUndo(layer)
            return true
        }

        val layer = this.layer ?: return true
        this.canvas.removeWithoutRedo(layer)
        this.refreshLayer(layer)
        this.canvas.apply(layer)

        this.layer = null
        this.firstPosition = null
        return true
    }

    override fun onCursorMoved() {
        val layer = this.layer ?: return
        this.canvas.removeWithoutRedo(layer)
        this.refreshLayer(layer)
        this.canvas.addWithoutUndo(layer)
    }

    private fun refreshLayer(layer: Layer) {
        layer.clear()
        val from = this.firstPosition ?: return
        val to = Point(this.cursor.x, this.cursor.y)
        val pixels = this.type.shape.calculatePixels(from, to)
        pixels.forEach { layer.setColor(it.x, it.y, this.canvas.currentColor) }
    }

    override fun onSecondary() : Boolean {
        this.cancel()
        val index = ShapeType.values().indexOf(this.type)
        val nextIndex = if (index == ShapeType.values().lastIndex) 0 else index + 1
        this.type = ShapeType.values()[nextIndex]
        this.onUpdateItem()
        return true
    }

    override fun onCancel() {
        this.firstPosition = null
        this.layer?.let { this.canvas.removeWithoutRedo(it) }
    }

    override fun getItemStack(player: Player): ItemStack {
        // TODO: translate
        val material = when (this.type) {
            ShapeType.LINE -> Material.DIAMOND_HOE
            ShapeType.RECTANGLE -> Material.STONE_HOE
            ShapeType.ELLIPSE -> Material.GOLDEN_HOE
        }
        
        return ItemBuilder(material)
                .name("Form".gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.type.displayName.comp()).info())
                .lore(
                        Component.empty(),
                        "Linksklicke".highlight() + ", um die Form zu wechseln.".info(),
                        "Rechtsklicke".highlight() + " zweimal, um die Form zu zeichnen.".info()
                )
                .build()
    }

}