package lab.oxgame.dao;

import java.util.List;
import java.util.Optional;
import lab.oxgame.model.Rozgrywka;

public interface RozgrywkaDAO {
	
	Optional<Rozgrywka> findById(Integer rozgrywkaId);
	
	List<Rozgrywka> findAll();
	
	void save(Rozgrywka rozgrywka);
	
	void deleteById(Integer rozgrywkaId);
	
	void deleteAll();
}
