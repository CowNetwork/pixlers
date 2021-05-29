package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class Layer(private val canvas: Canvas) {

    val width = this.canvas.width
    val height = this.canvas.height

    private val changes = mutableMapOf<Int, CanvasColor>()

    fun setColor(x: Int, y: Int, color: CanvasColor) {
        if (x < 0 || x >= this.canvas.width || y < 0 || y >= this.canvas.height) return
        if (this.canvas.calculateColor(x, y) == color) return
        this.changes[y * this.width + x] = color
    }

    fun unsetColor(x: Int, y: Int) {
        if (x < 0 || x >= this.canvas.width || y < 0 || y >= this.canvas.height) return
        this.changes.remove(y * this.width + x)
    }

    fun getColor(x: Int, y: Int) = this.changes[y * this.width + x] ?: this.canvas.calculateColor(x, y)

    fun getChanges() : Map<Point, CanvasColor> {
        val map = mutableMapOf<Point, CanvasColor>()
        this.changes.entries.forEach { (coords, color) ->
            val point = Point(coords % this.width, coords / this.width)
            map[point] = color
        }
        return map
    }

    fun getChange(x: Int, y: Int) = this.changes[y * this.width + x]

    fun clear() = this.changes.clear()

    fun isEmpty() = this.changes.isEmpty()

    fun calculateDifference(other: Layer) : Map<Point, CanvasColor> {
        val points = mutableMapOf<Point, CanvasColor>()

        val intersection = this.changes.keys.intersect(other.changes.keys)
        val keys = this.changes.keys.union(other.changes.keys).subtract(intersection)

        keys.forEach { coords ->
            val point = Point(coords % this.width, coords / this.width)
            points[point] = (this.changes[coords] ?: other.changes[coords])!!
        }

        return points
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Layer) return false
        if (this.width != other.width) return false
        if (this.height != other.height) return false
        if (this.changes.size != other.changes.size) return false
        return this.changes.entries.all { (coords, color) ->
            other.changes[coords] == color
        }
    }

    override fun hashCode(): Int {
        var result = canvas.hashCode()
        result = 31 * result + width
        result = 31 * result + height
        return result
    }

}
