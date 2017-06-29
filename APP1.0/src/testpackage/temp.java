package testpackage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class temp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("HTML");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());
        VBox root = new VBox();
        final WebView browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        Hyperlink hpl = new Hyperlink("java2s.com");
        String sysProxy=System.getProperty("http.proxyHost");
        String sysProxyPort=System.getProperty("http.proxyPort");
        for(String each : System.getProperties().stringPropertyNames()){
            System.out.println(each);
        }
        String myProxy="10.3.100.207";
        String myProxyPort="8080";
//        changeProxySettings(myProxy,myProxyPort);
//        System.setProperty("https.proxyHost",myProxy);
//        System.setProperty("https.proxyPort",myProxyPort);
        webEngine.load("https://www.google.com");
//        System.getProperties().remove("https.proxyHost");
//        System.getProperties().remove("https.proxyPort");

        root.getChildren().addAll(hpl,browser);
        scene.setRoot(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void changeProxySettings(String ip, String port) {

        StringBuffer output = new StringBuffer();

        System.setProperty("java.net.useSystemProxies", "true");

        String ENABLE_PROXY_CMD = " reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" \n" + "    /v ProxyEnable /t REG_DWORD /d 1 /f";

        String CHANGE_PROXY_CMD = "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" \n" + "    /v ProxyServer /t REG_SZ /d " + ip + ":" + port + "  /f";

        String DISABLE_PROXY_CMD = "reg add \"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\" \n" + "    /v ProxyEnable /t REG_DWORD /d 0 /f";

        Process processEnableProxy, processChangeProxy, processDisableProxy;
        try {
            processEnableProxy = Runtime.getRuntime().exec(ENABLE_PROXY_CMD);

            processEnableProxy.waitFor();//makes the current thread to wait until system settings are applied

            processChangeProxy = Runtime.getRuntime().exec(CHANGE_PROXY_CMD);

            processChangeProxy.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(processEnableProxy.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(output.toString());
    }
}