package example.Coupon_Project_3.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;

public interface CompanyRepository extends JpaRepository<Company, Integer>{
	

	
	// the Binary is for making the password case sensitive 
	@Query(value = "SELECT id FROM companies WHERE email = ?1 AND BINARY password = ?2 ", nativeQuery = true)
	Integer login(String email, String password);
	
	Optional<Company> findByEmailAndPassword(String email, String password);
	
	
	
	
	

	

	
	
	

	
}
