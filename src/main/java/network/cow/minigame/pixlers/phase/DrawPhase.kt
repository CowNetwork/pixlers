package network.cow.minigame.pixlers.phase

import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.CanvasColor
import network.cow.minigame.pixlers.Store
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.tool.ToolBox
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * @author Benedikt Wüller
 */
class DrawPhase(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase(game, config) {

    private val toolboxes = mutableMapOf<Player, ToolBox>()

    override fun onPlayerJoin(player: Player) {
        player.isFlying = true
    }

    override fun onPlayerLeave(player: Player) {
        val toolbox = this.toolboxes.remove(player) ?: return
        toolbox.remove()
        val canvas = toolbox.canvas
        if (canvas !is BlockCanvas) return
        canvas.removePlayer(player)
    }

    override fun onStart() {
        this.game.getIngamePlayers().forEach {
            it.gameMode = GameMode.CREATIVE
            it.isFlying = true

            // TODO: read location from world config
            val canvas = BlockCanvas(it.world.getBlockAt(-38, 113, -15), BlockFace.SOUTH, 80, 40, it)
            val toolbox = ToolBox(it, canvas)
            toolbox.apply()
            this.toolboxes[it] = toolbox
            canvas.currentColor = CanvasColor.BLACK
        }

        (this.game as SpigotGame).getSpigotActors().forEach {
            it.apply { player ->
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 1, true, false, false))
            }
        }
    }

    override fun onStop() {
        val drawings = mutableListOf<Pair<Player, Canvas>>()
        this.toolboxes.forEach { (player, toolbox) ->
            drawings.add(player to toolbox.canvas)
        }

        this.storeMiddleware.store(Store.STORE_KEY_DRAWINGS, drawings)

        this.toolboxes.values.forEach { it.remove() }
        this.toolboxes.clear()

        Bukkit.getOnlinePlayers().forEach { it.removePotionEffect(PotionEffectType.INVISIBILITY) }
    }

    override fun onTimeout() = Unit

}
