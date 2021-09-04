package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.tool.Layer
import org.bukkit.entity.Player
import java.util.Stack

/**
 * @author Benedikt Wüller
 */
abstract class Canvas(val width: Int, val height: Int, val palette: ColorPalette) {

    private val redoLayers = Stack<Layer>()
    private val layers = Stack<Layer>()

    protected val overlays = mutableMapOf<Player, Layer>()

    fun apply(layer: Layer) {
        if (layer.isEmpty()) return
        this.applyWithoutRefresh(layer)
        this.refresh(layer)
    }

    fun applyWithoutRefresh(layer: Layer) {
        if (layer.isEmpty()) return
        this.layers.add(layer)
        this.redoLayers.clear()
    }

    fun undo() {
        if (this.layers.isEmpty()) return
        val layer = this.layers.pop()
        this.redoLayers.add(layer)
        this.refresh(layer)
    }

    fun redo() {
        if (this.redoLayers.isEmpty()) return
        val layer = this.redoLayers.pop()
        this.layers.add(layer)
        this.refresh(layer)
    }

    fun setOverlay(player: Player, layer: Layer) {
        if (layer.isEmpty()) return

        val difference = this.overlays[player]?.calculateDifference(layer) ?: layer.getChanges()
        this.overlays[player] = layer

        difference.keys.forEach { this.refresh(player, it.x, it.y) }
    }

    fun removeOverlay(player: Player) {
        val layer = this.overlays.remove(player) ?: return
        this.refresh(player, layer)
    }

    fun bakeOverlay(player: Player) {
        val layer = this.overlays.remove(player) ?: return
        this.applyWithoutRefresh(layer)
    }

    fun refresh() {
        (0 until this.height).forEach { y ->
            (0 until this.width).forEach { x ->
                this.refresh(x, y)
            }
        }
    }

    private fun refresh(layer: Layer) = layer.getChanges().keys.forEach { this.refresh(it.x, it.y) }

    private fun refresh(x: Int, y: Int) {
        val color = this.calculateColor(x, y)!!
        this.drawColor(x, y, color)
    }

    private fun refresh(player: Player, layer: Layer) = layer.getChanges().keys.forEach { this.refresh(player, it.x, it.y) }

    private fun refresh(player: Player, x: Int, y: Int) {
        val overlay = this.overlays[player]
        val color = overlay?.getColor(x, y) ?: this.calculateColor(x, y)!!
        this.drawColor(player, x, y, color)
    }

    abstract fun drawColor(x: Int, y: Int, color: Int)

    abstract fun drawColor(player: Player, x: Int, y: Int, color: Int)

    fun calculateColor(x: Int, y: Int) : Int? {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) return null
        return (this.layers.size - 1 downTo 0).mapNotNull {
            val layer = this.layers[it]
            layer.getChange(x, y)
        }.firstOrNull() ?: this.palette.baseColor
    }

    fun castOrFindBlockCanvas() : BlockCanvas? {
        return when (this) {
            is BlockCanvas -> this
            is CompoundCanvas -> this.canvases.mapNotNull(Canvas::castOrFindBlockCanvas).firstOrNull()
            else -> null
        }
    }

}
