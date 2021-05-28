package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.tool.Layer
import java.util.Stack

/**
 * @author Benedikt Wüller
 */
abstract class Canvas(val width: Int, val height: Int) {

    companion object {
        val BASE_COLOR = CanvasColor.WHITE
    }

    init {
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                this.drawColor(x, y, CanvasColor.WHITE)
            }
        }
    }

    private val redoLayers = Stack<Layer>()
    private val layers = Stack<Layer>()

    var currentColor = BASE_COLOR

    fun apply(layer: Layer) {
        layer.cleanup()
        if (layer.isEmpty()) return
        this.layers.add(layer)
        this.redoLayers.clear()
        this.refresh(layer)
    }

    fun undo() {
        if (this.layers.isEmpty()) return
        val layer = this.layers.pop()
        this.layers.remove(layer)
        this.redoLayers.add(layer)
        this.refresh(layer)
    }

    fun redo() {
        if (this.redoLayers.isEmpty()) return
        val layer = this.redoLayers.pop()
        this.redoLayers.remove(layer)
        this.layers.add(layer)
        this.refresh(layer)
    }

    internal fun addWithoutUndo(layer: Layer) {
        layer.cleanup()
        if (layer.isEmpty()) return
        this.layers.add(layer)
        this.refresh(layer)
    }

    internal fun removeWithoutRedo(layer: Layer) {
        this.layers.remove(layer)
        this.refresh(layer)
    }

    fun refresh() {
        (0 until this.height).forEach { y ->
            (0 until this.width).forEach { x ->
                this.refresh(x, y)
            }
        }
    }

    private fun refresh(layer: Layer) = layer.getChanges().keys.forEach { (x, y) -> this.refresh(x, y) }

    private fun refresh(x: Int, y: Int) {
        val color = this.calculateColor(x, y)!!
        this.drawColor(x, y, color)
    }

    abstract fun drawColor(x: Int, y: Int, color: CanvasColor)

    fun calculateColor(x: Int, y: Int) : CanvasColor? {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) return null
        return (this.layers.size - 1 downTo 0).mapNotNull {
            val layer = this.layers[it]
            layer.getChange(x, y)
        }.firstOrNull() ?: BASE_COLOR
    }

}
