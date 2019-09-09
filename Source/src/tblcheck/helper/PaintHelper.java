package tblcheck.helper;

import javafx.scene.paint.Color;

/**
 * Created by cuongdm5 on 1/24/2016.
 */
public class PaintHelper {
    public static Color getSectionColor(int section) {
        switch (section) {
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.YELLOW;
        }
        return Color.GRAY;
    }
}
