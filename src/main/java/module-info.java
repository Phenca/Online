module online.mvc {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.apache.commons.net;

    opens online.mvc to javafx.fxml;
    opens online.mvc.models to javafx.base;
    exports online.mvc;
}