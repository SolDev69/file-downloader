package downloader.downloadfiles;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.spec.ECField;
import java.util.Objects;

public class HelloApplication extends Application {

    private static String[] savedArgs;

    @Override
    public void init() throws Exception {
        super.init();
        savedArgs = getParameters().getRaw().toArray(new String[0]);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("download-view.fxml"));
        Parent root = fxmlLoader.load();
        // Get the controller and call methods based on arguments
        DownloadController dc = fxmlLoader.getController();
        if (savedArgs.length > 0) {
            switch (savedArgs[0]) {
                case "NWTB":
                    dc.downloadNWTB();
                    System.exit(0);
                    break;
                case "current":
                    dc.downloadCurrent();
                    System.exit(0);
                    break;
                case "old":
                    dc.downloadOlder();
                    System.exit(0);
                    break;
                case "all":
                    dc.downloadAll();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid argument: " + savedArgs[0]);
                    System.exit(-1);
            }
        }
        else
        {
            Scene scene = new Scene(root, 320, 240);
            stage.setTitle("Map Downloader");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }


}