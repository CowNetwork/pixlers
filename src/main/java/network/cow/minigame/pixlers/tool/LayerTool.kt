package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
abstract class LayerTool(canvas: Canvas) : Tool(canvas) {

    protected abstract val primaryAction: (Layer.() -> Unit)
    protected open val secondaryAction: (Layer.() -> Unit)? = null

    override fun onPrimary() : Boolean {
        if (this.cursor.x < 0 || this.cursor.x >= this.canvas.width || this.cursor.y < 0 || this.cursor.y >= this.canvas.height) return false
        val layer = Layer(this.canvas)
        val init = this.primaryAction
        layer.init()
        this.canvas.apply(layer)
        return true
    }

    override fun onSecondary() : Boolean {
        if (this.cursor.x < 0 || this.cursor.x >= this.canvas.width || this.cursor.y < 0 || this.cursor.y >= this.canvas.height) return false
        val init = this.secondaryAction ?: return false
        val layer = Layer(this.canvas)
        layer.init()
        this.canvas.apply(layer)
        return true
    }

}
