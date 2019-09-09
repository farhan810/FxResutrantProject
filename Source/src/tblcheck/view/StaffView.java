package tblcheck.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import tblcheck.helper.FxHelper;
import tblcheck.model.Table;

/**
 * Created by cuongdm5 on 1/23/2016.
 */
public class StaffView {

    public class StaffViewRow {
        public TextField textField;
        public HBox hBox;
        public RowConstraints rowContraints;
    }

    @FXML
    public Button btnOK;
    @FXML
    public Button btnCancel;
    @FXML
    public Pane pnRoot;
    @FXML
    public ScrollPane pnScroll;
    @FXML
    public GridPane pnStaff;

    @FXML
    public HBox buttonContainerHbox;
    @FXML
    public Button addRowButton;
    
    
    private int dialogResult = -1;

    private static final int BTN_WIDTH = 25;
    private static final int SPACING = 10;
    private static final Insets insets = new Insets(5, 15, 5, 10);
    private int rows = 0;
    List<StaffViewRow> viewRows = new ArrayList<>();
    private Table tableObj = new Table();

    public StaffView() {
    }

    @FXML
    void initialize() {
        btnOK.setOnAction(event -> {
            dialogResult = 0;
            FxHelper.closeStage((Stage)pnRoot.getScene().getWindow());
        });
        btnCancel.setOnAction(event -> {
            dialogResult = -1;
            FxHelper.closeStage((Stage)pnRoot.getScene().getWindow());
        });
        pnStaff.heightProperty().addListener((observable, oldValue, newValue) -> {
            if ((double) newValue > (double) oldValue)
                pnScroll.setVvalue(pnScroll.getVmax());
        });
    }

    public void setStaff(List<String> staffs) {
        pnStaff.getRowConstraints().clear();
        pnStaff.getChildren().clear();
        staffs.forEach(this::addRow);
        if (staffs.size() == 0)
            this.addRow(null);
    }

    private void addRow(String text) {
        final StaffViewRow row = new StaffViewRow();
        final TextField tf = new TextField();
        tf.setText(text);
        final HBox hBox = new HBox();
        hBox.setSpacing(SPACING);
        hBox.setAlignment(Pos.CENTER_LEFT);
//        final Button btnAdd = new Button();
        addRowButton.setPrefSize(62, 30);
        addRowButton.setText("Add");
        final Button btnRemove = new Button();
        btnRemove.setPrefSize(BTN_WIDTH, BTN_WIDTH);
        btnRemove.setText("-");

        row.textField = tf;
        row.hBox = hBox;

        tf.setOnKeyReleased(event -> {
            if (tf.getText() == null || tf.getText().isEmpty())
            	addRowButton.setDisable(true);
            else addRowButton.setDisable(false);
        });
        tf.setOnAction(event -> {
            if (tf.getText() != null && !tf.getText().isEmpty()) {
                int index = viewRows.lastIndexOf(row);
                if (index == viewRows.size() - 1) {
                    // Auto add new row if user presses enter at last row
                    addRow(null);
                    btnRemove.setDisable(false);
                } else {
                    // focus to next row
                    viewRows.get(index + 1).textField.requestFocus();
                }
            }
        });

        tf.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (tf.getText() == null || tf.getText().isEmpty())
                    removeRow(row);
                else addRowButton.setDisable(false);
            }
        });
        addRowButton.setOnAction(event -> {
            if (tf.getText() != null && !tf.getText().isEmpty()) {
                addRow(null);
                btnRemove.setDisable(false);
            }
        });
        btnRemove.setOnAction(event -> {
//            removeChilds(tf, hBox);
        	String removedStaff = row.textField.getText();
//        	if(tableObj.getSavedStaffList().contains(removedStaff))
//            {
//            	System.out.println("");
//            }else{
//            	removeRow(row);
//            }
        	int i = 0;
        	for (Map.Entry<Integer, String> entry : tableObj.getMapSavedStaffList().entrySet())  {
               if(entry.getValue().equals(removedStaff))
               {
            	   i++;
            	   break;   
               }
        		
            }

        	if(i == 0)
        	{
        		removeRow(row);
        	}
        	else
        	{
        		try {
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.initOwner(pnRoot.getScene().getWindow());
        			alert.setTitle("Information Dialog");
        			alert.setHeaderText(null);
        			alert.setContentText("Remove this name from the Assign Sections list.");

        			alert.showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        	}
        	
        	
        });
        if (text == null || text.isEmpty())
        	addRowButton.setDisable(true);

        hBox.getChildren().addAll( btnRemove);
        rows = pnStaff.getChildren().size() / 2;
        viewRows.add(row);
        pnStaff.addRow(rows, tf, hBox);
//        pnStaff.add(tf, 0, rows);
//        pnStaff.add(hBox, 1, rows);
        pnStaff.setMargin(tf, insets);
        tf.requestFocus();
        rows = pnStaff.getChildren().size() / 2;
    }

    private synchronized void removeRow(StaffViewRow row) {
        if (pnStaff.getChildren().size() != 2) {
            pnStaff.getChildren().removeAll(row.textField, row.hBox);
            viewRows.remove(row);
        } else {
            row.textField.clear();
        }
        if (pnStaff.getChildren().size() != 2 && pnStaff.getChildren().size() > 2 )
            row.hBox.getChildren().get(1).setDisable(true);
        rows = pnStaff.getChildren().size() / 2;

        // GridPane bugs making missing row index
        // Reset row index
        int index = 0;
        for (StaffViewRow r : viewRows) {
            GridPane.setRowIndex(r.textField, index);
            GridPane.setRowIndex(r.hBox, index);
            index++;
        }
    }

    public List<String> getStaffs() {
        List<String> staffs = new ArrayList<String>();
        ObservableList<Node> nodes = pnStaff.getChildren();
        for (int i = 0; i < nodes.size(); i += 2) {
            TextField tf = (TextField) nodes.get(i);
            if (tf.getText() != null && !tf.getText().isEmpty())
                staffs.add(tf.getText());
        }
        return staffs;
    }

    public int getDialogResult() {
        return dialogResult;
    }
}
