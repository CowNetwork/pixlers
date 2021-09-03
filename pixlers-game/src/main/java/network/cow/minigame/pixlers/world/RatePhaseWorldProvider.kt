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
import java.nio.file.Paths
import java.util.UUID

/**
 * @author Benedikt Wüller
 */
class RatePhaseWorldProvider(game: SpigotGame, config: WorldProviderConfig) : WorldProvider(game, config) {

    override fun getSpawnLocations(actor: SpigotActor?): List<Location> {
        // TODO: read from world config
        return listOf(Location(this.game.world, -222.0, 110.0, -82.0, 0.0F, 0.0F))
    }

    override fun selectWorld(): World {
        // TODO: read world from config

        val targetName = UUID.randomUUID().toString()
        Paths.get("plugins/Pixlers/maps/pixlers-eval").toFile().copyRecursively(Paths.get(this.game.config.workingDirectory, targetName).toFile(), overwrite = true)

        val world = Bukkit.createWorld(WorldCreator(targetName).generateStructures(false).type(WorldType.FLAT))!!
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, false)
        world.setGameRule(GameRule.DO_MOB_SPAWNING, false)
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
        return world
    }

}
