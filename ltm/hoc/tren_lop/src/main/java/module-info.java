module vn.dainv.tren_lop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens vn.dainv.tren_lop to javafx.fxml;
    exports vn.dainv.tren_lop;
}