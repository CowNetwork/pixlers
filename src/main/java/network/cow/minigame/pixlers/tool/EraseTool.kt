package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class EraseTool : PaintTool() {

    override var color: CanvasColor
        get() = Canvas.BASE_COLOR
        set(_) = Unit

}
