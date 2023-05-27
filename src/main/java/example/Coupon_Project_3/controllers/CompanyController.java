package example.Coupon_Project_3.controllers;

import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import example.Coupon_Project_3.beans.Category;
import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Credentials;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.beans.UserSession;
import example.Coupon_Project_3.exceptions.CompanyAlreadyExistException;
import example.Coupon_Project_3.exceptions.CompanyNotFoundException;
import example.Coupon_Project_3.exceptions.CouponExistException;
import example.Coupon_Project_3.exceptions.CouponIDNotExist;
import example.Coupon_Project_3.exceptions.InvalidToken;
import example.Coupon_Project_3.exceptions.ListIsEmpthyException;
import example.Coupon_Project_3.exceptions.LogInFailException;
import example.Coupon_Project_3.exceptions.UserNotFoundException;
import example.Coupon_Project_3.services.CompanyService;
import example.Coupon_Project_3.services.SendEmail;

@RestController
@RequestMapping(path = "/company")
@CrossOrigin
public class CompanyController {

	private CompanyService companyService;
	//give us access to request headers coming from client side
	private HttpServletRequest request;
	@Autowired
	public HashMap<Integer, UserSession> companiesSessions;
	private Company tempCompany; 
	private String companyTmpToken;
	private SendEmail sendEmail;
	
	
public CompanyController(HttpServletRequest request, CompanyService companyService, SendEmail sendEmail) {
		
		this.request = request;
		this.companyService = companyService;
		this.sendEmail = sendEmail;
	}





	@PostMapping(path = "/add")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon){
		try {

			// RequestHandler method to handle client request
			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.addCoupon(coupon));
			
		}catch (CouponExistException e) {
			return ResponseEntity.status(403).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
		
	}
	
	
	@PutMapping(path = "/update")
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon) {
		try {

			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.updateCoupn(coupon));
				
		} catch (CouponIDNotExist e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
		
		
	}
	

	@DeleteMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteCoupon(@PathVariable int id) {
		try {

			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.deleteCoupon(id));
			
		} catch (CouponIDNotExist e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}	


	}
	
	@GetMapping(path = "/one/{couponId}")
	public ResponseEntity<?> getOneCoupon(@PathVariable int couponId) {
		try {

			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.getOneCoupon(couponId)) ;
			
		}catch (CouponIDNotExist e) {
			return ResponseEntity.status(403).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	@GetMapping(path = "/all")
	public ResponseEntity<?> getAllCoupons(){
		try {

			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.getAllCoupons()) ;
			
		}catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}

	}
	
	@GetMapping(path = "/category")
	public ResponseEntity<?> getAllCouponsByCategory(@RequestParam Category category){
		try {
			
			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.getAllCouponsByCategory(category)) ;
			
		}catch (ListIsEmpthyException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	@GetMapping(path = "/maxprice/{maxPrice}")
	public ResponseEntity<?> getAllCouponsByMaxPrice(@PathVariable double maxPrice){
		try {

			CompanyService service = RequestHandler();	
			return ResponseEntity.ok(service.getAllCouponsByMaxPrice(maxPrice)) ;
			
		}catch (ListIsEmpthyException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	
	@GetMapping(path = "/details")
	public ResponseEntity<?> getCompanyDetails(){
		try {
			
			CompanyService service = RequestHandler();
			return ResponseEntity.ok(service.getCompanyDetails()) ;
			
		}catch (CompanyNotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("session not found!");
		}
	}
	
	
//me improving the project:
	@PostMapping(path = "/verify")
	public ResponseEntity<?> userVerification(@RequestBody Company company) {
		try {
			//check if company email already exist
			companyService.verifyCompanyEmail(company.getEmail());

			tempCompany = company;
			companyTmpToken = verifyTempToken(company);
			String companyVerifyURL = "http://localhost:3000/CompanyVerifyPage/"+companyTmpToken;
			//sending the verification mail
			sendEmail.sendEmailVerify(companyVerifyURL, company.getEmail());
			
			return ResponseEntity.ok("pass") ;
		
		}catch (CompanyAlreadyExistException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	
	@PostMapping(path = "/signUp/{token}")
	public ResponseEntity<?> signUp(@PathVariable String token){
		try {
			
			companyService.comparingTokens(token, companyTmpToken);
			return ResponseEntity.ok(companyService.signUp(tempCompany));
			
		} catch (CompanyAlreadyExistException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (InvalidToken e) {
			return ResponseEntity.status(401).body(e.getMessage());
		}finally {
			tempCompany=null;
		}
		
	}
	
	private String verifyTempToken(Company company) {
		String tmpToken = JWT.create()
				.withIssuedAt(new Date())
				.withClaim("name", company.getName())
				.withClaim("email", company.getEmail())
				.sign(Algorithm.HMAC256("lococompanymagiccode"));
		return tmpToken;
	}
	
	
	
	//method to handle request for each method in the this controller:
	public CompanyService RequestHandler() throws UserNotFoundException {
		//i pull the token from the header and removing the word Bearer so ill stay only with the token
		String token = request.getHeader("authorization").replace("Bearer ", "");
		//i decode the token and get from it the id
		int companyId = JWT.decode(token).getClaim("id").asInt();
		// i use the id to find the client session
		UserSession session = companiesSessions.get(companyId);
		
		if(session != null) {
			//if session is not empty we will get the client service, we update lastActive time for user
			CompanyService service = (CompanyService) session.getService();
			session.setLastActive(System.currentTimeMillis());
			//i return the service
			return service;
		}else {
			throw  new UserNotFoundException("session not found");
		}
		
	}
	
	
	
	
	
}
