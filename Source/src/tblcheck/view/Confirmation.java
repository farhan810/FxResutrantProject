package tblcheck.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tblcheck.helper.FxHelper;

/**
 */
public class Confirmation {
	@FXML
	private Pane pnConfirm;
	@FXML
	private Label lblConfirmMsg;
        @FXML
	private Button btnNetwork;
	@FXML
	private Button btnYes;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnReset;
	@FXML
	private Button btnNo;

	int dialogResult = 0;

	@FXML
	void initialize() {
            btnNetwork.setOnAction(event -> {
                    dialogResult = 4;
                    FxHelper.closeStage((Stage) pnConfirm.getScene().getWindow());
            });
            btnYes.setOnAction(event -> {
                    dialogResult = 1;
                    FxHelper.closeStage((Stage) pnConfirm.getScene().getWindow());
            });
            btnSave.setOnAction(event -> {
                    dialogResult = 2;
                    FxHelper.closeStage((Stage) pnConfirm.getScene().getWindow());
            });
            btnReset.setOnAction(event -> {
                    dialogResult = 3;
                    FxHelper.closeStage((Stage) pnConfirm.getScene().getWindow());
            });
            btnNo.setOnAction(event -> {
                    dialogResult = 0;
                    FxHelper.closeStage((Stage) pnConfirm.getScene().getWindow());
            });
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

	public void setLabel(String labelVal) {
		lblConfirmMsg.setText(labelVal);
	}

	public Button getBtnYes() {
		return btnYes;
	}

	public void setBtnYes(Button btnYes) {
		this.btnYes = btnYes;
	}

        public Button getBtnNo() {
		return btnNo;
	}

	public void setBtnNo(Button btnNo) {
		this.btnNo = btnNo;
	}
        
	public Button getBtnNetwork() {
		return btnNetwork;
	}

	public void setBtnNetwork(Button btnNetwork) {
		this.btnNetwork = btnNetwork;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public Button getBtnReset() {
		return btnReset;
	}

	public void setBtnReset(Button btnReset) {
		this.btnReset = btnReset;
	}

}
