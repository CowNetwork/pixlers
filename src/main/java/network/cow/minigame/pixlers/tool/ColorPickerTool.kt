package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
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
class ColorPickerTool(canvas: Canvas) : Tool(canvas) {

    override fun onPrimary() = this.execute()

    override fun onSecondary() = this.execute()

    private fun execute() : Boolean {
        this.canvas.currentColor = this.canvas.calculateColor(this.cursor.x, this.cursor.y) ?: return false
        return true
    }

    override fun getItemStack(player: Player): ItemStack {
        // TODO: translate
        return ItemBuilder(Material.STONE_PICKAXE)
                .name("Pipette".gradient(Gradients.CORPORATE))
                .lore(
                        Component.empty(),
                        "Rechtsklicke".highlight() + " um eine Farbe auszuwählen.".info()
                )
                .build()
    }

}
