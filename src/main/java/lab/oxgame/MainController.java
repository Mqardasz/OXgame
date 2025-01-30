package lab.oxgame;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import lab.oxgame.dao.RozgrywkaDAO;
import lab.oxgame.dao.RozgrywkaDAOImpl;
import lab.oxgame.engine.OXGameImpl;
import lab.oxgame.exception.DBException;
import lab.oxgame.model.OXEnum;
import lab.oxgame.model.Rozgrywka;

public class MainController {

	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private RozgrywkaDAO rozgrywkaDAO;
	private ExecutorService executor;

	@FXML
	private Label statusLabel;
	@FXML
	private GridPane gameBoard;
	@FXML
	private ListView<String> gameHistoryList;

	private OXGameImpl game;

	public MainController() {
		this.rozgrywkaDAO = new RozgrywkaDAOImpl();
		this.executor = Executors.newSingleThreadExecutor();
		this.game = new OXGameImpl();
	}

	@FXML
	public void initialize() {
		updateBoard();
		loadGameHistory();
	}

	@FXML
	public void handleMove(ActionEvent event) {
		Button clickedButton = (Button) event.getSource();
		int index = GridPane.getRowIndex(clickedButton) * 3 + GridPane.getColumnIndex(clickedButton);
		if (game.wykonajRuch(index)) {
			updateBoard();
			saveGameState();
		}
		if (game.getZwyciezca() != OXEnum.BRAK) {
			statusLabel.setText("Zwycięzca: " + game.getZwyciezca());
		} else {
			statusLabel.setText("Tura: " + game.getKolejnosc());
		}
	}

	@FXML
	public void restartGame() {
		game.inicjalizuj();
		updateBoard();
		statusLabel.setText("Nowa gra!");
	}

	private void updateBoard() {
		OXEnum[] stan = game.getStan();
		for (int i = 0; i < 9; i++) {
			Button button = (Button) gameBoard.getChildren().get(i);
			button.setText(stan[i].toString());
			button.setDisable(stan[i] != OXEnum.BRAK || game.getZwyciezca() != OXEnum.BRAK);
		}
	}

	// Method to load game history from the database
	public void loadGameHistory() {
		executor.execute(() -> {
			try {
				List<Rozgrywka> rozgrywki = rozgrywkaDAO.findAll();
				Platform.runLater(() -> {
					gameHistoryList.getItems().clear();
					for (Rozgrywka rozgrywka : rozgrywki) {
						gameHistoryList.getItems().add(rozgrywka.toString());
					}
				});
			} catch (DBException e) {
				logger.error("Blad podczas pobierania historii gier!", e);
				String errDetails = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
				Platform.runLater(() -> showError(e.getMessage(), errDetails));
			}
		});
	}

	// Method to save the current game state to the database
	private void saveGameState() {
		executor.execute(() -> {
			try {
				Rozgrywka currentGame = new Rozgrywka("GraczX", "GraczO", game.getZwyciezca(), LocalDateTime.now());
				rozgrywkaDAO.save(currentGame);
				Platform.runLater(() -> loadGameHistory()); // Reload history to reflect new game state
			} catch (DBException e) {
				logger.error("Blad podczas zapisywania stanu gry!", e);
				String errDetails = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
				Platform.runLater(() -> showError(e.getMessage(), errDetails));
			}
		});
	}

	public void shutdown() {
		executor.shutdownNow();
	}

	private void showError(String testHeader, String textContent) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error dialog");
		alert.setHeaderText(testHeader);
		alert.setContentText(textContent);
		alert.showAndWait();
	}
}