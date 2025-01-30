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
		// Initialize the database
		DBinitializer.initialize();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oxgame/Main.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("OX Game");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		// Get the controller instance
		MainController controller = loader.getController();
		// Load game history when the application starts
		controller.loadGameHistory();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
