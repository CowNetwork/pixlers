package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.CanvasColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.NoteBlock
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
class BlockCanvas(private val origin: Block, facing: BlockFace, width: Int, height: Int) : Canvas(width, height) {

    private val blockFaceX = when (facing) {
        BlockFace.NORTH -> BlockFace.WEST
        BlockFace.EAST -> BlockFace.NORTH
        BlockFace.SOUTH -> BlockFace.EAST
        BlockFace.WEST -> BlockFace.SOUTH
        else -> BlockFace.EAST
    }

    override fun drawColor(x: Int, y: Int, color: CanvasColor) {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) error("The coordinates are out of bounds. Coordinates: ${x}x$y, Dimensions: ${this.width}x${this.height}")
        val block = this.origin.getRelative(this.blockFaceX, x).getRelative(BlockFace.DOWN, y)
        block.setType(Material.NOTE_BLOCK, false)
        val data = block.blockData as NoteBlock
        data.instrument = color.instrument
        data.note = color.note
        block.setBlockData(data, false)
    }

    fun getPoint(block: Block) : Point {
        val x: Int
        when (this.blockFaceX) {
            BlockFace.NORTH -> {
                if (this.origin.x != block.x) return Point(-1, -1)
                x = this.origin.z - block.z
            }
            BlockFace.EAST -> {
                if (this.origin.z != block.z) return Point(-1, -1)
                x = block.x - this.origin.x
            }
            BlockFace.SOUTH -> {
                if (this.origin.x != block.x) return Point(-1, -1)
                x = block.z - this.origin.z
            }
            BlockFace.WEST -> {
                if (this.origin.z != block.z) return Point(-1, -1)
                x = this.origin.x - block.x
            }
            else -> error("Invalid block face for x axis: ${this.blockFaceX}")
        }

        val y = this.origin.y - block.y
        return Point(x, y)
    }

}
