package example.Coupon_Project_3.DailyJob;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.db.CouponsRepository;
import example.Coupon_Project_3.db.CustomerRepository;
import example.Coupon_Project_3.exceptions.CouponIDNotExist;
import example.Coupon_Project_3.services.CompanyService;
import example.Coupon_Project_3.services.CustomerService;

@Service
public class CouponExpirationDailyJob implements Runnable{
	
	

	CouponsRepository couponsRepository;
	
	//dependency injection for having access to repositories actions
	public CouponExpirationDailyJob(CouponsRepository couponsRepository) {
		
		this.couponsRepository = couponsRepository;
	}


	//a flag for stoping the thread action
	private boolean quit = false;

	
	
	
	
	
	//stop method changing the flag to true and the thread will stop running
	public void stop() {
		System.out.println("stop scaning for expired coupons..");
		this.quit = true;
	}
	
	@Override
	public void run() {
		//while quit = true the thread will run every 24hr checking if there is a coupon with expired date, if yes it will delete
		//it from coupons purchase table and then the coupon itself
		
		while(!quit) {
			Calendar todayDate = Calendar.getInstance();
			List<Coupon> coupons;
//			
			System.out.println("Scaning for expired coupons to delete.. ");
			try {
				coupons = couponsRepository.findAll();
			
				for(Coupon coup: coupons) {
					if(coup.getEndDate().getTime() < todayDate.getTimeInMillis() ) {
						
						couponsRepository.deleteCouponPurchase(coup.getId());
						couponsRepository.deleteById(coup.getId());
					
					}
				}
						
				
				Thread.sleep(1000 * 60 * 60 * 24);
				
			
			//for testing:
				
//				Thread.sleep(1000 * 60);
				
				
			
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			} 
			
			
			
		
		}
		
	}
}
	
	


