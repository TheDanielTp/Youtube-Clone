module org.project.project_wetube {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.project.controller to javafx.fxml;
    exports org.project.controller to javafx.graphics;
}