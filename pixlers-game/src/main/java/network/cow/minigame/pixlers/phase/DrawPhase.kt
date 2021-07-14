package network.cow.minigame.pixlers.phase

import net.kyori.adventure.title.Title
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.info
import network.cow.messages.adventure.plus
import network.cow.messages.adventure.translate
import network.cow.messages.core.Gradients
import network.cow.messages.spigot.sendInfo
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotActor
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.noma.spigot.phase.VotePhase
import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.StoreKeys
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.canvas.CompoundCanvas
import network.cow.minigame.pixlers.canvas.ImageCanvas
import network.cow.minigame.pixlers.tool.ToolBox
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class DrawPhase(game: SpigotGame, config: PhaseConfig<Player, SpigotGame>) : SpigotPhase(game, config) {

    private val canvases = mutableMapOf<SpigotActor, Canvas>()
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
        val duration = this.game.store.get(StoreKeys.DURATION) ?: this.config.timeout.duration
        this.config.timeout.duration = duration

        val palette = this.game.store.get(StoreKeys.PALETTE) ?: ColorPalette(ColorPalette.Type.FULL)
        palette.draw(this.game.world.getBlockAt(-31, 0, 63))

        val topic = this.game.store.get(StoreKeys.FORCED_TOPIC)
            ?: this.game.store.get<VotePhase.Result<String>>(StoreKeys.VOTED_TOPIC)?.items?.first()?.value
            ?: error("No topic has been voted or selected.")

        Bukkit.getOnlinePlayers().forEach {
            val translatedTopic = topic.translate(it).gradient(Gradients.CORPORATE)
            it.showTitle(Title.title(translatedTopic, "Dieser Begriff muss gemalt werden".info())) // TODO: translate
            it.sendInfo("Dieser Begriff muss gemalt werden: ".comp() + translatedTopic) // TODO: translate
        }

        this.game.getSpigotActors().forEach {
            // TODO: read location from world config

            val canvas = CompoundCanvas(
                BlockCanvas(
                    this.game.world.getBlockAt(69, 83, 85),
                    BlockFace.NORTH,
                    120, 70,
                    *it.getPlayers().toTypedArray(),
                    palette = palette
                ),
                ImageCanvas(120, 70, palette, 4)
            )
            this.canvases[it] = canvas

            it.apply { player -> player.isInvisible = true }
        }

        this.game.getIngamePlayers().forEach {
            it.gameMode = GameMode.CREATIVE
            it.isFlying = true

            val toolbox = ToolBox(it, this.canvases[this.game.getActor(it)]!!, palette)
            toolbox.apply()
            this.toolboxes[it] = toolbox
        }
    }

    override fun onStop() {
        this.storeMiddleware.store(StoreKeys.STORE_KEY_DRAWINGS, this.canvases.map { it.toPair() })

        this.toolboxes.values.forEach { it.remove() }
        this.toolboxes.clear()

        Bukkit.getOnlinePlayers().forEach { it.isInvisible = false }
    }

    override fun onTimeout() = Unit

}
