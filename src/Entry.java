import java.sql.SQLException;

import com.amazon.ServiceProvider;
import com.amazon.dto.SignUpDTO;

public class Entry {
	public static void main(String[] args) throws SQLException {
		// DatabaseManager dm = new DatabaseManager();
		// dm.getConnection();
		ServiceProvider login = new ServiceProvider();

		SignUpDTO signUpDTO = new SignUpDTO();
		signUpDTO.setEmailId("test1@test.com");
		signUpDTO.setFirstname("test");
		signUpDTO.setLastname("test");
		signUpDTO.setPassword("test");
		// / login.createUser(signUpDTO);

	}
}
