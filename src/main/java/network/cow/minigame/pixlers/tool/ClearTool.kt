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
class ClearTool(canvas: Canvas) : Tool(canvas) {

    private val primaryAction: Layer.() -> Unit = {
        for (y in 0 until this.height) {
            for (x in 0 until this.width) {
                this.setColor(x, y, Canvas.BASE_COLOR)
            }
        }
    }

    override fun onPrimary(): Boolean {
        val layer = Layer(this.canvas)
        val init = this.primaryAction
        layer.init()
        this.canvas.apply(layer)
        return true
    }

    override fun getItemStack(player: Player): ItemStack {
        // TODO: translate
        return ItemBuilder(Material.IRON_HOE)
                .name("Alles löschen".gradient(Gradients.CORPORATE))
                .lore(
                        Component.empty(),
                        "Rechtsklicke".highlight() + ", um deine Leinwand zurückzusetzen.".info()
                )
                .build()
    }

}
