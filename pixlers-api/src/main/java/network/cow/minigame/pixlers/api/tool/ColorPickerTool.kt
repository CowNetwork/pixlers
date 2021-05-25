package network.cow.minigame.pixlers.api.tool

import network.cow.minigame.pixlers.api.canvas.Canvas

/**
 * @author Benedikt Wüller
 */
class ColorPickerTool(canvas: Canvas) : Tool(canvas) {

    override fun onPrimary() {
        this.canvas.currentColor = this.canvas.calculateColor(this.cursorX, this.cursorY) ?: return
    }

}
