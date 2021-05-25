package network.cow.minigame.pixlers.api.tool

import network.cow.minigame.pixlers.api.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class EraseTool(canvas: Canvas) : PaintTool(canvas) {

    override fun getColor() = Canvas.BASE_COLOR

}
