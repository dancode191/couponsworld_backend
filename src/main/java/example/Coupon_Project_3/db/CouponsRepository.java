package example.Coupon_Project_3.db;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import example.Coupon_Project_3.beans.Category;
import example.Coupon_Project_3.beans.Coupon;

@Transactional
public interface CouponsRepository extends JpaRepository<Coupon, Integer>{
	
	

	
//the method: getCouponsByCompanyID
		@Query(value = "SELECT * FROM coupons WHERE company_id = ?1", nativeQuery = true)
		List<Coupon> findCouponsByCompanyId(int companyID);
		
//the method: getCouponsByCompanyIdAndCategory
		@Query(value = "SELECT * FROM coupons WHERE company_id = ?1 AND category = ?2", nativeQuery = true)
		List<Coupon> findByCompanyIdAndCategory(int companyID, int category);
		
//the method: getCouponsByCompanyIdAndMaxPrice
		@Query(value = "SELECT * FROM coupons WHERE company_id = ?1 AND price <= ?2", nativeQuery = true)
		List<Coupon> findByCompanyIdAndMaxPrice(int companyID, double maxPrice);		
		

		
		
// my methods:		
			
		
//the method: get All Coupons By Customer Id
		@Query(value = "SELECT * FROM coupons JOIN customers_coupons ON customers_coupons.coupons_id = coupons.id "
				+ "WHERE customer_id = ?1", nativeQuery = true)
		List<Coupon> findAllCouponsByCustomerId(int customerID);	
		
	
//the method: add Coupon Purchase
		@Modifying
		@Query(value = "INSERT into customers_coupons values(?1 , ?2)", nativeQuery = true)
		void addCouponPurchase(int customerID, int couponID);

//the method: delete Coupon Purchase
		@Modifying
		@Query(value = "DELETE FROM customers_coupons WHERE coupons_id = ?1", nativeQuery = true)
		void deleteCouponPurchase(int couponID);
		
//the method: find All Customer Coupons By Id And Category
		@Query(value = "SELECT * FROM coupons JOIN customers_coupons ON customers_coupons.coupons_id = coupons.id "
				+ "WHERE customer_id = ?1 AND category = ?2", nativeQuery = true)
		List<Coupon> findAllCustomerCouponsByIdAndCategory(int customerID, int category);	
		
//the method: find All Customer Coupons By Id And Max Price
		@Query(value = "SELECT * FROM coupons JOIN customers_coupons ON customers_coupons.coupons_id = coupons.id "
				+ "WHERE customer_id = ?1 AND price <= ?2", nativeQuery = true)
		List<Coupon> findAllCustomerCouponsByIdAndMaxPrice(int customerID, double maxPrice);
		
		
		

}
