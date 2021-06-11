package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.ColorPalette
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class CompoundCanvas(vararg canvas: Canvas, palette: ColorPalette) : Canvas(canvas.first().width, canvas.first().width, palette) {

    val canvases = mutableListOf<Canvas>()

    init {
        for (it in canvas) {
            if (it.width != this.width || it.height != this.height) {
                error("All canvases must be the same dimensions.")
            }
        }
    }

    override fun drawColor(x: Int, y: Int, color: Int) = this.canvases.forEach { it.drawColor(x, y, color) }

    override fun drawColor(player: Player, x: Int, y: Int, color: Int) = this.canvases.forEach { it.drawColor(player, x, y, color) }

}
