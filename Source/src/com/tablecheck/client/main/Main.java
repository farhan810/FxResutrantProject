package com.tablecheck.client.main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.LoggerFactory;

import com.tablecheck.service.Constants;
import com.tablecheck.service.ServiceApplication;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.net.InetAddress;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import tblcheck.Resource;
import tblcheck.helper.HttpHelper;
import tblcheck.helper.MulticastPublisher;
import tblcheck.helper.MulticastReceiver;
import tblcheck.model.Config;
import tblcheck.view.MessageConstants;
import tblcheck.view.RoomView;
import tblcheck.viewmodel.RoomViewModel;

public class Main extends Application {

	private static Image icon;

	public static Image getIcon() {
		return icon;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		System.setProperty("glass.accessible.force", "false");

		icon = new Image(Resource.class.getResource("tablecheck.png").toExternalForm());
		normalStart(primaryStage);
		// try{
		// String url = "http://localhost/cgi-bin/writer_s.pl?cfg=seatme.cfg";
		//// url += "&ctn="+
		// URLEncoder.encode("T9=gray|-1","UTF-8")+"&restaurantid=1";
		// url += URLEncoder.encode("&ctn=T9=gray|-1&restaurantid=1", "UTF-8");
		// URI uri = URI.create(url);
		// System.out.println(uri.getQuery());
		// }catch(Exception ex){
		// ex.printStackTrace();
		// }
	}

	void normalStart(Stage primaryStage) throws Exception {
            Properties prop = loadProperties(Config.CONFIG_FILE);
            // Config config = new Config(prop);
            Config.loadProperty(prop);
            Config config = Config.getInstance();

            if (config.isServer()) {
                //Updated by Han -- increate timeout time for icmp for isReachable
                Config.getInstance().setServer(false);
                HttpHelper.changeServer(Config.getInstance().getServerPort(), 1000);
            }

            if (config.isClient()) {
                if (!Config.getInstance().isServer()) {
                    Set<String> loggers = new HashSet<>(Arrays.asList("org.apache.http"));

                    for (String log : loggers) {
                        Logger logger = (Logger) LoggerFactory.getLogger(log);
                        logger.setLevel(Level.ERROR);
                        logger.setAdditive(false);
                    }
                }

                RoomView rm = createRoom();
                RoomViewModel rvm = new RoomViewModel();
                rvm.setConfig(config);
                rm.setViewModel(rvm);
                rvm.loadLayout(config.getLayout());
                // rvm.setRoomVw(rm);
                rvm.start();

                primaryStage.setTitle("Table Check");
                primaryStage.setOnShown(event -> {
                        //System.out.println("ON_SHOWN");
                        rm.fitToScreen();
                        // rm.addGrid();
                });
                primaryStage.setOnCloseRequest(event -> {
                    //Stop all threads
                    Config.getInstance().setRThreadStatus(1);
                    Config.getInstance().setBThreadStatus(1);
                    rvm.exit();
                    // saveConfig(prop, config, CONFIG_FILE);
                    Platform.exit();
                });
                
                Thread th = new Thread(new Runnable() {
                    public void run() {
                        while(true) {
                            try {
                                if (Config.getInstance().getLThreadTime()>0 && (System.currentTimeMillis() -  Config.getInstance().getLThreadTime())> 9000) {
                                    System.out.println(">>>>>>> Restart Layout Resume Timer >>>>>>>");
                                    //rvm.restartTimer();
                                    rvm.pause();
                                    Thread.sleep(3000);
                                    rvm.resume();
                                }
                                Thread.sleep(5000);
                            } catch (Exception e) {
                                
                            }
                        }
                    }
                });
                th.start();
                
                Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
                Scene scene = new Scene(rm.getPane(), primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight());
//			Scene scene = new Scene(rm.getPane(),1280,720);
                // System.out.println("CSS: " +
                // Resource.class.getResource("main.css").getPath());
                scene.getStylesheets().add(Resource.class.getResource("main.css").toExternalForm());
                scene.getStylesheets().add(Resource.class.getResource("main-ext.css").toExternalForm());
                /*
                 * File f = new File("main-ext.css"); if(f.exists() &&
                 * !f.isDirectory()){ scene.getStylesheets().add("file:///" +
                 * f.getAbsolutePath().replace("\\", "/")); }
                 */
                //System.out.println("CSS: " + scene.getStylesheets());
                primaryStage.setScene(scene);
                primaryStage.getIcons().add(icon);
                primaryStage.setFullScreen(true);
                primaryStage.setFullScreenExitHint(null);
                primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
                primaryStage.show();

                // ==================== Code for retrive preveously saved option
                //System.out.println("Check Save Option After Load All UI....!!!");
                retriveSavedOption(rm, rvm);

                // String configurationStr = "";
                // try {
                // // Properties prop = new Properties();
                // FileInputStream input = new FileInputStream(CONFIG_FILE);
                // // prop.load(input);
                // Vector v = new Vector();
                // v.add(input);
                // Enumeration e = v.elements();
                // SequenceInputStream sis = new SequenceInputStream(e);
                // int i = 0;
                // while ((i = sis.read()) != -1) {
                // configurationStr = configurationStr + (char) i;
                // // System.out.print((char)i);
                // }
                // sis.close();
                // input.close();
                //
                // } catch (Exception e) {
                // // TODO Auto-generated catch block
                // e.printStackTrace();
                // }
                //
                // String[] list = configurationStr.split(">>>");
                // String preSaveOption = list[1];
                // String[] sapratedSaveOption = preSaveOption.split("::");
                // if (sapratedSaveOption.length > 1 &&
                // sapratedSaveOption[6].equalsIgnoreCase("false")) {
                // rvm.locked.set(false);
                // retriveSavedOption(rm, rvm, sapratedSaveOption);
                // } else {
                // rvm.locked.set(true);
                // retriveSavedOption(rm, rvm, sapratedSaveOption);
                // }
            }
	}

	private void retriveSavedOption(RoomView rm, RoomViewModel rvm) {

		Map<String, Boolean> mapOptions = new HashMap<>();

		mapOptions.put(Constants.menuMoveTables, false); // TODO - Make it true
															// on new features
		mapOptions.put(Constants.menuShowNetStatus, false);
		mapOptions.put(Constants.menuShowServerHost, false);
		mapOptions.put(Constants.menuShowclientHost, false);
		mapOptions.put(Constants.menuShowTime, false);
		mapOptions.put(Constants.menuShowState, false);
		mapOptions.put(Constants.menuShowRestroomStatus, false);
		mapOptions.put(Constants.menuShowManagerStatus, false);
		mapOptions.put(Constants.menuShowStatusGlance, false);
		mapOptions.put(Constants.menuLocked, false); // TODO - Make it true on
														// new features
		mapOptions.put(Constants.menuClearSectionStaff, false);
		mapOptions.put(Constants.menuClearTableSection, false);

		rvm.refreshMenuOptions(mapOptions);

		rm.redrawAsMenuOptions();
	}

	@Deprecated
	private void retriveSavedOption(RoomView rm, RoomViewModel rvm, String[] sapratedSaveOption) {
		// TODO Auto-generated method stub
		if (sapratedSaveOption[0].equalsIgnoreCase("true")) {
			boolean display = true;
			rm.pnStatus.setVisible(display);
			rm.btnShowNetStatus.setText(display ? "Hide Net Status" : "Show Net Status");
		} else {
			boolean display = false;
			rm.pnStatus.setVisible(display);
			rm.btnShowNetStatus.setText(display ? "Hide Net Status" : "Show Net Status");
		}

		if (sapratedSaveOption[1].equalsIgnoreCase("true")) {
			rm.btnShowTime.setText("Hide Time");
			rvm.setShowTime(true);
		} else {
			rm.btnShowTime.setText("Show Time");
			rvm.setShowTime(false);
		}

		if (sapratedSaveOption[2].equalsIgnoreCase("true")) {
			boolean showState = true;
			rvm.setShowState(showState);
			rm.btnShowState.setText(showState ? "Hide State" : "Show State");
		} else {
			boolean showState = false;
			rvm.setShowState(showState);
			rm.btnShowState.setText(showState ? "Hide State" : "Show State");
		}

		if (sapratedSaveOption[3].equalsIgnoreCase("true")) {
			boolean showRestRoomStatus = true;
			rvm.setShowRestRoomStatus(showRestRoomStatus);
			rm.btnShowRestroomStatus.setText(showRestRoomStatus ? "Hide Restroom Status" : "Show Restroom Status");

			rvm.setPauseRestRoomRefresh(!showRestRoomStatus);

			rm.M.setVisible(showRestRoomStatus);
			rm.Restroom.setVisible(showRestRoomStatus);
			rm.W.setVisible(showRestRoomStatus);
		} else {
			boolean showRestRoomStatus = false;
			rvm.setShowRestRoomStatus(showRestRoomStatus);
			rm.btnShowRestroomStatus.setText(showRestRoomStatus ? "Hide Restroom Status" : "Show Restroom Status");

			rvm.setPauseRestRoomRefresh(!showRestRoomStatus);

			rm.M.setVisible(showRestRoomStatus);
			rm.Restroom.setVisible(showRestRoomStatus);
			rm.W.setVisible(showRestRoomStatus);
		}

		if (sapratedSaveOption[4].equalsIgnoreCase("true")) {
			boolean showManagerStatus = true;
			rvm.setShowManagerStatus(showManagerStatus);
			rm.btnShowManagerStatus.setText(showManagerStatus ? "Hide Manager Status" : "Show Manager Status");

			rvm.setPauseManagerRefresh(!showManagerStatus);

			rm.H.setVisible(showManagerStatus);
			rm.Manager.setVisible(showManagerStatus);
			rm.K.setVisible(showManagerStatus);
		} else {
			boolean showManagerStatus = false;
			rvm.setShowManagerStatus(showManagerStatus);
			rm.btnShowManagerStatus.setText(showManagerStatus ? "Hide Manager Status" : "Show Manager Status");

			rvm.setPauseManagerRefresh(!showManagerStatus);

			rm.H.setVisible(showManagerStatus);
			rm.Manager.setVisible(showManagerStatus);
			rm.K.setVisible(showManagerStatus);
		}

		if (sapratedSaveOption[5].equalsIgnoreCase("true")) {
			boolean showStatusGlance = true;
			rvm.setShowStatusGlance(showStatusGlance);
			rm.btnShowStatusGlance.setText(showStatusGlance ? "Hide Wait Staff" : "Show Wait Staff");

			rm.pnStatusGlance.setVisible(showStatusGlance);
		} else {
			boolean showStatusGlance = false;
			rvm.setShowStatusGlance(showStatusGlance);
			rm.btnShowStatusGlance.setText(showStatusGlance ? "Hide Wait Staff" : "Show Wait Staff");

			rm.pnStatusGlance.setVisible(showStatusGlance);
		}
	}

	RoomView createRoom() throws Exception {
		FXMLLoader loader = new FXMLLoader(RoomView.class.getResource("RoomView.fxml"));
		loader.load();
		RoomView vm = loader.getController();
		return vm;
	}

	public static Properties loadProperties(String fileName) {
		Properties prop = new Properties();
		InputStream input = null;
		try {

			input = new FileInputStream(fileName);
			// load a properties file
			prop.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return prop;
	}

	public static void saveConfig(Properties prop, Config config, String fileName) {
		prop.setProperty("layout", config.getLayout());
		prop.setProperty("sections", String.valueOf(config.getSections()));
		OutputStream oStream = null;
		try {
			oStream = new FileOutputStream(fileName);
			prop.store(oStream, null);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (oStream != null)
				try {
					oStream.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	public static void main(String[] args) {

            
		         launch(args);       //faro added 
		       System.setErr(System.out);
                System.out.println("TableCheck - Build 20180204");
            
		if(args!=null && args.length>0){
			if(args[0] !=null && MessageConstants.RESET_TYPE_TEMP.equals(args[0])){
				Config.tempClient = true;
			}
		}
		
		launch(args);
	}
}
