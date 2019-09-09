package eu.hansolo.enzo.experimental.led;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Affine;
import javafx.stage.Stage;

public class Arrow extends Path {
	private static final double defaultArrowHeadSize = 5.0;

	public Arrow() {
		// TODO Auto-generated constructor stub
	}

	private static final double[] POLYGON_POINTS = { -5, -4, -5, 4, 5, 0 };

	public static void makeArrow(Pane parent, Circle start, Circle end, double t) {
		Polygon arrow = new Polygon(POLYGON_POINTS);
		arrow.setFill(Color.BLACK);
		arrow.setStroke(Color.BLACK);
		double dx = end.getCenterX() - start.getCenterX();
		double dy = end.getCenterY() - start.getCenterY();

		double d = Math.hypot(dx, dy);

		double sin = dy / d;
		double cos = dx / d;

		// matrix:
		// [ cos -sin 0 t * dx + start.getCenterX() ]
		// [ sin cos 0 t * dy + start.getCenterY() ]
		// [ 0 0 1 0 ]
		// [ 0 0 0 1 ]
		Affine affine = new Affine(cos, -sin, t * dx + start.getCenterX(), sin, cos, t * dy + start.getCenterY());

		arrow.getTransforms().add(affine);
		parent.getChildren().add(arrow);
	}

	
	public void start(Stage primaryStage) {
		Circle end = new Circle(200, 20, 5);
		Circle start = new Circle(20, 200, 5);
		Line line = new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(), end.getCenterY());

		Pane root = new Pane(line, start, end);

		makeArrow(root, start, end, 0.5);

		Scene scene = new Scene(root, 400, 400);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize) {
		super();
		strokeProperty().bind(fillProperty());
		setFill(Color.BLACK);

		// Line
		getElements().add(new MoveTo(startX, startY));
		getElements().add(new LineTo(endX, endY));

		// ArrowHead
		double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
		double sin = Math.sin(angle);
		double cos = Math.cos(angle);
		// point1
		double x1 = (-1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
		double y1 = (-1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
		// point2
		double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
		double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

		getElements().add(new LineTo(x1, y1));
		getElements().add(new LineTo(x2, y2));
		getElements().add(new LineTo(endX, endY));
	}

	public Arrow(double startX, double startY, double endX, double endY) {
		this(startX, startY, endX, endY, defaultArrowHeadSize);
	}
}

// import javafx.geometry.Point2D;
// import javafx.scene.Group;
// import javafx.scene.shape.ArcTo;
// import javafx.scene.shape.Line;
//
// public class Arrow extends Group {
//
// private static final String STYLE_CLASS_LINE = "arrow-line";
// private static final String STYLE_CLASS_HEAD = "arrow-head";
//
// private final Line line = new Line();
// private final ArrowHead head = new ArrowHead();
//
// private double startX;
// private double startY;
//
// private double endX;
// private double endY;
//
// /**
// * Creates a new {@link Arrow}.
// */
// public Arrow() {
//
// line.getStyleClass().add(STYLE_CLASS_LINE);
// head.getStyleClass().add(STYLE_CLASS_HEAD);
//
// getChildren().addAll(line, head);
// }
//
// /**
// * Sets the width of the arrow-head.
// *
// * @param width the width of the arrow-head
// */
// public void setHeadWidth(final double width) {
// head.setWidth(width);
// }
//
// /**
// * Sets the length of the arrow-head.
// *
// * @param length the length of the arrow-head
// */
// public void setHeadLength(final double length) {
// head.setLength(length);
// }
//
// /**
// * Sets the radius of curvature of the {@link ArcTo} at the base of the
// arrow-head.
// *
// * <p>
// * If this value is less than or equal to zero, a straight line will be drawn
// instead. The default is -1.
// * </p>
// *
// * @param radius the radius of curvature of the arc at the base of the
// arrow-head
// */
// public void setHeadRadius(final double radius) {
// head.setRadiusOfCurvature(radius);
// }
//
// /**
// * Gets the start point of the arrow.
// *
// * @return the start {@link Point2D} of the arrow
// */
// public Point2D getStart() {
// return new Point2D(startX, startY);
// }
//
// /**
// * Sets the start position of the arrow.
// *
// * @param startX the x-coordinate of the start position of the arrow
// * @param startY the y-coordinate of the start position of the arrow
// */
// public void setStart(final double startX, final double startY) {
// this.startX = startX;
// this.startY = startY;
// }
//
// /**
// * Gets the start point of the arrow.
// *
// * @return the start {@link Point2D} of the arrow
// */
// public Point2D getEnd() {
// return new Point2D(endX, endY);
// }
//
// /**
// * Sets the end position of the arrow.
// *
// * @param endX the x-coordinate of the end position of the arrow
// * @param endY the y-coordinate of the end position of the arrow
// */
// public void setEnd(final double endX, final double endY) {
// this.endX = endX;
// this.endY = endY;
// }
//
// /**
// * Draws the arrow for its current size and position values.
// */
// public void draw() {
//
// final double deltaX = endX - startX;
// final double deltaY = endY - startY;
//
// final double angle = Math.atan2(deltaX, deltaY);
//
// final double headX = endX - head.getLength() / 2 * Math.sin(angle);
// final double headY = endY - head.getLength() / 2 * Math.cos(angle);
//
// line.setStartX(GeometryUtils.moveOffPixel(startX));
// line.setStartY(GeometryUtils.moveOffPixel(startY));
// line.setEndX(GeometryUtils.moveOffPixel(headX));
// line.setEndY(GeometryUtils.moveOffPixel(headY));
//
// head.setCenter(headX, headY);
// head.setAngle(Math.toDegrees(-angle));
// head.draw();
// }
// }
//
