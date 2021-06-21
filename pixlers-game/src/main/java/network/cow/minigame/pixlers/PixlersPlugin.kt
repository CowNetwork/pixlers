package network.cow.minigame.pixlers

import network.cow.messages.adventure.gradient
import network.cow.messages.core.Gradients
import network.cow.messages.spigot.MessagesPlugin
import network.cow.minigame.noma.spigot.NomaGamePlugin

/**
 * @author Benedikt Wüller
 */
class PixlersPlugin : NomaGamePlugin() {

    override fun onEnable() {
        MessagesPlugin.PREFIX = "Pixlers".gradient(Gradients.MINIGAME)
        super.onEnable()
    }

}
