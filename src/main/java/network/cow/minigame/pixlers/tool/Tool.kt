package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
abstract class Tool(protected val canvas: Canvas) {

    protected val cursor = Point(-1, -1)

    var onUpdateItem: () -> Unit = {}

    fun updateCursor(x: Int, y: Int) {
        this.cursor.x = x
        this.cursor.y = y
        this.onCursorMoved()
    }

    protected open fun onCursorMoved() = Unit

    fun executePrimary() : Boolean {
        return this.onPrimary()
    }

    protected abstract fun onPrimary() : Boolean

    fun executeSecondary() : Boolean {
        return this.onSecondary()
    }

    protected open fun onSecondary() : Boolean = false

    fun cancel() = this.onCancel()

    protected open fun onCancel() = Unit

    abstract fun getItemStack(player: Player) : ItemStack

}
