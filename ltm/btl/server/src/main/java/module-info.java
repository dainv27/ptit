module vn.dainv.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens vn.dainv.server to javafx.fxml;
    exports vn.dainv.server;
}