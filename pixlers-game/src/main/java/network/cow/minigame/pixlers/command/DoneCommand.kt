package network.cow.minigame.pixlers.command

import net.kyori.adventure.text.event.ClickEvent
import network.cow.cowmands.Arguments
import network.cow.cowmands.Cowmand
import network.cow.messages.adventure.highlight
import network.cow.messages.spigot.broadcastTranslatedInfo
import network.cow.messages.spigot.sendTranslatedError
import network.cow.minigame.noma.spigot.SpigotGame
import network.cow.minigame.pixlers.Translations
import network.cow.minigame.pixlers.phase.DrawPhase
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * @author Benedikt Wüller
 */
class DoneCommand(private val game: SpigotGame) : Cowmand() {

    override val label = "done"
    override val aliases = listOf("ready", "fertig", "skip")

    private val donePlayers = mutableSetOf<Player>()

    override fun execute(sender: CommandSender, args: Arguments) {
        if (sender !is Player || !this.game.getIngamePlayers().contains(sender)) return

        if (this.game.getCurrentPhase() !is DrawPhase) {
            sender.sendTranslatedError(Translations.Commands.Done.ERROR_WRONG_PHASE)
            return
        }

        if (this.donePlayers.contains(sender)) {
            sender.sendTranslatedError(Translations.Commands.Done.ERROR_ALREADY_DONE)
            return
        }

        this.donePlayers.add(sender)
        if (this.donePlayers.size == this.game.getIngamePlayers().size) {
            this.game.nextPhase(true)
            return
        }

        Bukkit.getServer().broadcastTranslatedInfo(Translations.Commands.Done.SUCCESS,
            donePlayers.size.toString().highlight(),
            this.game.getIngamePlayers().size.toString().highlight(),
            "/done".highlight().clickEvent(ClickEvent.runCommand("done"))
        )
    }

}
