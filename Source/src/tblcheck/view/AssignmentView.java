package tblcheck.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import tblcheck.helper.CheckException;
import tblcheck.helper.HttpHelper;
import tblcheck.model.Config;
import tblcheck.model.Table;
import tblcheck.viewmodel.SectionViewModel;

/**
 * Created by cuongdm5 on 1/24/2016.
 */
public class AssignmentView{
    public Rectangle rect;
    public HBox cmbBox;
    public HBox btnBox;
    public SectionViewModel viewModel;

    private EventHandler removeHandler;
    private EventHandler saveHandler;

    public void setRemoveHandler(EventHandler handler) {
        this.removeHandler = handler;
    }

    public void setSaveHandler(EventHandler handler) {
        this.saveHandler = handler;
    }

    private static final EventType<Event> removeEvent = new EventType<>("REMOVE");
    private static final EventType<Event> saveEvent = new EventType<>("SAVE");

    static final int BTN_SAVE_WIDTH = 50;
    static final int BTN_RMV_WIDTH = 60;
    static final int RECT_WIDTH = 70;
    static final int RECT_HEIGHT = 25;
    static final int CMB_SECT_WIDTH = 135;
    static final int CMB_STAFF_WIDTH = 180;
    static final int SPACING = 10;

  private Table tableObj = new Table();
    
    public AssignmentView(SectionViewModel viewModel) {
//    	RoomView roomView = new RoomView();
        this.viewModel = viewModel;

        rect = new Rectangle(RECT_WIDTH, RECT_HEIGHT);
        rect.getStyleClass().setAll("rect-section-" + viewModel.sectionProperty().get());
        cmbBox = new HBox();
        final ComboBox<Integer> cmbSection = new ComboBox<>();
        cmbSection.setPrefWidth(CMB_SECT_WIDTH);
        cmbSection.setItems(viewModel.getSectionList());
        cmbSection.valueProperty().bindBidirectional(viewModel.sectionProperty().asObject());
        cmbSection.valueProperty().addListener((observable, oldValue, newValue) -> {
            rect.getStyleClass().setAll("rect-section-" + newValue);
        });

        final ComboBox<String> cmbStaff = new ComboBox<>();
        cmbStaff.setPrefWidth(CMB_STAFF_WIDTH);
        cmbStaff.setItems(viewModel.getStaffList());
        cmbStaff.valueProperty().bindBidirectional(viewModel.staffNameProperty());
        System.out.println("staff CB >>>>>>"+cmbStaff.getValue());
        
//        System.out.println("saved List>><><><><><>"+tableObj.getSavedStaffList());
        
//        if(!tableObj.getSavedStaffList().isEmpty())
//        {
//        	System.out.println("into for==="+tableObj.getSavedStaffList().size());
//        	
//        	int index = 0;
//        	for(String str : tableObj.getSavedStaffList())
//        	{
//        		System.out.println(index+"=ind====count="+tableObj.getStaffCount());
//        		System.out.println(">>>>>>>>>>"+viewModel.lockProperty().get());
//        		if(viewModel.lockProperty().get() && index == tableObj.getStaffCount())
//        		{
//        			cmbStaff.setValue(str);
//        			index++;
//        		}
//        	}
//        }
//        
//        System.out.println("staffCount===****************"+tableObj.getStaffCount());
//        tableObj.setStaffCount(tableObj.getStaffCount()+1);
        
        if(!tableObj.getMapSavedStaffList().isEmpty())
        {
        	if(viewModel.lockProperty().get() && tableObj.getMapSavedStaffList().get(cmbSection.getValue()) != null)
        	{
        		cmbStaff.setValue(tableObj.getMapSavedStaffList().get(cmbSection.getValue()));
        	}
        }
        
        
        cmbBox.getChildren().addAll(cmbSection, cmbStaff);
        cmbBox.setSpacing(SPACING);
        cmbBox.setAlignment(Pos.CENTER);
        cmbBox.disableProperty().bind(viewModel.lockProperty());
//        cmbSection.disableProperty().bind(viewModel.lockProperty());
//        cmbStaff.disableProperty().bind(viewModel.lockProperty());
        cmbSection.getStyleClass().add("cmb-section");
        cmbStaff.getStyleClass().add("cmb-section");

        btnBox = new HBox();
        final Button btnSave = new Button();
        btnSave.setText("Save");
        btnSave.setPrefWidth(BTN_SAVE_WIDTH);
        btnSave.setOnAction(event -> {
            if (this.saveHandler != null)
            {
            	//Logic for add staff member name at time of save assignee
//            	System.out.println("Save Before>>------------"+tableObj.getSavedStaffList());
//  --          	if(!tableObj.getSavedStaffList().contains(cmbStaff.getValue()))
//  --          	{
//            		tableObj.getSavedStaffList().add(cmbStaff.getValue());
//  --          	}
//            	System.out.println("Save After>>---------"+tableObj.getSavedStaffList());
 
            	tableObj.getMapSavedStaffList().put(cmbSection.getValue(), cmbStaff.getValue());
            	
            	
            	saveHandler.handle(new Event(this, null, saveEvent));
            	try {
					HttpHelper.post(Config.getInstance().getStaffSectionUrl(), tableObj.getMapSavedStaffList());
				} catch (CheckException e) {
					e.printStackTrace();
				}
            }	
        });
        btnSave.disableProperty().bind(viewModel.lockProperty());

        final Button btnRemove = new Button();
        btnRemove.setText("Remove");
        btnRemove.setPrefWidth(BTN_RMV_WIDTH);
        btnRemove.setOnAction(event -> {
        	if(tableObj.getStaffCount() > 0)
        	{
        		tableObj.setStaffCount(tableObj.getStaffCount() - 1);
        	}
            if (this.removeHandler != null)
//            	System.out.println("Remove After>>---------"+tableObj.getSavedStaffList());
//            	if(tableObj.getSavedStaffList().contains(cmbStaff.getValue()))
//            	{
//            		tableObj.getSavedStaffList().remove(cmbStaff.getValue());
//            	}
//            	System.out.println("Remove After>>---------"+tableObj.getSavedStaffList());
            	
            	tableObj.getMapSavedStaffList().remove(cmbSection.getValue());
            	this.removeHandler.handle(new Event(this, null, removeEvent));
            	try {
					HttpHelper.post(Config.getInstance().getStaffSectionUrl(), tableObj.getMapSavedStaffList());
				} catch (CheckException e) {
					e.printStackTrace();
				}
        });

        btnBox.getChildren().addAll(btnSave, btnRemove);
        btnBox.setSpacing(SPACING);
        btnBox.setAlignment(Pos.CENTER);
    }
}
