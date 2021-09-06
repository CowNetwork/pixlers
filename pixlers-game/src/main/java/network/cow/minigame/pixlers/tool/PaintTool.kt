package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.corporate
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
import java.awt.Point

/**
 * @author Benedikt Wüller
 */
open class PaintTool(toolBox: ToolBox, canvas: Canvas) : LayerTool(toolBox, canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    private var lastPrimaryAt = 0L
    var size: Int = 1; private set

    override val primaryAction: Layer.() -> Unit = {
        // TODO: constant
        val lastCursor = if (tick - lastPrimaryAt <= 5) lastCursor else cursor
        val points = getPointsInLine(lastCursor, cursor)

        points.forEach { point ->
            val coordinates = getPointsInCircle(point, size)
            coordinates.forEach { this.setColor(it.x, it.y, getColor()) }
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

    protected open fun getColor() = this.toolBox.color

    override fun getItemStack(player: Player): ItemStack {
        return ItemBuilder(Material.GOLDEN_SWORD)
            .name(Translations.Tool.Paint.NAME.translate(player).gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.size.toString().comp()).info())
            .lore(
                Component.empty(),
                Translations.Tool.Paint.ACTION_LEFT.translateToComponent(player, Translations.Action.LEFT.translate(player).highlight()).info(),
                Translations.Tool.Paint.ACTION_RIGHT.translateToComponent(player, Translations.Action.RIGHT.translate(player).highlight()).info()
            )
            .build()
    }

}
