package network.cow.minigame.pixlers

import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class DrawPhase(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase(game, config) {

    override fun onPlayerJoin(player: Player) {
        // TODO
    }

    override fun onPlayerLeave(player: Player) {
        // TODO
    }

    override fun onStart() {
        // TODO: spawn location relative to plot
        val location = Location((this.game as SpigotGame).world, 0.5, 95.0, 45.5)
        Bukkit.getOnlinePlayers().forEach { it.teleport(location) }
    }

    override fun onStop() {
        // TODO
    }

    override fun onTimeout() {
        // TODO
    }

}
