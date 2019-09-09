package tblcheck.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import java.util.prefs.Preferences;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablecheck.client.main.Main;
import com.tablecheck.service.ServiceApplication;
import com.tablecheck.service.controller.Utility;
import com.tablecheck.service.model.PingResponse;

import eu.hansolo.enzo.experimental.led.Led;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import tblcheck.Resource;
import tblcheck.controller.TableController;
import tblcheck.helper.ClientHelper;
import tblcheck.helper.HttpHelper;
import tblcheck.helper.LayoutHelper;
import tblcheck.helper.WindowsHelper;
import tblcheck.model.Config;
import tblcheck.model.ManagerStatus;
import tblcheck.model.RestRoomStatus;
import tblcheck.model.Table;
import tblcheck.service.ITableService;
import tblcheck.service.TableService;
import tblcheck.viewmodel.RoomViewModel;
import tblcheck.viewmodel.SectionViewModel;
import tblcheck.viewmodel.TableViewModel;

public class RoomView {
	private static final String file_table_state = "tablestat.json";
	private static final String file_restaurant_config = "restaurant_resources.json";
	@FXML
	public Group contentGroup;
	@FXML
	public Group zoomGroup;
	@FXML
	public GridPane pnStatusGlance;
	@FXML
	public ScrollPane pnStatusGlanceScroll;
	@FXML
	public Pane pnControl;
	@FXML
	public Slider sldScale;
	@FXML
	public Button btnFitToScreen;
	@FXML
	public Button btnLock;
	@FXML
	public MenuButton mnBtnOption;
	// @FXML
	// public MenuItem btnExit;//TODO Uncomment
	// @FXML
	// public Button btnExit;
	@FXML
	public MenuItem btnMinimize;
	@FXML
	public MenuItem btnMoveTables;
	@FXML
	public MenuItem btnShowNetStatus;
	@FXML
	public MenuItem btnShowServerHost;
	@FXML
	public MenuItem btnShowClientHost;
	@FXML
	public MenuItem btnShowTime;
	@FXML
	public MenuItem btnShowState;
	@FXML
	public MenuItem btnShowRestroomStatus;
	@FXML
	public MenuItem btnShowManagerStatus;
	@FXML
	public MenuItem btnShowStatusGlance;
	@FXML
	public MenuItem btnClearSectionStaff;
	@FXML
	public MenuItem btnClearTableSection;
	@FXML
	public MenuItem btnLockMenu;
	@FXML
	public Hyperlink lnkHome;
	@FXML
	public ImageView imvLogo;
	@FXML
	private Pane pnRoot;
	@FXML
	private Label lblLayout;
	@FXML
	private Label lblServerVer;
	@FXML
	private Button btnSelect;
	@FXML
	public StackPane pnStatus;
	@FXML
	public HBox serverBox;
	@FXML
	public HBox activeBox;
	@FXML
	public Text lblActive;
	@FXML
	public HBox clientBox;
	@FXML
	private Button btnAssignSection;
	@FXML
	private Button btnOption;
	@FXML
	private Button btnStaff;
	@FXML
	public Button M;
	@FXML
	public Button Restroom;
	@FXML
	public Button W;
	@FXML
	public Button H;
	@FXML
	public Button Manager;
	@FXML
	public Button K;
	@FXML
	private ScrollPane pnScroll;
	@FXML
	private Pane pnInnerContent;
	@FXML
	private Button btnUpdate;
	@FXML
	private Button btnServer;
	@FXML
	private AnchorPane pnContent;
	@FXML
	private Button btnAllGreen;
	@FXML
	private Text lblStatus;
	@FXML
	private Text lblServer;
	// @FXML
	// private Text lblServer1;
	@FXML
	private GridPane serverIndi;
	@FXML
	private Text lblClient;
	@FXML
	private Button btnLayout;
	@FXML
	private Circle batteryInd;
	@FXML
	private Text lblBatteryStts;
	Led led;

	private ContextMenu tblContextMenu;
	private ContextMenu ctxMenu;

	private TableController controller;

	private Label[][] labels;
	private Timer statusGlanceTimer;
	private HashMap<Integer, String> statusGlances;

	int section;
	double zoomFactor;
	private ObservableList<MenuItem> menuItems;
        
        public RoomView() {
	}

	private final int PADDING = 7;
	private DoubleProperty myScale = new SimpleDoubleProperty(1.0);
	private DoubleProperty minScale = new SimpleDoubleProperty(0.5);
	private DoubleProperty maxScale = new SimpleDoubleProperty(2.0);

	private RoomViewModel roomViewModel;
	private Node scaleNode;
	private boolean isSelected;
	private boolean lockScroll = false;

	private String saveOptionString = "";
	private String configurationStr = "";

	@FXML
	void initialize() {
		try {
			// HttpServer server = HttpServer.create(new
			// InetSocketAddress(9090), 0);
			// HttpHandler restRoomHandler = new HttpHandler() {
			// public void handle(HttpExchange he) throws IOException {
			// System.out.println("restRoomHandler >>" + roomViewModel);
			// InputStreamReader isr = new
			// InputStreamReader(he.getRequestBody(), "utf-8");
			// BufferedReader br = new BufferedReader(isr);
			// String query = br.readLine();
			// System.out.println(query);
			// roomViewModel.refreshRestroomStatus(query);
			// String response = "";
			// he.sendResponseHeaders(200, response.length());
			// OutputStream os = he.getResponseBody();
			// os.write(response.toString().getBytes());
			// os.close();
			// }
			// };
			// HttpHandler managerHandler = new HttpHandler() {
			// public void handle(HttpExchange he) throws IOException {
			// System.out.println("managerHandler >>" + roomViewModel);
			// InputStreamReader isr = new
			// InputStreamReader(he.getRequestBody(), "utf-8");
			// BufferedReader br = new BufferedReader(isr);
			// String query = br.readLine();
			// System.out.println(query);
			// roomViewModel.refreshManagerStatus(query);
			// String response = "";
			// he.sendResponseHeaders(200, response.length());
			// OutputStream os = he.getResponseBody();
			// os.write(response.toString().getBytes());
			// os.close();
			// }
			// };
			// HttpHandler staffNumberChangeHandler = new HttpHandler() {
			// public void handle(HttpExchange he) throws IOException {
			// System.out.println("staffNumberChangeHandler >>" +
			// roomViewModel);
			// InputStreamReader isr = new
			// InputStreamReader(he.getRequestBody(), "utf-8");
			// BufferedReader br = new BufferedReader(isr);
			// String query = br.readLine();
			// System.out.println(query);
			// roomViewModel.refreshStaffNumber(query);
			// String response = "";
			// he.sendResponseHeaders(200, response.length());
			// OutputStream os = he.getResponseBody();
			// os.write(response.toString().getBytes());
			// os.close();
			// }
			// };
			//
			// HttpHandler staffHandler = new HttpHandler() {
			// public void handle(HttpExchange he) throws IOException {
			// System.out.println("staffHandler >> " + roomViewModel);
			// InputStreamReader isr = new
			// InputStreamReader(he.getRequestBody(), "utf-8");
			// BufferedReader br = new BufferedReader(isr);
			// String temp = null;
			// String query = "";
			// while ((temp = br.readLine()) != null) {
			// query = query + temp + "\n";
			// }
			// System.out.println(query);
			// roomViewModel.refreshStaff(query);
			// String response = "";
			// he.sendResponseHeaders(200, response.length());
			// OutputStream os = he.getResponseBody();
			// os.write(response.toString().getBytes());
			// os.close();
			// }
			// };
			// HttpHandler staffSectionHandler = new HttpHandler() {
			// public void handle(HttpExchange he) throws IOException {
			// System.out.println("staffSectionHandler >>" + roomViewModel);
			// InputStreamReader isr = new
			// InputStreamReader(he.getRequestBody(), "utf-8");
			// BufferedReader br = new BufferedReader(isr);
			// String temp = null;
			// String query = "";
			// while ((temp = br.readLine()) != null) {
			// query = query + temp + "\n";
			// }
			// System.out.println(query);
			// roomViewModel.refreshStaffSection(query);
			// String response = "";
			// he.sendResponseHeaders(200, response.length());
			// OutputStream os = he.getResponseBody();
			// os.write(response.toString().getBytes());
			// os.close();
			// }
			// };
			// HttpHandler tableHandler = new HttpHandler() {
			// public void handle(HttpExchange he) throws IOException {
			// System.out.println("tableHandler >>" + roomViewModel);
			// InputStreamReader isr = new
			// InputStreamReader(he.getRequestBody(), "utf-8");
			// BufferedReader br = new BufferedReader(isr);
			// String query = br.readLine();
			// System.out.println(query);
			// roomViewModel.refreshData(query);
			// String response = "";
			// he.sendResponseHeaders(200, response.length());
			// OutputStream os = he.getResponseBody();
			// os.write(response.toString().getBytes());
			// os.close();
			// }
			// };

			// server.createContext("/restroom", restRoomHandler);
			// server.createContext("/manager", managerHandler);
			// server.createContext("/staff_number_change",
			// staffNumberChangeHandler);
			// server.createContext("/stafflist", staffHandler);
			// server.createContext("/staffsection", staffSectionHandler);
			// server.createContext("/table", tableHandler);
			// server.setExecutor(null); // creates a default executor
			// server.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		scaleNode = zoomGroup;
		tblContextMenu = new ContextMenu();
		menuItems = tblContextMenu.getItems();
		MenuItem menuItem = new MenuItem("None");
		menuItem.setOnAction(event -> {
			TableViewModel vm = (TableViewModel) tblContextMenu.getUserData();
			if (vm != null) {
				vm.sectionProperty().set(0);
				roomViewModel.updateTable(vm);
			}
		});
		menuItem.getStyleClass().add("section-0");
		tblContextMenu.setPrefWidth(120);
		menuItems.add(menuItem);

		statusGlanceTimer = new Timer();
		String[] colors = { "#F22613", "#C8F7C5", "#C0392B", "#6C7A89", "#663399", "#BE90D4", "#ABB7B7", "#C5EFF7",
				"#4183D7", "#36D7B7" };
		labels = new Label[20][2];
		int colorLbl = 0;
		for (int i = 0; i < labels.length; i++) {
			for (int j = 0; j < labels[i].length; j++) {
				labels[i][j] = new Label();
				pnStatusGlance.add(labels[i][j], j, i);
			}
			if (i % 10 == 0) {
				colorLbl = 0;
			}
			String color = "-fx-background-color: " + colors[colorLbl++] + "; -fx-font-size: 16pt;";
			// System.out.println(color);
			labels[i][0].setStyle(color);
		}
		statusGlanceTimer.schedule(new TimerTask() {
			@Override
			public void run() {
                            try{
				Platform.runLater(() -> {
					refreshStatusGlance();
				});
                            }catch(Exception e){
                                
                            }
			}

			private void refreshStatusGlance() {
				// System.out.println(labels);
				for (int i = 0; i < labels.length && i < 10; i++) {
					labels[i][0].setText("");
					labels[i][1].setText("");
				}
				statusGlances = roomViewModel.getStatusGlances();
				// System.out.println("statusGlances " + statusGlances);
				for (int i = 0; i < roomViewModel.sectionProperty().getValue() && i < labels.length && i < 10; i++) {
					labels[i][0].setText("    ");
					if (statusGlances != null && statusGlances.containsKey(i + 1)) {
						labels[i][1].setText(statusGlances.get(i + 1));
						labels[i][1].setStyle("-fx-font-size: 14pt;");
					} else {
						labels[i][1].setText("No wait staff");
						labels[i][1].setStyle("-fx-background-color: #FF99FF; -fx-font-size: 14pt;");
					}
				}
				if (statusGlances != null) {

					for (HashMap.Entry<Integer, String> entry : statusGlances.entrySet()) {
						labels[entry.getKey() - 1][0].setText("    ");
						labels[entry.getKey() - 1][1]
								.setText(entry.getValue().substring(0, Math.min(12, entry.getValue().length())));
						System.out.println(entry.getKey());
						System.out.println(entry.getValue());
					}
				}
			}
		}, 5000, 5000);

		ctxMenu = new ContextMenu();
		// menuItem
		// btnExit.setOnAction(event -> {
		// saveOptionMenus();
		// roomViewModel.exit();
		// Platform.exit();
		// // if (!Config.getInstance().isServer()) {
		// System.exit(0);
		// // }
		// });
		btnMinimize.setOnAction(event -> {
			((Stage) (pnRoot.getScene().getWindow())).setIconified(true);
		});
		btnShowNetStatus.setOnAction(event -> {
			boolean display = !pnStatus.isVisible();
			pnStatus.setVisible(display);
			roomViewModel.setShowNetStatus(display);
			btnShowNetStatus.setText(display ? "Hide Net Status" : "Show Net Status");
			saveOptionMenus();
		});
		btnShowServerHost.setOnAction(event -> {
			boolean display = !lblServer.isVisible();
			lblServer.setVisible(display);
			serverBox.setVisible(display);
			roomViewModel.setShowServerHost(display);
			btnShowServerHost.setText(display ? "Hide Server Host" : "Show Server Host");
			saveOptionMenus();
		});
		btnShowClientHost.setOnAction(event -> {
			boolean display = !lblClient.isVisible();
			lblClient.setVisible(display);
			clientBox.setVisible(display);
			roomViewModel.setShowClientHost(display);
			btnShowClientHost.setText(display ? "Hide Client Host" : "Show Client Host");
			saveOptionMenus();
		});
		btnShowTime.setOnAction(event -> {
			if (!roomViewModel.isShowTime()) {
				roomViewModel.setShowTime(true);
				btnShowTime.setText("Hide Time");
			} else {
				roomViewModel.setShowTime(false);
				btnShowTime.setText("Show Time");
			}
			saveOptionMenus();
		});
		btnShowState.setOnAction(event -> {
			boolean showState = !roomViewModel.isShowState();
			roomViewModel.setShowState(showState);
			btnShowState.setText(showState ? "Hide State" : "Show State");
			saveOptionMenus();
		});
		btnShowRestroomStatus.setOnAction(event -> {
			boolean showRestRoomStatus = !roomViewModel.isShowRestRoomStatus();
			roomViewModel.setShowRestRoomStatus(showRestRoomStatus);
			btnShowRestroomStatus.setText(showRestRoomStatus ? "Hide Restroom Status" : "Show Restroom Status");

			roomViewModel.setPauseRestRoomRefresh(!showRestRoomStatus);

			M.setVisible(showRestRoomStatus);
			Restroom.setVisible(showRestRoomStatus);
			W.setVisible(showRestRoomStatus);
			saveOptionMenus();
		});

		btnShowManagerStatus.setOnAction(event -> {
			boolean showManagerStatus = !roomViewModel.isShowManagerStatus();
			roomViewModel.setShowManagerStatus(showManagerStatus);
			btnShowManagerStatus.setText(showManagerStatus ? "Hide Manager Status" : "Show Manager Status");

			roomViewModel.setPauseManagerRefresh(!showManagerStatus);

			H.setVisible(showManagerStatus);
			Manager.setVisible(showManagerStatus);
			K.setVisible(showManagerStatus);
			saveOptionMenus();
		});

		btnShowStatusGlance.setOnAction(event -> {
			boolean showStatusGlance = !roomViewModel.isShowStatusGlance();
			roomViewModel.setShowStatusGlance(showStatusGlance);
			btnShowStatusGlance.setText(showStatusGlance ? "Hide Wait Staff" : "Show Wait Staff");

			this.pnStatusGlance.setVisible(showStatusGlance);
			this.pnStatusGlanceScroll.setVisible(roomViewModel.isShowStatusGlance());
			saveOptionMenus();
		});

		btnClearSectionStaff.setOnAction(event -> {
			boolean clearSectionStaff = !roomViewModel.isClearSectionStaff();
			roomViewModel.setClearSectionStaff(clearSectionStaff);
			// btnClearSectionStaff.setDisable(clearSectionStaff ? true :
			// false);

			if (clearSectionStaff) {
				for (SectionViewModel mod : roomViewModel.getSectionViewModelList()) {
					roomViewModel.removeSectionViewModel(mod, false);
				}
				roomViewModel.getSectionViewModelList().clear();
				roomViewModel.getStatusGlances().clear();
			}
			saveOptionMenus();
		});

		btnClearTableSection.setOnAction(event -> {
			boolean clearTableSection = !roomViewModel.isClearTableSection();
			roomViewModel.setClearTableSection(clearTableSection);
			// btnClearTableSection.setDisable(clearTableSection ? true :
			// false);
			System.out.println("clearTableSection  " + clearTableSection);
			if (clearTableSection) {
				roomViewModel.clearTableSections();
			}
			saveOptionMenus();
		});

		btnLockMenu.setOnAction(event -> {
			roomViewModel.lockOption();
			saveOptionMenus();
		});

		pnInnerContent.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		zoomGroup.scaleXProperty().bindBidirectional(myScale);
		zoomGroup.scaleYProperty().bindBidirectional(myScale);
		sldScale.maxProperty().bind(maxScale);
		sldScale.minProperty().bind(minScale);
		sldScale.valueProperty().bindBidirectional(myScale);

		Rectangle clip = new Rectangle(0, 0, 0, 0);
		clip.widthProperty().bind(pnContent.widthProperty());
		clip.heightProperty().bind(pnContent.heightProperty());
		pnContent.setClip(clip);

		pnContent.setOnZoom(event -> {
			System.out.println("[ZoomEvent]: " + event);
			if (!lockScroll)
				scale(event.getZoomFactor(), event.getX(), event.getY());
			event.consume();
		});

		btnOption.setOnAction(event -> {
			if (roomViewModel.lockedProperty().get()) {
				this.showPasswordDialog();
			}
		});
                
                
		btnMoveTables.setOnAction(event -> {
			try {
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.initModality(Modality.APPLICATION_MODAL);
				alert.initOwner(pnRoot.getScene().getWindow());
				alert.setHeaderText(null);
				alert.getButtonTypes().setAll(puBtnSet, ButtonType.CANCEL);
				if (Config.getInstance().isMovingTables()) {
					// alert.setTitle("Moving Tables");
					// alert.setContentText("Save Moving tables to a new
					// template");
					// Optional<ButtonType> result = alert.showAndWait();
					// if (result.isPresent() && result.get() == puBtnSet) {
					// Config.getInstance().setMovingTables(false);
					// btnMoveTables.setText("Move Tables");
					// }
					final Stage dialog = new Stage();
					dialog.initModality(Modality.APPLICATION_MODAL);
					dialog.initOwner(pnRoot.getScene().getWindow());
					dialog.initStyle(StageStyle.UTILITY);
					dialog.setResizable(false);

					FXMLLoader loader = new FXMLLoader(SaveLayoutConfirm.class.getResource("SaveLayoutConfirm.fxml"));
					Parent pn = loader.load();
					SaveLayoutConfirm layoutSave = loader.getController();

					Scene dialogScene = new Scene(pn, 480, 400);
					dialog.setScene(dialogScene);
					dialog.setTitle("Save New Layout Confirmation");
					dialog.setOnCloseRequest(event1 -> {
						if (layoutSave.getDialogResult() == 1 && layoutSave.getNameEntered() != null) {
							List<Table> tbls = new ArrayList<>();
							for (TableViewModel tblVm : roomViewModel.getTableList()) {
								Table tbl = tblVm.getTable();
								tbl.setLeft(tbl.getLeft() + Config.getInstance().getLayoutLeftAdjusted());
								tbl.setTop(tbl.getTop() + Config.getInstance().getLayoutTopAdjusted());
								tbls.add(tbl);
							}
							LayoutHelper.putTables(layoutSave.getNameEntered() + ".json", new ArrayList<>(tbls));
							Config.getInstance().setMovingTables(false);
							btnMoveTables.setText("Move Tables");
						} else if (layoutSave.getDialogResult() == 2) {
							// TODO - If Reset button should enabled for this to
							// work
							roomViewModel.loadLayout(Config.getInstance().getLayout());
							Config.getInstance().setMovingTables(false);
							btnMoveTables.setText("Move Tables");
						}
					});
					dialog.show();
				} else {
					alert.setTitle("Moving Tables");
					alert.setContentText("Moving tables will stop all other clients process");
					Optional<ButtonType> result = alert.showAndWait();
					if (result.isPresent() && result.get() == puBtnSet) {
						Config.getInstance().setMovingTables(true);
						btnMoveTables.setText("Save Table Move");
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		Table tableObj = new Table();
		btnAssignSection.setOnAction(event -> {
			try {
				tableObj.setStaffCount(0);
				tableObj.setCallFromAssignee(true);
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(pnRoot.getScene().getWindow());
				dialog.initStyle(StageStyle.UTILITY);
				dialog.setResizable(false);

				FXMLLoader loader = new FXMLLoader(StaffView.class.getResource("SectionView.fxml"));
				Parent pn = loader.load();
				SectionView sectionView = loader.getController();
				sectionView.setSectionViewModel(roomViewModel);

				Scene dialogScene = new Scene(pn, 600, 465);
				dialogScene.getStylesheets().add(Resource.class.getResource("assignment.css").toExternalForm());
				dialogScene.getStylesheets().add(Resource.class.getResource("assignment-ext.css").toExternalForm());
				// File f = new File("assignment-ext.css");
				System.out.println("Initialized assignment-ext.css");
				/*
				 * if(f.exists() && !f.isDirectory()){ String absolutePath =
				 * f.getAbsolutePath().replace("\\", "/");
				 * System.out.println("assignment-ext.css exists!! - " + absolutePath);
				 * dialogScene.getStylesheets().add("file:///" + absolutePath); }
				 */

				System.out.println("dialogScene - " + dialogScene.getStylesheets());

				dialog.setScene(dialogScene);
				// dialog.getIcons().add(Main.getIcon());
				dialog.setTitle("Assigned Sections");
				dialog.setOnCloseRequest(event1 -> {
					roomViewModel.setPauseStaffRefresh(false);
				});
				roomViewModel.setPauseStaffRefresh(true);
				dialog.showAndWait();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnStaff.setOnAction(event -> {
			try {
				final Stage dialog = new Stage();
				dialog.initModality(Modality.APPLICATION_MODAL);
				dialog.initOwner(pnRoot.getScene().getWindow());
				dialog.initStyle(StageStyle.UTILITY);
				dialog.setResizable(false);

				FXMLLoader loader = new FXMLLoader(StaffView.class.getResource("StaffView.fxml"));
				Parent pn = loader.load();
				StaffView staffView = loader.getController();
				staffView.setStaff(roomViewModel.getStaffList());

				Scene dialogScene = new Scene(pn, 600, 435);
				dialog.setScene(dialogScene);
				// dialog.getIcons().add(Main.getIcon());
				dialog.setTitle("Manage Staff");
				dialog.setOnCloseRequest(event1 -> {
					roomViewModel.setPauseStaffRefresh(false);
					if (staffView.getDialogResult() == 0)
						roomViewModel.updateStaffList(staffView.getStaffs());
				});
				roomViewModel.setPauseStaffRefresh(true);
				dialog.setOnShowing(event1 -> {
					System.out.println("Showing Staff");
					WindowsHelper.showVirtualKeyboard(roomViewModel.getConfig().isUseTabtip());
				});
				dialog.showAndWait();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		/*
		 * try { ITableService tableService = TableService.getInstance(); RestRoomStatus
		 * restRoomStatus = tableService.getRestroomStatus();
		 * roomViewModel.setMenRestroomStatusRed(restRoomStatus.isMenStatusRed() );
		 * roomViewModel.setWomenRestroomStatusRed(restRoomStatus. isWomenStatusRed());
		 * refreshRestRoomStatus(); }catch(Exception e){
		 * System.err.println("Exception - " + e); }
		 */

		M.setOnAction(event -> {
			try {
				System.out.println("M button clicked");
				ITableService tableService = TableService.getInstance();
				System.out.println("M button clicked tableService - " + tableService);
				boolean isMenRestRoomStatusRed = roomViewModel.isMenRestroomStatusRed();
				System.out.println("M button clicked isMenRestRoomStatusRed - " + isMenRestRoomStatusRed);
				if (tableService.updateRestroomStatus(
						new RestRoomStatus(!isMenRestRoomStatusRed, roomViewModel.isWomenRestroomStatusRed()))) {
					System.out.println("Update for restroom status M is successful. isMenRestRoomStatusRed - "
							+ (isMenRestRoomStatusRed) + " and isWoMenRestRoomStatusRed - "
							+ roomViewModel.isWomenRestroomStatusRed());
					roomViewModel.setMenRestroomStatusRed(!isMenRestRoomStatusRed);
				}
				roomViewModel.refreshRestRoomStatus();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		W.setOnAction(event -> {
			try {
				ITableService tableService = TableService.getInstance();
				boolean isWomenRestRoomStatusRed = roomViewModel.isWomenRestroomStatusRed();
				if (tableService.updateRestroomStatus(
						new RestRoomStatus(roomViewModel.isMenRestroomStatusRed(), !isWomenRestRoomStatusRed))) {
					roomViewModel.setWomenRestroomStatusRed(!isWomenRestRoomStatusRed);
				}
				roomViewModel.refreshRestRoomStatus();
				// if (roomViewModel.isWomenRestroomStatusRed()) {
				// W.styleProperty().bind(roomViewModel.wStyleNAProperty());
				// // W.setStyle("-fx-background-color:
				// // -fx-outer-border,#ffa389; -fx-outer-border: black;
				// // -fx-background-insets: 0, 0.5;");
				// } else {
				// W.styleProperty().bind(roomViewModel.wStyleProperty());
				// // W.setStyle("-fx-background-color:
				// // -fx-outer-border,#c3f7a8; -fx-outer-border: black;
				// // -fx-background-insets: 0, 0.5;");
				// }
				//
				// if (roomViewModel.isMenRestroomStatusRed() ||
				// roomViewModel.isWomenRestroomStatusRed()) {
				// // Restroom.setStyle("-fx-background-color:
				// // -fx-outer-border,#ffa389; -fx-outer-border: black;
				// // -fx-background-insets: 0, 0.5;");
				// Restroom.styleProperty().bind(roomViewModel.restroomStyleNAProperty());
				// } else {
				// // Restroom.setStyle("-fx-background-color:
				// // -fx-outer-border,#c3f7a8; -fx-outer-border: black;
				// // -fx-background-insets: 0, 0.5;");
				// Restroom.styleProperty().bind(roomViewModel.restroomStyleProperty());
				// }
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		H.setOnAction(event -> {
			try {
				ITableService tableService = TableService.getInstance();
				boolean isManagerNeededAtHostStand = roomViewModel.isManagerNeededAtH();
				if (tableService.updateManagerStatus(
						new ManagerStatus(!isManagerNeededAtHostStand, roomViewModel.isManagerNeededAtK()))) {
					roomViewModel.setManagerNeededAtH(!isManagerNeededAtHostStand);
				}
				roomViewModel.refreshManagerStatus();
				// if (roomViewModel.isManagerNeededAtH()) {
				// // H.setStyle(
				// // "-fx-background-color: -fx-outer-border,#ffa389;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// H.styleProperty().bind(roomViewModel.hStyleNAProperty());
				// } else {
				// // H.setStyle(
				// // "-fx-background-color: -fx-outer-border,#c3f7a8;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// H.styleProperty().bind(roomViewModel.hStyleProperty());
				// }
				//
				// if (roomViewModel.isManagerNeededAtH() ||
				// roomViewModel.isManagerNeededAtK()) {
				// // Manager.setStyle(
				// // "-fx-background-color: -fx-outer-border,#ffa389;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// Manager.styleProperty().bind(roomViewModel.managerNAStyleProperty());
				// } else {
				// // Manager.setStyle(
				// // "-fx-background-color: -fx-outer-border,#c3f7a8;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// Manager.styleProperty().bind(roomViewModel.managerStyleProperty());
				// }
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		K.setOnAction(event -> {
			try {
				ITableService tableService = TableService.getInstance();
				boolean isManagerNeededInKitchen = roomViewModel.isManagerNeededAtK();
				if (tableService.updateManagerStatus(
						new ManagerStatus(roomViewModel.isManagerNeededAtH(), !isManagerNeededInKitchen))) {
					roomViewModel.setManagerNeededAtK(!isManagerNeededInKitchen);
				}
				roomViewModel.refreshManagerStatus();
				// if (roomViewModel.isManagerNeededAtK()) {
				// // K.setStyle(
				// // "-fx-background-color: -fx-outer-border,#ffa389;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// K.styleProperty().bind(roomViewModel.kNAStyleProperty());
				// } else {
				// // K.setStyle(
				// // "-fx-background-color: -fx-outer-border,#c3f7a8;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// K.styleProperty().bind(roomViewModel.kStyleProperty());
				// }
				//
				// if (roomViewModel.isManagerNeededAtH() ||
				// roomViewModel.isManagerNeededAtK()) {
				// // Manager.setStyle(
				// // "-fx-background-color: -fx-outer-border,#ffa389;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// Manager.styleProperty().bind(roomViewModel.managerNAStyleProperty());
				// } else {
				// // Manager.setStyle(
				// // "-fx-background-color: -fx-outer-border,#c3f7a8;
				// // -fx-outer-border: black; -fx-background-insets: 0,
				// // 0.5;");
				// Manager.styleProperty().bind(roomViewModel.managerStyleProperty());
				// }
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnFitToScreen.setOnAction(event -> {
			this.fitToScreen();
		});

		btnLock.setOnAction(event -> {
			lockScroll = !lockScroll;
			if (lockScroll) {
				btnLock.getStyleClass().setAll("actionBtn", "activated");
				System.out.println(btnLock.getStyleClass().get(0));
				pnScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
				pnScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
				sldScale.setDisable(true);
			} else {
				btnLock.getStyleClass().setAll("actionBtn");
				pnScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
				pnScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
				sldScale.setDisable(false);
			}
		});
		pnScroll.addEventFilter(ScrollEvent.SCROLL, event -> {
			if (lockScroll)
				event.consume();
		});
		pnScroll.addEventFilter(KeyEvent.ANY, event -> {
			if (lockScroll)
				event.consume();
		});

		btnAllGreen.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(pnRoot.getScene().getWindow());
			alert.setTitle("Confirm All Tables Green");
			alert.setHeaderText(null);
			alert.getButtonTypes().setAll(puBtnSet, ButtonType.CANCEL);
			alert.setContentText("Are you sure you want to set all table to green?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.isPresent() && result.get() == puBtnSet)
				roomViewModel.setAllGreen();
		});

		btnUpdate.setOnAction(event -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initModality(Modality.APPLICATION_MODAL);
			alert.initOwner(pnRoot.getScene().getWindow());
			alert.setTitle("Update Hardware Panel");
			alert.setHeaderText(null);
			alert.getButtonTypes().setAll(ButtonType.OK);
			alert.setContentText("Please wait a couple of seconds till the hardware panels are updated!");
			alert.showAndWait();
			System.out.println("Update RTS");
			roomViewModel.updateAllTables(true);
		});

		btnServer.setOnAction(event -> {

			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(pnRoot.getScene().getWindow());
			dialog.initStyle(StageStyle.UTILITY);
			dialog.setResizable(false);

			FXMLLoader loader = new FXMLLoader(Confirmation.class.getResource("Confirmation.fxml"));
			Parent pn;
			try {
				pn = loader.load();
				Confirmation confirm = loader.getController();

				if (Config.getInstance().isServer()) {
					confirm.getBtnSave().setVisible(false);
					confirm.getBtnReset().setVisible(false);
					confirm.setLabel(MessageConstants.CONFIRM_RESET_SERVER);
				} else if (Config.getInstance().isHideResets()) {
					confirm.getBtnSave().setVisible(false);
					confirm.getBtnReset().setVisible(false);
					confirm.setLabel(MessageConstants.CONFIRM_RESET_CLIENT_HIDE);
				} else {
					confirm.getBtnSave().setVisible(true);
					confirm.getBtnReset().setVisible(true);
					confirm.setLabel(MessageConstants.CONFIRM_RESET_CLIENT);
				}
                                
                                if (this.getRegStatus()) {
                                    confirm.getBtnNetwork().setText("Swipe Off");
                                }
                                else {
                                    confirm.getBtnNetwork().setText("Swipe On");
                                }

				Scene dialogScene = new Scene(pn, 460, 180);
				dialog.setScene(dialogScene);
				dialog.setTitle("Confirm");
				dialog.setOnCloseRequest(event1 -> {
                                    Thread th = new Thread() {
                                        @Override
                                        public void run() {
                                            resetServerClient(confirm);
                                        }
                                    };
                                    th.start();
				});
				dialog.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		pnContent.setOnMouseClicked(event ->
		{
                    System.out.println("[ClickEvent]: " + event);
		});

		btnSelect.setOnAction(event -> {
                    isSelected = !isSelected;
                    if (isSelected)
                        btnSelect.getStyleClass().setAll("actionBtn", "activated");
                    else
                        btnSelect.getStyleClass().setAll("actionBtn");
                    roomViewModel.switchSelect();
		});

		btnLayout.setOnAction(event -> {
                    try {
                            String[] layoutList = LayoutHelper.getLayoutFiles();
                            if (layoutList == null) {
                                    // Layout folder doesnt exist
                                    return;
                            }
                            final Stage dialog = new Stage();
                            dialog.initModality(Modality.APPLICATION_MODAL);
                            dialog.initOwner(pnRoot.getScene().getWindow());
                            dialog.initStyle(StageStyle.UTILITY);
                            dialog.setResizable(false);

                            FXMLLoader loader = new FXMLLoader(LayoutSelector.class.getResource("LayoutSelector.fxml"));
                            Parent pn = loader.load();
                            LayoutSelector layoutSelector = loader.getController();
                            layoutSelector.setList(layoutList, roomViewModel.layoutProperty().get());

                            Scene dialogScene = new Scene(pn, 480, 400);
                            dialog.setScene(dialogScene);
                            // dialog.getIcons().add(Main.getIcon());
                            dialog.setTitle("Select Layout");
                            dialog.setOnCloseRequest(event1 -> {
                                    System.out.println("Dialog is closed: " + layoutSelector.getDialogResult());
                                    if (layoutSelector.getDialogResult() == 0) {
                                            Platform.runLater(() -> {
                                                    views.clear();
                                                    pnInnerContent.getChildren().clear();
                                                    roomViewModel.loadLayout(layoutList[layoutSelector.getSelectedResult()]);
                                                    fitToScreen();
                                            });
                                    }
                            });
                            dialog.show();
                    } catch (Exception ex) {
                            ex.printStackTrace();
                    }
		});
		this.lnkHome.setOnAction(event -> {
			WindowsHelper.openUrl("http://www.tablechecktechnologies.com");
			// try {
			// Runtime.getRuntime().exec("http://www.tablecheck.com");
			// } catch (Exception ex) {
			// ex.printStackTrace();
			// }
		});
		this.imvLogo.setImage(Main.getIcon());
		pnScroll.setFitToHeight(true);
		pnScroll.setFitToWidth(true);
	}
        
        public boolean getRegStatus(){
            String value = this.readRegistry("HKLM\\SOFTWARE\\Policies\\Microsoft\\Windows\\EdgeUI", "AllowEdgeSwipe");
            System.out.println(value);
            if(value != null && value.startsWith("0x0")){
                System.out.println(" registry false ");
                return false;
            }
            else return true;
        
        }
        
        public static final String readRegistry(String location, String key){
            try {
                // Run reg query, then read output with StreamReader (internal class)
                Process process = Runtime.getRuntime().exec("reg query " + 
                        '"'+ location + "\" /v " + key);

                StreamReader reader = new StreamReader(process.getInputStream());
                reader.start();
                process.waitFor();
                reader.join();
                
                System.out.println("reg query " + '"'+ location + "\" /v " + key);
                System.out.println(process.exitValue());

                // Parse out the value
                String[] parsed = reader.getResult().split("\\s+");
                if (parsed.length > 1) {
                    return parsed[parsed.length-1];
                }
            } catch (Exception e) {}

            return null;
        }
        
         public static boolean addValue(String key, String valName, String val){
            try
            {
                // Run reg query, then read output with StreamReader (internal class)
                Process process = Runtime.getRuntime().exec(
                        "reg add \"" + key + "\" /v \"" + valName + "\" /d \"" + val + "\" /t REG_DWORD /f");

                StreamReader reader = new StreamReader(process.getInputStream());
                reader.start();
                process.waitFor();
                reader.join();
                String output = reader.getResult();
                
                System.out.println("reg add \"" + key + "\" /v \"" + valName + "\" /d \"" + val + "\" /t REG_DWORD /f");
                System.out.println(process.exitValue());

                // Output has the following format:
                // \n<Version information>\n\n<key>\t<registry type>\t<value>
                return output.contains("The operation completed successfully");
            }
            catch (Exception e)
            {
            }
            return false;
        }
         
         public static boolean deleteValue(String key, String valueName){
            try
            {
                // Run reg query, then read output with StreamReader (internal class)
                Process process = Runtime.getRuntime().exec("reg delete \"" + key + "\" /v \"" + valueName + "\" /f");

                StreamReader reader = new StreamReader(process.getInputStream());
                reader.start();
                process.waitFor();
                reader.join();
                String output = reader.getResult();
                
                System.out.println("reg delete \"" + key + "\" /v \"" + valueName + "\" /f");
                System.out.println(process.exitValue());

                // Output has the following format:
                // \n<Version information>\n\n<key>\t<registry type>\t<value>
                return output.contains("The operation completed successfully");
            }
            catch (Exception e)
            {
            }
            return false;
        }

        static class StreamReader extends Thread {
            private InputStream is;
            private StringWriter sw= new StringWriter();

            public StreamReader(InputStream is) {
                this.is = is;
            }

            public void run() {
                try {
                    int c;
                    while ((c = is.read()) != -1)
                        sw.write(c);
                } catch (IOException e) { 
                }
            }

            public String getResult() {
                return sw.toString();
            }
        }
        
        private void confirmRegistryProcess(){
            try{
                boolean bStatus = this.getRegStatus();
                boolean resultCmd = false;
                String regFile;
                if (bStatus) 
                {
                    bStatus = false;
//                            regFile = "Disable_Swipe.reg";
                    regFile = "Disable_Screen_Edge_Swipe.reg";
                    resultCmd = this.addValue("HKLM\\SOFTWARE\\Policies\\Microsoft\\Windows\\EdgeUI", "AllowEdgeSwipe", "0x0");
                }
                else {
                    bStatus = true;
//                            regFile = "Enable_Swipe.reg";
                    regFile = "Enable_Screen_Edge_Swipe.reg";
                    resultCmd = this.deleteValue("HKLM\\SOFTWARE\\Policies\\Microsoft\\Windows\\EdgeUI", "AllowEdgeSwipe");
                }

                System.out.println(resultCmd);


//                        ProcessBuilder builder = new ProcessBuilder("regedit", "/s", regFile);
                try {
//                            builder.start().waitFor();
//                            String[] cmd = {"reg", regFile};
//                            Process p = Runtime.getRuntime().exec(cmd);
//                            p.waitFor();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

//                        Config.getInstance().setSwipeStatus(bStatus);
//                        Config.getInstance().saveConfig(Config.getInstance().isServer(), "Reset Swipe");

                //Restart OS
                // this.pnRoot.getChildren().add(progressIndicator);
                ClientHelper.restartApplication(null, new Thread() {
                    @Override
                    public void run() {
                            return;
                    }
                });
            }
            catch(Exception e){
            }
        }

	private void resetServerClient(Confirmation confirm) {
		// ProgressIndicator progressIndicator = new ProgressIndicator();
		// progressIndicator.toFront();
                
                System.out.println("RoomView::resetServer() called");
                
                this.getRegStatus();
                
                if (confirm.getDialogResult() == 4) {
                    //on-off Swipe function
                    try {
                        //password matching
//                        Platform.setImplicitExit(false);
                        Platform.runLater(new Runnable() {
                            public void run() {
                                showPasswordDialog();
                            }
                        });
                        
                       
                        
//                        boolean bStatus = Config.getInstance().getSwipeStatus();
//                        boolean bStatus = this.getRegStatus();
//                        boolean resultCmd = false;
//                        String regFile;
//                        if (bStatus) 
//                        {
//                            bStatus = false;
////                            regFile = "Disable_Swipe.reg";
//                            regFile = "Disable_Screen_Edge_Swipe.reg";
//                            resultCmd = this.addValue("HKLM\\SOFTWARE\\Policies\\Microsoft\\Windows\\EdgeUI", "AllowEdgeSwipe", "0x0");
//                        }
//                        else {
//                            bStatus = true;
////                            regFile = "Enable_Swipe.reg";
//                            regFile = "Enable_Screen_Edge_Swipe.reg";
//                            resultCmd = this.deleteValue("HKLM\\SOFTWARE\\Policies\\Microsoft\\Windows\\EdgeUI", "AllowEdgeSwipe");
//                        }
//                        
//                        System.out.println(resultCmd);
//                        
//
////                        ProcessBuilder builder = new ProcessBuilder("regedit", "/s", regFile);
//                        try {
////                            builder.start().waitFor();
////                            String[] cmd = {"reg", regFile};
////                            Process p = Runtime.getRuntime().exec(cmd);
////                            p.waitFor();
//                        } catch (Exception e) {
//                            // TODO Auto-generated catch block
//                            e.printStackTrace();
//                        }
//                        
////                        Config.getInstance().setSwipeStatus(bStatus);
////                        Config.getInstance().saveConfig(Config.getInstance().isServer(), "Reset Swipe");
//                        
//                        //Restart OS
//                        // this.pnRoot.getChildren().add(progressIndicator);
//                        ClientHelper.restartApplication(null, new Thread() {
//                            @Override
//                            public void run() {
//                                    return;
//                            }
//                        });
//                    } catch (IOException e) {
                    } catch (Exception e) {
			e.printStackTrace();
                    }
                }
                else if ((confirm.getDialogResult() == 1 && Config.getInstance().isServer())
				|| (confirm.getDialogResult() == 3 && !Config.getInstance().isServer())) {
			try {
                                if (Config.getInstance().isServer())
                                {
                                    Config.getInstance().setServer(false);
                                    Config.getInstance().saveConfig(false, "RoomView_Server_To_Client");
                                }
                                //Restart OS
				// this.pnRoot.getChildren().add(progressIndicator);
				ClientHelper.restartApplication(null, new Thread() {
                                    @Override
                                    public void run() {
                                            return;
                                    }
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ((confirm.getDialogResult() == 1 || confirm.getDialogResult() == 2)
				&& !Config.getInstance().isServer()) {
                    
                        System.out.println("RoomView::resetServer() phase 1/4");

			roomViewModel.pause();
			if (!roomViewModel.disconnected.get()) {
                                System.out.println("RoomView::resetServer() phase 2/4");
				// this.pnRoot.getChildren().add(progressIndicator);
				try {
					List<NameValuePair> params = new ArrayList<>();
					params.add(new BasicNameValuePair("resetType",
							confirm.getDialogResult() == 1 ? MessageConstants.RESET_TYPE_TEMP
									: MessageConstants.RESET_TYPE_PERM));
					String resp = HttpHelper.sendPost(Config.getInstance().getServerShutUrl(), params);
					PingResponse pingResp = (PingResponse) HttpHelper.parseResponse(resp, PingResponse.class);
					if (pingResp != null) {
                                                System.out.println("RoomView::resetServer() phase 3/4");
						try {
							ObjectMapper mapper = new ObjectMapper();
							mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
							Utility.writeFileContent(file_restaurant_config,
									mapper.writeValueAsBytes(pingResp.getResources()));
							Utility.writeFileContent(file_table_state, mapper.writeValueAsBytes(pingResp.getTables()));
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
						try {
							if (pingResp.isShutInitiated()) {
                                                            System.out.println(">>>>>> RoomView::resetServer() phase 4/4");
                                                            roomViewModel.disconnected.set(true);

                                                            HttpHelper.changeServer(Config.getInstance().getServerPort(), 1000);

//								String[] arguments = new String[] { "123" };
//								ServiceApplication.main(arguments);
//								Config.getInstance().setServer(true);
//								Config.getInstance().setServerHost(Config.getInstance().getClientHost());
//								Config.getInstance().setServerChanging(false);
//								Config.getInstance().setServerConnected(true);
//								if (confirm.getDialogResult() == 2) {
//                                                                    Config.getInstance().saveConfig(true);
//								}
                                                            Thread.sleep(800);
                                                            //roomViewModel.disconnected.set(false);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
                                    e.printStackTrace();
				}
				// this.pnRoot.getChildren().remove(progressIndicator);
			}else{
                            System.out.println(">>>>>> RoomView::resetServer() completed");
                            try {
                                roomViewModel.disconnected.set(true);
                                HttpHelper.changeServer(Config.getInstance().getServerPort(), 1000);
//                            String[] arguments = new String[] { "123" };
//                            ServiceApplication.main(arguments);
//                            Config.getInstance().setServer(true);
//                            Config.getInstance().setServerHost(Config.getInstance().getClientHost());
//                            Config.getInstance().setServerChanging(false);
//                            Config.getInstance().setServerConnected(true);
//                            if (confirm.getDialogResult() == 2) {
//                                Config.getInstance().saveConfig(true);
//                            }
                                Thread.sleep(800);
                                //roomViewModel.disconnected.set(false);
                            } catch (InterruptedException ex) {
                                
                            }
                        }
			roomViewModel.resume();
		}
	}

	private void saveOptionMenus() {
		roomViewModel.saveMenuOptions();
	}

	@Deprecated
	private void saveOptionMenue1() {
		// TODO Auto-generated method stub
		try {
			FileInputStream input = new FileInputStream("tablecheck.cfg");
			Vector v = new Vector();
			v.add(input);
			Enumeration e = v.elements();
			SequenceInputStream sis = new SequenceInputStream(e);
			int i = 0;
			while ((i = sis.read()) != -1) {
				configurationStr = configurationStr + (char) i;
				System.out.print((char) i);
			}
			sis.close();
			input.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// RoomViewModel roomViewModel = new RoomViewModel();
		// saveOptionString = ">>>"+btnShowNetStatus.getText()+
		// "::"+btnShowTime.getText()+
		// "::"+btnShowState.getText()+
		// "::"+btnShowRestroomStatus.getText()+
		// "::"+btnShowManagerStatus.getText()+
		// "::"+btnShowStatusGlance.getText()+
		// "::"+roomViewModel.lockedProperty().get();

		saveOptionString = ">>>" + roomViewModel.isShowNetStatus() + "::" + roomViewModel.isShowTime() + "::"
				+ roomViewModel.isShowState() + "::" + roomViewModel.isShowRestRoomStatus() + "::"
				+ roomViewModel.isShowManagerStatus() + "::" + roomViewModel.isShowStatusGlance() + "::"
				+ roomViewModel.lockedProperty().get();

		String preveousSaved = configurationStr.substring(configurationStr.indexOf(">>>"));
		System.out.println("====preveousSaved====" + preveousSaved);
		configurationStr = configurationStr.replace(preveousSaved, saveOptionString);

		System.out.println(">>" + configurationStr);

		try {
			FileOutputStream fos = new FileOutputStream("tablecheck.cfg");
			byte b[] = configurationStr.getBytes();
			fos.write(b);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Deprecated
	private void refreshRestRoomStatus() {
		System.out.println("refreshRestRoomStatus - M - " + M + ", roomViewModel.isMenRestroomStatusRed() - "
				+ roomViewModel.isMenRestroomStatusRed() + " and roomViewModel.isWomenRestroomStatusRed() - "
				+ roomViewModel.isWomenRestroomStatusRed());
		if (roomViewModel.isMenRestroomStatusRed()) {
			// System.out.println("Setting M style to red");
			// M.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			M.styleProperty().bind(roomViewModel.mStyleNAProperty());
			System.out.println("Set M style to red");
		} else {
			// System.out.println("Setting M style to green");
			// M.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			M.styleProperty().bind(roomViewModel.mStyleProperty());
			System.out.println("Set M style to green");
		}

		if (roomViewModel.isWomenRestroomStatusRed()) {
			// System.out.println("Setting W style to red");
			// W.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			W.styleProperty().bind(roomViewModel.wStyleNAProperty());
			System.out.println("Set W style to red");
		} else {
			// System.out.println("Setting W style to green");
			// W.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			W.styleProperty().bind(roomViewModel.wStyleProperty());
			System.out.println("Set W style to green");
		}

		if (roomViewModel.isMenRestroomStatusRed() || roomViewModel.isWomenRestroomStatusRed()) {
			// Restroom.setStyle("-fx-background-color:
			// -fx-outer-border,#ffa389; -fx-outer-border: black;
			// -fx-background-insets: 0, 0.5;");
			Restroom.styleProperty().bind(roomViewModel.restroomStyleNAProperty());
		} else {
			// Restroom.setStyle("-fx-background-color:
			// -fx-outer-border,#c3f7a8; -fx-outer-border: black;
			// -fx-background-insets: 0, 0.5;");
			Restroom.styleProperty().bind(roomViewModel.restroomStyleProperty());
		}
		// System.out.println("Completed!!");
	}

	@Deprecated
	private void refreshManagerStatus() {
		System.out.println("refreshManagerStatus - H - " + H + ", roomViewModel.isManagerNeededAtH() - "
				+ roomViewModel.isManagerNeededAtH() + " and roomViewModel.isManagerNeededAtK() - "
				+ roomViewModel.isManagerNeededAtK());
		if (roomViewModel.isManagerNeededAtH()) {
			H.setStyle(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			H.setStyle(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}

		if (roomViewModel.isManagerNeededAtK()) {
			K.setStyle(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			K.setStyle(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}

		if (roomViewModel.isManagerNeededAtH() || roomViewModel.isManagerNeededAtK()) {
			Manager.setStyle(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			Manager.setStyle(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
	}

	final ButtonType puButton_Update = new ButtonType("Update", ButtonBar.ButtonData.YES);
	final ButtonType puBtnSet = new ButtonType("Set", ButtonBar.ButtonData.YES);

	List<TableView> views = new ArrayList<TableView>();

	private void showPasswordDialog() {
		try {
			final Stage dialog = new Stage();
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(pnRoot.getScene().getWindow());
			dialog.initStyle(StageStyle.UTILITY);
			dialog.setResizable(false);

			FXMLLoader loader = new FXMLLoader(RoomView.class.getResource("PasswordView.fxml"));
			Pane pn = loader.load();
			PasswordView pwView = loader.getController();
//			pwView.setShowKeyboard(roomViewModel.getConfig().isShowKeyboard());
                        pwView.setShowKeyboard(true);
			pwView.setUseTabtip(roomViewModel.getConfig().isUseTabtip());
//                        System.out.println(roomViewModel.lockedProperty());
                        roomViewModel.locked.set(true);
//                        System.out.println(roomViewModel.lockedProperty());

			Scene dialogScene = new Scene(pn, 382, 135);
			dialog.setScene(dialogScene);
			// dialog.getIcons().add(Main.getIcon());
			dialog.setTitle("Enter password");
			dialog.setOnCloseRequest(event1 -> {
				if (pwView.getDialogResult() == 0) {
					String password = pwView.getEnteredPassword();
                                        Boolean resultPassword = roomViewModel.unLockPW(password);
					if (!resultPassword) {
                                            Alert alert = new Alert(Alert.AlertType.ERROR);
                                            alert.initModality(Modality.APPLICATION_MODAL);
                                            alert.initOwner(dialog);
                                            alert.setTitle("Incorrect password");
                                            alert.setHeaderText(null);
                                            alert.setContentText("The password is incorrect!");
                                            alert.showAndWait();
					}
                                        else if(resultPassword){
                                            System.out.println("password is correct");
                                            this.confirmRegistryProcess();
                                        }
				}
			});
			dialog.showAndWait();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void initializeRestRoomButtons() {
		try {
			System.out.println("Begin initializeRestRoomButtons");
			System.out.println("Doing initializeRestRoomButtons");
			/*
			 * Thread restRoomThread = new Thread(new Runnable() {
			 * 
			 * @Override public void run() { while(true) { try { refreshRestRoomStatus();
			 * Thread.sleep(1000l); }catch(Exception e){ System.err.println(e); } } } });
			 * restRoomThread.start();
			 */
			// ITableService tableService = TableService.getInstance();
			// RestRoomStatus restRoomStatus = tableService.getRestroomStatus();
			// roomViewModel.setMenRestroomStatusRed(restRoomStatus.isMenStatusRed());
			// roomViewModel.setWomenRestroomStatusRed(restRoomStatus.isWomenStatusRed());
			// refreshRestRoomStatus();
		} catch (Exception e) {
			System.err.println("Exception - " + e);
		}
	}

	@Deprecated
	private void initializeManagerButtons() {
		try {
			/*
			 * Thread managerThread = new Thread(new Runnable() {
			 * 
			 * @Override public void run() { while(true) { try { refreshManagerStatus();
			 * Thread.sleep(1000l); }catch(Exception e){ System.err.println(e); } } } });
			 * managerThread.start();
			 */
			ITableService tableService = TableService.getInstance();
			ManagerStatus managerStatus = tableService.getManagerStatus();
			roomViewModel.setManagerNeededAtH(managerStatus.isManagerNeededAtHostStand());
			roomViewModel.setManagerNeededAtK(managerStatus.isManagerNeededInKitchen());
			System.out.println("Doing initializeManagerButtons");
			// refreshManagerStatus();
		} catch (Exception e) {
			System.err.println("Exception - " + e);
		}
	}

	public void setViewModel(RoomViewModel viewModel) {
		this.roomViewModel = viewModel;
		this.roomViewModel.setM(M);
		this.roomViewModel.setW(W);
		this.roomViewModel.setRestroom(Restroom);
		this.roomViewModel.setH(H);
		this.roomViewModel.setK(K);
		this.roomViewModel.setManager(Manager);
		M.styleProperty().bind(roomViewModel.mStyleProperty());
		W.styleProperty().bind(roomViewModel.wStyleProperty());
		Restroom.styleProperty().bind(roomViewModel.restroomStyleProperty());
		H.styleProperty().bind(roomViewModel.hStyleProperty());
		K.styleProperty().bind(roomViewModel.kStyleProperty());
		Manager.styleProperty().bind(roomViewModel.managerStyleProperty());
		// initializeRestRoomButtons();
		// initializeManagerButtons();
		this.zoomFactor = viewModel.getConfig().getZoomFactor();
		lblClient.setText(Config.getInstance().getClientHost());
		System.out.println("Zoom Factor: " + zoomFactor);
		this.roomViewModel.setTableListChangeListener(c -> {
			while (c.next()) {
				if (c.wasAdded()) {
					try {
						for (TableViewModel tbvm : c.getAddedSubList()) {
							FXMLLoader loader = new FXMLLoader(TableView.class.getResource("TableView.fxml"));
							Node pane = loader.load();
							TableView vm = loader.getController();
							vm.setTableViewModel(tbvm);
							vm.setCtxMenu(tblContextMenu);
							views.add(vm);
							pnInnerContent.getChildren().add(pane);
							addDetailNode(tbvm);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (c.wasUpdated()) {
					//System.out.println("update .....................");
				} else if (c.wasReplaced()) {
					//System.out.println("replaced .....................");
				} else if (c.wasRemoved()) {
					for (TableViewModel tbvm : c.getRemoved()) {

					}
				}
			}
			times.forEach(t -> t.toFront());
		});
		this.roomViewModel.disconnected.addListener((observable, oldValue, newValue) -> {
			//System.out.println("Disconnected :: " + oldValue + " >> << " + newValue);
			// if (Config.getInstance().isServer()) {
			// btnServer.setVisible(false);
			// } else {
			// btnServer.setVisible(true);
			// }
			pnStatus.setVisible(true);
			if (newValue) {
				pnStatus.getStyleClass().setAll("pnl-status", "disconnected");
				// serverBox.getStyleClass().setAll("pnl-status",
				// "disconnected");
				// activeBox.getStyleClass().setAll("pnl-status",
				// "disconnected");
				lblStatus.setText("Disconnected");
				lblServer.setText("Disconnected");
				// lblServer1.setText("Disconnected");
				lblActive.setText("Saving Data");
				btnServer.setDisable(false);
				btnServer.setVisible(true);
				serverBox.setVisible(false);
				lblServer.setVisible(false);
				// lblServer1.setVisible(false);
				activeBox.setVisible(false);
				// serverIndi.setVisible(false);
				led.setBlinking(false);
				led.setLedColor(Color.ORANGERED);
			} else {
				pnStatus.getStyleClass().setAll("pnl-status", "connected");
				// serverBox.getStyleClass().setAll("pnl-status", "connected");
				// activeBox.getStyleClass().setAll("pnl-status", "connected");
				lblStatus.setText(Config.getInstance().isServer() ? "Server" : "Connected");
				lblServer.setText(Config.getInstance().getServerHost());
				// lblServer1.setText(Config.getInstance().getServerHost());
				lblActive.setText("Run");
				// btnServer.setDisable(true);
				// btnServer.setVisible(false);
				serverBox.setVisible(true);
				// lblServer.setVisible(true);
				// lblServer1.setVisible(true);
				activeBox.setVisible(true);
				if (led == null) {
					led = new Led(Color.LIMEGREEN, false, 500_000_000l, true);
					led.setMinSize(25, 25);
					serverIndi.add(led, 15, 15);
				}
				led.setLedColor(Color.LIMEGREEN);
				led.setBlinking(true);
				serverIndi.setVisible(true);
			}
		});
		this.lblLayout.textProperty().bind(viewModel.layoutProperty());
		this.lblServerVer.setText(Config.getInstance().getServerVersion());
		this.roomViewModel.lockedProperty().addListener((observable, oldValue, newValue) -> {
			// this.pnControl.setVisible(!newValue); //TODO Uncomment on new
			// features
			newValue = true;
			if (newValue) {
				// locked
				mnBtnOption.setVisible(false);
				// btnOption.toFront();
			} else {
				// btnOption.toBack();
				mnBtnOption.setVisible(true);
			}
			redrawAsMenuOptions();
		});
		this.setSection(this.roomViewModel.sectionProperty().get());
		this.roomViewModel.sectionProperty().addListener((observable, oldValue, newValue) -> {
			setSection((int) newValue);
		});
		this.roomViewModel.showStatusGlance.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showNetStatus.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showServerHost.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showClientHost.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showManagerStatus.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showRestRoomStatus.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showTime.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.showState.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.clearSectionStaff.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.clearTableSection.addListener((observable, oldValue, newValue) -> {
			redrawAsMenuOptions();
		});
		this.roomViewModel.batteryStatus.addListener((observable, oldValue, newValue) -> {
			batteryInd.visibleProperty().set(true);

			lblBatteryStts.setText(newValue + "% Battery");
			if (newValue.intValue() == 106) {
				batteryInd.setStyle("batteryGood");
				batteryInd.getStyleClass().setAll("batteryGood");
				lblBatteryStts.setText("Charging");
			} else if (newValue.intValue() == 110) {
				batteryInd.visibleProperty().set(false);
				lblBatteryStts.setText("");
			} else if (newValue.intValue() <= 100 && newValue.intValue() > 20) {
				batteryInd.setStyle("batteryGood");
				batteryInd.getStyleClass().setAll("batteryGood");
//				lblBatteryStts.setText("Battery Good");
			} else if (newValue.intValue() <= 20 && newValue.intValue() > 10) {
				batteryInd.setStyle("batteryWarn");
				batteryInd.getStyleClass().setAll("batteryWarn");
//				lblBatteryStts.setText("Warning");
			} else if (newValue.intValue() <= 10 && newValue.intValue() > 0) {
				batteryInd.setStyle("chargeOff");
				batteryInd.getStyleClass().setAll("chargeOff");
//				lblBatteryStts.setText("Charge Now");
			} else {// if(newValue.intValue() == 0) {
				batteryInd.visibleProperty().set(false);// setStyle("chargeOff");
//				batteryInd.getStyleClass().setAll("chargeOff");
//				lblBatteryStts.setText("");
			}
		});
	}

	private void setSection(int section) {
		if (section > this.section) {
			for (int i = this.section + 1; i <= section; i++) {
				final int index = i;
				MenuItem mItem = new MenuItem("Section " + i);
				mItem.getStyleClass().add("section-" + index);
				mItem.setOnAction(event -> {
					TableViewModel vm = (TableViewModel) tblContextMenu.getUserData();
					if (vm != null) {
						vm.sectionProperty().set(index);
						roomViewModel.updateTable(vm);
					}
				});
				menuItems.add(mItem);
			}
		} else if (section < this.section) {
			menuItems.remove(section + 1, this.section + 1);
		}
		this.section = section;
	}

	public void fitToScreen() {
		int left = Integer.MAX_VALUE;
		int top = Integer.MAX_VALUE;
		int right = Integer.MIN_VALUE;
		int bottom = Integer.MIN_VALUE;
		for (TableView tv : views) {
			Bounds b = tv.getBoundsInParent();
			TableViewModel tvm = tv.getViewModel();
			if (tvm.leftProperty().get() < left)
				left = (int) tvm.leftProperty().get();
			if (tvm.topProperty().get() < top)
				top = (int) tvm.topProperty().get();
			if (tvm.leftProperty().get() + b.getWidth() > right)
				right = (int) (tvm.leftProperty().get() + b.getWidth());
			if (tvm.topProperty().get() + b.getHeight() > bottom)
				bottom = (int) (tvm.topProperty().get() + b.getHeight());
		}
		System.out.format("Top: %d\tLeft: %d\tRight: %d\tBottom: %d\n", top, left, right, bottom);

		pnInnerContent.setPrefSize(right + 2 * PADDING, bottom + 2 * PADDING);

		System.out.format("pnContent: Width: %f\tHeight: %f\n", pnContent.getWidth(), pnContent.getHeight());
		System.out.format("scrollPane: Width: %f\tHeight: %f\n", pnScroll.getWidth(), pnScroll.getHeight());
		System.out.format("scrollPane: Viewport Width: %f\tHeight: %f\n", pnScroll.getViewportBounds().getWidth(),
				pnScroll.getViewportBounds().getHeight());

		double width = pnScroll.getWidth() - 8;
		double height = pnScroll.getHeight() - 8;

		System.out.format("contentGroup: Width: %f\tHeight: %f\n", contentGroup.getBoundsInParent().getWidth(),
				contentGroup.getBoundsInParent().getHeight());
		System.out.format("zoomGroup: Width: %f\tHeight: %f\n", zoomGroup.getBoundsInParent().getWidth(),
				zoomGroup.getBoundsInParent().getHeight());
		System.out.format("pnInnerContent: Width: %f\tHeight: %f\n", pnInnerContent.getWidth(),
				pnInnerContent.getHeight());

		double pWidth = pnInnerContent.getPrefWidth();
		double pHeight = pnInnerContent.getPrefHeight();
		System.out.format("pnInnerContent pref: Width: %f\tHeight: %f\n", pWidth, pHeight);

		double scale = Math.min(width / pWidth, height / pHeight);
		if (scale < 0.1)
			scale = 0.1;
		System.out.format("Scale: %f\tScaleX: %f\tScaleY: %f\n", scale, zoomGroup.getScaleX(), zoomGroup.getScaleY());

		minScale.set(scale);
		maxScale.set(scale * 5);
		myScale.set(scale);
	}

	public void addGrid() {
		double w = pnInnerContent.getBoundsInLocal().getWidth();
		double h = pnInnerContent.getBoundsInLocal().getHeight();
		System.out.format("W: %f\tH: %f\n", w, h);
		Canvas grid = new Canvas(w, h);
		grid.setMouseTransparent(true);

		GraphicsContext gc = grid.getGraphicsContext2D();

		gc.setStroke(Color.GRAY);
		gc.setLineWidth(1);

		// draw grid lines
		double offset = 50;
		for (double i = offset; i < w; i += offset) {
			// vertical
			gc.strokeLine(i, 0, i, h);
			// horizontal
			gc.strokeLine(0, i, w, i);
		}
		gc.setFill(Color.RED);
		gc.fillOval(w / 2 - 12.5, h / 2 - 12.5, 25, 25);
		pnInnerContent.getChildren().add(grid);
		grid.toBack();

		Shape ck = new Rectangle(w / 2 - 25, h / 2 - 25, 50, 50);
		ck.setFill(Color.BLUE);
		ck.setMouseTransparent(true);
		pnInnerContent.getChildren().add(ck);
		ck.toBack();
	}

	Canvas timeCanvas;

	boolean drawedTime = false;
	List<Text> times = new ArrayList<Text>();

	private void addDetailNode(TableViewModel tvm) {
		if (tvm.getTable().getType() != Table.Type.TABLE)
			return;
		double t_w = tvm.radiusProperty().get() * 2;
		double t_h = tvm.heightProperty().get();
		if (!tvm.circleVisibleProperty().get()) {
			// t_h = tvm.heightProperty().get();
		} else {
			// t_w += 6;
			t_h = t_h + (t_w - t_h) / 2;
		}
		tvm.updateElapsed();
		Text t = new Text();
		t.visibleProperty().bind(tvm.showDetailProperty());
		t.textProperty().bind(tvm.detailProperty());
		t.setLayoutX(tvm.leftProperty().get());
		t.setLayoutY(tvm.topProperty().get() + t_h + 12);
		t.setFont(Font.font(null, FontWeight.NORMAL, 14));
		times.add(t);
		t.setBoundsType(TextBoundsType.VISUAL);
		t.setWrappingWidth(t_w);
		t.setTextAlignment(TextAlignment.CENTER);
		pnInnerContent.getChildren().add(t);
		// t.setStyle("-fx-border-width:1px; -fx-border-color:red;");
		// gc.fillText(tvm.detailProperty().get(), tvm.leftProperty().get() +
		// t_w / 2, tvm.topProperty().get() + t_h + 5);
		//
		// VBox vB = new VBox();
		// vB.setLayoutX(tvm.leftProperty().get());
		// vB.setLayoutY(tvm.topProperty().get() + tvm.heightProperty().get() +
		// 8);
		// vB.setPrefWidth(t_w);
		// vB.setAlignment(Pos.CENTER);
		// vB.getChildren().add(t);
		// pnInnerContent.getChildren().add(vB);
	}

	private void scale(double factor, double x, double y) {
		if (lockScroll)
			return;
		double sclFactor = 1 - (1 - factor) * zoomFactor; // 1 - (1-factor)/2;
		// 1 - 0.5 + factor / 2
		System.out.format("Scale factor: %f\tCalc factor: %f\n", factor, sclFactor);
		double scale = myScale.get() * sclFactor;
		// double delta = 1.01;
		// if (kCode == KeyCode.A)
		// scale *= delta;
		// else if (kCode == KeyCode.D)
		// scale /= delta;
		// else return;

		if (scale < minScale.get())
			scale = minScale.get();
		else if (scale > maxScale.get())
			scale = maxScale.get();

		System.out.format("[Scale] old: %f\tnew: %f\n", myScale.get(), scale);
		double hValue = pnScroll.getHvalue();
		double vValue = pnScroll.getVvalue();

		double W = zoomGroup.getBoundsInParent().getWidth();
		double H = zoomGroup.getBoundsInParent().getHeight();
		double w = pnScroll.getViewportBounds().getWidth();
		double h = pnScroll.getViewportBounds().getHeight();
		// double vX = pnScroll.getViewportBounds().getWidth() / 2;
		// double vY = pnScroll.getViewportBounds().getHeight() / 2;
		double vX = x;
		double vY = y;
		double Z = scale;
		double hF = ((hValue * (W - w) * Z) + vX * (Z - 1)) / (W * Z - w);
		double vF = ((vValue * (H - h) * Z) + vY * (Z - 1)) / (H * Z - h);
		System.out.format("[hvValue] hValue: %f\tvValue: %f\n", hF, vF);
		// zoomGroup.setScaleX(Z);
		// zoomGroup.setScaleY(Z);
		myScale.set(Z);
		// double lX = (hValue * (W - w));
		// double lY = (vValue * (H - h));
		// transNode.setTranslateX(transNode.getTranslateX() - (lX + vX) )* (Z -
		// 1));
		// transNode.setTranslateY(transNode.getTranslateY() - (lY + vY) * (Z -
		// 1));
		pnScroll.setHvalue(hF);
		pnScroll.setVvalue(vF);
	}

	public Parent getPane() {
		return pnRoot;
	}

	public RoomViewModel getRoomViewModel() {
		return roomViewModel;
	}

	public void redrawAsMenuOptions() {
		System.out.println(" " + roomViewModel.isShowNetStatus() + " " + roomViewModel.isShowRestRoomStatus());

		btnShowNetStatus.setText(roomViewModel.isShowNetStatus() ? "Hide Net Status" : "Show Net Status");

		btnShowServerHost.setText(roomViewModel.isShowServerHost() ? "Hide Server Host" : "Show Server Host");

		btnShowClientHost.setText(roomViewModel.isShowClientHost() ? "Hide Client Host" : "Show Client Host");

		btnShowTime.setText(roomViewModel.isShowTime() ? "Hide Time" : "Show Time");

		btnShowState.setText(roomViewModel.isShowState() ? "Hide State" : "Show State");

		btnShowRestroomStatus
				.setText(roomViewModel.isShowRestRoomStatus() ? "Hide Restroom Status" : "Show Restroom Status");
		btnShowManagerStatus
				.setText(roomViewModel.isShowManagerStatus() ? "Hide Manager Status" : "Show Manager Status");
		btnShowStatusGlance.setText(roomViewModel.isShowStatusGlance() ? "Hide Wait Staff" : "Show Wait Staff");

		lblServer.setText(Config.getInstance().getServerHost());
		// lblServer1.setText(Config.getInstance().getServerHost());

		pnStatus.setVisible(roomViewModel.isShowNetStatus());
		// lblServer.setVisible(roomViewModel.isShowServerHost());
		// lblServer1.setVisible(roomViewModel.isShowServerHost());
		serverBox.setVisible(roomViewModel.isShowServerHost());

		// TODO - uncomment on new features
		// lblClient.setVisible(roomViewModel.isShowClientHost());
		// clientBox.setVisible(roomViewModel.isShowClientHost());

		M.setVisible(roomViewModel.isShowRestRoomStatus());
		Restroom.setVisible(roomViewModel.isShowRestRoomStatus());
		W.setVisible(roomViewModel.isShowRestRoomStatus());

		H.setVisible(roomViewModel.isShowManagerStatus());
		Manager.setVisible(roomViewModel.isShowManagerStatus());
		K.setVisible(roomViewModel.isShowManagerStatus());
		// TODO - uncomment on new features
		pnStatusGlance.setVisible(false);// roomViewModel.isShowStatusGlance());
		pnStatusGlanceScroll.setVisible(false);// roomViewModel.isShowStatusGlance());

		btnClearSectionStaff.setDisable(
				roomViewModel.getStatusGlances() != null && !roomViewModel.getStatusGlances().isEmpty() ? false : true);
		btnClearTableSection.setDisable(roomViewModel.getTablesCount() > 0 ? false : true);
		// roomViewModel.setPauseRestRoomRefresh(!roomViewModel.isShowRestRoomStatus());
		// roomViewModel.setPauseManagerRefresh(!roomViewModel.isShowManagerStatus());
	}
}
