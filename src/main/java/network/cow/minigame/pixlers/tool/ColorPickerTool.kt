package network.cow.minigame.pixlers.tool

import network.cow.minigame.pixlers.CanvasColor

/**
 * @author Benedikt Wüller
 */
class ColorPickerTool(onPick: (CanvasColor) -> Unit) : Tool() {

    override val primaryAction: Action.(Int, Int) -> Unit = { x, y ->
        this.getColor(x, y)?.let(onPick)
    }

}
