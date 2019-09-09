package tblcheck.helper;

/**
 * Created by cuongdm5 on 2/3/2016.
 */
public class WindowsHelper {

    public static void showVirtualKeyboard(boolean useTabtip) {
        String openApp =
                useTabtip
                        ? "cmd /c \"C:\\Program Files\\Common Files\\Microsoft Shared\\ink\\TabTip.exe\""
                        : "cmd /c \"C:\\Windows\\System32\\osk.exe\"";
        try {
            Runtime.getRuntime().exec(openApp);
//                ProcessBuilder pb = new ProcessBuilder(openApp);
//                pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideVirtualKeyboard(boolean useTabtip){
        String kill = useTabtip ? "cmd /c taskkill /IM TabTip.exe" : "cmd /c taskkill /IM osk.exe";
        try {
            Runtime.getRuntime().exec(kill);
//            Runtime.getRuntime().exec("cmd /c start \"\" \"stop_osk.lnk\"");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openUrl(String url){
        String openCmd = "cmd /c start " + url;
        try {
            Runtime.getRuntime().exec(openCmd);
//                ProcessBuilder pb = new ProcessBuilder(openApp);
//                pb.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
