package tblcheck.viewmodel;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.NamedArg;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import tblcheck.model.Table;

/**
 * Created by cuongdm5 on 1/10/2016.
 */
public class TableViewModel {

	public enum NotifyType {
		Update,
		Reset,
		Remap
	}

	static final EventType<UpdateArgs> updateEventType = new EventType<>("UpdateStatus");
	static final EventType<UpdateArgs> remapEventType = new EventType<>("RemapPosition");
	static final EventType<UpdateArgs> resetEventType = new EventType<>("ResetStatus");

	public class UpdateArgs extends Event {
		private TableViewModel src;
		private NotifyType type;

		public UpdateArgs(@NamedArg("eventType") EventType<? extends Event> eventType) {
			super(eventType);
			if (eventType.equals(updateEventType))
				type = NotifyType.Update;
			else if (eventType.equals(remapEventType))
				type = NotifyType.Remap;
			else
				type = NotifyType.Reset;
		}

		public UpdateArgs(@NamedArg("source") Object source, @NamedArg("target") EventTarget target,
				@NamedArg("eventType") EventType<? extends Event> eventType) {
			super(source, target, eventType);
			if (eventType.equals(updateEventType))
				type = NotifyType.Update;
			else if (eventType.equals(remapEventType))
				type = NotifyType.Remap;
			else
				type = NotifyType.Reset;
			src = (TableViewModel) source;
		}

		public TableViewModel getSrc() {
			return src;
		}

		public NotifyType getType() {
			return type;
		}
	}

	private Table table;
	private StringProperty text;
	private DoubleProperty left, top, width, height, radius, rotation;
	private BooleanProperty circleVisible;
	private BooleanProperty rectVisible;
	private BooleanProperty arcVisible;
	private BooleanProperty borderVisible;
	private BooleanProperty mouseTransparent;
	private List<Transform> transforms;
	private ObjectProperty<Paint> fill;
	private IntegerProperty section;
	private ObjectProperty<Cursor> cursorType;
	private EventHandler<UpdateArgs> onUpdateHandler;
	private BooleanProperty showDetail;
	private StringProperty detail;

	public static final byte SHOW_NONE = 0;
	public static final byte SHOW_ELAPSED = 1;
	public static final byte SHOW_STATE = 2;
	public static final byte SHOW_ELAPSED_STATE = SHOW_ELAPSED | SHOW_STATE;
	private byte showDetailFlag = SHOW_NONE;
	private boolean isSelected;

	public ReadOnlyStringProperty textProperty() {
		return this.text;
	}

	public ReadOnlyDoubleProperty leftProperty() {
		return this.left;
	}

	public ReadOnlyDoubleProperty topProperty() {
		return this.top;
	}

	public ReadOnlyDoubleProperty widthProperty() {
		return this.width;
	}

	public ReadOnlyDoubleProperty heightProperty() {
		return this.height;
	}

	public ReadOnlyDoubleProperty radiusProperty() {
		return this.radius;
	}

	public ReadOnlyDoubleProperty rotationProperty() {
		return this.rotation;
	}

	public ReadOnlyBooleanProperty circleVisibleProperty() {
		return this.circleVisible;
	}

	public ReadOnlyBooleanProperty arcVisibleProperty() {
		return this.arcVisible;
	}

	public ReadOnlyBooleanProperty rectVisibleProperty() {
		return this.rectVisible;
	}

	public ReadOnlyBooleanProperty borderVisibleProperty() {
		return this.borderVisible;
	}

	public ReadOnlyBooleanProperty mouseTransparentProperty() {
		return this.mouseTransparent;
	}

	public List<Transform> getTransforms() {
		return this.transforms;
	}

	public ReadOnlyObjectProperty<Paint> fillProperty() {
		return this.fill;
	}

	public IntegerProperty sectionProperty() {
		return this.section;
	}

	public ReadOnlyObjectProperty<Cursor> cursorTypeProperty() {
		return this.cursorType;
	}

	public ReadOnlyBooleanProperty showDetailProperty() {
		return this.showDetail;
	}

	public ReadOnlyStringProperty detailProperty() {
		return this.detail;
	}

	public void toNextState() {
		System.out.println(table.getType());
		if (isSelected)
			setState(Table.State.RESERVE);
		else
			switch (table.getState()) {
			case READY:
			case RESERVE:
				setState(Table.State.IN_USE);
				break;
			case IN_USE:
				setState(Table.State.DONE);
				break;
			case DONE:
				setState(Table.State.READY);
				break;
			default:
				break;
			}
		onUpdate(new UpdateArgs(this, null, updateEventType));
	}

	private void setState(Table.State state) {
		table.setState(state);
		fill.set(getFillColor());
		updateElapsed();
	}

	public TableViewModel(Table table) {
		this.table = table;
		text = new SimpleStringProperty(table, "text", table.getText());
		left = new SimpleDoubleProperty(table, "left", table.getLeft());
		top = new SimpleDoubleProperty(table, "top", table.getTop());
		// radius = new SimpleDoubleProperty(table, "radius",
		// Math.sqrt(table.getWidth() * table.getWidth() / 2));
		radius = new SimpleDoubleProperty(table, "radius", table.getWidth() / 2);
		if (table.getVariants().contains(Table.Variant.CIRCLE) || table.getVariants().contains(Table.Variant.STOOL)) {
			double w = Math.sqrt(table.getWidth() * table.getWidth() / 2);
			width = new SimpleDoubleProperty(table, "width", w);
			height = new SimpleDoubleProperty(table, "width", w);
			// width = new SimpleDoubleProperty(table,"width")
		} else {
			width = new SimpleDoubleProperty(table, "width", table.getWidth());
			height = new SimpleDoubleProperty(table, "height", table.getHeight());
		}
		rotation = new SimpleDoubleProperty(table, "rotation", table.getRotation());
		transforms = new ArrayList<Transform>();

		if (table.getType() != Table.Type.ARROW) {
			if (width.get() == 0) {
				width = new SimpleDoubleProperty(10);
			}
			if (height.get() == 0) {
				height = new SimpleDoubleProperty(10);
			}
		}
		// label + table: center
		// wall: top,left
		double pivotX = 0, pivotY = 0;
		if (table.getType() == Table.Type.TABLE || table.getType() == Table.Type.LABEL) {
			pivotX = table.getWidth() / 2;
			if (table.getVariants().contains(Table.Variant.CIRCLE) || table.getVariants().contains(Table.Variant.STOOL))
				pivotY = width.get() / 2;
			else
				pivotY = table.getHeight() / 2;
		}
		if (table.getRotation() == 90) {
			transforms.add(new Rotate(table.getRotation() / 2, pivotX, pivotY));
			transforms.add(new Rotate(table.getRotation() / 2, pivotX, pivotY));
		} else {
			transforms.add(new Rotate(table.getRotation(), pivotX, pivotY));
		}

		System.out.println(table.getType());
		circleVisible = new SimpleBooleanProperty(table, "circleVisible",
				table.getType() == Table.Type.STOOL || table.getVariants().contains(Table.Variant.STOOL)
						|| ((table.getType() == Table.Type.LABEL || table.getType() == Table.Type.TABLE)
								&& table.getVariants().contains(Table.Variant.CIRCLE)
								&& (table.getOnlyCircle().equalsIgnoreCase("circle")
										|| table.getOnlyCircle().equalsIgnoreCase("diamond"))));
		// ============ comment by Anil dev=============
		// rectVisible = new SimpleBooleanProperty(table, "rectVisible",
		// table.getType() != Table.Type.LABEL);
		rectVisible = new SimpleBooleanProperty(table, "rectVisible",
				table.getType() != Table.Type.STOOL && !table.getVariants().contains(Table.Variant.STOOL)&& table.getType() != Table.Type.LABEL
						&& !table.getOnlyCircle().equalsIgnoreCase("circle")
						&& !table.getOnlyCircle().equalsIgnoreCase("arc") && table.getType() != Table.Type.ARROW

		);
		arcVisible = new SimpleBooleanProperty(table, "rectVisible", table.getOnlyCircle().equalsIgnoreCase("arc"));

		borderVisible = new SimpleBooleanProperty(table, "borderVisible", table.isBorder());

		mouseTransparent = new SimpleBooleanProperty(table, "mouseTransparent", table.getType() != Table.Type.TABLE);

		// if (table.getType() == Table.Type.LABEL || table.getType() ==
		// Table.Type.STOOL) {
		// fill = new SimpleObjectProperty<>(table, "fill", Color.WHITE);
		// } else {
		fill = new SimpleObjectProperty<>(table, "fill", getFillColor());
		// }
		section = new SimpleIntegerProperty(table, "section", table.getSection());
		section.addListener((observable, oldValue, newValue) -> {
			this.table.setSection((int) newValue);
		});
		cursorType = new SimpleObjectProperty<>(table, "cursorType", Cursor.DEFAULT);
		showDetail = new SimpleBooleanProperty(false);
		detail = new SimpleStringProperty("");
		isSelected = false;
	}

	public void setShowElapsed(boolean value) {
		if (value) {
			showDetailFlag |= SHOW_ELAPSED;
		} else
			showDetailFlag &= ~(SHOW_ELAPSED);
		updateElapsed();
		showDetail.set(showDetailFlag != SHOW_NONE);
	}

	public void setShowState(boolean value) {
		if (value) {
			showDetailFlag |= SHOW_STATE;
		} else
			showDetailFlag &= ~(SHOW_STATE);
		updateElapsed();
		showDetail.set(showDetailFlag != SHOW_NONE);
	}

	private Paint getFillColor() {
		Paint color = Color.TRANSPARENT;
		switch (table.getState()) {
		case READY:
			color = Color.LIMEGREEN;
			break;
		case IN_USE:
			color = Color.GRAY;
			break;
		case DONE:
			color = Color.RED;
			break;
		case RESERVE:
			color = Color.YELLOW;
			break;
		case STATIC:
			// default:
			color = table.getType() == Table.Type.STAIRS || table.getType() == Table.Type.STOOL ? Color.WHITE
					: Color.BLACK;// DARKGRAY;
			// color = table.getType() == Table.Type.WALL ? Color.BLACK : color;
			color = table.getType() == Table.Type.PLANT ? Color.TRANSPARENT : color;
			// if (table.getSubType().equalsIgnoreCase("stool")) {
			// color = Color.WHITE;
			// } else {
			// color = table.getType() == Table.Type.STAIRS || table.getType()
			// == Table.Type.STOOL ? Color.WHITE : Color.DARKGRAY;
			// }
			break;
		}
		return color;
	}

	public void setFillColor(String color) {
		switch (color.toUpperCase()) {
		case "GREEN":
			setState(Table.State.READY);
			break;
		case "GRAY":
			setState(Table.State.IN_USE);
			break;
		case "YELLOW":
			setState(Table.State.RESERVE);
			break;
		case "RED":
			setState(Table.State.DONE);
			break;
		}
	}

	static final String ELAPSED_FORMAT = "%02d";// :%02d
	static final String ELAPSED_STATE_FORMAT = "%02d\n%s";// :%02d
	static final String STATE_READY = "Available";
	static final String STATE_IN_USE = "Busy";
	static final String STATE_RESERVE = "Reserved";
	static final String STATE_DONE = "Finished";

	static String getState(Table.State state) {
		switch (state) {
		case READY:
			return STATE_READY;
		case IN_USE:
			return STATE_IN_USE;
		case RESERVE:
			return STATE_RESERVE;
		case DONE:
			return STATE_DONE;
		}
		return null;
	}

	public void updateElapsed() {
		switch (showDetailFlag) {
		case SHOW_NONE:
			this.detail.set(null);
			return;
		case SHOW_STATE:
			this.detail.set(getState(table.getState()));
			break;
		case SHOW_ELAPSED: {
			long elapsed = table.getElapsedTime() / 1000;
			this.detail.set(String.format(ELAPSED_FORMAT, elapsed / 60));// ,
																			// elapsed
																			// %
																			// 60
			break;
		}
		case SHOW_ELAPSED_STATE: {
			long elapsed = table.getElapsedTime() / 1000;
			this.detail.set(String.format(ELAPSED_STATE_FORMAT, elapsed / 60, getState(table.getState())));// ,
																											// elapsed
																											// %
																											// 60
			break;
		}
		}
	}

	public void setOnUpdate(EventHandler<UpdateArgs> handler) {
		this.onUpdateHandler = handler;
	}

	private void onUpdate(UpdateArgs arg) {
		if (this.onUpdateHandler != null)
			this.onUpdateHandler.handle(arg);
	}

	public void dragged(double left, double top, boolean doUpdate) {
		System.out.println(table.getId() + " " + table.getLeft() + " " + table.getTop() + " " + left + " " + top);

		table.setLeft(table.getLeft() + left);
		table.setTop(table.getTop() + top);

		if (doUpdate) {
			onUpdate(new UpdateArgs(this, null, remapEventType));
		}
	}

	public void resetGreen() {
		if (!table.getSubType().equalsIgnoreCase("stool")) {
			setState(Table.State.READY);
		}
		table.setLastChanged();
		onUpdate(new UpdateArgs(this, null, resetEventType));
	}

	public void resetSection() {
		table.setLastChanged();
		table.setSection(0);
		onUpdate(new UpdateArgs(this, null, resetEventType));
	}

	public void switchSelect() {
		isSelected = !isSelected;
		cursorType.set(isSelected ? Cursor.HAND : Cursor.DEFAULT);
	}

	public Table getTable() {
		return table;
	}
}
