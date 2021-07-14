package network.cow.minigame.pixlers.world

import network.cow.minigame.noma.spigot.SpigotActor
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.config.WorldProviderConfig
import network.cow.minigame.noma.spigot.world.provider.WorldProvider
import org.bukkit.Bukkit
import org.bukkit.GameRule
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.WorldType

/**
 * @author Benedikt Wüller
 */
class DrawPhaseWorldProvider(game: SpigotGame, config: WorldProviderConfig) : WorldProvider(game, config) {

    override fun getSpawnLocations(actor: SpigotActor?): List<Location> {
        // TODO: read from world config
        return listOf(Location(this.game.world, 10.0, 49.0, 18.0, 0.0F, 0.0F))
    }

    override fun selectWorld(): World {
        // TODO: read world from config
        val world = Bukkit.createWorld(WorldCreator("pixlers-cinema").generateStructures(false).type(WorldType.FLAT))!!
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        return world
    }

}
