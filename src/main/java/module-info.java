module org.project.project_wetube {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.google.gson;
    requires javafx.media;
    requires java.desktop;
    requires com.sun.jna.platform;
    requires com.sun.jna;

    opens org.project.controller to javafx.fxml;
    exports org.project.controller to javafx.graphics;
}