package network.cow.minigame.pixlers

import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import java.awt.Color
import javax.imageio.ImageIO

/**
 * @author Benedikt Wüller
 */
class ColorPalette(type: Type) {

    companion object {
        // This is required, because instruments are not named like in vanilla and therefore
        // can not be sorted alphabetically.
        private val INSTRUMENTS = listOf(
            Instrument.BANJO,
            Instrument.BASS_DRUM,
            Instrument.BASS_GUITAR,
            Instrument.BELL,
            Instrument.BIT,
            Instrument.CHIME,
            Instrument.COW_BELL,
            Instrument.DIDGERIDOO,
            Instrument.FLUTE,
            Instrument.GUITAR,
            Instrument.PIANO, // harp
            Instrument.STICKS, // hat
            Instrument.IRON_XYLOPHONE,
            Instrument.PLING,
            Instrument.SNARE_DRUM,
            Instrument.XYLOPHONE
        )

        const val STORE_KEY = "pixlers.color_palette"
    }

    private val image = ImageIO.read(this.javaClass.classLoader.getResourceAsStream(type.fileName))

    val baseColor: Int = this.image.width - 2
    val initialColor: Int = this.image.height * this.image.width - 1

    fun draw(from: Block, size: Int = 3) {
        repeat(this.image.width) { baseX ->
            repeat(this.image.height) inner@{ baseY ->
                val color = Color(image.getRGB(baseX, baseY))
                if (color.alpha < 255) return@inner
                repeat(size) { deltaX ->
                    repeat(size) { deltaY ->
                        val x = baseX * size + deltaX
                        val y = baseY * size + deltaY
                        this.applyBlockData(baseY * this.image.width + baseX, from.getRelative(x, 0, y))
                    }
                }
            }
        }
    }

    fun getBlockData(index: Int) : Triple<Instrument, Boolean, Note> {
        val column = index % this.image.width
        val row = index / this.image.width
        val instrument = INSTRUMENTS[column % INSTRUMENTS.size]
        val isPowered = column >= INSTRUMENTS.size
        val note = Note(row)
        return Triple(instrument, isPowered, note)
    }

    fun applyBlockData(index: Int, data: NoteBlock) {
        this.getBlockData(index).let {
            data.instrument = it.first
            data.isPowered = it.second
            data.note = it.third
        }
    }

    fun applyBlockData(index: Int, block: Block) {
        block.setType(Material.NOTE_BLOCK, false)
        val data = block.blockData as NoteBlock
        this.getBlockData(index).let {
            data.instrument = it.first
            data.isPowered = it.second
            data.note = it.third
        }
        block.setBlockData(data, false)
    }

    fun getColor(index: Int) : Color {
        val x = index % this.image.width
        val y = index / this.image.width
        return Color(this.image.getRGB(x, y))
    }

    fun getIndex(data: NoteBlock) : Int {
        var column = INSTRUMENTS.indexOf(data.instrument)
        if (data.isPowered) column += INSTRUMENTS.size
        val row = data.note.id
        return row * this.image.width + column
    }

    enum class Type(val fileName: String) {
        FULL("palette.png")
    }

}
