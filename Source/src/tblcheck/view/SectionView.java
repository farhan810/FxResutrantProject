package tblcheck.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tblcheck.helper.CheckException;
import tblcheck.helper.FxHelper;
import tblcheck.model.SectionAssignment;
import tblcheck.viewmodel.RoomViewModel;
import tblcheck.viewmodel.SectionViewModel;

/**
 * Created by cuongdm5 on 1/24/2016.
 */
public class SectionView implements PopupDialog {

	@FXML
	public Pane pnRoot;
	@FXML
	public GridPane pnAssignments;
	@FXML
	public ChoiceBox cmbSections;
	@FXML
	public Button btnSetSection;
	@FXML
	public Button btnAdd;
	@FXML
	public Button btnClose;
	@FXML
	public ScrollPane pnScroll;

	private int maxSection;
	private int section;
	private int dialogResult = -1;
	private boolean dialogOpened;
	private RoomViewModel roomViewModel;
	private ObservableList<Integer> sectionList;
	private ObservableList<Integer> allSectionList;
	private static final Insets insets = new Insets(5, 15, 5, 10);

	@SuppressWarnings("unchecked")
	@FXML
	void initialize() {
		this.dialogOpened = false;
		btnAdd.setOnAction(event -> {
			if (roomViewModel.getStaffList().size() == 0)
				return;
			SectionAssignment ass = new SectionAssignment(1, roomViewModel.getStaffList().get(0));
			SectionViewModel svm = new SectionViewModel(ass, this.sectionList, roomViewModel.getStaffList());
			this.roomViewModel.addSectionViewModel(svm);
			this.addAssignmentToGrid(svm);
		});

		btnClose.setOnAction(event -> {
			FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
		});
		this.sectionList = FXCollections.observableArrayList();
		this.allSectionList = FXCollections.observableArrayList();
		// this.cmbSections.setItems(this.sectionList);
		this.cmbSections.setItems(this.allSectionList);
		this.cmbSections.setOnAction(event -> {
			if (this.dialogOpened == true) {
				try {
					final Stage dialog = new Stage();
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.initOwner(pnRoot.getScene().getWindow());
					dialog.initStyle(StageStyle.UTILITY);
					dialog.setResizable(false);

					FXMLLoader loader = new FXMLLoader(RoomView.class.getResource("StaffNumber.fxml"));
					Pane pn = loader.load();
					StaffNumber snView = loader.getController();
					snView.setShowKeyboard(roomViewModel.getConfig().isShowKeyboard());
					snView.setUseTabtip(roomViewModel.getConfig().isUseTabtip());

					Scene dialogScene = new Scene(pn, 382, 135);
					dialog.setScene(dialogScene);
					dialog.setTitle("Confirm selection change");
					dialog.setOnCloseRequest(event1 -> {
						if (snView.getDialogResult() == 1) {
							String val = cmbSections.getValue().toString();
							// this.roomViewModel.staffNumberChange(val);
							try {
								this.roomViewModel.updateSection(Integer.parseInt(val));
								this.pnAssignments.getChildren().clear();
							} catch (CheckException e) {
								e.printStackTrace();
							}
							//System.out.println("Staff Number changed to: " + val);
						} else {
							Integer val = this.roomViewModel.sectionProperty().get();
							this.cmbSections.getSelectionModel().select(val);
							//System.out.println("Staff Number not changed, still is: " + val);
						}
					});
					dialog.showAndWait();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			this.dialogOpened = true;
		});
		this.btnSetSection.setOnAction(event -> {
			Integer val = (Integer) cmbSections.getValue();
			this.roomViewModel.sectionProperty().set(val);
		});
		this.pnAssignments.heightProperty().addListener((observable, oldValue, newValue) -> {
			if ((double) newValue > (double) oldValue)
				pnScroll.setVvalue(pnScroll.getVmax());
		});
	}

	public SectionView() {

	}

	private void setSection(int section) {
		//System.out.println("Set section: " + section);
		this.cmbSections.setValue(section);
		if (section > this.section) {
			for (int i = this.section + 1; i <= section; i++) {
				sectionList.add(i);
			}
		} else if (section < this.section) {
			sectionList.remove(section, this.section);
		}
		this.section = section;
	}

	private void setMaxSection(int section) {
		if (section > this.maxSection) {
			for (int i = this.maxSection + 1; i <= section; i++) {
				allSectionList.add(i);
			}
		} else if (section < this.maxSection) {
			allSectionList.remove(section, this.maxSection);
		}
		this.maxSection = section;
	}

	@Override
	public int getDialogResult() {
		return dialogResult;
	}

	public void setSectionViewModel(RoomViewModel viewModel) {
		this.roomViewModel = viewModel;
		this.setMaxSection(roomViewModel.MAX_SECTION);
		//System.out.println(roomViewModel.sectionProperty().get());
		this.setSection(roomViewModel.sectionProperty().get());
		this.roomViewModel.sectionProperty().addListener((observable, oldValue, newValue) -> {
			setSection((int) newValue);
		});
		this.pnAssignments.getRowConstraints().clear();
		this.pnAssignments.getChildren().clear();
		this.roomViewModel.getSectionViewModelList().forEach(this::addAssignmentToGrid);
	}

	private synchronized void addAssignmentToGrid(SectionViewModel svm) {
		try {
			int row = -1;
			if (!pnAssignments.getChildren().isEmpty())
				row = GridPane.getRowIndex(pnAssignments.getChildren().get(pnAssignments.getChildren().size() - 1));
			AssignmentView assView = new AssignmentView(svm);
			assView.setRemoveHandler(event -> removeAssignment(assView));
			assView.setSaveHandler(event -> saveAssignment(assView));
			this.pnAssignments.addRow(row + 1, assView.rect, assView.cmbBox, assView.btnBox);
			this.pnAssignments.setMargin(assView.rect, insets);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static final int BTN_SAVE_WIDTH = 50;
	static final int BTN_RMV_WIDTH = 60;
	static final int RECT_WIDTH = 70;
	static final int RECT_HEIGHT = 25;
	static final int CMB_SECT_WIDTH = 135;
	static final int CMB_STAFF_WIDTH = 180;
	static final int SPACING = 10;
	//
	// private void addAssignment(SectionViewModel vm) {
	// Rectangle rect = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
	// rect.fillProperty().bind(vm.sectionColorProperty());
	//
	// final HBox cmbBox = new HBox();
	// final ComboBox<Integer> cmbSection = new ComboBox<>();
	// cmbSection.setPrefWidth(CMB_SECT_WIDTH);
	//// cmbSection.setItems(vm.getSectionList());
	//// cmbSection.valueProperty().bind(viewModel.sectionProperty().asObject());
	// ComboBox<String> cmbStaff = new ComboBox<String>();
	// cmbStaff.setPrefWidth(CMB_STAFF_WIDTH);
	//// cmbStaff.setItems(vm.getStaffList());
	// cmbStaff.getItems().add("A");
	// cmbStaff.getItems().add("B");
	// cmbStaff.getItems().add("C");
	// cmbStaff.getItems().add("D");
	//// cmbStaff.valueProperty().bind(viewModel.staffNameProperty());
	// cmbBox.getChildren().addAll(cmbSection, cmbStaff);
	// cmbBox.setSpacing(SPACING);
	// cmbBox.setAlignment(Pos.CENTER);
	// cmbBox.disableProperty().bind(vm.lockProperty());
	//
	// HBox btnBox = new HBox();
	// Button btnSave = new Button();
	// btnSave.setText("Save");
	// btnSave.setPrefWidth(BTN_SAVE_WIDTH);
	// btnSave.disableProperty().bind(vm.lockProperty());
	//
	// Button btnRemove = new Button();
	// btnRemove.setText("Remove");
	// btnRemove.setPrefWidth(BTN_RMV_WIDTH);
	// btnBox.getChildren().addAll(btnSave, btnRemove);
	// btnBox.setSpacing(SPACING);
	// btnBox.setAlignment(Pos.CENTER);
	//
	// pnAssignments.addRow(rows++, rect, cmbBox, btnBox);
	// pnAssignments.setMargin(rect, insets);
	// }

	private synchronized void removeAssignment(AssignmentView v) {
		this.pnAssignments.getChildren().removeAll(v.rect, v.cmbBox, v.btnBox);
		this.roomViewModel.removeSectionViewModel(v.viewModel, true);
	}

	private void saveAssignment(AssignmentView v) {
		v.viewModel.lock(true);
		this.roomViewModel.saveSectionViewModel(v.viewModel);
	}
}
