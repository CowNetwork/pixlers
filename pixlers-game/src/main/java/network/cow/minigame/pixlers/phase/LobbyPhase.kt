package network.cow.minigame.pixlers.phase

import network.cow.cowmands.Cowmands
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.LobbyVotePhase
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.StoreKeys
import network.cow.minigame.pixlers.command.PixlersCommand
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Benedikt Wüller
 */
class LobbyPhase(game: SpigotGame, config: PhaseConfig<Player, SpigotGame>) : LobbyVotePhase(game, config) {

    override fun onStart() {
        super.onStart()
        Cowmands.register(JavaPlugin.getPlugin(PixlersPlugin::class.java), PixlersCommand(this.storeMiddleware))
    }

    override fun onStop() {
        super.onStop()
        val commandMap = Bukkit.getCommandMap()
        commandMap.getCommand("pixlers")?.unregister(commandMap)
    }

    override fun onStopped() {
        val duration = this.game.store.get<Long>(StoreKeys.DURATION) ?: return
        this.game.getPhase("draw").config.timeout.duration = duration
    }

}
