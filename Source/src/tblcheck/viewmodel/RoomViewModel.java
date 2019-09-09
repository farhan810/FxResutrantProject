package tblcheck.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.util.NumberUtils;

import com.tablecheck.service.Constants;
import com.tablecheck.service.controller.Utility;
import com.tablecheck.service.model.PingResponse;
import com.tablecheck.service.model.TableRequest;
import com.tablecheck.service.model.TableResponse;
import com.tablecheck.service.model.TableResponseList;
import com.tablecheck.windows.Kernel32;
import java.net.InetAddress;
import java.net.Socket;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import tblcheck.controller.INotifiable;
import tblcheck.controller.TableController;
import tblcheck.helper.CheckException;
import tblcheck.helper.DataHelper;
import tblcheck.helper.HttpHelper;
import tblcheck.helper.LayoutHelper;
import tblcheck.helper.MulticastPublisher;
import tblcheck.helper.MulticastReceiver;
import tblcheck.model.Config;
import tblcheck.model.MessageRequest;
import tblcheck.model.SectionAssignment;
import tblcheck.model.Table;
import tblcheck.model.ResponseMessage.BaseResponseMessage;
import tblcheck.view.RoomView;
import tblcheck.viewmodel.TableViewModel.NotifyType;

/**
 * Created by cuongdm5 on 1/10/2016.
 */
public class RoomViewModel implements INotifiable {

        private int refCnt = 0;
	private RoomView roomVw;
	// private static final Gson gson = new Gson();
	public BooleanProperty disconnected;
	// private StringProperty connStatus;
	private int failed = 0;
	private ObservableList<TableViewModel> tableList; // all tables
	private ListChangeListener<TableViewModel> onTableListChange;
	private List<TableViewModel> tableObjects; // tables only
	private Map<String, TableViewModel> tableMap;
	private TableController controller;
	private Timer refreshTimer;
	private Config config;
	Map<String, Boolean> mapMenuOptions;

	private ObservableList<SectionViewModel> sectionViewModelList;
	private HashMap<Integer, String> statusGlances;
	private ObservableList<String> staffList;
	public final int MAX_SECTION = 25;
	private IntegerProperty section;
	private StringProperty layout;
	private boolean pauseStaffRefresh;
	private boolean pauseRestRoomRefresh = true;
	private boolean pauseManagerRefresh = true;
	public BooleanProperty showTime;
	public BooleanProperty showState;
	public BooleanProperty showStatusGlance;
	public BooleanProperty showRestRoomStatus;
	public BooleanProperty showManagerStatus;
	private boolean isMenRestroomStatusRed = false;
	private boolean isWomenRestroomStatusRed = false;
	private boolean isManagerNeededAtH = false;
	private boolean isManagerNeededAtK = false;
	public BooleanProperty showNetStatus;
	public BooleanProperty showServerHost;
	public BooleanProperty showClientHost;
	public BooleanProperty clearSectionStaff;
	public BooleanProperty clearTableSection;
	public IntegerProperty batteryStatus;

	public boolean isSectionChanged;
	Kernel32.SYSTEM_POWER_STATUS batteryPowerStatus = null;
	// Status indicator properties
	/*
	 * private StringProperty mStyle;
	 * 
	 * public ReadOnlyStringProperty mStyleProperty(){ return this.mStyle; }
	 */

	public BooleanProperty locked;
	public EventHandler updateElapsed;

	public ReadOnlyBooleanProperty lockedProperty() {
		return locked;
	}

	public boolean unLockPW(String pw) {
		if (config.getPassword().equals(pw)) {
			locked.set(false);
			new Thread() {
				public void run() {
					controller.lockMenus(false);
				}
			}.start();
		}
		return !locked.get();
	}

	public void lockOption() {
		if (controller.lockMenus(true))
			locked.set(true);

	}

	public boolean isShowNetStatus() {
		return showNetStatus.get();
	}

	public void setShowNetStatus(Boolean showNetStatus) {
		this.showNetStatus.set(showNetStatus);
	}

	public boolean isShowServerHost() {
		return showServerHost.get();
	}

	public void setShowServerHost(Boolean showServerHost) {
		this.showServerHost.set(showServerHost);
	}

	public boolean isShowClientHost() {
		return showClientHost.get();
	}

	public void setShowClientHost(Boolean showClientHost) {
		this.showClientHost.set(showClientHost);
	}

	public void setPauseStaffRefresh(boolean value) {
		this.pauseStaffRefresh = value;
	}

	public void setPauseRestRoomRefresh(boolean value) {
		this.pauseRestRoomRefresh = value;
	}

	public void setPauseManagerRefresh(boolean value) {
		this.pauseManagerRefresh = value;
	}

	public boolean isShowTime() {
		return this.showTime.get();
	}

	public void setShowTime(Boolean value) {
		this.showTime.set(value);
		for (TableViewModel tvm : tableObjects)
			tvm.setShowElapsed(value);
		if (value) {
			this.updateElapsed();
		}
	}

	public boolean isShowStatusGlance() {
		return this.showStatusGlance.get();
	}

	public void setShowStatusGlance(boolean value) {
		this.showStatusGlance.set(value);
	}

	public boolean isShowRestRoomStatus() {
		return this.showRestRoomStatus.get();
	}

	public void setShowRestRoomStatus(Boolean value) {
		this.showRestRoomStatus.set(value);
	}

	public boolean isShowManagerStatus() {
		return this.showManagerStatus.get();
	}

	public void setShowManagerStatus(Boolean value) {
		this.showManagerStatus.set(value);
	}

	public boolean isMenRestroomStatusRed() {
		return isMenRestroomStatusRed;
	}

	public boolean isWomenRestroomStatusRed() {
		return isWomenRestroomStatusRed;
	}

	public boolean isManagerNeededAtH() {
		return isManagerNeededAtH;
	}

	public boolean isManagerNeededAtK() {
		return isManagerNeededAtK;
	}

	public void setMenRestroomStatusRed(boolean menRestroomStatusRed) {
		isMenRestroomStatusRed = menRestroomStatusRed;
	}

	public void setWomenRestroomStatusRed(boolean womenRestroomStatusRed) {
		isWomenRestroomStatusRed = womenRestroomStatusRed;
	}

	public void setManagerNeededAtH(boolean managerNeededAtH) {
		isManagerNeededAtH = managerNeededAtH;
	}

	public void setManagerNeededAtK(boolean managerNeededAtK) {
		isManagerNeededAtK = managerNeededAtK;
	}

	public boolean isShowState() {
		return this.showState.get();
	}

	public void setShowState(Boolean value) {
		this.showState.set(value);
		for (TableViewModel tvm : tableObjects)
			tvm.setShowState(value);
	}

	private void updateElapsed() {
		for (TableViewModel tvm : tableObjects) {
			tvm.updateElapsed();
		}
		if (this.updateElapsed != null)
			this.updateElapsed.handle(new Event(this, null, null));
	}

	public IntegerProperty sectionProperty() {
		return section;
	}

	public StringProperty layoutProperty() {
		return layout;
	}

	public ObservableList<String> getStaffList() {
		return this.staffList;
	}

	public void setStaffList(List<String> newList) {
		if (newList.equals(this.staffList)) {
			return;
		}
		this.staffList.setAll(newList);
		String newValue = newList.size() == 0 ? null : newList.get(0);
		// If assignment is not locked and staff is removed
		for (SectionViewModel svm : sectionViewModelList) {
			if (svm.lockProperty().get())
				continue;
			if (newList.contains(svm.staffNameProperty().get()))
				continue;
			svm.staffNameProperty().set(newValue);
		}
		System.out.println("Staff list");
		for (String staff : this.staffList)
			System.out.println("Staff: " + staff);
	}

	public void updateStaffList(List<String> newList) {
		if (newList.equals(this.staffList))
			return;
		setStaffList(newList);
		try {
			controller.updateStaff(newList);
			Platform.runLater(() -> {
				disconnected.set(false);
			});
		} catch (CheckException e) {
			Platform.runLater(() -> {
				disconnected.set(true);
			});
		}
	}

	public void setStaffSection(int sectionCount, Map<Integer, String> data) {
		System.out.println("Staff Section:" + data);

		if (isSectionChanged || this.sectionProperty().get() != sectionCount) {
			this.config.setSections(sectionCount);
			this.section = new SimpleIntegerProperty(sectionCount);
			this.sectionProperty().set(sectionCount);
			// setPauseStaffRefresh(false);
			isSectionChanged = false;
		}

		this.sectionViewModelList = FXCollections.observableArrayList();
		this.statusGlances = new HashMap<Integer, String>();
		if (data != null) {
			for (Integer s : data.keySet()) {
				try {
					if (!this.statusGlances.containsKey(s)) {
						this.statusGlances.put(s, data.get(s));
					}
					SectionAssignment ass = new SectionAssignment(s, data.get(s));
					SectionViewModel svm = new SectionViewModel(ass, FXCollections.observableArrayList(),
							FXCollections.observableArrayList());
					svm.lock(true);
					this.sectionViewModelList.add(svm);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	@Deprecated
	public void setStaffSection(String data) {
//		System.out.println("Staff Section:");
		System.out.println(data);
		String[] parts = data.split("\n");
		this.sectionViewModelList = FXCollections.observableArrayList();
		this.statusGlances = new HashMap<Integer, String>();
		for (String s : parts) {
			try {
				String[] newParts = s.split(":");
				String[] sections = newParts[1].split(",");
				for (String section : sections) {
					if (!this.statusGlances.containsKey(Integer.parseInt(section))) {
						this.statusGlances.put(Integer.parseInt(section), newParts[0]);
					}
					SectionAssignment ass = new SectionAssignment(Integer.parseInt(section), newParts[0]);
					SectionViewModel svm = new SectionViewModel(ass, FXCollections.observableArrayList(),
							FXCollections.observableArrayList());
					svm.lock(true);
					this.sectionViewModelList.add(svm);
				}
			} catch (Exception ex) {
			}
		}
	}

	public ListChangeListener<TableViewModel> getTableListChangeListener() {
		return this.onTableListChange;
	}

	public void setTableListChangeListener(ListChangeListener<TableViewModel> listener) {
		this.onTableListChange = listener;
	}

	public ObservableList<SectionViewModel> getSectionViewModelList() {
		return this.sectionViewModelList;
	}

	public HashMap<Integer, String> getStatusGlances() {
		return this.statusGlances;
	}

	public RoomViewModel() {
		this.disconnected = new SimpleBooleanProperty(true);
		// this.connStatus = new SimpleStringProperty("Connected");
		this.tableObjects = new ArrayList<>();
		this.tableMap = new HashMap<>();
		this.tableList = FXCollections.observableList(new ArrayList<>());
		this.tableList.addListener((ListChangeListener<TableViewModel>) c -> {
			if (onTableListChange != null)
				onTableListChange.onChanged(c);
		});
		this.sectionViewModelList = FXCollections.observableArrayList();
		this.staffList = FXCollections.observableList(new ArrayList<>());
		this.section = new SimpleIntegerProperty(1);
		this.layout = new SimpleStringProperty();
		this.locked = new SimpleBooleanProperty(true);
		this.controller = new TableController(this);

		this.showTime = new SimpleBooleanProperty(false);
		this.showState = new SimpleBooleanProperty(false);
		this.showStatusGlance = new SimpleBooleanProperty(false);
		this.showRestRoomStatus = new SimpleBooleanProperty(false);
		this.showManagerStatus = new SimpleBooleanProperty(false);
		this.showNetStatus = new SimpleBooleanProperty(false);
		this.showServerHost = new SimpleBooleanProperty(false);
		this.showClientHost = new SimpleBooleanProperty(false);
		this.clearSectionStaff = new SimpleBooleanProperty(false);
		this.clearTableSection = new SimpleBooleanProperty(false);

		this.batteryStatus = new SimpleIntegerProperty(-1);
//		new Thread(() -> {
//			try {
//				if ("64".equals(System.getProperty("sun.arch.data.model"))) {
//					batteryPowerStatus = new Kernel32.SYSTEM_POWER_STATUS();
//					Kernel32.INSTANCE.GetSystemPowerStatus(batteryPowerStatus);
//				} else if ("32".equals(System.getProperty("sun.arch.data.model"))) {
//					batteryPowerStatus = new Kernel32.SYSTEM_POWER_STATUS();
//					Kernel32.INSTANCE.GetSystemPowerStatus(batteryPowerStatus);
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}).start();
	}

	// public ReadOnlyStringProperty connStatusProperty() {
	// return connStatus;
	// }

	public BooleanProperty getDisconnected() {
		return disconnected;
	}

	public void setDisconnected(BooleanProperty disconnected) {
		this.disconnected = disconnected;
	}

	public synchronized void start() {
		this.pause();
                controller.scanServer();
		this.resume();
		this.resetInd();
		// controller.start();
		// controller.refreshPushNotification();

	}

	public synchronized void resetInd() {
		this.refreshTimer = new Timer();
		this.refreshTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					try {
                                            if ("64".equals(System.getProperty("sun.arch.data.model"))) {
                                                    batteryPowerStatus = new Kernel32.SYSTEM_POWER_STATUS();
                                                    Kernel32.INSTANCE.GetSystemPowerStatus(batteryPowerStatus);
                                            } else if ("32".equals(System.getProperty("sun.arch.data.model"))) {
                                                    batteryPowerStatus = new Kernel32.SYSTEM_POWER_STATUS();
                                                    Kernel32.INSTANCE.GetSystemPowerStatus(batteryPowerStatus);
                                            }
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (batteryPowerStatus != null) {
						int percent = Integer.parseInt(batteryPowerStatus.getBatteryLifePercentage());
						if ("No Battery".equalsIgnoreCase(batteryPowerStatus.getBatteryFlagString())) {
							batteryStatus.set(110);
						} else if ("Online".equalsIgnoreCase(batteryPowerStatus.getACLineStatusString())) {
							batteryStatus.set(106);
						} else {
							batteryStatus.set(percent);
						}
//						if ("Online".equalsIgnoreCase(batteryPowerStatus.getACLineStatusString())) {
//							batteryStatus.set(106);
//						} else if (percent > 66) {
//							batteryStatus.set(1);
//						} else if (percent > 33) {
//							batteryStatus.set(2);
//						} else if (percent < 33) {
//							batteryStatus.set(3);
//						} else if ("High".equalsIgnoreCase(batteryPowerStatus.getBatteryFlagString())) {
//							batteryStatus.set(1);
//						} else if ("Low".equalsIgnoreCase(batteryPowerStatus.getBatteryFlagString())) {
//							batteryStatus.set(2);
//						} else if ("Critical".equalsIgnoreCase(batteryPowerStatus.getBatteryFlagString())) {
//							batteryStatus.set(3);
//						} else if ("Charging".equalsIgnoreCase(batteryPowerStatus.getBatteryFlagString())) {
//							batteryStatus.set(4);
//						} else if ("No Battery".equalsIgnoreCase(batteryPowerStatus.getBatteryFlagString())) {
//							batteryStatus.set(5);
//						} else if ("Online".equalsIgnoreCase(batteryPowerStatus.getACLineStatusString())) {
//							batteryStatus.set(6);
//						} else if ("Offline".equalsIgnoreCase(batteryPowerStatus.getACLineStatusString())) {
//							batteryStatus.set(7);
//						} 
					} else {
						batteryStatus.set(0);
					}
				} catch (Exception e) {
                                    try{
					Platform.runLater(() -> {

					});
                                    }catch(Exception e2){
                                        
                                    }
				}
			}
		}, 10, 1000 * 10);
	}

	public synchronized void resume() {
		this.refreshTimer = new Timer();
		this.refreshTimer.schedule(new TimerTask() {
			@Override
			public void run() {
                            Config.getInstance().setLThreadTime(System.currentTimeMillis());
                            if (Config.getInstance().getRThreadStatus()==2) {
                                System.out.println(">>>>>>> Restart Receiver Thread >>>>>>>");
                                refCnt = 0;
                                MulticastReceiver receiver = new MulticastReceiver();
                                new Thread(receiver).start();  
                            }
                            if (Config.getInstance().isServer() && Config.getInstance().getBThreadStatus()==2) {
                                System.out.println(">>>>>>> Restart Broadcast Thread >>>>>>>");
                                MulticastPublisher publisher = new MulticastPublisher();
                                new Thread(publisher).start();
                            }
                            if (Config.getInstance().isServerConnected() && Config.getInstance().getServerHost()!=null) {
                                System.out.println(">>>> PingResponse");
                                try {
                                        PingResponse pingResp = controller.refreshPingStatus();
                                        if (pingResp != null && !pingResp.isShutInitiated()) {
                                            refCnt=0;
                                                Platform.runLater(() -> {
                                                        Config.getInstance().setServerConnected(true);
                                                        disconnected.set(false);
                                                        if (pingResp.getTables() != null && pingResp.getTables().size() > 0) {
                                                            refreshTables(pingResp.getTables());
                                                        } else if (Config.getInstance().isServer()) {
                                                            byte[] bytes = Utility.readFileContent(Constants.file_table_state);
                                                            if (bytes == null || bytes.length <= 0) {
                                                                setAllGreen();
                                                            }
                                                        }
                                                        if (pingResp.getResources() != null) {
                                                            setStaffList(pingResp.getResources().getStaffs());
                                                            refreshManagerStatus(pingResp.getResources().getManagerState());
                                                            refreshRestroomStatus(pingResp.getResources().getRestroomState());
                                                            setStaffSection(pingResp.getResources().getSections(),
                                                                            pingResp.getResources().getSectionStaff());
                                                            // if (refCnt % 10 == 0) {
                                                            refreshMenuOptions(pingResp.getResources().getMenuOptions());
                                                            // }
                                                        }
                                                        Config.getInstance().setHostsToScan(pingResp.getHostsToScan());
                                                        //For buffering handling when disconnected.
                                                        controller.postEventRequest();
                                                });
                                        } else {
                                          
                                        	refCnt++;
                                            Config.getInstance().setServerConnected(false);
                                            Platform.runLater(() -> {
                                                    disconnected.set(true);
                                            });
                                        }
                                } catch (Exception e) {
                                    try{
                                        refCnt++;
                                        if (refCnt>1) {
                                            System.out.println(">>>>>>> PingResponse Error - occured 2 times over");
                                            System.out.println("farhan line here ");
                                            Config.getInstance().setServerConnected(false);
                                            System.out.println(" farooooo before Thread");
                                            Config.getInstance().setRThreadStatus(1);
                                            if (Config.getInstance().isServer())
                                                Config.getInstance().setBThreadStatus(1);
                                        }           
                                        Platform.runLater(() -> {
                                        	System.out.println("Before the discounet ");
                                            disconnected.set(true);
                                            Config.getInstance().setServerConnected(false);
                                        });
                                    }catch(Exception e2){
                                        updateAllTables(true);
                                        System.out.println(">>>>>>> PingResponse exception");
                                        Config.getInstance().setServerConnected(false);
                                        disconnected.set(true);
                                    }
                                }
                            }
                            else {
                                Config.getInstance().setServerConnected(false);
                                Platform.runLater(() -> {
                                    disconnected.set(true);
                                });
                            }
			}
		}, 10, 1000 * 2);
	}

	public synchronized void pause() {
            if (this.refreshTimer != null)
                    this.refreshTimer.cancel();
            this.refreshTimer = null;
	}

	public synchronized void exit() {
		this.pause();
		this.controller.exit();
	}

	public void addTables(List<Table> tables) {
		try {
			for (Table tb : tables) {
				if (!Table.Type.LED.equals(tb.getType())) {
					TableViewModel tvm = new TableViewModel(tb);
					tvm.setOnUpdate(event -> {
						if (event.getType() == NotifyType.Update) {
							controller.updateTable(event.getSrc().getTable());
						} else if (event.getType() == NotifyType.Remap) {
							// controller.updateTable(event.getSrc().getTable());
						}
					});
					tableList.add(tvm);
					if (tb.getType() == Table.Type.TABLE) {
						tableObjects.add(tvm);
						tableMap.put(tb.getTParameter(), tvm);
					}
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void updateTable(TableViewModel model) {
		controller.updateTable(model.getTable());
	}

	@Override
	public void onMessageResponse(Object resp, MessageRequest.RequestCommand cmd) {

		// switch (cmd) {
		// case READ_RESOURCES:
		// Platform.runLater(() -> {
		// refreshTables(((PingResponse) resp).getTables());
		// });
		// break;
		// case READ_TABLE:
		// Platform.runLater(() -> {
		// refreshTables((List<TableResponse>) resp);
		// });
		// break;
		// }

	}

	@Override
	public void onMessageResponse(BaseResponseMessage msgResponse, MessageRequest.RequestCommand cmd) {
		// MessageRequest.RequestCommand reqType =
		// msgResponse.getRequestCommand();
		if (msgResponse.getStatus().equalsIgnoreCase("error")) {
			return;
		}
		String data = ((BaseResponseMessage<String>) msgResponse).getData();
		switch (cmd) {
		case READ_TABLE:
			Platform.runLater(() -> {
				refreshData(data);
			});
			break;
		case READ_STAFF:
			if (!pauseStaffRefresh) {
				// pauseStaffRefresh = true;
				Platform.runLater(() -> {
					refreshStaff(data);
				});
			}
			break;
		case READ_STAFFSECTION:
			Platform.runLater(() -> {
				refreshStaffSection(data);
			});
			break;
		case READ_RESTROOM:
			if (!pauseRestRoomRefresh) {
				Platform.runLater(() -> {
					refreshRestroomStatus(data);
				});
			}
			break;
		case READ_MANAGER:
			if (!pauseManagerRefresh) {
				Platform.runLater(() -> {
					// refreshManagerStatus(data);
				});
			}
			break;
		}
	}

	public void refreshRestroomStatus(Map<String, Boolean> data) {
		if (this.refreshTimer == null || data == null || data.size() <= 0) {
			return;
		}

		// System.out.print("refreshRestroomStatus >> " + data);

		this.isMenRestroomStatusRed = data.get("M");
		this.isWomenRestroomStatusRed = data.get("W");
		// System.out.println("refreshRestroomStatus isMenRestroomStatusRed - "
		// + isMenRestroomStatusRed
		// + ", isWomenRestroomStatusRed - " + isWomenRestroomStatusRed);
		refreshRestRoomStatus();
	}

	public void refreshMenuOptions(Map<String, Boolean> data) {
		if (this.refreshTimer == null || data == null || data.size() <= 0) {
			return;
		}
		try {
			DataHelper<Boolean> dataHelp = new DataHelper<>();
			mapMenuOptions = data;
			setShowNetStatus(dataHelp.getValueOrDefault(data.get(Constants.menuShowNetStatus), false));
			setShowServerHost(dataHelp.getValueOrDefault(data.get(Constants.menuShowServerHost), false));
			setShowClientHost(dataHelp.getValueOrDefault(data.get(Constants.menuShowclientHost), false));
			setShowTime(dataHelp.getValueOrDefault(data.get(Constants.menuShowTime), false));
			setShowState(dataHelp.getValueOrDefault(data.get(Constants.menuShowState), false));
			setShowRestRoomStatus(dataHelp.getValueOrDefault(data.get(Constants.menuShowRestroomStatus), false));
			setShowManagerStatus(dataHelp.getValueOrDefault(data.get(Constants.menuShowManagerStatus), false));
			setShowStatusGlance(dataHelp.getValueOrDefault(data.get(Constants.menuShowStatusGlance), false));
			setClearSectionStaff(dataHelp.getValueOrDefault(data.get(Constants.menuClearSectionStaff), false));
			setClearTableSection(dataHelp.getValueOrDefault(data.get(Constants.menuClearTableSection), false));
			locked.set(dataHelp.getValueOrDefault(data.get(Constants.menuLocked), false));
			// roomVw.refreshMenuOptions();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Deprecated
	// public void refreshMenuOptions() {
	// roomVw.pnStatus.setVisible(isShowNetStatus());
	// roomVw.btnShowNetStatus.setText(isShowNetStatus() ? "Hide Net Status" :
	// "Show Net Status");
	//
	// roomVw.btnShowTime.setText(isShowTime() ? "Hide Time" : "Show Time");
	//
	// roomVw.btnShowState.setText(isShowState() ? "Hide State" : "Show State");
	//
	// roomVw.btnShowRestroomStatus.setText(isShowRestRoomStatus() ? "Hide
	// Restroom Status" : "Show Restroom Status");
	//
	// this.setPauseRestRoomRefresh(!isShowRestRoomStatus());
	//
	// roomVw.M.setVisible(isShowRestRoomStatus());
	// roomVw.Restroom.setVisible(isShowRestRoomStatus());
	// roomVw.W.setVisible(isShowRestRoomStatus());
	//
	// roomVw.btnShowManagerStatus.setText(isShowManagerStatus() ? "Hide Manager
	// Status" : "Show Manager Status");
	//
	// this.setPauseManagerRefresh(!isShowManagerStatus());
	//
	// roomVw.H.setVisible(isShowManagerStatus());
	// roomVw.Manager.setVisible(isShowManagerStatus());
	// roomVw.K.setVisible(isShowManagerStatus());
	//
	// roomVw.btnShowStatusGlance.setText(isShowStatusGlance().get() ? "Hide
	// Wait Staff" : "Show Wait Staff");
	//
	// roomVw.pnStatusGlance.setVisible(isShowStatusGlance().get());
	//
	// }

	// @Deprecated
	// public void refreshManagerStatus(String data) {
	// if (this.refreshTimer == null || data == null || data.trim().length() <=
	// 0) {
	// return;
	// }
	//
	// String[] status = data.split("~");
	// if (status != null && status.length == 2) {
	// this.isManagerNeededAtH = Boolean.parseBoolean(status[0]);
	// this.isManagerNeededAtK = Boolean.parseBoolean(status[1]);
	// }
	// System.out.println("refreshManagerStatus isManagerNeededAtH - " +
	// isManagerNeededAtH + ", isManagerNeededAtK - "
	// + isManagerNeededAtK);
	// refreshManagerStatus();
	// }

	public void refreshManagerStatus(Map<String, Boolean> data) {
		if (this.refreshTimer == null || data == null || data.size() <= 0) {
			return;
		}

		this.isManagerNeededAtH = data.get("H");
		this.isManagerNeededAtK = data.get("K");
		// System.out.println("refreshManagerStatus isManagerNeededAtH - " +
		// isManagerNeededAtH + ", isManagerNeededAtK - "
		// + isManagerNeededAtK);
		refreshManagerStatus();
	}

	public void refreshTables(List<TableResponse> tbls) {
		// Currently paused refresh. Do not update
		if (this.refreshTimer == null || tbls == null)
			return;

		// System.out.println("<<>>>" + data);

		// String[] tbls = data.split("~");
		String tParam, color;
		int section;
		try {
			for (TableResponse table : tbls) {
				// for (String table : tbls) {
				// String[] arr = table.trim().split("[=\\| ]");
				tParam = table.getTableId();// arr[0];
				color = table.getColor();// arr[1];
				section = table.getSection();// Integer.parseInt(arr[2]);

				// if (section == 0)
				// section = -1;

				TableViewModel tbvm = tableMap.get(tParam);// (tparam, color,
															// section,
															// forceAll);
				if (tbvm != null) {
					tbvm.sectionProperty().set(section);
					tbvm.setFillColor(color);
				}
			}
		} catch (Exception e) {
			System.out.println("## parse error ##");
			System.out.println(
					"Update Error.\nA parse error occurred (The output from the server is not formatted correctly.)");
			e.printStackTrace();
		}
	}

	public void refreshData(String data) {
		// Currently paused refresh. Do not update
		if (this.refreshTimer == null)
			return;

		System.out.println("<<>>>" + data);

		String[] tbls = data.split("~");
		String tParam, color;
		int section;
		try {
			for (String table : tbls) {
				String[] arr = table.trim().split("[=\\| ]");
				tParam = arr[0];
				color = arr[1];
				section = Integer.parseInt(arr[2]);

				// if (section == 0)
				// section = -1;

				TableViewModel tbvm = tableMap.get(tParam);// (tparam, color,
															// section,
															// forceAll);
				if (tbvm != null) {
					tbvm.sectionProperty().set(section);
					tbvm.setFillColor(color);
				}
			}
		} catch (Exception e) {
			System.out.println("## parse error ##");
			System.out.println(
					"Update Error.\nA parse error occurred (The output from the server is not formatted correctly.)");
			e.printStackTrace();
		}
	}

	@Deprecated
	public void refreshStaff(String data) {
		System.out.println(data);
		String[] staffs = data.split("\n");
		System.out.println(staffs);
		setStaffList(Arrays.asList(staffs));
	}

	@Deprecated
	public void refreshStaffSection(String data) {
		System.out.println("Staff Section data:");
		System.out.println(data);
		setStaffSection(data);
	}

	public void refreshStaffNumber(String data) {
		System.out.println(data);
		this.config.setSections(Integer.parseInt(data));
		this.section = new SimpleIntegerProperty(Integer.parseInt(data));
		setStaffSection("");
		this.sectionProperty().set(Integer.parseInt(data));
		System.out.println(this.config.getSections());
	}

	private Button m;
	private Button w;
	private Button restroom;
	private StringProperty mStyle;
	private StringProperty wStyle;
	private StringProperty restroomStyle;
	private StringProperty mStyleNA;
	private StringProperty wStyleNA;
	private StringProperty restroomNAStyle;

	public StringProperty mStyleProperty() {
		if (this.mStyle == null) {
			this.mStyle = new SimpleStringProperty(m, "mStyle",
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.mStyle;
	}

	public StringProperty wStyleProperty() {
		if (this.wStyle == null) {
			this.wStyle = new SimpleStringProperty(w, "wStyle",
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.wStyle;
	}

	public StringProperty restroomStyleProperty() {
		if (this.restroomStyle == null) {
			this.restroomStyle = new SimpleStringProperty(restroom, "restroomStyle",
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.restroomStyle;
	}

	public StringProperty mStyleNAProperty() {
		if (this.mStyleNA == null) {
			this.mStyleNA = new SimpleStringProperty(m, "mStyleNA",
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.mStyleNA;
	}

	public StringProperty wStyleNAProperty() {
		if (this.wStyleNA == null) {
			this.wStyleNA = new SimpleStringProperty(w, "wStyleNA",
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.wStyleNA;
	}

	public StringProperty restroomStyleNAProperty() {
		if (this.restroomNAStyle == null) {
			this.restroomNAStyle = new SimpleStringProperty(restroom, "restroomStyleNA",
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.restroomNAStyle;
	}

	public void setM(Button m) {
		this.m = m;
	}

	public void setW(Button w) {
		this.w = w;
	}

	public void setRestroom(Button restroom) {
		this.restroom = restroom;
	}

	public void refreshRestRoomStatus() {
		// System.out.println(
		// "refreshRestRoomStatus - M, roomViewModel.isMenRestroomStatusRed() -
		// " + isMenRestroomStatusRed()
		// + " and roomViewModel.isWomenRestroomStatusRed() - " +
		// isWomenRestroomStatusRed());
		if (isMenRestroomStatusRed()) {
			// System.out.println("Setting M style to red");
			// M.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// mStyle = new SimpleStringProperty(m, "mStyle",
			// "-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			mStyle.setValue(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// System.out.println("Set M style to " + mStyle.getValue());
		} else {
			// System.out.println("Setting M style to green");
			// M.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// mStyle = new SimpleStringProperty(m, "mStyle",
			// "-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			mStyle.setValue(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// System.out.println("Set M style to " + mStyle.getValue());
		}

		if (isWomenRestroomStatusRed()) {
			// System.out.println("Setting W style to red");
			// w.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// wStyle = new SimpleStringProperty(w, "wStyle",
			// "-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			wStyle.setValue(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// System.out.println("Set W style to " + wStyle.getValue());
		} else {
			// System.out.println("Setting W style to green");
			// w.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// wStyle = new SimpleStringProperty(w, "wStyle",
			// "-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			wStyle.set(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// System.out.println("Set W style to " + wStyle.getValue());
		}

		if (isMenRestroomStatusRed() || isWomenRestroomStatusRed()) {
			// Restroom.setStyle("-fx-background-color:
			// -fx-outer-border,#ffa389; -fx-outer-border: black;
			// -fx-background-insets: 0, 0.5;");
			// restroomStyle = new SimpleStringProperty(restroom,
			// "restroomStyle", "-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			restroomStyle.setValue(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			// Restroom.setStyle("-fx-background-color:
			// -fx-outer-border,#c3f7a8; -fx-outer-border: black;
			// -fx-background-insets: 0, 0.5;");
			// restroomStyle = new SimpleStringProperty(restroom,
			// "restroomStyle", "-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			restroomStyle.setValue(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		// System.out.println("Completed!!");
	}

	private Button h;
	private Button k;
	private Button manager;
	private StringProperty hStyle;
	private StringProperty kStyle;
	private StringProperty managerStyle;

	public StringProperty hStyleProperty() {
		if (this.hStyle == null) {
			hStyle = new SimpleStringProperty(h, "hStyle",
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.hStyle;
	}

	public StringProperty kStyleProperty() {
		if (this.kStyle == null) {
			this.kStyle = new SimpleStringProperty(k, "kStyle",
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.kStyle;
	}

	public StringProperty managerStyleProperty() {
		if (this.managerStyle == null) {
			this.managerStyle = new SimpleStringProperty(manager, "managerStyle",
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.managerStyle;
	}

	private StringProperty hNAStyle;
	private StringProperty kNAStyle;
	private StringProperty managerNAStyle;

	public StringProperty hStyleNAProperty() {
		if (this.hNAStyle == null) {
			hNAStyle = new SimpleStringProperty(h, "hNAStyle",
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.hNAStyle;
	}

	public StringProperty kNAStyleProperty() {
		if (this.kNAStyle == null) {
			this.kNAStyle = new SimpleStringProperty(k, "kNAStyle",
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.kNAStyle;
	}

	public StringProperty managerNAStyleProperty() {
		if (this.managerNAStyle == null) {
			this.managerNAStyle = new SimpleStringProperty(manager, "managerNAStyle",
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
		return this.managerNAStyle;
	}

	public void setH(Button h) {
		this.h = h;
	}

	public void setK(Button k) {
		this.k = k;
	}

	public void setManager(Button manager) {
		this.manager = manager;
	}

	public void refreshManagerStatus() {
		// System.out.println("refreshManagerStatus - H - ,
		// roomViewModel.isManagerNeededAtH() - " + isManagerNeededAtH()
		// + " and roomViewModel.isManagerNeededAtK() - " +
		// isManagerNeededAtK());
		if (isManagerNeededAtH()) {
			// H.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// hStyle = new SimpleStringProperty(h, "hStyle",
			// "-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			hStyle.setValue(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			// H.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// hStyle = new SimpleStringProperty(h, "hStyle",
			// "-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			hStyle.setValue(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}

		if (isManagerNeededAtK()) {
			// K.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// kStyle = new SimpleStringProperty(k, "kStyle",
			// "-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			kStyle.setValue(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			// K.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// kStyle = new SimpleStringProperty(k, "kStyle",
			// "-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			kStyle.setValue(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}

		if (isManagerNeededAtH() || isManagerNeededAtK()) {
			// Manager.setStyle("-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// managerStyle = new SimpleStringProperty(manager, "managerStyle",
			// "-fx-background-color: -fx-outer-border,#ffa389;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			managerStyle.setValue(
					"-fx-background-color: -fx-outer-border,#ffa389; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		} else {
			// Manager.setStyle("-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			// managerStyle = new SimpleStringProperty(manager, "managerStyle",
			// "-fx-background-color: -fx-outer-border,#c3f7a8;
			// -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
			managerStyle.setValue(
					"-fx-background-color: -fx-outer-border,#c3f7a8; -fx-outer-border: black; -fx-background-insets: 0, 0.5;");
		}
	}

	@Override
	public synchronized void notifyFailRequest() {
		Platform.runLater(() -> {
			disconnected.set(true);
		});
	}

	@Deprecated
	public synchronized void notifyFailRequest1() {
		System.out.println("reached again");
		failed++;
		if (failed <= 3) {
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("refresh ping status called");
					try {
						controller.refreshPingStatus();
						Platform.runLater(() -> {
							disconnected.set(false);
						});
					} catch (CheckException e) {
						e.printStackTrace();
                                                try{
                                                    Platform.runLater(() -> {
                                                            disconnected.set(true);
                                                    });
                                                }catch(Exception e2){
                                                    
                                                }
					}
				}
			}, 3000);
		} else {
			Platform.runLater(() -> {
				disconnected.set(true);
			});
		}
	}

	@Override
	public void notifySuccessRequest() {
		// System.out.println("back up again");
		// if (failed != 0) {
		// controller.refreshPushNotification();
		// }
		// failed = 0;
		Platform.runLater(() -> {
			disconnected.set(false);
		});
	}

	public String encodeAllTables() {
		StringBuilder sb = new StringBuilder();
		for (TableViewModel tb : tableObjects) {
			sb.append(tb.getTable().encode(true));
			sb.append("~");
		}
		if (sb.length() > 0)
			sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	public TableResponseList encodeTables() {
		TableResponseList tables = new TableResponseList();
		for (TableViewModel tb : tableObjects) {
			TableResponse table = new TableResponse();
			table.setColor(tb.getTable().getColor().toLowerCase());
			table.setSection(tb.getTable().getSection());
			table.setTableId(tb.getTable().getTParameter());
			tables.add(table);
		}
		return tables;
	}

	public int getTablesCount() {
		return tableObjects.size();
	}

	public Config getConfig() {
		return this.config;
	}

	public void setConfig(Config config) {
		this.config = config;
		this.controller.setPushNotificationUrl(config.getPushNotificationUrl());
		this.controller.setReadRestroomUrl(config.getReadRestroomUrl());
		this.controller.setReadStaffNumberChangeUrl(config.getReadStaffNumberChangeUrl());
		this.controller.setPingUrl(config.getPingUrl());
		this.controller.setReadManagerUrl(config.getReadManagerUrl());
		this.controller.setReadUrl(config.getReadUrl());
		this.controller.setWriteUrl(config.getWriteUrl());
		this.controller.setStaffListUrl(config.getStaffListUrl());
		this.controller.setStaffLogUrl(config.getStaffLogUrl());
		this.controller.setStaffSectionUrl(config.getStaffSectionUrl());
		this.controller.setMenuOptionUrl(config.getMenuOptionUrl());
		this.section.set(new Integer(config.getSections()));
		// this.layout.set(config.getLayout());
	}

	public void setAllGreen() {
		pause();
		// controller.clear();
		tableObjects.forEach(TableViewModel::resetGreen);
		controller.updateAllTables(false, encodeTables());
		resume();
	}

	public void updateAllTables(boolean rts) {
		pause();
		// controller.clear();
		controller.updateAllTables(true, encodeTables());
		resume();
	}
        
        public void restartTimer() {
		pause();
		// controller.clear();
		resume();
	}
        

	public void switchSelect() {
		tableObjects.forEach(TableViewModel::switchSelect);
	}

	public void loadLayout(String fileName) {
		if (layout.get() != null && layout.get().equals(fileName))
			return;
		System.out.println("Load new layout: " + fileName);
		pause();
		// controller.clear();
		layout.set(fileName);
		config.setLayout(fileName);

		List<Table> tables = LayoutHelper.getTables(fileName);
		LayoutHelper.reScaleTableLayout(tables);
		// LayoutHelper.sortTables(tables);

		tableList.clear();
		tableObjects.clear();
		tableMap.clear();
		addTables(tables);

		resume();
	}

	public void clearTableSections() {
            System.out.println("Clearing Sections : ");
            pause();
            tableObjects.forEach(TableViewModel::resetSection);
            controller.updateAllTables(false, encodeTables());
            resume();
	}

	public void removeTables(List<Table> tables) {
		try {
			for (Table tb : tables) {
				TableViewModel tvm = new TableViewModel(tb);
				tvm.setOnUpdate(event -> {
					if (event.getType() == NotifyType.Update) {
						controller.updateTable(event.getSrc().getTable());
					} else if (event.getType() == NotifyType.Remap) {
						// controller.updateTable(event.getSrc().getTable());
					}
				});
				tableList.add(tvm);
				if (tb.getType() == Table.Type.TABLE) {
					tableObjects.add(tvm);
					tableMap.put(tb.getTParameter(), tvm);
				}
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void updateSection(int section) throws CheckException {
		controller.updateSectionCount(section);

		Platform.runLater(() -> {
			isSectionChanged = true;
			if (sectionViewModelList != null) {
				sectionViewModelList.clear();
			}
			if (statusGlances != null) {
				statusGlances.clear();
			}
			sectionProperty().set(section);
			disconnected.set(false);
		});
	}

	public void staffNumberChange(String val) {
		try {
			controller.staffNumberChangeRequest(val);
			Platform.runLater(() -> {
				disconnected.set(false);
			});
		} catch (CheckException e) {
			Platform.runLater(() -> {
				disconnected.set(true);
			});
		}
	}

	public void saveSectionViewModel(SectionViewModel viewModel) {
		if (viewModel.lockProperty().get()) {
			// Send save to server
			String resp = controller.addStaffLog(viewModel.getAssignment(), "start");
			if ("Success".equalsIgnoreCase(resp)) {

			}
		}
	}

	public void saveMenuOptions() {
		if (mapMenuOptions == null) {
			mapMenuOptions = new HashMap<>();
		}
		mapMenuOptions.put(Constants.menuShowNetStatus, isShowNetStatus());
		mapMenuOptions.put(Constants.menuShowServerHost, isShowServerHost());
		mapMenuOptions.put(Constants.menuShowclientHost, isShowClientHost());
		mapMenuOptions.put(Constants.menuShowTime, isShowTime());
		mapMenuOptions.put(Constants.menuShowState, isShowState());
		mapMenuOptions.put(Constants.menuShowRestroomStatus, isShowRestRoomStatus());
		mapMenuOptions.put(Constants.menuShowManagerStatus, isShowManagerStatus());
		mapMenuOptions.put(Constants.menuShowStatusGlance, isShowStatusGlance());
		mapMenuOptions.put(Constants.menuClearSectionStaff, isClearSectionStaff());
		mapMenuOptions.put(Constants.menuClearTableSection, isClearTableSection());
		mapMenuOptions.put(Constants.menuLocked, lockedProperty().get());

		controller.saveOptions(mapMenuOptions);
	}

	public void removeSectionViewModel(SectionViewModel viewModel, boolean isSingle) {
		if (isSingle)
			this.sectionViewModelList.remove(viewModel);
		if (viewModel.lockProperty().get()) {
			// Send remove to sever
			String resp = controller.addStaffLog(viewModel.getAssignment(), "stop");
			if ("Success".equalsIgnoreCase(resp)) {

			}
		}
		Platform.runLater(() -> {
			updateElapsed();
		});
	}

	public void addSectionViewModel(SectionViewModel viewModel) {
		this.sectionViewModelList.add(viewModel);
	}

	public boolean isClearSectionStaff() {
		return clearSectionStaff.get();
	}

	public void setClearSectionStaff(boolean clearSectionStaff) {
		this.clearSectionStaff.set(clearSectionStaff);
	}

	public boolean isClearTableSection() {
		return clearTableSection.get();
	}

	public void setClearTableSection(boolean clearTableSection) {
		this.clearTableSection.set(clearTableSection);
	}

	public RoomView getRoomVw() {
		return roomVw;
	}

	public void setRoomVw(RoomView roomVw) {
		this.roomVw = roomVw;
	}

	public Map<String, Boolean> getMapMenuOptions() {
		return mapMenuOptions;
	}

	public void setMapMenuOptions(Map<String, Boolean> mapMenuOptions) {
		this.mapMenuOptions = mapMenuOptions;
	}

	@Deprecated
	public void refreshRestroomStatus(String data) {
		if (this.refreshTimer == null || data == null || data.trim().length() <= 0) {
			return;
		}

		// System.out.print("refreshRestroomStatus >> " + data);

		// HttpHelper.parseResponse(data, type)

		String[] status = data.split("~");
		if (status != null && status.length == 2) {
			this.isMenRestroomStatusRed = Boolean.parseBoolean(status[0]);
			this.isWomenRestroomStatusRed = Boolean.parseBoolean(status[1]);
		}
		// System.out.println("refreshRestroomStatus isMenRestroomStatusRed - "
		// + isMenRestroomStatusRed
		// + ", isWomenRestroomStatusRed - " + isWomenRestroomStatusRed);
		refreshRestRoomStatus();
	}

	@Deprecated
	public void refreshTables() {
		controller.refreshTables();
	}

	public List<TableViewModel> getTableObjects() {
		return tableObjects;
	}

	public void setTableObjects(List<TableViewModel> tableObjects) {
		this.tableObjects = tableObjects;
	}

	public ObservableList<TableViewModel> getTableList() {
		return tableList;
	}

	public void setTableList(ObservableList<TableViewModel> tableList) {
		this.tableList = tableList;
	}

	public IntegerProperty getBatteryStatus() {
		return batteryStatus;
	}

	public void setBatteryStatus(IntegerProperty batteryStatus) {
		this.batteryStatus = batteryStatus;
	}

}
