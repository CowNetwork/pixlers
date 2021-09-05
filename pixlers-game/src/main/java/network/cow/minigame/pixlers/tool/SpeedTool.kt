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
import network.cow.spigot.extensions.ItemBuilder
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Benedikt Wüller
 */
class SpeedTool(toolBox: ToolBox, canvas: Canvas) : Tool(toolBox, canvas) {

    companion object {
        private const val MAX_FLY_SPEED = 1.0f
        private const val MIN_FLY_SPEED = 0.1f
        private const val STEP_SIZE = 0.1f
    }

    override fun onPrimary() : Boolean {
        this.player.flySpeed = minOf(MAX_FLY_SPEED, this.player.flySpeed + STEP_SIZE)
        this.onUpdateItem?.let { it() }
        return true
    }

    override fun onSecondary() : Boolean {
        this.player.flySpeed = maxOf(MIN_FLY_SPEED, this.player.flySpeed - STEP_SIZE)
        this.onUpdateItem?.let { it() }
        return true
    }

    override fun getItemStack(player: Player): ItemStack {
        return ItemBuilder(Material.NETHERITE_PICKAXE)
            .name(Translations.Tool.Speed.NAME.translate(player).gradient(Gradients.CORPORATE) + " (%1\$s)".formatToComponent(this.player.flySpeed.toString().comp()).info())
            .lore(
                Component.empty(),
                Translations.Tool.Speed.ACTION_LEFT.translateToComponent(player, Translations.Action.LEFT.translate(player).highlight()).info(),
                Translations.Tool.Speed.ACTION_RIGHT.translateToComponent(player, Translations.Action.RIGHT.translate(player).highlight()).info()
            )
            .build()
    }

}
