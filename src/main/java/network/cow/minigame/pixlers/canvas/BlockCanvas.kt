package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.api.CanvasColor
import network.cow.minigame.pixlers.api.canvas.Canvas
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock

/**
 * @author Benedikt Wüller
 */
class BlockCanvas(private val origin: Block, width: Int, height: Int) : Canvas(width, height) {

    override fun drawColor(x: Int, y: Int, color: CanvasColor) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) error("The coordinates are out of bounds. Coordinates: ${x}x$y, Dimensions: ${this.width}x${this.height}")
        val block = this.origin.getRelative(x, this.height - y, 0)
        block.type = Material.NOTE_BLOCK
        (block.blockData as NoteBlock).apply {
            this.instrument = color.instrument
            this.note = color.note
        }
    }

}
