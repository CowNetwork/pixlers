package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class ColorPickerTool(canvas: Canvas) : Tool(canvas) {

    override fun onPrimary() {
        this.canvas.currentColor = this.canvas.calculateColor(this.cursorX, this.cursorY) ?: return
    }

}
