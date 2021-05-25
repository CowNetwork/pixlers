package network.cow.minigame.pixlers.api

import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.Note
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import java.awt.Color

/**
 * @author Benedikt Wüller
 */
enum class CanvasColor(val color: Color, val instrument: Instrument, val note: Note) {

    // <editor-fold desc="Red">
    RED_0(Color(128, 0, 0), Instrument.BANJO, Note(0)),
    RED_1(Color(153, 0, 0), Instrument.BANJO, Note(1)),
    RED_2(Color(179, 0, 0), Instrument.BANJO, Note(2)),
    RED_3(Color(204, 0, 0), Instrument.BANJO, Note(3)),
    RED_4(Color(230, 0, 0), Instrument.BANJO, Note(4)),
    RED_5(Color(255, 0, 0), Instrument.BANJO, Note(5)),
    RED_6(Color(255, 38, 38), Instrument.BANJO, Note(6)),
    RED_7(Color(255, 77, 77), Instrument.BANJO, Note(7)),
    RED_8(Color(255, 115, 115), Instrument.BANJO, Note(8)),
    RED_9(Color(255, 153, 153), Instrument.BANJO, Note(9)),
    RED_10(Color(255, 191, 191), Instrument.BANJO, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Orange">
    ORANGE_0(Color(128, 70, 0), Instrument.BASS_DRUM, Note(0)),
    ORANGE_1(Color(153, 84, 0), Instrument.BASS_DRUM, Note(1)),
    ORANGE_2(Color(179, 98, 0), Instrument.BASS_DRUM, Note(2)),
    ORANGE_3(Color(204, 112, 0), Instrument.BASS_DRUM, Note(3)),
    ORANGE_4(Color(230, 126, 0), Instrument.BASS_DRUM, Note(4)),
    ORANGE_5(Color(255, 140, 0), Instrument.BASS_DRUM, Note(5)),
    ORANGE_6(Color(255, 157, 38), Instrument.BASS_DRUM, Note(6)),
    ORANGE_7(Color(255, 175, 77), Instrument.BASS_DRUM, Note(7)),
    ORANGE_8(Color(255, 192, 115), Instrument.BASS_DRUM, Note(8)),
    ORANGE_9(Color(255, 209, 153), Instrument.BASS_DRUM, Note(9)),
    ORANGE_10(Color(255, 226, 191), Instrument.BASS_DRUM, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Yellow">
    YELLOW_0(Color(128, 128, 0), Instrument.BASS_GUITAR, Note(0)),
    YELLOW_1(Color(153, 153, 0), Instrument.BASS_GUITAR, Note(1)),
    YELLOW_2(Color(179, 179, 0), Instrument.BASS_GUITAR, Note(2)),
    YELLOW_3(Color(204, 204, 0), Instrument.BASS_GUITAR, Note(3)),
    YELLOW_4(Color(230, 230, 0), Instrument.BASS_GUITAR, Note(4)),
    YELLOW_5(Color(255, 255, 0), Instrument.BASS_GUITAR, Note(5)),
    YELLOW_6(Color(255, 255, 38), Instrument.BASS_GUITAR, Note(6)),
    YELLOW_7(Color(255, 255, 77), Instrument.BASS_GUITAR, Note(7)),
    YELLOW_8(Color(255, 255, 115), Instrument.BASS_GUITAR, Note(8)),
    YELLOW_9(Color(255, 255, 153), Instrument.BASS_GUITAR, Note(9)),
    YELLOW_10(Color(255, 255, 191), Instrument.BASS_GUITAR, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Yellow">
    BLUE_0(Color(0, 0, 128), Instrument.BELL, Note(0)),
    BLUE_1(Color(0, 0, 153), Instrument.BELL, Note(1)),
    BLUE_2(Color(0, 0, 179), Instrument.BELL, Note(2)),
    BLUE_3(Color(0, 0, 204), Instrument.BELL, Note(3)),
    BLUE_4(Color(0, 0, 230), Instrument.BELL, Note(4)),
    BLUE_5(Color(0, 0, 255), Instrument.BELL, Note(5)),
    BLUE_6(Color(38, 38, 255), Instrument.BELL, Note(6)),
    BLUE_7(Color(77, 77, 255), Instrument.BELL, Note(7)),
    BLUE_8(Color(115, 115, 255), Instrument.BELL, Note(8)),
    BLUE_9(Color(153, 153, 255), Instrument.BELL, Note(9)),
    BLUE_10(Color(191, 191, 255), Instrument.BELL, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Cyan">
    CYAN_0(Color(2, 82, 76), Instrument.BIT, Note(0)),
    CYAN_1(Color(2, 107, 99), Instrument.BIT, Note(1)),
    CYAN_2(Color(3, 133, 123), Instrument.BIT, Note(2)),
    CYAN_3(Color(3, 158, 147), Instrument.BIT, Note(3)),
    CYAN_4(Color(4, 184, 170), Instrument.BIT, Note(4)),
    CYAN_5(Color(4, 209, 194), Instrument.BIT, Note(5)),
    CYAN_6(Color(36, 209, 196), Instrument.BIT, Note(6)),
    CYAN_7(Color(67, 209, 199), Instrument.BIT, Note(7)),
    CYAN_8(Color(98, 209, 201), Instrument.BIT, Note(8)),
    CYAN_9(Color(130, 209, 203), Instrument.BIT, Note(9)),
    CYAN_10(Color(161, 209, 205), Instrument.BIT, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Green">
    GREEN_0(Color(0, 128, 0), Instrument.CHIME, Note(0)),
    GREEN_1(Color(0, 153, 0), Instrument.CHIME, Note(1)),
    GREEN_2(Color(0, 179, 0), Instrument.CHIME, Note(2)),
    GREEN_3(Color(0, 204, 0), Instrument.CHIME, Note(3)),
    GREEN_4(Color(0, 230, 0), Instrument.CHIME, Note(4)),
    GREEN_5(Color(0, 255, 0), Instrument.CHIME, Note(5)),
    GREEN_6(Color(38, 255, 38), Instrument.CHIME, Note(6)),
    GREEN_7(Color(77, 255, 77), Instrument.CHIME, Note(7)),
    GREEN_8(Color(115, 255, 115), Instrument.CHIME, Note(8)),
    GREEN_9(Color(153, 255, 153), Instrument.CHIME, Note(9)),
    GREEN_10(Color(191, 255, 191), Instrument.CHIME, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Dark Green">
    DARK_GREEN_0(Color(17, 48, 26), Instrument.COW_BELL, Note(0)),
    DARK_GREEN_1(Color(21, 61, 33), Instrument.COW_BELL, Note(1)),
    DARK_GREEN_2(Color(26, 74, 40), Instrument.COW_BELL, Note(2)),
    DARK_GREEN_3(Color(30, 87, 47), Instrument.COW_BELL, Note(3)),
    DARK_GREEN_4(Color(35, 99, 54), Instrument.COW_BELL, Note(4)),
    DARK_GREEN_5(Color(40, 113, 62), Instrument.COW_BELL, Note(5)),
    DARK_GREEN_6(Color(44, 125, 69), Instrument.COW_BELL, Note(6)),
    DARK_GREEN_7(Color(49, 138, 76), Instrument.COW_BELL, Note(7)),
    DARK_GREEN_8(Color(53, 150, 83), Instrument.COW_BELL, Note(8)),
    DARK_GREEN_9(Color(58, 163, 90), Instrument.COW_BELL, Note(9)),
    DARK_GREEN_10(Color(62, 176, 97), Instrument.COW_BELL, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Purple">
    PURPLE_0(Color(68, 0, 128), Instrument.DIDGERIDOO, Note(0)),
    PURPLE_1(Color(82, 0, 153), Instrument.DIDGERIDOO, Note(1)),
    PURPLE_2(Color(95, 0, 179), Instrument.DIDGERIDOO, Note(2)),
    PURPLE_3(Color(109, 0, 204), Instrument.DIDGERIDOO, Note(3)),
    PURPLE_4(Color(122, 0, 230), Instrument.DIDGERIDOO, Note(4)),
    PURPLE_5(Color(136, 0, 255), Instrument.DIDGERIDOO, Note(5)),
    PURPLE_6(Color(154, 38, 255), Instrument.DIDGERIDOO, Note(6)),
    PURPLE_7(Color(172, 77, 255), Instrument.DIDGERIDOO, Note(7)),
    PURPLE_8(Color(190, 115, 255), Instrument.DIDGERIDOO, Note(8)),
    PURPLE_9(Color(207, 153, 255), Instrument.DIDGERIDOO, Note(9)),
    PURPLE_10(Color(255, 191, 255), Instrument.DIDGERIDOO, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Magenta">
    MAGENTA_0(Color(128, 10, 73), Instrument.FLUTE, Note(0)),
    MAGENTA_1(Color(153, 12, 88), Instrument.FLUTE, Note(1)),
    MAGENTA_2(Color(179, 14, 103), Instrument.FLUTE, Note(2)),
    MAGENTA_3(Color(204, 16, 118), Instrument.FLUTE, Note(3)),
    MAGENTA_4(Color(230, 18, 132), Instrument.FLUTE, Note(4)),
    MAGENTA_5(Color(255, 20, 147), Instrument.FLUTE, Note(5)),
    MAGENTA_6(Color(255, 51, 161), Instrument.FLUTE, Note(6)),
    MAGENTA_7(Color(255, 82, 175), Instrument.FLUTE, Note(7)),
    MAGENTA_8(Color(255, 112, 189), Instrument.FLUTE, Note(8)),
    MAGENTA_9(Color(255, 143, 203), Instrument.FLUTE, Note(9)),
    MAGENTA_10(Color(255, 173, 217), Instrument.FLUTE, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Pink">
    PINK_0(Color(128, 0, 128), Instrument.GUITAR, Note(0)),
    PINK_1(Color(153, 0, 153), Instrument.GUITAR, Note(1)),
    PINK_2(Color(179, 0, 179), Instrument.GUITAR, Note(2)),
    PINK_3(Color(204, 0, 204), Instrument.GUITAR, Note(3)),
    PINK_4(Color(230, 0, 230), Instrument.GUITAR, Note(4)),
    PINK_5(Color(255, 0, 255), Instrument.GUITAR, Note(5)),
    PINK_6(Color(255, 38, 255), Instrument.GUITAR, Note(6)),
    PINK_7(Color(255, 77, 255), Instrument.GUITAR, Note(7)),
    PINK_8(Color(255, 115, 255), Instrument.GUITAR, Note(8)),
    PINK_9(Color(255, 153, 255), Instrument.GUITAR, Note(9)),
    PINK_10(Color(255, 191, 255), Instrument.GUITAR, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Brown">
    BROWN_0(Color(77, 38, 10), Instrument.PIANO, Note(0)),
    BROWN_1(Color(89, 44, 12), Instrument.PIANO, Note(1)),
    BROWN_2(Color(102, 51, 14), Instrument.PIANO, Note(2)),
    BROWN_3(Color(115, 57, 16), Instrument.PIANO, Note(3)),
    BROWN_4(Color(128, 63, 17), Instrument.PIANO, Note(4)),
    BROWN_5(Color(139, 69, 19), Instrument.PIANO, Note(5)),
    BROWN_6(Color(153, 76, 21), Instrument.PIANO, Note(6)),
    BROWN_7(Color(166, 72, 23), Instrument.PIANO, Note(7)),
    BROWN_8(Color(179, 89, 24), Instrument.PIANO, Note(8)),
    BROWN_9(Color(191, 95, 26), Instrument.PIANO, Note(9)),
    BROWN_10(Color(204, 101, 28), Instrument.PIANO, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Skin">
    SKIN_0(Color(96, 57, 9), Instrument.STICKS, Note(0)),
    SKIN_1(Color(62, 43, 19), Instrument.STICKS, Note(1)),
    SKIN_2(Color(137, 76, 26), Instrument.STICKS, Note(2)),
    SKIN_3(Color(126, 77, 28), Instrument.STICKS, Note(3)),
    SKIN_4(Color(88, 53, 18), Instrument.STICKS, Note(4)),
    SKIN_5(Color(141, 85, 36), Instrument.STICKS, Note(5)),
    SKIN_6(Color(198, 134, 66), Instrument.STICKS, Note(6)),
    SKIN_7(Color(224, 172, 105), Instrument.STICKS, Note(7)),
    SKIN_8(Color(241, 194, 125), Instrument.STICKS, Note(8)),
    SKIN_9(Color(255, 219, 172), Instrument.STICKS, Note(9)),
    SKIN_10(Color(255, 233, 204), Instrument.STICKS, Note(10)),
    // </editor-fold>

    // <editor-fold desc="Gray">
    BLACK(Color(0, 0, 0), Instrument.IRON_XYLOPHONE, Note(0)),
    GRAY_0(Color(26, 26, 26), Instrument.IRON_XYLOPHONE, Note(1)),
    GRAY_1(Color(51, 51, 51), Instrument.IRON_XYLOPHONE, Note(2)),
    GRAY_2(Color(77, 77, 77), Instrument.IRON_XYLOPHONE, Note(3)),
    GRAY_3(Color(102, 102, 102), Instrument.IRON_XYLOPHONE, Note(4)),
    GRAY_4(Color(128, 128, 128), Instrument.IRON_XYLOPHONE, Note(5)),
    GRAY_5(Color(153, 153, 153), Instrument.IRON_XYLOPHONE, Note(6)),
    GRAY_6(Color(179, 179, 179), Instrument.IRON_XYLOPHONE, Note(7)),
    GRAY_7(Color(204, 204, 204), Instrument.IRON_XYLOPHONE, Note(8)),
    GRAY_8(Color(230, 230, 230), Instrument.IRON_XYLOPHONE, Note(9)),
    WHITE(Color(255, 255, 255), Instrument.IRON_XYLOPHONE, Note(10)),
    // </editor-fold>

}

fun Block.getCanvasColor() : CanvasColor? {
    if (this.type != Material.NOTE_BLOCK) return null
    val data = this.blockData as NoteBlock
    return CanvasColor.values().firstOrNull { data.instrument == it.instrument && data.note == it.note }
}

fun Color.toCanvasColor() : CanvasColor? {
    return CanvasColor.values().firstOrNull { it.color.rgb == this.rgb }
}
