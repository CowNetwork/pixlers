package network.cow.minigame.pixlers

import network.cow.cowmands.Cowmands
import network.cow.messages.adventure.gradient
import network.cow.messages.core.Gradients
import network.cow.messages.spigot.MessagesPlugin
import network.cow.minigame.noma.spigot.NomaGamePlugin
import network.cow.minigame.pixlers.command.PixlersCommand
import network.cow.minigame.pixlers.listener.CancelListener
import org.bukkit.Bukkit

/**
 * @author Benedikt Wüller
 */
class PixlersPlugin : NomaGamePlugin() {

    override fun onEnable() {
        MessagesPlugin.PREFIX = "Pixlers".gradient(Gradients.MINIGAME)
        Cowmands.register(this, PixlersCommand(this.game.store))
        Bukkit.getPluginManager().registerEvents(CancelListener(), this)
        super.onEnable()
    }

}
