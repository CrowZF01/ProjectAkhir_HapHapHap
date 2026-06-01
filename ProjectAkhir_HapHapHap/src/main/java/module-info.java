module com.felix_71241153.app.projectakhir_haphaphap {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.felix_71241153.app.projectakhir_haphaphap to javafx.fxml;
    exports com.felix_71241153.app.projectakhir_haphaphap;
}