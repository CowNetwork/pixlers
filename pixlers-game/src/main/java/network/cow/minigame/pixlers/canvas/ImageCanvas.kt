package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.ColorPalette
import org.bukkit.entity.Player
import java.awt.image.BufferedImage

/**
 * @author Benedikt Wüller
 */
class ImageCanvas(width: Int, height: Int, palette: ColorPalette) : Canvas(width, height, palette) {

    private val image = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)

    init {
        (0 until this.height).forEach { y ->
            (0 until this.width).forEach { x ->
                this.drawColor(x, y, this.palette.baseColor)
            }
        }
    }

    override fun drawColor(x: Int, y: Int, color: Int) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) error("The coordinates are out of bounds. Coordinates: ${x}x$y, Dimensions: ${this.width}x${this.height}")
        this.image.setRGB(x, y, this.palette.getColor(color).rgb)
    }

    override fun drawColor(player: Player, x: Int, y: Int, color: Int) = Unit

}
