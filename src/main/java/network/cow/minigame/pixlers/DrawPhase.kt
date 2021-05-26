package network.cow.minigame.pixlers

import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.canvas.BlockCanvas
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class DrawPhase(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase(game, config) {

    private val toolboxes = mutableMapOf<Player, ToolBox>()

    override fun onPlayerJoin(player: Player) {
        // TODO
    }

    override fun onPlayerLeave(player: Player) {
        this.toolboxes.remove(player)?.remove()
    }

    override fun onStart() {
        // TODO: spawn location relative to plot
        val location = Location((this.game as SpigotGame).world, 0.5, 95.0, 45.5)
        Bukkit.getOnlinePlayers().forEach { it.teleport(location) }

        this.game.getIngamePlayers().forEach {
            val canvas = BlockCanvas(it.world.getBlockAt(-38, 113, -15), BlockFace.SOUTH, 80, 40)
            val toolbox = ToolBox(it, canvas)
            toolbox.apply()
            this.toolboxes[it] = toolbox
            canvas.currentColor = CanvasColor.BLACK
        }
    }

    override fun onStop() {
        // TODO
    }

    override fun onTimeout() {
        // TODO
    }

}
