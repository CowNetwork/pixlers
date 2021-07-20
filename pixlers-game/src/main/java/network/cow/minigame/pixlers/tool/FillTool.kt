package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.plus
import network.cow.messages.adventure.translate
import network.cow.messages.adventure.translateToComponent
import network.cow.messages.core.Gradients
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.awt.Point
import java.util.LinkedList

/**
 * @author Benedikt Wüller
 */
class FillTool(toolBox: ToolBox, canvas: Canvas) : LayerTool(toolBox, canvas) {

    override val primaryAction: (Layer.() -> Unit) = {
        val pixels = mutableSetOf<Int>()
        val queue = LinkedList<Int>()
        queue.push(cursor.y * this.width + cursor.x)

        val replaceColor = this.getColor(cursor.x, cursor.y)
        do {
            val coords = queue.pop()
            val x = coords % this.width
            val y = coords / this.width

            val color = this.getColor(x, y)
            if (color != replaceColor) continue

            pixels.add(y * this.width + x)
            this.setColor(x, y, toolBox.color)

            listOf(Point(x - 1, y), Point(x + 1, y), Point(x, y - 1), Point(x, y + 1))
                    .filter { it.x >= 0 && it.x < this.width && it.y >= 0 && it.y < this.height }
                    .map { it.y * this.width + it.x }
                    .filterNot { pixels.contains(it) }
                    .forEach {
                        if (queue.contains(it)) return@forEach
                        queue.add(it)
                    }
        } while (queue.isNotEmpty())
    }

    override fun getItemStack(player: Player): ItemStack {
        return ItemBuilder(Material.WOODEN_HOE)
            .name(Translations.Tool.Fill.NAME.translate(player).gradient(Gradients.CORPORATE))
            .lore(
                Component.empty(),
                Translations.Tool.Fill.ACTION_RIGHT.translateToComponent(player, Translations.Action.RIGHT.translate(player).highlight()).info()
            )
            .build()
    }

}
