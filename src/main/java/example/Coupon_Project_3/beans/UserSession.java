package example.Coupon_Project_3.beans;

import example.Coupon_Project_3.services.ClientService;

public class UserSession {

	
	private ClientService service;
	private long lastActive;
	
	
	public UserSession(ClientService service, long lastActive) {
		
		this.service = service;
		this.lastActive = lastActive;
	}


	public ClientService getService() {
		return service;
	}


	public void setService(ClientService service) {
		this.service = service;
	}


	public long getLastActive() {
		return lastActive;
	}


	public void setLastActive(long lastActive) {
		this.lastActive = lastActive;
	}
	
	
	
}
