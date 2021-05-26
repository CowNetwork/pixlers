package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.getPointsInCircle

/**
 * @author Benedikt Wüller
 */
open class SprayCanTool(canvas: Canvas) : LayerTool(canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    var size: Int = 3; private set

    override val primaryAction: Layer.() -> Unit = {
        val coordinates = getPointsInCircle(cursor, size)
        coordinates.filter { Math.random() >= 0.99 }.forEach { this.setColor(it.x, it.y, canvas.currentColor) }
    }

    override val secondaryAction: Layer.() -> Unit = {
        val index = sizes.indexOf(size)
        val nextIndex = if (index == sizes.lastIndex) 0 else index + 1
        size = sizes[nextIndex]
    }

}
