package network.cow.minigame.pixlers.phase

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent
import com.destroystokyo.paper.event.player.PlayerStopSpectatingEntityEvent
import net.kyori.adventure.title.Title
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.translate
import network.cow.messages.adventure.translateToComponent
import network.cow.messages.core.Gradients
import network.cow.messages.spigot.sendInfo
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotActor
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.noma.spigot.phase.VotePhase
import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.StoreKeys
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.canvas.CompoundCanvas
import network.cow.minigame.pixlers.canvas.ImageCanvas
import network.cow.minigame.pixlers.tool.ToolBox
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.plugin.java.JavaPlugin

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
            it.showTitle(Title.title(translatedTopic, Translations.Phases.Draw.INFO_TITLE.translate(it).info()))
            it.sendInfo(Translations.Phases.Draw.INFO_CHAT.translateToComponent(it, translatedTopic.highlight()))
        }

        this.game.getSpigotActors().forEach {
            // TODO: read location from world config

            val canvas = CompoundCanvas(
                BlockCanvas(
                    this.game.world.getBlockAt(69, 85, 90),
                    BlockFace.NORTH,
                    120, 70,
                    *it.getPlayers().toTypedArray(),
                    palette = palette
                ),
                ImageCanvas(120, 70, palette, 4)
            )
            this.canvases[it] = canvas
        }

        val plugin = JavaPlugin.getPlugin(PixlersPlugin::class.java)
        val players = this.game.getIngamePlayers()
        players.forEach { player ->
            player.gameMode = GameMode.CREATIVE
            player.isFlying = true

            val toolbox = ToolBox(player, this.canvases[this.game.getActor(player)]!!, palette)
            toolbox.apply()
            this.toolboxes[player] = toolbox

            // Hide for all ingame players.
            players.filter { it != player }.forEach { player.hidePlayer(plugin, it) }
        }
    }

    override fun onStop() {
        this.storeMiddleware.store(StoreKeys.STORE_KEY_DRAWINGS, this.canvases.map { it.toPair() })

        this.toolboxes.values.forEach { it.remove() }
        this.toolboxes.clear()

        val plugin = JavaPlugin.getPlugin(PixlersPlugin::class.java)
        val players = this.game.getIngamePlayers()
        players.forEach { player ->
            // Show for all ingame players.
            players.filter { it != player }.forEach { player.showPlayer(plugin, it) }
        }
    }

    override fun onTimeout() = Unit

    @EventHandler
    private fun onSpectate(event: PlayerStartSpectatingEntityEvent) {
        val newTarget = event.newSpectatorTarget
        if (newTarget !is Player) return
        val actor = this.game.getSpigotActor(newTarget)
        val canvas = actor?.let { this.canvases[it]?.castOrFindBlockCanvas() }
        canvas?.addPlayer(event.player)
    }

    @EventHandler
    private fun onStopSpectate(event: PlayerStopSpectatingEntityEvent) {
        val oldTarget = event.spectatorTarget
        if (oldTarget !is Player) return
        val actor = this.game.getSpigotActor(oldTarget)
        val canvas = actor?.let { this.canvases[it]?.castOrFindBlockCanvas() }
        canvas?.removePlayer(event.player)
    }

}
