package tblcheck.view;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tblcheck.helper.FxHelper;

/**
 * Created by cuongdm5 on 1/25/2016.
 */
public class LayoutSelector {
    @FXML
    public Pane pnRoot;
    @FXML
    public ScrollPane pnScroll;
    @FXML
    public ListView lstLayouts;
    @FXML
    public Button btnOK;
    @FXML
    public Button btnCancel;

    private int dialogResult = -1;
    private int selectedResult;

    @FXML
    void initialize() {
        btnOK.setOnAction(event -> {
            dialogResult = 0;
            selectedResult = lstLayouts.getSelectionModel().getSelectedIndex();
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
        btnCancel.setOnAction(event -> {
            dialogResult = -1;
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
        lstLayouts.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    public LayoutSelector() {

    }

    public void setList(String[] list, String current) {
        lstLayouts.setItems(FXCollections.observableArrayList(list));
        lstLayouts.getSelectionModel().select(current);
    }

    public int getDialogResult() {
        return this.dialogResult;
    }

    public int getSelectedResult() {
        return this.selectedResult;
    }
}
