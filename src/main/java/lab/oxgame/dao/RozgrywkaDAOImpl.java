package lab.oxgame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lab.oxgame.datasource.DataSource;
import lab.oxgame.exception.DBException;
import lab.oxgame.model.OXEnum;
import lab.oxgame.model.Rozgrywka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RozgrywkaDAOImpl implements RozgrywkaDAO {
	private static final Logger logger = LoggerFactory.getLogger(RozgrywkaDAOImpl.class);

	@Override
	public Optional<Rozgrywka> findById(Integer rozgrywkaId) {
		String query = "SELECT * FROM rozgrywka WHERE rozgrywka_id = ?";
		try (Connection connect = DataSource.getConnection();
			 PreparedStatement preparedStmt = connect.prepareStatement(query)) {
			preparedStmt.setInt(1, rozgrywkaId);
			try (ResultSet rs = preparedStmt.executeQuery()) {
				if (rs.next()) {
					String graczO = rs.getString("gracz_o");
					String graczX = rs.getString("gracz_x");
					OXEnum zwyciezca = OXEnum.fromString(rs.getString("zwyciezca"));
					LocalDateTime dataczasRozgrywki = rs.getObject("dataczas_rozgrywki", LocalDateTime.class);

					Rozgrywka rozgrywka = new Rozgrywka(graczX, graczO, zwyciezca, dataczasRozgrywki);
					return Optional.of(rozgrywka);
				}
			}
		} catch (SQLException e) {
			logger.error("Error finding game by ID", e);
			throw new DBException("Blad podczas pobierania rozgrywki z bazy", e);
		}

		return Optional.empty();
	}

	@Override
	public List<Rozgrywka> findAll() {
		List<Rozgrywka> rozgrywki = new ArrayList<>();
		String query = "SELECT * FROM rozgrywka ORDER BY dataczas_rozgrywki DESC";
		try (Connection connect = DataSource.getConnection();
			 Statement stmt = connect.createStatement();
			 ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				Integer rozgrywkaId = rs.getInt("rozgrywka_id");
				String graczO = rs.getString("gracz_o");
				String graczX = rs.getString("gracz_x");
				OXEnum zwyciezca = OXEnum.fromString(rs.getString("zwyciezca"));
				LocalDateTime dataczasRozgrywki = rs.getObject("dataczas_rozgrywki", LocalDateTime.class);

				Rozgrywka rozgrywka = new Rozgrywka(rozgrywkaId, graczX, graczO, zwyciezca, dataczasRozgrywki);

				rozgrywki.add(rozgrywka);
			}
		} catch (SQLException e) {
			logger.error("Error finding all games", e);
			throw new DBException("Blad podczas pobierania rozgrywek z bazy", e);
		}

		return rozgrywki;
	}

	@Override
	public void save(Rozgrywka rozgrywka) {
		String query = "INSERT INTO rozgrywka(gracz_o, gracz_x, zwyciezca, dataczas_rozgrywki) VALUES (?, ?, ?, ?)";
		try (Connection connect = DataSource.getConnection();
			 PreparedStatement preparedStmt = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			preparedStmt.setString(1, rozgrywka.getGraczO());
			preparedStmt.setString(2, rozgrywka.getGraczX());
			preparedStmt.setString(3, rozgrywka.getZwyciezca().toString());
			preparedStmt.setObject(4, rozgrywka.getDataczasRozgrywki());
			preparedStmt.executeUpdate();

			try (ResultSet rs = preparedStmt.getGeneratedKeys()) {
				if (rs.next())
					rozgrywka.setRozgrywkaId(rs.getInt(1));
			}
		} catch (SQLException e) {
			logger.error("Error saving game", e);
			throw new DBException("Blad podczas zapisywania rozgrywek w bazie danych!", e);
		}
	}

	@Override
	public void deleteById(Integer rozgrywkaId) {
		String query = "DELETE FROM rozgrywka WHERE rozgrywka_id = ?";
		try (Connection connect = DataSource.getConnection();
			 PreparedStatement preparedStmt = connect.prepareStatement(query)) {
			preparedStmt.setInt(1, rozgrywkaId);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("Error deleting game by ID", e);
			throw new DBException("Blad podczas usuwania rozgrywki z bazy danych!", e);
		}
	}

	@Override
	public void deleteAll() {
		String query = "DELETE FROM rozgrywka";
		try (Connection connect = DataSource.getConnection();
			 PreparedStatement preparedStmt = connect.prepareStatement(query)) {
			preparedStmt.execute();
		} catch (SQLException e) {
			logger.error("Error deleting all games", e);
			throw new DBException("Blad podczas usuwania rozgrywek i resetowania ID!", e);
		}
	}
}