module org.project.project_wetube {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.project.youtube to javafx.fxml;
    exports org.project.youtube;
}