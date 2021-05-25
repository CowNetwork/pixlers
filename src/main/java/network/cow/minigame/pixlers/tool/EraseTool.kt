package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class EraseTool(canvas: Canvas) : PaintTool(canvas) {

    override fun getColor() = Canvas.BASE_COLOR

}
