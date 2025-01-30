package lab.oxgame;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/oxgame/Main.fxml"));
		Parent root = loader.load();
		primaryStage.setTitle("OX Game");
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}

/*	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/com/example/oxgame/Main.fxml"));
			
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			MainController controller = loader.getController();
			primaryStage.setOnCloseRequest(event -> {
				controller.shutdown();
				Platform.exit();
			});
			primaryStage.setScene(scene);
			primaryStage.sizeToScene();
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Main.fxml"));
//			Scene scene = new Scene(root,400,400);
//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
//			primaryStage.setScene(scene);
//			primaryStage.show();
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args) {
		launch(args);

	}
}
