package network.cow.minigame.pixlers.api.canvas

import network.cow.minigame.pixlers.api.CanvasColor
import java.awt.image.BufferedImage

/**
 * @author Benedikt Wüller
 */
class ImageCanvas(width: Int, height: Int) : Canvas(width, height) {

    private val image = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB)

    init {
        (0 until height).forEach { y ->
            (0 until width).forEach { x ->
                this.image.setRGB(x, y, BASE_COLOR.color.rgb)
            }
        }
    }

    override fun drawColor(x: Int, y: Int, color: CanvasColor) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) error("The coordinates are out of bounds. Coordinates: ${x}x$y, Dimensions: ${this.width}x${this.height}")
        this.image.setRGB(x, y, color.color.rgb)
    }

}
