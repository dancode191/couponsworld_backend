package example.Coupon_Project_3.db;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	
//	login
	// the Binary is for making the password case sensitive 
	@Query(value = "SELECT id FROM customers WHERE email = ?1 AND BINARY password = ?2 ", nativeQuery = true)
	Integer login(String email, String password) ;
	
//the method: delete Coupon Purchase by customer id
	@Transactional // add this for method deleteCustomer in admin service
	@Modifying
	@Query(value = "DELETE FROM customers_coupons WHERE customer_id = ?1", nativeQuery = true)
	void deleteCouponsPurchaseByCustomerId(int customerID);
	
	
	Optional<Customer> findByEmailAndPassword(String email, String password);
	
	


}
