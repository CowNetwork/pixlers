package network.cow.minigame.pixlers.pool

import dev.benedikt.localize.LocalizeService
import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.translate
import network.cow.minigame.noma.api.config.PoolConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.pool.SpigotPool
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */

class TopicPool(game: SpigotGame, config: PoolConfig<Player, SpigotGame>) : SpigotPool<String>(game, config) {

    companion object {
        const val PREFIX = "minigame.pixlers.topics."
    }

    override fun getKeys() : List<String> = LocalizeService.getKeys(PREFIX).map { it.replaceFirst(PREFIX, "") }

    override fun getItem(key: String): String = "$PREFIX$key"

    override fun getDisplayItem(player: Player, key: String) = ItemBuilder(Material.PAPER).name(this.getItem(key).translate(player).corporate()).build()

}
