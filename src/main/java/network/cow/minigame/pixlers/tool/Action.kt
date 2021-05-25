package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class Action(private val canvas: Canvas) {

    val width = this.canvas.width
    val height = this.canvas.height

    private val previous = mutableMapOf<Pair<Int, Int>, CanvasColor>()
    private val changes = mutableMapOf<Pair<Int, Int>, CanvasColor>()

    fun setColor(x: Int, y: Int, color: CanvasColor) {
        if (x < 0 || x >= this.canvas.width || y < 0 || y >= this.canvas.height) return
        this.previous[x to y] = this.canvas.getColor(x, y)!!
        this.changes[x to y] = color
    }

    fun unsetColor(x: Int, y: Int) {
        if (x < 0 || x >= this.canvas.width || y < 0 || y >= this.canvas.height) return
        this.previous.remove(x to y)
        this.changes.remove(x to y)
    }

    fun getColor(x: Int, y: Int) : CanvasColor? {
        return this.changes[x to y] ?: this.canvas.getColor(x, y)
    }

    fun apply() {
        this.changes.forEach { (coords, color) -> this.canvas.setColor(coords.first, coords.second, color) }
    }

    fun revert() {
        this.previous.forEach { (coords, color) -> this.canvas.setColor(coords.first, coords.second, color) }
    }

}
