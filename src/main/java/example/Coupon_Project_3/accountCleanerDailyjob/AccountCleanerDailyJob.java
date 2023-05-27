package example.Coupon_Project_3.accountCleanerDailyjob;

import java.util.Calendar;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Coupon_Project_3.beans.UserSession;


@Service
public class AccountCleanerDailyJob implements Runnable{
	
	@Autowired
	public HashMap<Integer, UserSession> companiesSessions;
	@Autowired
	public HashMap<Integer, UserSession> customersSessions;
	
	private boolean quit = false;
	
	
	
	public void stop() {
		System.out.println("stop scaning for sleeping accounts..");
		this.quit = true;
	}
	
	
	@Override
	public void run() {

		while(!quit) {
			Calendar currentTime = Calendar.getInstance();
			
			System.out.println("Scaning for sleeping accounts to remove.. ");
			try {
				
				
			for(Integer Key : companiesSessions.keySet()) {
//				30 min = 1800000 miliseconds
//				5min = 300000 miliseconds
//				2min = 120000 miliseconds
				//i check if more then 30 min pass since last user active
				if((currentTime.getTimeInMillis())-(companiesSessions.get(Key).getLastActive()) > 1800000) {
					System.out.println("deleteing companiesSessions from hashmap: " + companiesSessions.get(Key));
					companiesSessions.remove(Key);
					
				}
			}
			// i do the same for customer sessions
			for(Integer Key : customersSessions.keySet()) {
				if((currentTime.getTimeInMillis())- (customersSessions.get(Key).getLastActive()) > 1800000) {
					System.out.println("deleteing customersSessions from hashmap: " + customersSessions.get(Key));
					customersSessions.remove(Key);
					
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
