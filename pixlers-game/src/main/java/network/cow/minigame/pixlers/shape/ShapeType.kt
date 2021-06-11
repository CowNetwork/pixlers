package network.cow.minigame.pixlers.shape

/**
 * @author Benedikt Wüller
 */
enum class ShapeType(val displayName: String, val shape: Shape) {

    LINE("Linie", Line()),
    RECTANGLE("Rechteck", Rectangle()),
    ELLIPSE("Ellipse", Ellipse()),
    DONGUS("Dongus", Dongus())

}
