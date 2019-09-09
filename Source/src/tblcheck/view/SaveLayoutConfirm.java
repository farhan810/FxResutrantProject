package tblcheck.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tblcheck.helper.FxHelper;

/**
 */
public class SaveLayoutConfirm {
    @FXML
    private Pane pnRoot;
    @FXML
    private Label lblConfirmMsg;
    @FXML
    private TextField txtLayout;
    @FXML
    private Button btnYes;
//    @FXML
//    private Button btnReset;
    @FXML
    private Button btnNo;

    int dialogResult = 0;
    String nameEntered;
    boolean useTabtip = false;
    boolean showKeyboard = true;

    @FXML
    void initialize() {
        btnYes.setOnAction(event -> {
            dialogResult = 1;
            nameEntered = txtLayout.getText();
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
//        btnReset.setOnAction(event -> {
//            dialogResult = 2;
//            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
//        });
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

	public Label getLblConfirmMsg() {
		return lblConfirmMsg;
	}

	public void setLblConfirmMsg(Label lblConfirmMsg) {
		this.lblConfirmMsg = lblConfirmMsg;
	}

	public String getNameEntered() {
		return nameEntered;
	}

	public void setNameEntered(String nameEntered) {
		this.nameEntered = nameEntered;
	}
    
}
