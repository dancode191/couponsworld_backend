package example.Coupon_Project_3.controllers;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import example.Coupon_Project_3.DailyJob.CouponExpirationDailyJob;
import example.Coupon_Project_3.accountCleanerDailyjob.AccountCleanerDailyJob;
import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Credentials;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.beans.UserSession;
import example.Coupon_Project_3.exceptions.LogInFailException;
import example.Coupon_Project_3.exceptions.UserNotFoundException;
import example.Coupon_Project_3.loginManager.LoginManager;
import example.Coupon_Project_3.services.AdminService;
import example.Coupon_Project_3.services.ClientService;
import example.Coupon_Project_3.services.CompanyService;
import example.Coupon_Project_3.services.CustomerService;

@RestController
@RequestMapping(path = "/auth")
@CrossOrigin
public class LoginController {

	
	private LoginManager loginManager;
	private HashMap<Integer, UserSession> companiesSessions;
	private HashMap<Integer, UserSession> customersSessions;
//	private HttpServletRequest request; // for logout method to get info from token and delete relevant session
	
	
	public LoginController(LoginManager loginManager, HashMap<Integer, UserSession> companiesSessions, HashMap<Integer, UserSession> customersSessions) {
		
		this.loginManager = loginManager;
		this.companiesSessions = companiesSessions;
		this.customersSessions = customersSessions;
//		this.request = request;
	}
	
	//this for when client logout its stop the threads activity
//	@Autowired
//	private final ApplicationContext applicationContext = null;
	
	
	
	
	@PostMapping(path = "/login")
	public ResponseEntity<?> login(@RequestBody Credentials cred) {
		try {
			ClientService service = loginManager.login(cred.getEmail(), cred.getPassword(), cred.getType());
			int id =0;
			
			if(service instanceof AdminService) {
		
				return ResponseEntity.ok(adminCreateToken()) ;
				
			}
			
			if(service instanceof CompanyService) {
				id = ((CompanyService) service).getCompanyID();
				//save id,srevice and lastActive  in hashmap:
				companiesSessions.put(id, new UserSession(service, System.currentTimeMillis()));
				System.out.println("checking login controller id and service: " + id + " / " + service);
				Company company = ((CompanyService) service).getUser(cred.getEmail(), cred.getPassword());

				return ResponseEntity.ok(companyCreateToken(company));
				
			}else if(service instanceof CustomerService) {
				id = ((CustomerService) service).getCustomerID();
				
				customersSessions.put(id, new UserSession(service, System.currentTimeMillis()));
				System.out.println("checking login controller id and service: " + id + " / " + service);
				Customer customer = ((CustomerService) service).getUser(cred.getEmail(), cred.getPassword());
				
				return ResponseEntity.ok(customerCreateToken(customer));
	
			}
			
			
			return null; 
			
		} catch (LogInFailException | UserNotFoundException e) {
			
			return ResponseEntity.status(404).body(e.getMessage());
		} 
	}
	

	// methods to create token for client:
	
	private String adminCreateToken() {
		String token = JWT.create()
				.withIssuedAt(new Date())
				.withClaim("name","admin")
				.withClaim("email", "admin@admin.com")
				.withClaim("type", "Admin")
				.sign(Algorithm.HMAC256("akonamatata"));
		return token;
	}
	//the method get the client type object, from the obj we take relevant info to create the token, we also add time of issued
	//and we give a unique sign to create a unique token for this application users
	private String companyCreateToken(Company company) {
		String token = JWT.create()
				.withIssuedAt(new Date())
				.withClaim("id", company.getId())
				.withClaim("name", company.getName())
				.withClaim("email", company.getEmail())
				.withClaim("type", "Company")
				.sign(Algorithm.HMAC256("akonamatata"));
		
		return token;
	}
	
	private String customerCreateToken(Customer customer) {
		String token = JWT.create()
				.withIssuedAt(new Date())
				.withClaim("id", customer.getId())
				.withClaim("firstName", customer.getFirstName())
				.withClaim("lastName", customer.getLastName())
				.withClaim("email", customer.getEmail())
				.withClaim("type", "Customer")
				.sign(Algorithm.HMAC256("akonamatata"));
		
		return token;
	}
}
