package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
abstract class Tool {

    protected abstract val primaryAction: (Action.(Int, Int) -> Unit)
    protected open val secondaryAction: (Action.(Int, Int) -> Unit) = { x, y -> primaryAction(x, y) }

    open var color: CanvasColor = CanvasColor.WHITE

    fun executePrimaryAction(canvas: Canvas, cursorX: Int, cursorY: Int) : Action {
        val action = Action(canvas)
        val init = this.primaryAction
        action.init(cursorX, cursorY)
        action.apply()
        return action
    }

    fun executeSecondaryAction(canvas: Canvas, cursorX: Int, cursorY: Int) : Action {
        val action = Action(canvas)
        val init = this.secondaryAction
        action.init(cursorX, cursorY)
        action.apply()
        return action
    }

}
