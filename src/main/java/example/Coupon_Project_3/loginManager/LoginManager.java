package example.Coupon_Project_3.loginManager;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import example.Coupon_Project_3.exceptions.LogInFailException;
import example.Coupon_Project_3.services.AdminService;
import example.Coupon_Project_3.services.ClientService;
import example.Coupon_Project_3.services.CompanyService;
import example.Coupon_Project_3.services.CustomerService;

@Service
public class LoginManager {
	
	//dependency injection with autowired:
	@Autowired
	private final ApplicationContext applicationContext = null;

	//we calling the login method,according to the client type it will go and check in the relevant DB table if email and password
	//exist, if its exist it will return a service object according to the client type and allow him access to the service methods
	//if not match found it will throw exception
	
	public ClientService login(String email, String password, ClientType clientType ) throws LogInFailException {
		
		switch (clientType) {
		
		case ADMIN:
			AdminService adminService = applicationContext.getBean(AdminService.class);
			if(adminService.login(email, password) == -1) 
				throw new LogInFailException("wrong email or password.. please try again"); 
			
			return adminService;
			
		case COMPANY:
			CompanyService companyService = applicationContext.getBean(CompanyService.class);
			
			if(companyService.login(email, password) >= 1) 
				return companyService;
			
			throw new LogInFailException("wrong email or password.. please try again"); 
			

		case CUSTOMER:
			CustomerService customerService = applicationContext.getBean(CustomerService.class);
			if(customerService.login(email, password) >= 1) 
				return customerService;
			
			throw new LogInFailException("wrong email or password.. please try again"); 
				
			

		default:
			return null;
		}
	}
	
	
	
}
