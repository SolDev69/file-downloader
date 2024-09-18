package downloader.downloadfiles;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DownloaderApp extends Application {

    private static String[] savedArgs;

    @Override
    public void init() throws Exception {
        super.init();
        savedArgs = getParameters().getRaw().toArray(new String[0]);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(DownloaderApp.class.getResource("download-view.fxml"));
        Parent root = fxmlLoader.load();
        // Get the controller and call methods based on arguments
        DownloadController dc = fxmlLoader.getController();
        if (savedArgs.length > 0) {
            switch (savedArgs[0]) {
                case "NWTB":
                    dc.downloadNWTB();
                    break;
                case "current":
                    dc.downloadCurrent();
                    break;
                case "old":
                    dc.downloadOlder();
                    break;
                case "all":
                    dc.downloadAll();
                    break;
                default:
                    System.err.println("Invalid argument: " + savedArgs[0]);
            }
            Platform.exit();
        }
        else
        {
            Scene scene = new Scene(root, 320, 240);
            stage.setTitle("Map Downloader");
            stage.setScene(scene);
            stage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
