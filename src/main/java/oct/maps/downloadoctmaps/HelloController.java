package oct.maps.downloadoctmaps;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelloController {

    @FXML
    private TextArea urlText;

    @FXML
    private TextArea suffixText;



    @FXML
    protected void download()
    {
        try {
            for (int i = 0; i < 999; i++) {
                String formattedNumber = String.format("%03d", i);
                downloadFile(urlText.getText() + formattedNumber + suffixText.getText(), formattedNumber);
            }
            showCompletionWindow();
        } catch (IOException e)
        {
            System.err.println("Something went wrong!\n");
            e.printStackTrace();
        }
    }


    void downloadFile(String fileURL, String numbers) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");

            if (disposition != null) {
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10, disposition.length() - 1);
                }
            } else {
                fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
            }

            InputStream inputStream = httpConn.getInputStream();
            String saveFilePath = System.getProperty("user.dir") + "/" + fileName;

            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            int bytesRead;
            byte[] buffer = new byte[4096];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            System.out.println("Downloading map for route " + numbers + ": File downloaded to " + saveFilePath);

        } else {
            System.out.println("Downloading map for route " + numbers + ": No file to download. Server replied HTTP code: " + responseCode);
        }

        httpConn.disconnect();
    }

    void showCompletionWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("done-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage doneStage = new Stage();
            doneStage.initModality(Modality.APPLICATION_MODAL);
            doneStage.setTitle("Download Complete");
            doneStage.setScene(scene);
            doneStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}