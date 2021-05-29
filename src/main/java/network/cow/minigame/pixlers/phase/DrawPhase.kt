package network.cow.minigame.pixlers.phase

import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.tool.ToolBox
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Benedikt Wüller
 */
class DrawPhase(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase(game, config) {

    private val toolboxes = mutableMapOf<Player, ToolBox>()

    override fun onPlayerJoin(player: Player) = Unit

    override fun onPlayerLeave(player: Player) {
        this.toolboxes.remove(player)?.remove()
    }

    override fun onStart() {
        // TODO: read location from world config
        val location = Location((this.game as SpigotGame).world, 0.5, 95.0, 45.5, 180.0F, 0.0F)
        Bukkit.getOnlinePlayers().forEach { it.teleport(location) }

        this.game.getIngamePlayers().forEach {
            // TODO: read location from world config
            val canvas = BlockCanvas(it, it.world.getBlockAt(-38, 113, -15), BlockFace.SOUTH, 80, 40)
            val toolbox = ToolBox(it, canvas)
            toolbox.apply()
            this.toolboxes[it] = toolbox
            canvas.currentColor = CanvasColor.BLACK
        }

        this.game.getSpigotActors().forEach {
            it.apply { player ->
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 1, true, false, false))
            }
        }
    }

    override fun onStop() {
        this.toolboxes.values.forEach { it.remove() }
        this.toolboxes.clear()
    }

    override fun onTimeout() = Unit

}
