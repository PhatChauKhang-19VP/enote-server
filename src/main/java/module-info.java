module pck.enoteserver {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires cloudinary.core;
    requires org.apache.commons.io;

    opens pck.enote_server to javafx.fxml;
    exports pck.enote_server;
    exports pck.enote_server.api.req;
    opens pck.enote_server.api.req to javafx.fxml;
    exports pck.enote_server.controller;
    opens pck.enote_server.controller to javafx.fxml;
    opens pck.enote_server.be.server to javafx.fxml;
    exports pck.enote_server.be.server;
}