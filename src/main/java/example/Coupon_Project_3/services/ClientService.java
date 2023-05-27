package example.Coupon_Project_3.services;

import java.sql.SQLException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.db.CompanyRepository;
import example.Coupon_Project_3.db.CouponsRepository;
import example.Coupon_Project_3.db.CustomerRepository;
import example.Coupon_Project_3.exceptions.LogInFailException;


@Service
public abstract class ClientService {
	
	public CompanyRepository companyRepository;
	public CouponsRepository couponsRepository;
	public CustomerRepository customerRepository;
	
	
//getting access to repositories by dependency injection:
public ClientService(CompanyRepository companyRepository, CouponsRepository couponsRepository,
			CustomerRepository customerRepository) {
		super();
		this.companyRepository = companyRepository;
		this.couponsRepository = couponsRepository;
		this.customerRepository = customerRepository;
	}


	  abstract int login(String email, String password) throws LogInFailException;
	  
}
