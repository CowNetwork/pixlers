package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.corporate
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
open class PaintTool(toolBox: ToolBox, canvas: Canvas) : LayerTool(toolBox, canvas) {

    protected open val sizes = listOf(1, 2, 3, 4, 5)

    var size: Int = 1; private set

    override val primaryAction: Layer.() -> Unit = {
        val coordinates = getPointsInCircle(cursor, size)
        coordinates.forEach { this.setColor(it.x, it.y, getColor()) }
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
        // TODO: translate
        return ItemBuilder(Material.GOLDEN_SWORD)
                .name("Stift".gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.size.toString().comp()).info())
                .lore(
                        Component.empty(),
                        "Linksklicke".highlight() + ", um die Größe zu verändern.".info(),
                        "Rechtsklicke".highlight() + ", um zu zeichnen.".info()
                )
                .build()
    }

}
