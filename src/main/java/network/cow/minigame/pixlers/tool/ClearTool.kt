package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class ClearTool : Tool() {

    override var color: CanvasColor
        get() = Canvas.BASE_COLOR
        set(_) = Unit

    override val action: Action.(Int, Int) -> Unit = { _, _ ->
        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                this.setColor(x, y, color)
            }
        }
    }

}
