package network.cow.minigame.pixlers.tool

/**
 * @author Benedikt Wüller
 */
class StateTool(onUndo: () -> Unit, onRedo: () -> Unit) : Tool() {

    override val primaryAction: Action.(Int, Int) -> Unit = { _, _ -> onUndo() }

    override val secondaryAction: Action.(Int, Int) -> Unit = { _, _ -> onRedo() }

}
