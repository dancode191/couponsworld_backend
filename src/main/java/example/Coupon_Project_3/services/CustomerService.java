package example.Coupon_Project_3.services;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.springframework.stereotype.Service;

import example.Coupon_Project_3.beans.Category;
import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.db.CompanyRepository;
import example.Coupon_Project_3.db.CouponsRepository;
import example.Coupon_Project_3.db.CustomerRepository;
import example.Coupon_Project_3.exceptions.CompanyAlreadyExistException;
import example.Coupon_Project_3.exceptions.CouponExistException;
import example.Coupon_Project_3.exceptions.CouponExpiredException;
import example.Coupon_Project_3.exceptions.CouponIDNotExist;
import example.Coupon_Project_3.exceptions.CouponNotInStockException;
import example.Coupon_Project_3.exceptions.CustomerExistException;
import example.Coupon_Project_3.exceptions.CustomerNotFoundException;
import example.Coupon_Project_3.exceptions.ListIsEmpthyException;
import example.Coupon_Project_3.exceptions.LogInFailException;
import example.Coupon_Project_3.exceptions.UserNotFoundException;

@Service
public class CustomerService extends ClientService{
	
	
	private int customerID;

	//getting access to repositories from ClientService
	public CustomerService(CompanyRepository companyRepository, CouponsRepository couponsRepository, CustomerRepository customerRepository) {
		super(companyRepository, couponsRepository, customerRepository);
		
	}

	
	public int getCustomerID() {
		return customerID;
	}
	
	
	
	
	
// LOG-IN
	@Override
	public int login(String email, String password) throws LogInFailException {
		
		//if customer login return null (cause its not exist in DB) it will throw exception
		if((customerRepository.login(email, password)) == null)	{
			throw new LogInFailException("wrong email or password.. please try again");
		}
		//if it pass it return customer id:
		customerID = customerRepository.login(email, password);
		System.out.println("welcome customer id: "+ customerID );
		return customerID;	
	}

	
// GET USER
	public Customer getUser(String email, String password) throws UserNotFoundException {
		return customerRepository.findByEmailAndPassword(email, password).orElseThrow(()-> new UserNotFoundException("user not found !"));
	}
	
	
// PURCHAS COUPON
	public String PurchaseCoupon(Coupon coupon) throws CouponExistException, CouponNotInStockException, CouponExpiredException {
		Calendar todayDate = Calendar.getInstance();
		List<Coupon>  customerCoupons = couponsRepository.findAllCouponsByCustomerId(customerID);
		
		//check if coupon client was already exist in his purchase list:
		for(Coupon coupID: customerCoupons) {
			if(coupID.getId() == coupon.getId()) {
				throw new CouponExistException ("coupon with id: "+coupID.getId()+" already exist in your coupons purchase list");
			}
		}
		// check if coupon amount is not 0 and also check if coupon date is expired:
		if(coupon.getAmount() < 1) {
			throw new CouponNotInStockException("coupon is out of stock");
		}else if(coupon.getEndDate().getTime() < todayDate.getTimeInMillis()) {
			throw new CouponExpiredException("Coupon date is expired");
		}
		//add the purchase coupon to the purchase table, and update the coupon amount:
		addCouponPurchase(customerID, coupon.getId());
		coupon.setAmount(coupon.getAmount()-1);
		couponsRepository.save(coupon);
		return "coupon was purchase successfuly";

	}
	
	
// GET ALL COUPONS:
	public List<Coupon> getAllCoupons(){
		return couponsRepository.findAll();
	}
	
	
// GET CUSTOMER COUPONS:
	public List<Coupon> getCustomerCoupons() throws ListIsEmpthyException{  
		
		List<Coupon> customerCoupons = couponsRepository.findAllCouponsByCustomerId(customerID);
		//check if customer coupons list is empty	
		if(customerCoupons.size() >=1) 
			return customerCoupons;
		
		else
			throw new ListIsEmpthyException("no results found, no coupons purchased yet..");
		
	}
	
	
// GET CUSTOMER COUPONS BY Category:
	public List<Coupon> getCustomerCouponsByCategory(Category category) throws ListIsEmpthyException{  
		
		List<Coupon> customerCoupons = couponsRepository.findAllCustomerCouponsByIdAndCategory(customerID, category.ordinal());
		//check if customer coupons list by a specific category is empty	
		if(customerCoupons.size() >=1) 
			return customerCoupons;
		
		else
			throw new ListIsEmpthyException("no results found, no coupon found from category: " + category);
	}
	
	
// GET CUSTOMER COUPONS BY MAX PRICE:
	public List<Coupon> getCustomerCouponsByMaxPrice(double maxPrice) throws ListIsEmpthyException {  
		List<Coupon> customerCoupons = couponsRepository.findAllCustomerCouponsByIdAndMaxPrice(customerID, maxPrice);
		//check if customer coupons list by max price is empty
		if(customerCoupons.size() >=1) 
			return customerCoupons;
		
		else
			throw new ListIsEmpthyException("no results found, no coupon found under max price: " + maxPrice + "$");
	}
	
	
// GET CUSTOMER DETAILS:
	public Customer getCustomerDetails() throws CustomerNotFoundException {
		return customerRepository.findById(customerID).orElseThrow(()-> new CustomerNotFoundException("Cant find customer by id: " + customerID));
	}
	
	
	public Coupon getOneCouponByID(int couponID) throws CouponIDNotExist {
		Coupon coupon = couponsRepository.findById(couponID).orElseThrow(()-> new CouponIDNotExist("coupon ID not exist in your company coupons"));
		return coupon;
	}
	
	public void addCouponPurchase(int customerID, int couponID) {
		couponsRepository.addCouponPurchase(customerID, couponID);
	}
	
//me improving the project:

//add away for customer to sign-up
	public Customer signUp(Customer customer) throws CustomerExistException {
		
		verifyCustomerEmail(customer.getEmail());
		customerRepository.save(customer);
		return customer;
	}
	
	public void verifyCustomerEmail(String email) throws CustomerExistException {
		List<Customer> customers = customerRepository.findAll();
		
		for(Customer cust: customers) {
			if(cust.getEmail().equalsIgnoreCase(email)) {
				throw new CustomerExistException("fail to add new customer, customer email already exist in our data base..");
			}
		}
	}
	
	//check if the token created in the backend is same as token sent to frontend for email verifying
	public void comparingTokens(String token, String customerTmpToken) throws UserNotFoundException {
		
		if(token.equals(customerTmpToken)) {
			System.out.println("tokens are match");
		}else {
			throw new UserNotFoundException("token is not valid !!!");
		}
	}
	

}
