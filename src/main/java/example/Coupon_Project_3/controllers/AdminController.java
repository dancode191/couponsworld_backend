package example.Coupon_Project_3.controllers;

import java.util.List;

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

import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Credentials;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.exceptions.CompanyAlreadyExistException;
import example.Coupon_Project_3.exceptions.CompanyNotFoundException;
import example.Coupon_Project_3.exceptions.CompanyUpdateFailException;
import example.Coupon_Project_3.exceptions.CustomerExistException;
import example.Coupon_Project_3.exceptions.CustomerNotFoundException;
import example.Coupon_Project_3.loginManager.LoginManager;
import example.Coupon_Project_3.services.AdminService;

@RestController
@RequestMapping(path = "/admin")
@CrossOrigin 
public class AdminController {

	private AdminService adminService;
	private LoginManager loginManager;

	
	public AdminController(AdminService adminService, LoginManager loginManager) {
		
		this.adminService = adminService;
		this.loginManager = loginManager;
	}
	
	
	
	
	
	@PostMapping(path = "/company")
	public ResponseEntity<?> addCompany (@RequestBody Company company) { 
		
			try {
				return ResponseEntity.ok(adminService.addCompany(company));
			} catch (CompanyAlreadyExistException e) {
				return ResponseEntity.status(400).body(e.getMessage()); 
			}
	}
	
	@PutMapping(path = "/updatecompany")
	public ResponseEntity<String> updateCompany(@RequestBody Company company) {
		try {
			return ResponseEntity.ok(adminService.updateCompany(company)) ;
		} catch (CompanyNotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		} catch (CompanyUpdateFailException e) {
			return ResponseEntity.status(406).body(e.getMessage());
		}
	}
	
	@DeleteMapping(path = "/deletecompany/{id}")
	public boolean deleteCompany(@PathVariable int id) {
		return adminService.deleteCompany(id);
	}
	
	@GetMapping(path = "allcompanies")
	public List<Company> getAllCompanies(){
		
		return adminService.getAllCompanies();
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<?> getOneCompany(@PathVariable int id) {
		try {
			
			return ResponseEntity.ok(adminService.getOneCompany(id));
		} catch (CompanyNotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	@PostMapping(path = "/customer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
		try {
			return ResponseEntity.ok(adminService.AddCustomer(customer));
		} catch (CustomerExistException e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
	}
	
	@PutMapping(path = "/updatecustomer")
	public ResponseEntity<String> updateCustomer(@RequestBody Customer customer){
		try {
			return ResponseEntity.ok(adminService.updateCustomer(customer));
		} catch (CustomerNotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	@DeleteMapping(path = "/deletecustomer/{id}")
	public boolean deleteCustomer(@PathVariable int id) {
		return adminService.deleteCustomer(id);
	}
	
	@GetMapping(path = "/allcustomers")
	public List<Customer> getAllCustomers(){
		return adminService.getAllCustomer();
	}
	
	@GetMapping(path = "/onecustomer/{id}")
	public ResponseEntity<?> getOneCustomer(@PathVariable int id) {
		try {
			return ResponseEntity.ok(adminService.getOneCustomer(id));
		} catch (CustomerNotFoundException e) {
			return ResponseEntity.status(404).body(e.getMessage());
		}
	}
	
	
//i add those 2 methods to admin cause a problem i had to display coupons in admin-> company/customer details(client side)
	@GetMapping(path = "/allcompanycoupons/{id}")
	public List<Coupon> getAllCompanyCouponsById(@PathVariable int id){
		return adminService.getAllCompanyCouponsById(id);
	}
	
	@GetMapping(path = "/allcustomercoupons/{id}")
	public List<Coupon> getAllCustomerCouponsById(@PathVariable int id){
		return adminService.getAllCustomerCouponsById(id);
	}
	
	
	
	
	
	
	
	
	
}
