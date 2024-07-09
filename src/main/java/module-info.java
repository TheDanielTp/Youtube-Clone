module org.project.project_wetube {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.google.gson;
    requires javafx.media;

    opens org.project.controller to javafx.fxml;
    exports org.project.controller to javafx.graphics;
}