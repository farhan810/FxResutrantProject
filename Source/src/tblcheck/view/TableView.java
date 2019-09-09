package tblcheck.view;

/**
 * Created by cuongdm5 on 1/10/2016.
 */

import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import tblcheck.model.Config;
import tblcheck.model.Table;
import tblcheck.model.Table.Variant;
import tblcheck.viewmodel.TableViewModel;

public class TableView {

	@FXML
	public Group parentGroup;
	@FXML
	public VBox vBox;
	@FXML
	public Text lblElapsed;
	@FXML
	private Rectangle rect;
	@FXML
	private Group grp;
	@FXML
	private Circle cir;
	@FXML
	private Arc arc;
	@FXML
	private Text text;
	@FXML
	private StackPane pane;

	private ContextMenu ctxMenu;
	private Canvas cvInfo;

	private TableViewModel viewModel;

	public void setTableViewModel(TableViewModel viewModel) {
		setTableViewModelPane(viewModel);
	}

	public void setTableViewModelPane(TableViewModel viewModel) {
		this.viewModel = viewModel;
		Table tb = viewModel.getTable();
		grp.layoutXProperty().bind(viewModel.leftProperty());
		grp.layoutYProperty().bind(viewModel.topProperty());
		pane.setPrefHeight(viewModel.heightProperty().get());
		pane.setPrefWidth(viewModel.radiusProperty().get() * 2);

		if (tb.getType() == Table.Type.PLANT) {
			Image images = new Image("images/plant.jpg");
			ImageView image = new ImageView(images);
			image.setFitWidth(tb.getWidth());
			image.setFitHeight(tb.getHeight());

			pane.getChildren().add(image);
			cir.setVisible(false);
			rect.setVisible(false);
			arc.setVisible(false);
			text.setVisible(false);
			return;
		}

		text.textProperty().bind(viewModel.textProperty());
		text.setFont(Font.font(null, FontWeight.findByWeight(tb.getFontWeight()), tb.getFontSize()));

		pane.getStyleClass().addAll("tbl-" + tb.getType().toString().toLowerCase());
		if (tb.getType() == Table.Type.LABEL || tb.getType() == Table.Type.STOOL) {
			final int text_padding = 6 * 2;
			text.wrappingWidthProperty().add(1);
			if (viewModel.widthProperty().get() < text.getLayoutBounds().getWidth() + text_padding)
				pane.setPrefWidth(text.getLayoutBounds().getWidth());// +
																		// text_padding);
			if (!tb.isBorder())
				pane.getStyleClass().add("no-border");
		} else if (tb.getType() == Table.Type.ARROW) {
			if (tb.getRotation() == 45) {
				grp.layoutXProperty().bind(viewModel.leftProperty().add(2));
				grp.layoutYProperty().bind(viewModel.topProperty().add(3));
			} else if (tb.getRotation() == 90) {
				grp.layoutXProperty().bind(viewModel.leftProperty().add(8));
				grp.layoutYProperty().bind(viewModel.topProperty().add(3));
			} else if (tb.getRotation() == 135) {
				grp.layoutXProperty().bind(viewModel.leftProperty().add(11));
				grp.layoutYProperty().bind(viewModel.topProperty().add(11));
			} else if (tb.getRotation() == 180) {
				grp.layoutXProperty().bind(viewModel.leftProperty().add(11));
				grp.layoutYProperty().bind(viewModel.topProperty().add(11));
			}
			double width = tb.getWidth();
			double height = tb.getHeight();
			double strokeWidth = 0.0;
			if (width < 20)
				width = 20;
			if (height < 20)
				height = 20;
			height = height / 2;

			Polygon triangle = new Polygon(0.0, 0.0, width, 0.0, height, width / 2, 0.0, 0.0);
			triangle.setStroke(Color.BLACK);
			// triangle.setStrokeLineCap(StrokeLineCap.ROUND);
			triangle.setStrokeWidth(strokeWidth);
			triangle.setFill(Color.BLACK);

			triangle.setPickOnBounds(true);
			// triangle.getStyleClass().addAll(STYLE_CLASS_DEFAULT,
			// "predefined-filter-icon"); //NON-NLS
			triangle.setRotate(tb.getRotation() + 135);
			pane.getChildren().add(triangle);

		} else if (tb.getType() == Table.Type.STAIRS) {
			double w = tb.getWidth();
			double h = tb.getHeight();
			Canvas c = new Canvas(w, h);
			c.setMouseTransparent(true);
			GraphicsContext gc = c.getGraphicsContext2D();
			gc.setStroke(Color.BLACK);
			gc.setLineWidth(1);
			double offset = 10;
			int steps = tb.getSteps();
			if (tb.getVariants().contains(Variant.VERTICAL)) {
				offset = w / steps;
				for (double i = offset; i < w; i += offset) {
					gc.strokeLine(i, 0, i, h);
				}
			} else {
				offset = h / steps;
				for (double i = offset; i < h; i += offset) {
					// horizontal
					gc.strokeLine(0, i, w, i);
				}
			}
			pane.getChildren().add(c);
			// c.toBack();
		}
		// if(viewModel.getTransforms().size() > 0)
		// pane.getTransforms().add(new Rotate(tb.getRotation(),0,0));
		pane.getTransforms().addAll(viewModel.getTransforms());
		// pane.setRotate(tb.getRotation());

		rect.widthProperty().bind(viewModel.widthProperty());
		rect.heightProperty().bind(viewModel.heightProperty());
		// rect.rotateProperty().bind(viewModel.rotationProperty());

		if (viewModel.getTransforms().size() > 0)
			try {
				text.setRotate(-viewModel.rotationProperty().get());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		// grp.getChildren().clear();
		// rect.getTransforms().addAll(viewModel.getTransforms());
		// grp.getChildren().add(rect);
		// grp.getTransforms().addAll(viewModel.getTransforms());

		cir.visibleProperty().bind(viewModel.circleVisibleProperty());
		rect.visibleProperty().bind(viewModel.rectVisibleProperty());
		cir.radiusProperty().bind(viewModel.radiusProperty());

		arc.visibleProperty().bind(viewModel.arcVisibleProperty());
		arc.radiusXProperty().bind(viewModel.radiusProperty());
		arc.radiusYProperty().bind(viewModel.radiusProperty());

		// this.viewModel.sectionProperty().addListener((observable, oldValue,
		// newValue) -> {
		// this.cir.getStyleClass().setAll("tbl-section","section-" + (int)
		// newValue);
		// this.arc.getStyleClass().setAll("tbl-section","section-" + (int)
		// newValue);
		// if (!viewModel.circleVisibleProperty().get()) {
		// this.rect.getStyleClass().setAll("tbl-section","section-" + (int)
		// newValue);
		// }
		// });

		this.viewModel.sectionProperty().addListener((observable, oldValue, newValue) -> {
			System.out.println("======Right click and selection on table=====");
			if ((int) newValue == 0) {
				this.cir.getStyleClass().removeAll("tbl-section", "section-" + (int) oldValue);
				this.arc.getStyleClass().removeAll("tbl-section", "section-" + (int) oldValue);
				if (!viewModel.circleVisibleProperty().get()) {
					this.rect.getStyleClass().removeAll("tbl-section", "section-" + (int) oldValue);
				}
			} else {
				this.cir.getStyleClass().setAll("tbl-section", "section-" + (int) newValue);
				this.arc.getStyleClass().setAll("tbl-section", "section-" + (int) newValue);
				if (!viewModel.circleVisibleProperty().get()) {
					this.rect.getStyleClass().setAll("tbl-section", "section-" + (int) newValue);
				}

			}

		});

		if (!viewModel.getTable().getSubType().equalsIgnoreCase("stool")) {
			pane.setOnMouseClicked(event -> {
				if (event.getButton() == MouseButton.PRIMARY && viewModel.getTable().getType() == Table.Type.TABLE
						&& Config.getInstance().isMovingTables()) {
					if (Table.State.READY.equals(viewModel.getTable().getState())) {
						pane.setOnDragDetected(event1 -> {
							pane.startFullDrag();
							// pane.startDragAndDrop(TransferMode.MOVE);
							// System.out.println("dragged detected>> " +
							// this.getPane().getTranslateX() + " "
							// + this.getPane().getTranslateY());
						});
						pane.setOnMouseReleased(event1 -> {
							System.out.println("dragged released >> " + this.getPane().getTranslateX() + " "
									+ this.getPane().getTranslateY());
							viewModel.dragged(this.getPane().getTranslateX(), this.getPane().getTranslateY(), true);
						});
						pane.setOnMouseDragged(event1 -> {
							this.getPane().setTranslateX(event1.getX() + this.getPane().getTranslateX());
							this.getPane().setTranslateY(event1.getY() + this.getPane().getTranslateY());
							event1.consume();
						});
					}
				} else if (event.getButton() == MouseButton.SECONDARY
						&& viewModel.getTable().getType() == Table.Type.TABLE) {
					System.out.println("Secondary mouse button clicked");
					ctxMenu.show(pane, event.getScreenX(), event.getScreenY());
					ctxMenu.setUserData(viewModel);
					int i = -1;
					for (MenuItem menuItem : ctxMenu.getItems()) {
						i++;
						if (i == viewModel.sectionProperty().get()) {
							menuItem.setDisable(true);
						} else {
							menuItem.setDisable(false);
						}
					}
				} else
					viewModel.toNextState();
				System.out.println(viewModel.fillProperty().get());

			});
		}

		pane.mouseTransparentProperty().bind(viewModel.mouseTransparentProperty());
		pane.cursorProperty().bind(viewModel.cursorTypeProperty());
		if (tb.getType() == Table.Type.TABLE) {
			rect.fillProperty().bind(viewModel.fillProperty());
			cir.fillProperty().bind(viewModel.fillProperty());
			arc.fillProperty().bind(viewModel.fillProperty());

			// cvInfo = new Canvas(pane.getPrefWidth(), 28);
			// cvInfo.setLayoutY(pane.getPrefHeight());
			//// lblElapsed.visibleProperty().bind(viewModel.showDetailProperty());
			//// lblElapsed.textProperty().bind(viewModel.detailProperty());
			// grp.getChildren().add(cvInfo);
			// viewModel.detailProperty().addListener((observable, oldValue,
			// newValue) -> {
			// if (viewModel.showDetailProperty().get()) {
			// GraphicsContext gc = cvInfo.getGraphicsContext2D();
			// gc.setTextAlign(TextAlignment.CENTER);
			// gc.setTextBaseline(VPos.CENTER);
			// gc.clearRect(0, 0, cvInfo.getWidth(), cvInfo.getHeight());
			// gc.fillText(
			// viewModel.detailProperty().get() + "\nABC",
			// Math.round(cvInfo.getWidth() / 2),
			// Math.round(cvInfo.getHeight() / 2)
			// );
			// }
			// });
		} else if (tb.getType() == Table.Type.LABEL || tb.getType() == Table.Type.STOOL) {
			cir.fillProperty().bind(viewModel.fillProperty());
			rect.fillProperty().bind(viewModel.fillProperty());
		} else {

		}
	}

	public Bounds getLayoutBounds() {
		return grp.getLayoutBounds();
	}

	public Bounds getBoundsInLocal() {
		return grp.getBoundsInLocal();
	}

	public Bounds getBoundsInParent() {
		return grp.getBoundsInParent();
	}

	public void setRotation(double rotate, double x, double y, double z) {
		grp.getTransforms().clear();
		grp.getTransforms().add(new Rotate(rotate, x, y));
		// pane.requestLayout();
		// pane.setRotate(rotate);
	}

	public TableViewModel getViewModel() {
		return viewModel;
	}

	public Pane getPane() {
		return pane;
	}

	public void setCtxMenu(ContextMenu ctxMenu) {
		this.ctxMenu = ctxMenu;
	}

	@FXML
	void initialize() {
		pane.setCache(true);
		pane.setCacheHint(CacheHint.SCALE_AND_ROTATE);
		pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		rect.maxWidth(Double.MAX_VALUE);
		rect.maxHeight(Double.MAX_VALUE);
		cir.maxWidth(Double.MAX_VALUE);
		cir.maxHeight(Double.MAX_VALUE);
		cir.toBack();
		// text.setCache(true);
		// text.setCacheHint(CacheHint.SCALE);
		// text.setSmooth(true);
		// text.setFontSmoothingType(FontSmoothingType.LCD);
	}
}
