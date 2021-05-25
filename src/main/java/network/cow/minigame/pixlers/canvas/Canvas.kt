package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.CanvasColor

/**
 * @author Benedikt Wüller
 */
abstract class Canvas(val width: Int, val height: Int) {

    companion object {
        val BASE_COLOR = CanvasColor.WHITE
    }

    abstract fun setColor(x: Int, y: Int, color: CanvasColor)

    abstract fun getColor(x: Int, y: Int) : CanvasColor?

}
