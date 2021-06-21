package network.cow.minigame.pixlers.command

import network.cow.cowmands.Arguments
import network.cow.cowmands.Cowmand
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.plus
import network.cow.messages.spigot.sendInfo
import network.cow.minigame.noma.api.store.Store
import network.cow.minigame.pixlers.StoreKeys
import org.bukkit.command.CommandSender
import java.awt.Component

/**
 * @author Benedikt Wüller
 */
class PixlersCommand(private val store: Store) : Cowmand() {

    override val label = "pixlers"
    override val permission = "cow.minigame.pixlers.command"
    override val subCommands = listOf(TopicCommand(), TimeCommand())

    override fun execute(sender: CommandSender, args: Arguments) {
        if (args.isEmpty()) {
            sender.sendInfo("Usage: ".comp() + "/pixlers <topic|time>".highlight())
            return
        }
    }

    inner class TopicCommand : Cowmand() {

        override val label = "topic"
        override val permission = "cow.minigame.pixlers.command.topic"

        override fun execute(sender: CommandSender, args: Arguments) {
            if (args.isEmpty()) {
                sender.sendInfo("Usage: ".comp() + "/pixlers topic <topic>".highlight())
                return
            }

            val topic = (0..args.size).joinToString(" ") { args[it] }
            this@PixlersCommand.store.set(StoreKeys.FORCED_TOPIC, topic)
            sender.sendInfo("The topic has been changed to".comp() + topic.highlight() + ".")
        }

    }

    inner class TimeCommand : Cowmand() {

        override val label = "time"
        override val permission = "cow.minigame.pixlers.command.time"

        override fun execute(sender: CommandSender, args: Arguments) {
            if (args.size != 1) {
                sender.sendInfo("Usage: ".comp() + "/pixlers time <seconds>".highlight())
                return
            }

            val duration = args[0].toLong()
            val time = if (duration == 1L) "one second" else "$duration seconds"
            sender.sendInfo("The duration has been changed to ".comp() + time.highlight() + ".")
        }

    }

}
