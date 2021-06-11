package network.cow.minigame.pixlers.phase

import com.google.common.collect.HashBiMap
import network.cow.messages.adventure.corporate
import network.cow.minigame.noma.api.actor.Actor
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotCountdownTimer
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.Store
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask
import java.util.LinkedList
import java.util.Queue
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class RatePhase(game: SpigotGame, config: PhaseConfig<Player, SpigotGame>) : SpigotPhase(game, config) {

    private lateinit var canvases: List<BlockCanvas>
    private val drawings: Queue<Pair<Actor<Player>, Canvas>> = LinkedList()

    private val canvasActors = mutableMapOf<Canvas, Actor<Player>>()
    private val canvasMappings = HashBiMap.create<Canvas, BlockCanvas>()
    private val selections = mutableMapOf<Player, Canvas>()

    private val timer = SpigotCountdownTimer(30L).onDone(::next)

    private lateinit var startTask: BukkitTask
    private lateinit var particleTask: BukkitTask

    private lateinit var palette: ColorPalette

    override fun onPlayerJoin(player: Player) {
        if (!this::canvases.isInitialized) return
        this.canvases.forEach { it.addPlayer(player) }
    }

    override fun onPlayerLeave(player: Player) {
        if (!this::canvases.isInitialized) return
        this.canvases.forEach { it.removePlayer(player) }
    }

    override fun onStart() {
        this.palette = this.game.store.get(ColorPalette.STORE_KEY) ?: ColorPalette(ColorPalette.Type.FULL)

        val world = this.game.world

        val drawings = this.game.store.get<List<Pair<Actor<Player>, Canvas>>>(Store.STORE_KEY_DRAWINGS) ?: emptyList()
        this.drawings.addAll(drawings.shuffled())

        val plugin = JavaPlugin.getPlugin(PixlersPlugin::class.java)
        this.startTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val players = Bukkit.getOnlinePlayers().toTypedArray()

            this.canvases = listOf(
                    BlockCanvas(world.getBlockAt(168, 192, -11), BlockFace.SOUTH, 80, 40, *players, palette = palette),
                    BlockCanvas(world.getBlockAt(259, 192, -11), BlockFace.SOUTH, 80, 40, *players, palette = palette),
                    BlockCanvas(world.getBlockAt(214, 143, -11), BlockFace.SOUTH, 80, 40, *players, palette = palette),
            )

            this.game.getIngamePlayers().forEach {
                val item = ItemBuilder(Material.NAME_TAG)
                        .name("Wähle deinen Favoriten".corporate()) // TODO: translate
                        .build()
                it.inventory.addItem(item)
            }

            this.next()
        }, 20L)

        this.particleTask = Bukkit.getScheduler().runTaskTimer(plugin, Runnable {
            this.selections.forEach { (player, canvas) ->
                val mappedCanvas = this.canvasMappings[canvas] ?: return@forEach
                val boundingBox = mappedCanvas.boundingBox.clone().expand(0.25)

                val particlesPerBlock = 4
                val particlesX = (boundingBox.widthX * particlesPerBlock).roundToInt()
                val particlesY = (boundingBox.height * particlesPerBlock).roundToInt()
                val particlesZ = (boundingBox.widthZ * particlesPerBlock).roundToInt()

                val stepSize = 1.0 / particlesPerBlock
                (0 until particlesX).forEach {
                    player.spawnParticle(Particle.FLAME, boundingBox.minX + it * stepSize, boundingBox.minY, boundingBox.minZ, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.minX + it * stepSize, boundingBox.minY, boundingBox.maxZ, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.minX + it * stepSize, boundingBox.maxY, boundingBox.minZ, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.minX + it * stepSize, boundingBox.maxY, boundingBox.maxZ, 1, 0.0, 0.0, 0.0, 0.0)
                }

                (0 until particlesY).forEach {
                    player.spawnParticle(Particle.FLAME, boundingBox.minX, boundingBox.minY + it * stepSize, boundingBox.minZ, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.minX, boundingBox.minY + it * stepSize, boundingBox.maxZ, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.maxX, boundingBox.minY + it * stepSize, boundingBox.minZ, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.maxX, boundingBox.minY + it * stepSize, boundingBox.maxZ, 1, 0.0, 0.0, 0.0, 0.0)
                }

                (0 until particlesZ).forEach {
                    player.spawnParticle(Particle.FLAME, boundingBox.minX, boundingBox.minY, boundingBox.minZ + it * stepSize, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.minX, boundingBox.maxY, boundingBox.minZ + it * stepSize, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.maxX, boundingBox.minY, boundingBox.minZ + it * stepSize, 1, 0.0, 0.0, 0.0, 0.0)
                    player.spawnParticle(Particle.FLAME, boundingBox.maxX, boundingBox.maxY, boundingBox.minZ + it * stepSize, 1, 0.0, 0.0, 0.0, 0.0)
                }
            }
        }, 4L, 4L)
    }

    private fun next() {
        this.timer.reset()

        if (this.selections.isNotEmpty()) {
            val ratings = mutableMapOf<Canvas, Int>()
            this.selections.forEach {
                val votes = ratings[it.value] ?: 0
                ratings[it.value] = votes + 1
            }

            val results = ratings.entries.map { it.toPair() }.shuffled().sortedByDescending { it.second }
            val canvas = results.first().first
            this.drawings.add(this.canvasActors[canvas]!! to canvas)
        }

        this.selections.clear()
        this.canvasMappings.clear()
        this.canvasActors.clear()

        val amount = when {
            this.drawings.size == 3 -> 3
            this.drawings.size < 3 || this.drawings.size == 4 -> 2
            else -> 3
        }

        (0 until amount).map { this.canvases[it] }.forEach {
            it.refresh()
            val (player, canvas) = this.drawings.poll()
            this.canvasActors[canvas] = player
            this.canvasMappings[canvas] = it

            for (x in 0 until canvas.width) {
                for (y in 0 until canvas.height) {
                    it.drawColor(x, y, canvas.calculateColor(x, y) ?: this.palette.baseColor)
                }
            }
        }

        // Make sure to stop this phase, if there are not enough drawing to vote on.
        if (this.drawings.size <= 1) {
            this.game.nextPhase(true)
            return
        }

        this.timer.start()
    }

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        event.isCancelled = true

        val player = event.player
        if (!this.game.getIngamePlayers().contains(player)) return

        val canvas = this.canvases.firstOrNull { it.calculatePointOnCanvas(player) != null } ?: return
        this.selections[player] = this.canvasMappings.inverse()[canvas] ?: return

        // TODO: send message
    }

    override fun onStop() {
        this.startTask.cancel()
        this.particleTask.cancel()

        this.timer.reset()

        if (this::canvases.isInitialized) {
            Bukkit.getOnlinePlayers().forEach { player ->
                this.canvases.forEach { it.removePlayer(player) }
            }
        }

        if (this.drawings.isNotEmpty()) {
//            this.storeMiddleware.store(EndPhase.STORE_KEY, listOf(this.drawings.first().first))
        }
    }

    override fun onTimeout() = Unit

}
