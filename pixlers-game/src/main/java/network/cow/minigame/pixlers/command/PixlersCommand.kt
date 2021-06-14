package network.cow.minigame.pixlers.command

import network.cow.cowmands.Arguments
import network.cow.cowmands.Cowmand
import network.cow.messages.adventure.comp
import network.cow.messages.adventure.highlight
import network.cow.messages.adventure.plus
import network.cow.messages.spigot.sendInfo
import network.cow.minigame.noma.api.store.middleware.StoreMiddleware
import network.cow.minigame.pixlers.StoreKeys
import org.bukkit.command.CommandSender

/**
 * @author Benedikt Wüller
 */
class  PixlersCommand(private val storeMiddleware: StoreMiddleware) : Cowmand() {

    override val label = "pixlers"
    override val permission = "cow.minigame.pixlers.command"

    override val subCommands = listOf(TopicCommand(), TimeCommand())

    override fun execute(sender: CommandSender, args: Arguments) = Unit

    inner class TopicCommand : Cowmand() {

        override val label = "topic"
        override val permission = "cow.minigame.pixlers.command.topic"

        override fun execute(sender: CommandSender, args: Arguments) {
            if (args.isEmpty()) {
                sender.sendInfo("Usage: " + "/pixlers topic <topic>".highlight())
                return
            }

            val topic = (0..args.size).joinToString(" ") { args[it] }
            this@PixlersCommand.storeMiddleware.store(StoreKeys.FORCED_TOPIC, topic)
            sender.sendInfo("Der Begriff wurde zu ".comp() + topic.highlight() + " geändert.")
        }

    }

    inner class TimeCommand : Cowmand() {

        override val label = "time"
        override val permission = "cow.minigame.pixlers.command.time"

        override fun execute(sender: CommandSender, args: Arguments) {
            if (args.size != 1) {
                sender.sendInfo("Usage: " + "/pixlers time <seconds>".highlight())
                return
            }

            val duration = args[0].toLong()
            val time = if (duration == 1L) "eine Sekunde" else "$duration Sekunden"
            sender.sendInfo("Der Zeit wurde auf ".comp() + time.highlight() + " gesetzt.")
        }

    }

}
