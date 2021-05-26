package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.formatToComponent
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.plus
import network.cow.messages.core.Gradients
import network.cow.minigame.pixlers.canvas.Canvas
import network.cow.minigame.pixlers.getPointsInCircle
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Benedikt Wüller
 */
open class SprayCanTool(canvas: Canvas) : LayerTool(canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    var size: Int = 3; private set

    override val primaryAction: Layer.() -> Unit = {
        val coordinates = getPointsInCircle(cursor, size)
        coordinates.filter { Math.random() <= 0.75 }.forEach { this.setColor(it.x, it.y, canvas.currentColor) }
    }

    override val secondaryAction: Layer.() -> Unit = {
        val index = sizes.indexOf(size)
        val nextIndex = if (index == sizes.lastIndex) 0 else index + 1
        size = sizes[nextIndex]
        onUpdateItem()
    }

    override fun getItemStack(player: Player): ItemStack {
        // TODO: translate
        return ItemBuilder(Material.WOODEN_SWORD)
                .name("Sprühdose".gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.size.toString().comp()).info())
                .lore(
                        Component.empty(),
                        "Linksklicke".highlight() + ", um die Größe zu verändern.".info(),
                        "Rechtsklicke".highlight() + ", um zu zeichnen.".info()
                )
                .build()
    }

}
