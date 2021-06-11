package network.cow.minigame.pixlers

import network.cow.minigame.pixlers.state.Instrument
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Comparator
import javax.imageio.ImageIO

/**
 * I know scripts maybe should not be written in kotlin but who cares ¯\_(ツ)_/¯
 *
 * @author Benedikt Wüller
 */

fun main() {
    val targetRoot = File("./assets/minecraft")
    val targetBlockStates = File("./assets/minecraft/blockstates")
    val targetModels = File("./assets/minecraft/models/block")
    val targetTextures = File("./assets/minecraft/textures/block")

    if (targetRoot.exists()) {
        Files.walk(targetRoot.toPath())
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete)
    }

    targetRoot.mkdirs()
    targetBlockStates.mkdirs()
    targetModels.mkdirs()
    targetTextures.mkdirs()

    val palette = ImageIO.read(File("./pixlers-game/src/main/resources/palette.png"))
    val blockStates = mutableListOf<Pair<String, String>>()

    repeat(palette.width) { x ->
        repeat(palette.height) { y ->
            val name = "${x}x$y"
            val state = getState(x, y)

            // Write texture file.
            val image = BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
            image.setRGB(0, 0, palette.getRGB(x, y))
            ImageIO.write(image, "PNG", Paths.get(targetTextures.absolutePath, "$name.png").toFile())

            // Write model file.
            Files.writeString(Paths.get(targetModels.absolutePath, "$name.json"), "{\"parent\":\"minecraft:block/cube_all\",\"textures\":{\"all\":\"minecraft:block/$name\"}}")

            blockStates.add(state to name)
        }
    }

    val content = "{\"variants\":{" + blockStates.joinToString(",") { "\"${it.first}\":{\"model\":\"minecraft:block/${it.second}\"}" } + "}}"
    Files.writeString(Paths.get(targetBlockStates.absolutePath, "note_block.json"), content)
}

fun getState(column: Int, row: Int) : String {
    val instruments = Instrument.values()
    val instrument = instruments[column % instruments.size].name.lowercase()
    val powered = column >= instruments.size
    return "instrument=$instrument,note=$row,powered=$powered"
}
