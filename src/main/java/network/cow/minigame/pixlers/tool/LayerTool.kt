package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
abstract class LayerTool(canvas: Canvas) : Tool(canvas) {

    protected abstract val primaryAction: (Layer.() -> Unit)
    protected open val secondaryAction: (Layer.() -> Unit)? = null

    override fun onPrimary() {
        val layer = Layer(this.canvas)
        val init = this.primaryAction
        layer.init()
        this.canvas.apply(layer)
    }

    override fun onSecondary() {
        val layer = Layer(this.canvas)
        val init = this.secondaryAction ?: return
        layer.init()
        this.canvas.apply(layer)
    }

}
