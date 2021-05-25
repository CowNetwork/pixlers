package network.cow.minigame.pixlers.api.tool

import network.cow.minigame.pixlers.api.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class ClearTool(canvas: Canvas) : LayerTool(canvas) {

    override val primaryAction: Layer.() -> Unit = {
        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                this.setColor(x, y, Canvas.BASE_COLOR)
            }
        }
    }

}
