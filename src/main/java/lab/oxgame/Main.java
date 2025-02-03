package lab.oxgame;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lab.oxgame.datasource.DBinitializer;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// zainicjuj baze danych (gdyby byla pusta)
		DBinitializer.initialize();
		// zaladuj GUI
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oxgame/Main.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("OX Game");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		// instancja klasy MainController
		MainController controller = loader.getController();
		// wczytaj historie gier
		controller.loadGameHistory();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
