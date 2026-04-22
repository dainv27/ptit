module vn.dainv.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;

    requires org.kordamp.bootstrapfx.core;

    opens vn.dainv.server to javafx.fxml;
    exports vn.dainv.server;
}