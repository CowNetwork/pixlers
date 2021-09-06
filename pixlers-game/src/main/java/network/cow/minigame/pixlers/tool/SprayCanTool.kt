package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.formatToComponent
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.plus
import network.cow.messages.adventure.translate
import network.cow.messages.adventure.translateToComponent
import network.cow.messages.core.Gradients
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.getPointsInCircle
import network.cow.minigame.pixlers.getPointsInLine
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Benedikt Wüller
 */
open class SprayCanTool(toolBox: ToolBox, canvas: Canvas) : LayerTool(toolBox, canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    private var lastPrimaryAt = 0L
    var size: Int = 3; private set

    override val primaryAction: Layer.() -> Unit = {
        // TODO: constant
        val lastCursor = if (tick - lastPrimaryAt <= 4) lastCursor else cursor
        val points = getPointsInLine(lastCursor, cursor).shuffled().filterIndexed { i, _ -> i % 3 == 0 }

        points.forEach { point ->
            val coordinates = getPointsInCircle(point, size)
            coordinates.filter { Math.random() <= 0.5 }.forEach { this.setColor(it.x, it.y, toolBox.color) }
        }

        lastPrimaryAt = tick
    }

    override fun onSecondary() : Boolean {
        val index = this.sizes.indexOf(size)
        val nextIndex = if (index == this.sizes.lastIndex) 0 else index + 1
        this.size = this.sizes[nextIndex]
        this.onUpdateItem?.let { it() }
        return true
    }

    override fun getItemStack(player: Player): ItemStack {
        return ItemBuilder(Material.WOODEN_SWORD)
            .name(Translations.Tool.SprayCan.NAME.translate(player).gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.size.toString().comp()).info())
            .lore(
                Component.empty(),
                Translations.Tool.SprayCan.ACTION_LEFT.translateToComponent(player, Translations.Action.LEFT.translate(player).highlight()).info(),
                Translations.Tool.SprayCan.ACTION_RIGHT.translateToComponent(player, Translations.Action.RIGHT.translate(player).highlight()).info()
            )
            .build()
    }

}
