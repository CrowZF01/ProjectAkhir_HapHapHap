module com.felix_71241153.app.projectakhir_haphaphap {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires javafx.graphics;

    opens com.felix_71241153.app.projectakhir_haphaphap to javafx.fxml;
    exports com.felix_71241153.app.projectakhir_haphaphap;
    opens controller to javafx.fxml;

    exports controller;
    exports model;
    exports database;
    exports util;
    exports dao;
}