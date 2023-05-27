package example.Coupon_Project_3.beans;

import example.Coupon_Project_3.loginManager.ClientType;

public class Credentials {
	
	private String email;
	private String password;
	private ClientType type;
	
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ClientType getType() {
		return type;
	}
	public void setType(ClientType type) {
		this.type = type;
	}
	
	

}
