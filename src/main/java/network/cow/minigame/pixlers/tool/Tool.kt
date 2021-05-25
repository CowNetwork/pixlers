package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
abstract class Tool {

    protected abstract val action: (Action.(Int, Int) -> Unit)

    open var color: CanvasColor = CanvasColor.WHITE

    fun apply(canvas: Canvas, cursorX: Int, cursorY: Int) : Action {
        val action = Action(canvas)
        val init = this.action
        action.init(cursorX, cursorY)
        action.apply()
        return action
    }

}
