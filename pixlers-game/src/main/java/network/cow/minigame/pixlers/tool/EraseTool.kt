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
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Benedikt Wüller
 */
class EraseTool(toolBox: ToolBox, canvas: Canvas) : PaintTool(toolBox, canvas) {

    override fun getColor() = this.canvas.palette.baseColor

    override fun getItemStack(player: Player): ItemStack {
        // TODO: translate
        return ItemBuilder(Material.DIAMOND_SWORD)
                .name("Radiergummi".gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.size.toString().comp()).info())
                .lore(
                        Component.empty(),
                        "Linksklicke".highlight() + ", um die Größe zu verändern.".info(),
                        "Rechtsklicke".highlight() + ", um zu radieren.".info()
                )
                .build()
    }

}
