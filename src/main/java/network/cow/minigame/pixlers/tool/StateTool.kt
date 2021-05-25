package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class StateTool(canvas: Canvas) : Tool(canvas) {

    override fun onPrimary() = this.canvas.undo()

    override fun onSecondary() = this.canvas.redo()

}
