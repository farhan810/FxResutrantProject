package tblcheck.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tblcheck.helper.FxHelper;
import tblcheck.helper.WindowsHelper;

/**
 * Created by cuongdm5 on 1/25/2016.
 */
public class PasswordView {
    @FXML
    private Pane pnRoot;
    @FXML
    private Button btnOK;
    @FXML
    private Button btnCancel;
    @FXML
    private PasswordField pwField;

    int dialogResult = -1;
    boolean useTabtip = false;
    boolean showKeyboard = true;
    String enteredPassword;

    @FXML
    void initialize() {
        btnOK.setOnAction(event -> {
            dialogResult = 0;
            enteredPassword = pwField.getText();
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
        btnCancel.setOnAction(event -> {
            dialogResult = -1;
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
        pwField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!showKeyboard)
                return;
            if (newValue) // is focused
                WindowsHelper.showVirtualKeyboard(useTabtip);
            else WindowsHelper.hideVirtualKeyboard(useTabtip);
        });
        pwField.setOnAction(event -> {
            dialogResult = 0;
            enteredPassword = pwField.getText();
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

    public String getEnteredPassword() {
        return this.enteredPassword;
    }
}
