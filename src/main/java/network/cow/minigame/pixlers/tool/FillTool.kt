package network.cow.minigame.pixlers.tool

import java.util.LinkedList

/**
 * @author Benedikt Wüller
 */
class FillTool : Tool() {

    override val primaryAction: Action.(Int, Int) -> Unit = { x, y ->
        val pixels = mutableListOf<Pair<Int, Int>>()
        val queue = LinkedList<Pair<Int, Int>>()
        queue.push(x to y)

        val replaceColor = this.getColor(x, y)
        do {
            val coords = queue.poll()

            val color = this.getColor(coords.first, coords.second)
            if (color != replaceColor) continue

            pixels.add(coords)
            queue.push((x - 1) to y)
            queue.push((x + 1) to y)
            queue.push(x to (y - 1))
            queue.push(x to (y + 1))
        } while (queue.isNotEmpty())

        pixels.forEach { (x, y) -> this.setColor(x, y, color) }
    }

}
