package network.cow.minigame.pixlers.phase

import com.google.common.collect.HashBiMap
import network.cow.messages.adventure.corporate
import network.cow.minigame.noma.api.Game
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotCountdownTimer
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.EndPhase
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.Store
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.canvas.CompoundCanvas
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
import java.util.Stack
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class RatePhase(game: Game<Player>, config: PhaseConfig<Player>) : SpigotPhase(game, config) {

    private lateinit var canvases: List<BlockCanvas>
    private val ratings: Queue<Pair<Player, Canvas>> = LinkedList()
    private val results = Stack<Canvas>()

    private val canvasPlayers = mutableMapOf<Canvas, Player>()

    private val canvasMappings = HashBiMap.create<Canvas, BlockCanvas>()
    private val selections = mutableMapOf<Player, Canvas>()

    private val timer = SpigotCountdownTimer(30L).onDone(::next)

    private lateinit var startTask: BukkitTask
    private lateinit var particleTask: BukkitTask

    override fun onPlayerJoin(player: Player) {
        if (!this::canvases.isInitialized) return
        this.canvases.forEach { it.addPlayer(player) }
    }

    override fun onPlayerLeave(player: Player) {
        if (!this::canvases.isInitialized) return
        this.canvases.forEach { it.removePlayer(player) }
    }

    override fun onStart() {
        val world = (this.game as SpigotGame).world

        val drawings = this.game.store.get<List<Pair<Player, Canvas>>>(Store.STORE_KEY_DRAWINGS) ?: emptyList()
        this.ratings.addAll(drawings.shuffled())

        val plugin = JavaPlugin.getPlugin(PixlersPlugin::class.java)
        this.startTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val players = Bukkit.getOnlinePlayers().toTypedArray()

            this.canvases = listOf(
                    BlockCanvas(world.getBlockAt(168, 192, -11), BlockFace.SOUTH, 80, 40, *players),
                    BlockCanvas(world.getBlockAt(259, 192, -11), BlockFace.SOUTH, 80, 40, *players),
                    BlockCanvas(world.getBlockAt(214, 143, -11), BlockFace.SOUTH, 80, 40, *players),
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

            val results = ratings.entries.map { it.toPair() }.sortedByDescending { it.second }
            val winner = results.first()
            results.drop(1).forEach { this.results.add(it.first) }

            val canvas = winner.first
            this.ratings.add(this.canvasPlayers[canvas]!! to canvas)
        }

        this.selections.clear()
        this.canvasMappings.clear()
        this.canvasPlayers.clear()

        this.canvases.forEach {
            it.refresh()
            if (this.ratings.isEmpty()) return@forEach

            val (player, canvas) = this.ratings.poll()
            this.canvasPlayers[canvas] = player
            this.canvasMappings[canvas] = it

            for (x in 0 until canvas.width) {
                for (y in 0 until canvas.height) {
                    it.drawColor(x, y, canvas.calculateColor(x, y) ?: Canvas.BASE_COLOR)
                }
            }
        }

        // Make sure to stop this phase, if there are not enough drawing to vote on.
        if (!this.validate()) return

        this.timer.start()
    }

    private fun validate() : Boolean {
        if (this.ratings.size >= 2) return true
        this.game.nextPhase(true)
        return false
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

        val winners = this.ratings.map { it.first }
        this.storeMiddleware.store(EndPhase.STORE_KEY_WINNERS, winners)
    }

    override fun onTimeout() = Unit

}
