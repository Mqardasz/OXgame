package lab.oxgame;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import lab.oxgame.dao.RozgrywkaDAO;
import lab.oxgame.exception.DBException;
import lab.oxgame.model.OXEnum;
import lab.oxgame.model.Rozgrywka;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import lab.oxgame.engine.OXGameImpl;
import lab.oxgame.dao.RozgrywkaDAOImpl;
public class MainController {
	
	private static final Logger logger = LoggerFactory.getLogger(MainController.class);
	private RozgrywkaDAO rozgrywkaDAO;
	private ExecutorService executor;
	
	public MainController() {
		this.rozgrywkaDAO = new RozgrywkaDAOImpl();
		this.executor = Executors.newSingleThreadExecutor();
		this.game = new OXGameImpl();
	}
	
	@FXML
	public void onActionBtnNew(ActionEvent event) {
		executor.execute(() -> testDB());
	}
	
	private void testDB() {
		try {
			// Testowanie metody save() - dodawanie kilku rekordów
		    Rozgrywka rozgrywka1 = new Rozgrywka("GraczO1", "GraczX1", OXEnum.BRAK, LocalDateTime.now());
		    Rozgrywka rozgrywka2 = new Rozgrywka("GraczO2", "GraczX2", OXEnum.O, LocalDateTime.now().minusHours(1));
		    Rozgrywka rozgrywka3 = new Rozgrywka("GraczO3", "GraczX3", OXEnum.X, LocalDateTime.now().minusDays(1));

		    rozgrywkaDAO.save(rozgrywka1);
		    rozgrywkaDAO.save(rozgrywka2);
		    rozgrywkaDAO.save(rozgrywka3);

		    logger.info("Dodano kilka nowych rozgrywek do bazy.");
	
	        // Testowanie metody findAll()
	        List<Rozgrywka> wszystkieRozgrywki = rozgrywkaDAO.findAll();
	        logger.info("Wszystkie rozgrywki w bazie:");
	        for (Rozgrywka rozgrywka : wszystkieRozgrywki) {
	        	logger.info(rozgrywka.toString());
	        }
	
	        // Testowanie metody findById()
	        if (!wszystkieRozgrywki.isEmpty()) {
	            Integer testId = wszystkieRozgrywki.get(0).getRozgrywkaId();
	            Optional<Rozgrywka> znalezionaRozgrywka = rozgrywkaDAO.findById(testId);
	            if (znalezionaRozgrywka.isPresent()) {
	            	logger.info("Znaleziono rozgrywkę o ID " + testId + ": " + znalezionaRozgrywka.toString());
	            } else {
	            	logger.info("Nie znaleziono rozgrywki o ID " + testId);
	            }
	        }
	
	        // Testowanie metody deleteById()
	        if (!wszystkieRozgrywki.isEmpty()) {
	            Integer deleteId = wszystkieRozgrywki.get(0).getRozgrywkaId();
	            rozgrywkaDAO.deleteById(deleteId);
	            logger.info("Usunięto rozgrywkę o ID: " + deleteId);
	        }
	
	        // Testowanie metody deleteAll()
	        rozgrywkaDAO.deleteAll();
	        logger.info("Usunięto wszystkie rozgrywki z bazy.");
	
	        // Ponowny test findAll() po usunięciu
	        List<Rozgrywka> pusteRozgrywki = rozgrywkaDAO.findAll();
	        if (pusteRozgrywki.isEmpty()) {
	        	logger.info("Baza danych jest pusta.");
	        } else {
	        	logger.info("Baza danych nadal zawiera rozgrywki: " + pusteRozgrywki);
	        }

		
		} catch(DBException e) {
			logger.error("Blad podczas operacji bazodanowych!", e);
			String errDetails = e.getCause().getMessage();
			Platform.runLater(() -> showError(e.getMessage(), errDetails));
		}
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


	@FXML
	private Label statusLabel;

	@FXML
	private GridPane gameBoard;

	private OXGameImpl game;


	@FXML
	private Button btn0, btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8;


	@FXML
	public void initialize() {
		updateBoard();
	}

	@FXML
	public void handleMove(javafx.event.ActionEvent event) {
		Button clickedButton = (Button) event.getSource();
		int index = GridPane.getRowIndex(clickedButton) * 3 + GridPane.getColumnIndex(clickedButton);
		if (game.wykonajRuch(index)) {
			updateBoard();
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
}
