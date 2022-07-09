package Abby.demo.dao;

import org.springframework.stereotype.Repository;

@Repository("alpha_dao1")
public class Dao1_exm implements Dao1 {

	@Override
	public String select() {
		// TODO Auto-generated method stub
		return "exp";
	}

}
