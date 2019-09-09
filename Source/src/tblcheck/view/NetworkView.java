package tblcheck.view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import tblcheck.helper.ClientHelper;
import tblcheck.helper.FxHelper;
import tblcheck.helper.WindowsHelper;

/**
 * Created by Han on 3/25/2019.
 */
public class NetworkView {
    @FXML
    private GridPane pnRoot;
    @FXML
    private Button btnOK;
    @FXML
    private TextField ssIDField;
    @FXML
    private PasswordField pwField;
    @FXML
    private Label lblTxt;

    int dialogResult = -1;
    boolean useTabtip = false;
    boolean showKeyboard = true;
    String enteredPassword, enteredSSID;
    

    @FXML
    void initialize() {
        btnOK.setOnAction(event -> {
            dialogResult = 0;
            enteredSSID = ssIDField.getText();
            enteredPassword = pwField.getText();
            btnOK.setDisable(true);
            lblTxt.setText("connecting... \n");
            addWirelessNetwork(enteredSSID, enteredPassword);
            FxHelper.closeStage((Stage) pnRoot.getScene().getWindow());
        });
        ssIDField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!showKeyboard)
                return;
            //if (newValue) // is focused
                WindowsHelper.showVirtualKeyboard(useTabtip);
            //else WindowsHelper.hideVirtualKeyboard(useTabtip);
        });
        pwField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!showKeyboard)
                return;
            //if (newValue) // is focused
                WindowsHelper.showVirtualKeyboard(useTabtip);
            //else WindowsHelper.hideVirtualKeyboard(useTabtip);
        });
    }

    public void setUseTabtip(boolean value) {
        this.useTabtip = value;
    }

    public void setShowKeyboard(boolean value) {
        this.showKeyboard = value;
    }

    public int getDialogResult() {
        return this.dialogResult;
    }

    public String getEnteredPassword() {
        return this.enteredPassword;
    }
    
    public String getEnteredSSID() {
        return this.enteredSSID;
    }
    
    public void addWirelessNetwork(String ssID, String password) {
        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(
            "wireless_profile.xml"));
            File dir = new File("c:/tmp");
            if(!dir.exists())
                dir.mkdirs();
            dir.setReadable(true);
            dir.setWritable(true);
            
            File UIFile = new File("c:/tmp/profile.xml");
            UIFile.setReadable(true);
            UIFile.setWritable(true);
               // if File doesnt exists, then create it
               if (!UIFile.exists()) {
                   UIFile.createNewFile();
               }
            FileWriter filewriter = new FileWriter(UIFile.getAbsoluteFile());
            BufferedWriter outputStream= new BufferedWriter(filewriter);
            String line;
            while ((line = inputStream.readLine()) != null) {
                line = line.replaceAll("\\{SSID\\}", ssID);
                line = line.replaceAll("\\{password\\}", password);
                outputStream.write(line);
                outputStream.write("\n");
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            
            
            Thread th3 = new Thread() {
                @Override
                public void run() {
                    try {
                        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan start hostednetwork");
                        System.out.println("==== running command ===");
                        System.out.println("netsh wlan start hostednetwork");
                        Process p = builder.start();
                        Thread.sleep(3000);
                        p.destroy();
                        
                        //Restart OS
                        ClientHelper.restartApplication(null, new Thread() {
                            @Override
                            public void run() {
                                    return;
                            }
                        });
                        //lblTxt.setText("Connected!");
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            
            Thread th2 = new Thread() {
                @Override
                public void run() {
                    try {
                        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan set hostednetwork mode=allow ssid="+ssID+" key="+password);
                        Process p = builder.start();
                        System.out.println("==== running command ===");
                        System.out.println("netsh wlan set hostednetwork mode=allow ssid="+ssID+" key="+password);
                        p.waitFor(3, TimeUnit.SECONDS);
                        Thread.sleep(3000);
                        p.destroy();
                        th3.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            
            Thread th1 = new Thread() {
                @Override
                public void run() {
                    try {
                        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan stop hostednetwork");
                        Process p = builder.start();
                        System.out.println("==== running command ===");
                        System.out.println("netsh wlan stop hostednetwork");
                        p.waitFor(3, TimeUnit.SECONDS);
                        Thread.sleep(3000);
                        p.destroy();
                        th2.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            
            Thread th = new Thread() {
                @Override
                public void run() {
                    try {
                        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan add profile filename=\"c:\\tmp\\profile.xml\"");
                        Process p = builder.start();
                        System.out.println("==== running command ===");
                        System.out.println("netsh wlan add profile filename=\"c:\\tmp\\profile.xml");
                        p.waitFor(3, TimeUnit.SECONDS);
                        Thread.sleep(3000);
                        p.destroy();
                        th1.start();
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            th.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void showAllWirelessNetwork(String ssID, String password) {
        ArrayList<String>ssids=new ArrayList<String>();
        ArrayList<String>signals=new ArrayList<String>();
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "netsh wlan show all");
        builder.redirectErrorStream(true);
        try {
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (r.read()!=-1) {
                line = r.readLine();
                if (line.contains("SSID")||line.contains("Signal")){
                    if(!line.contains("BSSID"))
                        if(line.contains("SSID")&&!line.contains("name")&&!line.contains("SSIDs"))
                        {
                            line=line.substring(8);
                            ssids.add(line);

                        }
                        if(line.contains("Signal"))
                        {
                            line=line.substring(30);
                            signals.add(line);

                        }

                        if(signals.size()==7)
                        {
                            break;
                        }

                }

            }
            for (int i=0;i<ssids.size();i++)
            {
                System.out.println("SSID name == "+ssids.get(i)+"   and its signal == "+signals.get(i)  );
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
