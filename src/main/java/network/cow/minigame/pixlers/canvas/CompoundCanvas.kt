package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.CanvasColor

/**
 * @author Benedikt Wüller
 */
class CompoundCanvas(vararg canvas: Canvas) : Canvas(canvas.first().width, canvas.first().width) {

    private val canvas = mutableListOf<Canvas>()

    init {
        for (it in canvas) {
            if (it.width != this.width || it.height != this.height) {
                error("All canvas must be the same dimensions.")
            }
        }
    }

    override fun setColor(x: Int, y: Int, color: CanvasColor) {
        this.canvas.forEach { it.setColor(x, y, color) }
    }

    override fun getColor(x: Int, y: Int): CanvasColor? {
        return this.canvas.first().getColor(x, y)
    }

}
