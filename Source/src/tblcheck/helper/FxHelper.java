package tblcheck.helper;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Created by cuongdm5 on 1/25/2016.
 */
public class FxHelper {

    public static void closeStage(Stage stage){
        stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
