package example.Coupon_Project_3.controllers;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import example.Coupon_Project_3.beans.Category;
import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.beans.UserSession;
import example.Coupon_Project_3.exceptions.CouponExistException;
import example.Coupon_Project_3.exceptions.CouponExpiredException;
import example.Coupon_Project_3.exceptions.CouponNotInStockException;
import example.Coupon_Project_3.exceptions.CustomerExistException;
import example.Coupon_Project_3.exceptions.CustomerNotFoundException;
import example.Coupon_Project_3.exceptions.ListIsEmpthyException;
import example.Coupon_Project_3.exceptions.UserNotFoundException;
import example.Coupon_Project_3.services.CompanyService;
import example.Coupon_Project_3.services.CustomerService;
import example.Coupon_Project_3.services.SendEmail;

@RestController
@RequestMapping(path = "/customer")
@CrossOrigin
public class CustomerController {

	private CustomerService customerService;
	private HttpServletRequest request;
	@Autowired
	public HashMap<Integer, UserSession> customersSessions;
	private Customer tempCustomer; 
	private String customerTmpToken;
	private SendEmail sendEmail;
	
	public CustomerController(HttpServletRequest request, CustomerService customerService, SendEmail sendEmail) {
		
		this.request = request;
		this.customerService = customerService;
		this.sendEmail = sendEmail;
	}
	

	
	@PostMapping(path = "/purchasecoupon")
	public ResponseEntity<?> PurchaseCoupon(@RequestBody Coupon coupon) {
		try {
			// i call request handler method to get client service  
			CustomerService service = RequestHandler();
			return ResponseEntity.ok(service.PurchaseCoupon(coupon));

		}catch (CouponExistException | CouponNotInStockException | CouponExpiredException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
		
	}
	
	@GetMapping(path = "/all")
	public ResponseEntity<?> getAllCoupons(){
		try {

			CustomerService service = RequestHandler();
			return ResponseEntity.ok(service.getAllCoupons());

		}catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	@GetMapping(path = "/allcustomercoupons")
	public ResponseEntity<?> getCustomerCoupons(){
		try {

			CustomerService service = RequestHandler();
			return ResponseEntity.ok(service.getCustomerCoupons());

		}catch (ListIsEmpthyException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	@GetMapping(path = "/category/{category}")
	public ResponseEntity<?> getCustomerCouponsByCategory(@PathVariable Category category){
		try {

			CustomerService service = RequestHandler();
			return ResponseEntity.ok(service.getCustomerCouponsByCategory(category));

		}catch (ListIsEmpthyException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	@GetMapping(path = "/maxprice/{maxPrice}")
	public ResponseEntity<?> getCustomerCouponsByMaxPrice(@PathVariable double maxPrice){
		try {

			CustomerService service = RequestHandler();
			return ResponseEntity.ok(service.getCustomerCouponsByMaxPrice(maxPrice));
		
		}catch (ListIsEmpthyException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	@GetMapping(path = "/details")
	public ResponseEntity<?> getCustomerDetails(){
		try {

			CustomerService service = RequestHandler();
			return ResponseEntity.ok(service.getCustomerDetails());

		}catch (CustomerNotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
		
	}
	
//me improving the project:
	
	@PostMapping(path = "/verify")
	public ResponseEntity<?> userVerification(@RequestBody Customer customer) {
		try {
			customerService.verifyCustomerEmail(customer.getEmail());

			tempCustomer = customer;
			customerTmpToken = verifyTempToken(customer);
			String customerVerifyURL = "http://localhost:3000/CustomerVerifyPage/"+customerTmpToken;
			sendEmail.sendEmailVerify(customerVerifyURL, customer.getEmail());
			
			return ResponseEntity.ok("pass");
		}catch (CustomerExistException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
		
		
		
	}
	
	
//add away for customer to signup and add himself to DB after his account was verified
		@PostMapping(path = "/signUp/{token}")
		public ResponseEntity<?> signUp(@PathVariable String token){	
			try {
				
				customerService.comparingTokens(token, customerTmpToken);
				return ResponseEntity.ok(customerService.signUp(tempCustomer));
				
			}catch (CustomerExistException e) {
				return ResponseEntity.status(404).body(e.getMessage());
			}catch (UserNotFoundException e) {
				return ResponseEntity.status(401).body(e.getMessage());
			}finally {
				tempCustomer=null;
				customerTmpToken = "";
			}
		}
		
		private String verifyTempToken(Customer customer) {
			String tmpToken = JWT.create()
					.withIssuedAt(new Date())
					.withClaim("firstName", customer.getFirstName())
					.withClaim("lastName", customer.getLastName())
					.withClaim("email", customer.getEmail())
					.sign(Algorithm.HMAC256("lococustomermagiccode"));
			return tmpToken;
		}
	
	
			
//method to handle reuqest for each method in the this controller:
		public CustomerService RequestHandler() throws UserNotFoundException {
			
			// we get the token from the header request, from that token we get the customer id, and we use that id to get
			//the customer session, which give us access to customer service we will use for customer actions, and for 
			//customer last time active
			
			String token = request.getHeader("authorization").replace("Bearer ", "");
			
			int customerId = JWT.decode(token).getClaim("id").asInt();
			
			UserSession session = customersSessions.get(customerId);
			
			if(session != null) {
				// if sessions is not null we get the custoemr service (after convertion) and update the last time active value
				//then we use it for using the PurchaseCoupon method in customer service
				CustomerService service = (CustomerService) session.getService();
				session.setLastActive(System.currentTimeMillis());
				return service;
			}else {
				throw  new UserNotFoundException("session not found");
			}
			
		}
		

	

	
	
}
