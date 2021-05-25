package network.cow.minigame.pixlers.api.tool

import network.cow.minigame.pixlers.api.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class StateTool(canvas: Canvas) : Tool(canvas) {

    override fun onPrimary() = this.canvas.undo()

    override fun onSecondary() = this.canvas.redo()

}
