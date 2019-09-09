package tblcheck.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tblcheck.helper.FxHelper;

/**
 * Created by Sambuddha Basu.
 */
public class StaffNumber {
    @FXML
    private Pane pnRoot;
    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    int dialogResult = 0;
    boolean useTabtip = false;
    boolean showKeyboard = true;

    @FXML
    void initialize() {
        btnYes.setOnAction(event -> {
            dialogResult = 1;
            //enteredPassword = pwField.getText();
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
        btnNo.setOnAction(event -> {
            dialogResult = 0;
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
    }

    public void setUseTabtip(boolean value) {
        this.useTabtip = value;
    }

    public void setShowKeyboard(boolean value) {
        this.showKeyboard = value;
    }

    public int getDialogResult() {
        return this.dialogResult;
    }
}
