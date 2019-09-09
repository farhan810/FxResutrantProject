package tblcheck.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import tblcheck.model.SectionAssignment;

/**
 * Created by cuongdm5 on 1/24/2016.
 */
public class SectionViewModel {
	private SectionAssignment assignment;
	private StringProperty staffName;
	private IntegerProperty section;
	private ObservableList<Integer> sectionList;
	private ObservableList<String> staffList;
	private BooleanProperty isLocked;

	public SectionViewModel(SectionAssignment assignment, ObservableList<Integer> sectionList,
			ObservableList<String> staffList) {
		this.assignment = assignment;
		this.sectionList = sectionList;
		this.staffList = staffList;

		this.section = new SimpleIntegerProperty(assignment, "section", assignment.getSection());
		this.section.addListener((observable, oldValue, newValue) -> {
			this.setSection((int) newValue);
		});

//		System.out.println("staffName-=-===-=-=-=-=-=>>>>>" + assignment.getStaffName());
		this.staffName = new SimpleStringProperty(assignment, "staffName", assignment.getStaffName());
		// this.staffName.addListener((observable, oldValue, newValue) -> {
		// System.out.println("observer===>"+observable);
		// System.out.println("oldValue===>"+oldValue);
		// System.out.println("newValue===>"+newValue);
		// System.out.println(oldValue+"=============================="+newValue);
		//// if(newValue != null)
		//// {
		// this.setStaffName(newValue);

		//// }
		// });

		this.staffName.addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// TODO Auto-generated method stub
				System.out.print("observer===>" + observable);
//				System.out.print(" | oldValue===>" + oldValue);
//				System.out.print(" | newValue===>" + newValue);
				System.out.println(" | " + oldValue + "==============================" + newValue);
				if (newValue != null) {
//					System.out.println(oldValue + "=========<><><><><>=====================" + newValue);
					setStaffName(newValue);
				}
			}
		});

		this.isLocked = new SimpleBooleanProperty(false);
	}

	public IntegerProperty sectionProperty() {
		return this.section;
	}

	public StringProperty staffNameProperty() {
		return this.staffName;
	}

	private void setStaffName(String name) {
		this.assignment.setStaffName(name);
	}

	private void setSection(int section) {
		this.assignment.setSection(section);
	}

	public ObservableList<String> getStaffList() {
		return this.staffList;
	}

	public ObservableList<Integer> getSectionList() {
		return this.sectionList;
	}

	public void lock(boolean locked) {
		this.isLocked.set(locked);
	}

	public ReadOnlyBooleanProperty lockProperty() {
		return this.isLocked;
	}

	public SectionAssignment getAssignment() {
		return this.assignment;
	}
}
