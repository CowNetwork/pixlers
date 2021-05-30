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
class RatePhaseWorldProvider(game: SpigotGame, config: WorldProviderConfig) : WorldProvider(game, config) {

    override fun getSpawnLocations(actor: SpigotActor?): List<Location> {
        // TODO: read from world config
        return listOf(Location(this.game.world, 254.0, 153.0, 62.0, 180.0F, 0.0F))
    }

    override fun selectWorld(): World = this.game.world

}
