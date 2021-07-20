package network.cow.minigame.pixlers.shape

/**
 * @author Benedikt Wüller
 */
enum class ShapeType(val translationKey: String, val shape: Shape) {

    LINE("minigame.pixlers.tool.shape.shapes.line", Line()),
    RECTANGLE("minigame.pixlers.tool.shape.shapes.rectangle", Rectangle()),
    ELLIPSE("minigame.pixlers.tool.shape.shapes.ellipse", Ellipse()),
    DONGUS("minigame.pixlers.tool.shape.shapes.dongus", Dongus())

}
