package network.cow.minigame.pixlers.phase

import network.cow.minigame.noma.api.actor.Actor
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.ColorPalette
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
class DrawPhase(game: SpigotGame, config: PhaseConfig<Player, SpigotGame>) : SpigotPhase(game, config) {

    private val canvases = mutableMapOf<Actor<Player>, Canvas>()
    private val toolboxes = mutableMapOf<Player, ToolBox>()

    override fun onPlayerJoin(player: Player) {
        player.isFlying = true
    }

    override fun onPlayerLeave(player: Player) {
        this.canvases.values.filterIsInstance<BlockCanvas>().forEach { it.removePlayer(player) }
        val toolbox = this.toolboxes.remove(player) ?: return
        toolbox.remove()
        toolbox.canvas
    }

    override fun onStart() {
        val palette = this.game.store.get(ColorPalette.STORE_KEY) ?: ColorPalette(ColorPalette.Type.FULL)
        palette.draw(this.game.world.getBlockAt(-39, 70, -12))

        this.game.getActors().forEach {
            // TODO: read location from world config
            this.canvases[it] = BlockCanvas(
                this.game.world.getBlockAt(-38, 113, -15),
                BlockFace.SOUTH,
                80, 40,
                *it.getPlayers().toTypedArray(),
                palette = palette
            )
        }

        this.game.getIngamePlayers().forEach {
            it.gameMode = GameMode.CREATIVE
            it.isFlying = true

            val toolbox = ToolBox(it, this.canvases[this.game.getActor(it)]!!, palette)
            toolbox.apply()
            this.toolboxes[it] = toolbox
        }

        this.game.getSpigotActors().forEach {
            it.apply { player ->
                player.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 1, true, false, false))
            }
        }
    }

    override fun onStop() {
        this.storeMiddleware.store(Store.STORE_KEY_DRAWINGS, this.canvases.map { it.toPair() })

        this.toolboxes.values.forEach { it.remove() }
        this.toolboxes.clear()

        Bukkit.getOnlinePlayers().forEach { it.removePotionEffect(PotionEffectType.INVISIBILITY) }
    }

    override fun onTimeout() = Unit

}
