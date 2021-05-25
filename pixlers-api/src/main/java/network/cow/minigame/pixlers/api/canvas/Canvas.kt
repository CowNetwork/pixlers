package network.cow.minigame.pixlers.api.canvas

import network.cow.minigame.pixlers.api.CanvasColor
import network.cow.minigame.pixlers.api.tool.Layer
import java.util.Stack

/**
 * @author Benedikt Wüller
 */
abstract class Canvas(val width: Int, val height: Int) {

    companion object {
        val BASE_COLOR = CanvasColor.WHITE
    }

    private val redoLayers = Stack<Layer>()
    private val layers = Stack<Layer>()

    var currentColor = BASE_COLOR

    fun apply(layer: Layer) {
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
