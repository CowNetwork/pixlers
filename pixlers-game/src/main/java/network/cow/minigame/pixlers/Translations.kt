package network.cow.minigame.pixlers

/**
 * @author Benedikt Wüller
 */
object Translations {

    private const val PREFIX = "minigame.pixlers"
    const val COLOR = "$PREFIX.color"

    object Action {
        private const val PREFIX = "${Translations.PREFIX}.action"
        const val RIGHT = "$PREFIX.right"
        const val LEFT = "$PREFIX.left"
    }

    object Phases {
        object Draw {
            private const val PREFIX = "${Translations.PREFIX}.phases.draw"
            const val INFO_TITLE = "$PREFIX.info_title"
            const val INFO_CHAT = "$PREFIX.info_chat"
        }

        object Rate {
            private const val PREFIX = "${Translations.PREFIX}.phases.rate"
            const val ITEM = "$PREFIX.item"
            const val SELECTED = "$PREFIX.selected"

            object Canvas {
                private const val PREFIX = "${Translations.Phases.Rate.PREFIX}.canvas"
                const val FIRST = "$PREFIX.first"
                const val SECOND = "$PREFIX.second"
                const val THIRD = "$PREFIX.third"
            }
        }
    }

    object Tool {
        private const val PREFIX = "${Translations.PREFIX}.tool"

        object Clear {
            const val NAME = "$PREFIX.clear.name"
            const val ACTION_RIGHT = "$PREFIX.clear.action_right"
        }

        object ColorPicker {
            const val NAME = "$PREFIX.color_picker.name"
            const val ACTION_RIGHT = "$PREFIX.color_picker.action_right"
        }

        object Fill {
            const val NAME = "$PREFIX.fill.name"
            const val ACTION_RIGHT = "$PREFIX.fill.action_right"
        }

        object Paint {
            const val NAME = "$PREFIX.paint.name"
            const val ACTION_LEFT = "$PREFIX.paint.action_left"
            const val ACTION_RIGHT = "$PREFIX.paint.action_right"
        }

        object Eraser {
            const val NAME = "$PREFIX.eraser.name"
            const val ACTION_LEFT = "$PREFIX.eraser.action_left"
            const val ACTION_RIGHT = "$PREFIX.eraser.action_right"
        }

        object Shape {
            const val NAME = "$PREFIX.shape.name"
            const val ACTION_LEFT = "$PREFIX.shape.action_left"
            const val ACTION_RIGHT = "$PREFIX.shape.action_right"
        }

        object SprayCan {
            const val NAME = "$PREFIX.spray_can.name"
            const val ACTION_LEFT = "$PREFIX.spray_can.action_left"
            const val ACTION_RIGHT = "$PREFIX.spray_can.action_right"
        }

        object State {
            const val NAME = "$PREFIX.state.name"
            const val ACTION_LEFT = "$PREFIX.state.action_left"
            const val ACTION_RIGHT = "$PREFIX.state.action_right"
        }
    }

}
