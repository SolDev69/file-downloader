module downloader.downloadfiles {
    requires javafx.controls;
    requires javafx.fxml;


    opens downloader.downloadfiles to javafx.fxml;
    exports downloader.downloadfiles;
}