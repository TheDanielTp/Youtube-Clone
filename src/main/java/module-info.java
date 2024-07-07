module org.project.project_wetube {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    opens com.wetube to javafx.fxml;
    exports com.wetube;
}