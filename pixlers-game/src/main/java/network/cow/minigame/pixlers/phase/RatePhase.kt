package network.cow.minigame.pixlers.phase

import com.google.common.collect.HashBiMap
import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.corporate
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.translate
import network.cow.messages.adventure.translateToComponent
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
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.canvas.BlockCanvas
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitTask
import java.util.LinkedList
import kotlin.math.roundToInt

/**
 * @author Benedikt Wüller
 */
class RatePhase(game: SpigotGame, config: PhaseConfig<Player, SpigotGame>) : SpigotPhase(game, config) {

    private lateinit var canvases: List<BlockCanvas>
    private val drawings = LinkedList<Pair<SpigotActor, Canvas>>()

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

            players.forEach { it.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 1, true, false, false)) }

            // TODO: read from config
            this.canvases = listOf(
                    BlockCanvas(world.getBlockAt(-98, 183, 15), BlockFace.NORTH, 120, 70, *players, palette = palette, isVirtual = false),
                    BlockCanvas(world.getBlockAt(-228, 183, 15), BlockFace.NORTH, 120, 70, *players, palette = palette, isVirtual = false),
                    BlockCanvas(world.getBlockAt(-163, 104, 15), BlockFace.NORTH, 120, 70, *players, palette = palette, isVirtual = false),
            )

            this.game.getIngamePlayers().forEach {
                val item = ItemBuilder(Material.NAME_TAG)
                        .name(Translations.Phases.Rate.ITEM.translate(it).corporate())
                        .build()

                it.inventory.addItem(item)
                it.flySpeed = 0.5f // TODO: move to constant or config
                it.inventory.heldItemSlot = 0
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
            this.canvases.forEach { it.refresh() }
            if (this.drawings.isNotEmpty()) {
                val it = this.canvases.first()
                val (_, canvas) = this.drawings.firstOrNull() ?: return
                for (x in 0 until canvas.width) {
                    for (y in 0 until canvas.height) {
                        it.drawColor(x, y, canvas.calculateColor(x, y) ?: this.palette.baseColor)
                    }
                }
            }
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

        val actor = this.canvasActors[canvas]!!
        if (actor == this.game.getSpigotActor(player)) {
            // TODO: send message
            return
        }

        player.playSound(player.eyeLocation, Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F)

        val players = actor.getPlayers()
        val names = players.map { it.displayName() }
        var name = Component.empty()
        this.canvasActors[canvas]!!.getPlayers().map {
            it.displayName()
        }.forEachIndexed { index, author ->
            name = name.append(author)
            if (index < names.lastIndex - 1) {
                name = name.append(", ".comp())
            } else if (index < names.lastIndex) {
                name = name.append(" & ".comp())
            }
        }

        val canvasKey = when (this.canvases.indexOf(canvas)) {
            0 -> Translations.Phases.Rate.Canvas.FIRST
            1 -> Translations.Phases.Rate.Canvas.SECOND
            2 -> Translations.Phases.Rate.Canvas.THIRD
            else -> throw IllegalStateException("Canvas not mapped.")
        }

        // TODO: add anonymous voting

        player.sendInfo(Translations.Phases.Rate.SELECTED.translateToComponent(player, canvasKey.translate(player).highlight(), name))
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

        Bukkit.getOnlinePlayers().forEach { player ->
            player.inventory.clear()
        }

        if (this.drawings.isNotEmpty()) {
            val result = EndPhase.Result(listOf(setOf(this.drawings.first().first)))
            this.storeMiddleware.store(EndPhase.STORE_KEY, result)
        }
    }

    override fun onTimeout() = Unit

}
