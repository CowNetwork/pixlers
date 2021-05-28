package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class Layer(private val canvas: Canvas) {

    val width = this.canvas.width
    val height = this.canvas.height

    private val changes = mutableMapOf<Pair<Int, Int>, CanvasColor>()

    fun setColor(x: Int, y: Int, color: CanvasColor) {
        if (x < 0 || x >= this.canvas.width || y < 0 || y >= this.canvas.height) return
        this.changes[x to y] = color
    }

    fun unsetColor(x: Int, y: Int) {
        if (x < 0 || x >= this.canvas.width || y < 0 || y >= this.canvas.height) return
        this.changes.remove(x to y)
    }

    fun getColor(x: Int, y: Int) = this.changes[x to y] ?: this.canvas.calculateColor(x, y)

    fun getChanges() : Map<Pair<Int, Int>, CanvasColor> = this.changes

    fun getChange(x: Int, y: Int) = this.changes[x to y]

    fun cleanup() {
        val keys = this.changes.entries.filter { (coords, color) ->
            this.canvas.calculateColor(coords.first, coords.second) == color
        }.map {
            it.key
        }
        keys.forEach(this.changes::remove)
    }

    fun clear() = this.changes.clear()

    fun isEmpty() = this.changes.isEmpty()

}
