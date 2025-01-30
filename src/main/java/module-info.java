module oxGame {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.slf4j;

    opens lab.oxgame to javafx.graphics, javafx.fxml;
}
