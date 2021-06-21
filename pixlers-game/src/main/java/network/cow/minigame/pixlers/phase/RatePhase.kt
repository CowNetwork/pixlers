package network.cow.minigame.pixlers.phase

import com.google.common.collect.HashBiMap
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.plus
import network.cow.messages.spigot.sendInfo
import network.cow.minigame.noma.api.config.PhaseConfig
import network.cow.minigame.noma.spigot.SpigotActor
import network.cow.minigame.noma.spigot.SpigotCountdownTimer
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.noma.spigot.phase.EndPhase
import network.cow.minigame.noma.spigot.phase.SpigotPhase
import network.cow.minigame.pixlers.ColorPalette
import network.cow.minigame.pixlers.PixlersPlugin
import network.cow.minigame.pixlers.StoreKeys
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
    private val drawings: Queue<Pair<SpigotActor, Canvas>> = LinkedList()

    private val canvasActors = mutableMapOf<Canvas, SpigotActor>()
    private val canvasMappings = HashBiMap.create<Canvas, BlockCanvas>()
    private val selections = mutableMapOf<Player, Canvas>()

    private val timer = SpigotCountdownTimer(20L).onDone(::next)

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
        this.palette = this.game.store.get(StoreKeys.PALETTE) ?: ColorPalette(ColorPalette.Type.FULL)

        val world = this.game.world

        val drawings = this.game.store.get<List<Pair<SpigotActor, Canvas>>>(StoreKeys.STORE_KEY_DRAWINGS) ?: emptyList()
        this.drawings.addAll(drawings.shuffled())

        val plugin = JavaPlugin.getPlugin(PixlersPlugin::class.java)
        this.startTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            val players = Bukkit.getOnlinePlayers().toTypedArray()

            this.canvases = listOf(
                    BlockCanvas(world.getBlockAt(168, 192, -11), BlockFace.SOUTH, 80, 40, *players, palette = palette, isVirtual = false),
                    BlockCanvas(world.getBlockAt(259, 192, -11), BlockFace.SOUTH, 80, 40, *players, palette = palette, isVirtual = false),
                    BlockCanvas(world.getBlockAt(214, 143, -11), BlockFace.SOUTH, 80, 40, *players, palette = palette, isVirtual = false),
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
                val boundingBox = mappedCanvas.boundingBox.clone().expand(0.5)

                val particlesPerBlock = 3
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

        // Make sure to stop this phase, if there are not enough drawing to vote on.
        if (this.drawings.size <= 1) {
            this.game.nextPhase(true)
            return
        }

        this.canvases.forEach {
            it.refresh()
            val (player, canvas) = this.drawings.poll() ?: return@forEach
            this.canvasActors[canvas] = player
            this.canvasMappings[canvas] = it

            for (x in 0 until canvas.width) {
                for (y in 0 until canvas.height) {
                    it.drawColor(x, y, canvas.calculateColor(x, y) ?: this.palette.baseColor)
                }
            }
        }

        this.timer.start()
    }

    @EventHandler
    private fun onInteract(event: PlayerInteractEvent) {
        if (!this::canvases.isInitialized) return
        event.isCancelled = true

        val player = event.player
        if (!this.game.getIngamePlayers().contains(player)) return

        val canvas = this.canvases.firstOrNull { it.calculatePointOnCanvas(player) != null } ?: return
        this.selections[player] = this.canvasMappings.inverse()[canvas] ?: return

        // TODO: translate
        player.sendInfo("Du hast ".comp() + "Bild ${this.canvases.indexOf(canvas) + 1}".highlight() + " ausgewählt.")
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
            val result = EndPhase.Result(listOf(setOf(this.drawings.first().first)))
            this.storeMiddleware.store(EndPhase.STORE_KEY, result)
        }
    }

    override fun onTimeout() = Unit

}
