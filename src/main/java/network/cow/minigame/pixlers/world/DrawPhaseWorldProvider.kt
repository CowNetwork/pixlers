package network.cow.minigame.pixlers.world

import network.cow.minigame.noma.spigot.SpigotActor
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.config.WorldProviderConfig
import network.cow.minigame.noma.spigot.world.WorldProvider
import org.bukkit.Location
import org.bukkit.World

/**
 * @author Benedikt Wüller
 */
class DrawPhaseWorldProvider(game: SpigotGame, config: WorldProviderConfig) : WorldProvider(game, config) {

    override fun getSpawnLocations(actor: SpigotActor?): List<Location> {
        // TODO: read from world config
        return listOf(Location(this.game.world, 0.5, 95.0, 45.5, 180.0F, 0.0F))
    }

    override fun selectWorld(): World = this.game.world

}
