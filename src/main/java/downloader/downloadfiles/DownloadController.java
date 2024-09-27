package downloader.downloadfiles;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadController {

    @FXML
    protected void initialize() {
    }

    void download(String a, String b) {
        download(a, b, "");
    }

    void download(String a, String b, String subdirectory) {
        try {
            // Determine the download directory
            String baseDirectory = System.getProperty("user.dir") + "/OCTMaps" + (subdirectory.isEmpty() ? "" : "/" + subdirectory);
            File directory = new File(baseDirectory);

            // Create the directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Download the files
            for (int i = 0; i < 1000; i++) {
                String formattedNumber = String.format("%03d", i);
                downloadFile(a + formattedNumber + b, formattedNumber, baseDirectory);
            }
            showCompletionWindow();
        } catch (IOException e) {
            System.err.println("Something went wrong!\n");
            e.printStackTrace();
        }
    }

    @FXML
    protected void downloadCurrent() {
        download("https://www.octranspo.com/images/files/routes_pdf/map_carte_", ".pdf", "current");
    }

    @FXML
    protected void downloadNWTB() {
        download("https://www.octranspo.com/images/files/files/routes_pdf_future/RD_Map_", "_(Jan2024).pdf", "NWTB");
    }

    @FXML
    protected void downloadOlder() {
        download("https://www.octranspo.com/images/files/files/routes_pdf/map_carte_", ".pdf", "older");
    }

    @FXML
    protected void downloadAll() {
        ExecutorService executor = Executors.newFixedThreadPool(3); // Create a pool of 3 threads

        // Submit each download task to the executor
        executor.submit(() -> {
            System.out.println("Starting download for current maps...");
            download("https://www.octranspo.com/images/files/routes_pdf/map_carte_", ".pdf", "current");
            System.out.println("Finished downloading current maps.");
        });

        executor.submit(() -> {
            System.out.println("Starting download for NWTB maps...");
            download("https://www.octranspo.com/images/files/files/routes_pdf_future/RD_Map_", "_(Jan2024).pdf", "NWTB");
            System.out.println("Finished downloading NWTB maps.");
        });

        executor.submit(() -> {
            System.out.println("Starting download for older maps...");
            download("https://www.octranspo.com/images/files/files/routes_pdf/map_carte_", ".pdf", "older");
            System.out.println("Finished downloading older maps.");
        });

        executor.shutdown(); // Gracefully shutdown the executor after tasks finish
    }

    void downloadFile(String fileURL, String numbers, String downloadDirectory) throws IOException {
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
            String saveFilePath = downloadDirectory + "/" + fileName;

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
            FXMLLoader fxmlLoader = new FXMLLoader(DownloaderApp.class.getResource("done-view.fxml"));
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
