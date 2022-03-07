module cz.stv.canvasdemofx {
    requires javafx.controls;
    requires javafx.fxml;
  requires java.base;

    opens cz.stv.canvasdemofx to javafx.fxml;
    exports cz.stv.canvasdemofx;
}