module org {
    requires javafx.controls;
    requires javafx.fxml;


    opens org to javafx.fxml;
    exports org;
    exports org.entity;
    opens org.entity to javafx.fxml;
    exports org.singleton;
    opens org.singleton to javafx.fxml;
    exports org.controller;
    opens org.controller to javafx.fxml;
    exports org.adapter;
    opens org.adapter to javafx.fxml;
}