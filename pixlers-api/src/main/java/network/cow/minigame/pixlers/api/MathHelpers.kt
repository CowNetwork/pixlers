package network.cow.minigame.pixlers.api

/**
 * @author Benedikt Wüller
 */

fun getCoordinatesInCircle(centerX: Int, centerY: Int, diameter: Int) : List<Pair<Int, Int>> {
    val coordinates = mutableListOf<Pair<Int, Int>>()
    val radius = diameter / 2
    for (relativeY in 0 until diameter) {
        for (relativeX in 0 until diameter) {
            val x = centerX - radius + relativeY
            val y = centerY - radius + relativeX
            coordinates.add(x to y)
        }
    }
    return coordinates
}
