module vn.dainv.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens vn.dainv.client to javafx.fxml;
    exports vn.dainv.client;
}