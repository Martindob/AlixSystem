package alix.common.packets.inventory.click;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;

public final class ContainerClickWrapper {

    public static AlixClickType getAlixClickType(WrapperPlayClientClickWindow wrapper) {
        if (wrapper == null || wrapper.getWindowClickType() == null) {
            return AlixClickType.NEITHER;
        }

        var c = wrapper.getWindowClickType();
        int button = wrapper.getButton();

        switch (c) {
            case PICKUP:     // Mode 0: Left/Right click (inside or outside inventory)
            case QUICK_MOVE: // Mode 1: Shift + Left/Right click
                if (button == 0) {
                    return AlixClickType.LEFT_CLICK;
                } else if (button == 1) {
                    return AlixClickType.RIGHT_CLICK;
                }
                break;

            case PICKUP_ALL: // Mode 6: Double Click
                if (button == 0) {
                    return AlixClickType.LEFT_CLICK;
                }
                break;

            default:
                // Mode 2 (SWAP - Hotbar keys/Offhand),
                // Mode 3 (CLONE - Middle click),
                // Mode 4 (THROW - Q / Ctrl+Q),
                // Mode 5 (QUICK_CRAFT - Dragging)
                break;
        }

        return AlixClickType.NEITHER;
    }
}