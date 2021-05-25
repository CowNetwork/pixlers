package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
abstract class Tool(protected val canvas: Canvas) {

    protected var cursorX: Int = -1
    protected var cursorY: Int = -1

    fun updateCursor(x: Int, y: Int) {
        this.cursorX = x
        this.cursorY = y
        this.onCursorMoved()
    }

    protected open fun onCursorMoved() = Unit

    fun executePrimary() = this.onPrimary()

    protected abstract fun onPrimary()

    fun executeSecondary() = this.onSecondary()

    protected open fun onSecondary() = Unit

    fun cancel() = this.onCancel()

    protected open fun onCancel() = Unit

}
