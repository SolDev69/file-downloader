module oct.maps.downloadoctmaps {
    requires javafx.controls;
    requires javafx.fxml;


    opens oct.maps.downloadoctmaps to javafx.fxml;
    exports oct.maps.downloadoctmaps;
}