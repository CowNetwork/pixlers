package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.shape.ShapeType
import network.cow.minigame.pixlers.canvas.Canvas
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class ShapeTool(canvas: Canvas) : Tool(canvas) {

    var type: ShapeType = ShapeType.LINE; private set

    private var firstPosition: Point? = null
    private var layer: Layer? = null

    override fun onPrimary() {
        if (this.firstPosition == null) {
            this.firstPosition = Point(this.cursor.x, this.cursor.y)

            val layer = Layer(this.canvas)
            this.layer = layer
            this.refreshLayer(layer)
            this.canvas.addWithoutUndo(layer)
            return
        }

        val layer = this.layer ?: return
        this.canvas.removeWithoutRedo(layer)
        this.refreshLayer(layer)
        this.canvas.apply(layer)

        this.layer = null
        this.firstPosition = null
    }

    override fun onCursorMoved() {
        val layer = this.layer ?: return
        this.canvas.removeWithoutRedo(layer)
        this.refreshLayer(layer)
        this.canvas.addWithoutUndo(layer)
    }

    private fun refreshLayer(layer: Layer) {
        val from = this.firstPosition ?: return
        val to = Point(this.cursor.x, this.cursor.y)
        val pixels = this.type.shape.calculatePixels(from, to)
        pixels.forEach { layer.setColor(it.x, it.y, this.canvas.currentColor) }
    }

    override fun onSecondary() {
        this.cancel()
        val index = ShapeType.values().indexOf(this.type)
        val nextIndex = if (index == ShapeType.values().lastIndex) 0 else index + 1
        this.type = ShapeType.values()[nextIndex]
    }

    override fun onCancel() {
        this.firstPosition = null
        this.layer?.let { this.canvas.removeWithoutRedo(it) }
    }

}
