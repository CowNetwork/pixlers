package network.cow.minigame.pixlers.canvas

import network.cow.minigame.pixlers.ColorPalette
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox
import java.awt.Point
import java.awt.geom.Point2D
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class BlockCanvas(
    private val origin: Block, private val facing: BlockFace, width: Int, height: Int,
    vararg players: Player, palette: ColorPalette, val isVirtual: Boolean = true
) : Canvas(width, height, palette) {

    companion object {
        private const val MAX_RAY_TRACING_DISTANCE = 200.0
    }

    private val players = mutableSetOf(*players)

    private val blockFaceX = when (facing) {
        BlockFace.NORTH -> BlockFace.WEST
        BlockFace.EAST -> BlockFace.NORTH
        BlockFace.SOUTH -> BlockFace.EAST
        BlockFace.WEST -> BlockFace.SOUTH
        else -> BlockFace.EAST
    }

    val boundingBox: BoundingBox

    init {
        val from = this.origin.boundingBox
        val to = this.origin.getRelative(this.blockFaceX, this.width - 1).getRelative(BlockFace.DOWN, this.height - 1).boundingBox
        this.boundingBox = from.clone().union(to.clone())

        (0 until this.height).forEach { y ->
            (0 until this.width).forEach { x ->
                this.drawColor(x, y, this.palette.baseColor)
            }
        }
    }

    override fun drawColor(x: Int, y: Int, color: Int) = this.drawColor(x, y, color, *this.players.toTypedArray())

    override fun drawColor(player: Player, x: Int, y: Int, color: Int) = this.drawColor(x, y, color, player)

    private fun drawColor(x: Int, y: Int, color: Int, vararg players: Player) {
        val block = this.getBlockAt(x, y)

        if (this.isVirtual) {
            val data = Bukkit.createBlockData(Material.NOTE_BLOCK) as NoteBlock
            this.palette.applyBlockData(color, data)
            players.forEach { it.sendBlockChange(block.location, data) }
        } else {
            this.palette.setBlock(color, block)
        }
    }

    fun getBlockAt(x: Int, y: Int): Block {
        if (x < 0 || x >= this.width || y < 0 || y >= this.height) error("The coordinates are out of bounds. Coordinates: ${x}x$y, Dimensions: ${this.width}x${this.height}")
        return origin.getRelative(blockFaceX, x).getRelative(BlockFace.DOWN, y)
    }

    fun getBlockAt(point: Point) = this.getBlockAt(point.x, point.y)

    fun calculatePointOnCanvas(player: Player): Point? {
        val boundingBox = this.boundingBox
        val rayTrace = boundingBox.rayTrace(player.eyeLocation.toVector(), player.location.direction, MAX_RAY_TRACING_DISTANCE) ?: return null
        val position = rayTrace.hitPosition

        val point = when (this.facing) {
            BlockFace.NORTH -> Point2D.Double(boundingBox.maxX - position.x, boundingBox.maxY - position.y)
            BlockFace.SOUTH -> Point2D.Double(position.x - boundingBox.minX, boundingBox.maxY - position.y)
            BlockFace.EAST -> Point2D.Double(boundingBox.maxZ - position.z, boundingBox.maxY - position.y)
            BlockFace.WEST -> Point2D.Double(position.z - boundingBox.minZ, boundingBox.maxY - position.y)
            else -> return null
        }

        return Point(point.x.roundToInt(), point.y .roundToInt())
    }

    fun removePlayer(player: Player) {
        if (!this.isVirtual) return
        this.players.remove(player)

        (0 until this.height).forEach { y ->
            (0 until this.width).forEach { x ->
                this.drawColor(x, y, this.palette.baseColor, player)
            }
        }
    }

    fun addPlayer(player: Player) {
        if (!this.isVirtual) return
        this.players.add(player)
        (0 until this.height).forEach { y ->
            (0 until this.width).forEach { x ->
                this.drawColor(x, y, this.calculateColor(x, y) ?: this.palette.baseColor, player)
            }
        }
    }

}
