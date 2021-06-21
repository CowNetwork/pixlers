package network.cow.minigame.pixlers.tool

import net.kyori.adventure.text.Component
import network.cow.messages.adventure.gradient
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.info
import network.cow.messages.adventure.separator
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
class StateTool(toolBox: ToolBox, canvas: Canvas) : Tool(toolBox, canvas) {

    override fun onPrimary() : Boolean {
        this.canvas.undo()
        return true
    }

    override fun onSecondary() : Boolean {
        this.canvas.redo()
        return true
    }

    override fun getItemStack(player: Player): ItemStack {
        // TODO: translate
        return ItemBuilder(Material.DIAMOND_PICKAXE)
                .name("Wiederherstellen &  Rückgängig".gradient(Gradients.CORPORATE))
                .lore(
                        Component.empty(),
                        "Linksklicke".highlight() + ", um eine Aktion wiederherzustellen.".info(),
                        "Rechtsklicke".highlight() + " um eine Aktion rückgängig zu machen.".info()
                )
                .build()
    }

}