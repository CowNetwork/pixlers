package network.cow.minigame.pixlers.canvas

import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class CompoundCanvas(vararg canvases: Canvas) : Canvas(canvases.first().width, canvases.first().width, canvases.first().palette) {

    val canvases = mutableListOf(*canvases)

    init {
        for (it in canvases) {
            if (it.width != this.canvases.first().width || it.height != this.canvases.first().height) {
                error("All canvases must be the same dimensions.")
            }
        }
    }

    override fun drawColor(x: Int, y: Int, color: Int) = this.canvases.forEach { it.drawColor(x, y, color) }

    override fun drawColor(player: Player, x: Int, y: Int, color: Int) = this.canvases.forEach { it.drawColor(player, x, y, color) }

}
