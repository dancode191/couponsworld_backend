package example.Coupon_Project_3;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Properties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import example.Coupon_Project_3.CouponProject3Application;
import example.Coupon_Project_3.DailyJob.CouponExpirationDailyJob;
import example.Coupon_Project_3.accountCleanerDailyjob.AccountCleanerDailyJob;
import example.Coupon_Project_3.beans.Category;
import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.beans.UserSession;
import example.Coupon_Project_3.db.CompanyRepository;
import example.Coupon_Project_3.exceptions.CompanyAlreadyExistException;
import example.Coupon_Project_3.exceptions.CompanyNotFoundException;
import example.Coupon_Project_3.exceptions.CompanyUpdateFailException;
import example.Coupon_Project_3.exceptions.CouponExistException;
import example.Coupon_Project_3.exceptions.CouponExpiredException;
import example.Coupon_Project_3.exceptions.CouponIDNotExist;
import example.Coupon_Project_3.exceptions.CouponNotInStockException;
import example.Coupon_Project_3.exceptions.CustomerExistException;
import example.Coupon_Project_3.exceptions.CustomerNotFoundException;
import example.Coupon_Project_3.exceptions.ListIsEmpthyException;
import example.Coupon_Project_3.exceptions.LogInFailException;
import example.Coupon_Project_3.loginManager.ClientType;
import example.Coupon_Project_3.loginManager.LoginManager;
import example.Coupon_Project_3.services.AdminService;
import example.Coupon_Project_3.services.CompanyService;
import example.Coupon_Project_3.services.CustomerService;



@SpringBootApplication
public class CouponProject3Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(CouponProject3Application.class, args);

	//  ###### Project 3 - Daniel Malka ######
		 
		 
		 
		// calling daily tread - deleting expried coupons
				 
//					CouponExpirationDailyJob dailyJob = ctx.getBean(CouponExpirationDailyJob.class);
//					Thread activeDailyJob = new Thread(dailyJob);
//					activeDailyJob.start();

//		 calling daily tread - deleting sleeping accounts
//					AccountCleanerDailyJob cleanSleepingAccounts = ctx.getBean(AccountCleanerDailyJob.class);
//					Thread activeCleanAccounts = new Thread(cleanSleepingAccounts);
//					activeCleanAccounts.start();
					
		
		


				
				 
		// Stoping daily tread job - deleting expried coupons:
							
//				 CouponExpirationDailyJob stopDailyJob = ctx.getBean(CouponExpirationDailyJob.class);
//				 stopDailyJob.stop();
		
		// Stoping daily tread job - deleting sleeping accounts:
//					AccountCleanerDailyJob stopCleaningAccounts = ctx.getBean(AccountCleanerDailyJob.class);
//					stopCleaningAccounts.stop();
		
		
		
		
	}
	
	
	

	
	
	@Bean 
	public HashMap<Integer, UserSession> sessions(){
		return new HashMap<Integer, UserSession>(); 
	}

}
