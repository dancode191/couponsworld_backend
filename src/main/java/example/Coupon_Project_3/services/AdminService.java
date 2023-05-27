package example.Coupon_Project_3.services;



import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.beans.Customer;
import example.Coupon_Project_3.db.CompanyRepository;
import example.Coupon_Project_3.db.CouponsRepository;
import example.Coupon_Project_3.db.CustomerRepository;
import example.Coupon_Project_3.exceptions.CompanyAlreadyExistException;
import example.Coupon_Project_3.exceptions.CompanyNotFoundException;
import example.Coupon_Project_3.exceptions.CompanyUpdateFailException;
import example.Coupon_Project_3.exceptions.CustomerExistException;
import example.Coupon_Project_3.exceptions.CustomerNotFoundException;
import example.Coupon_Project_3.exceptions.LogInFailException;

@Service
public class AdminService extends ClientService{


//we get access to the repositories:
public AdminService(CompanyRepository companyRepository, CouponsRepository couponsRepository,
	CustomerRepository customerRepository) {
	super(companyRepository, couponsRepository, customerRepository);

}
	


	
//LOG-IN

	@Override
	public int login(String email, String password) {
		if(email.equalsIgnoreCase("admin@admin.com") && password.equals("admin") ) {
			System.out.println("welcome, you are log in as Admin");
			return 1;
		}
		return -1;
	}
	
//ADD COMPANY
	
	// i change the method to return Company
	public Company addCompany(Company company) throws CompanyAlreadyExistException {
		List<Company> companies = companyRepository.findAll();
		for(Company comp: companies) {
			//check if company name already exist:
			if(comp.getName().equalsIgnoreCase(company.getName()))
				throw new CompanyAlreadyExistException("Add company fail, company name already exist in your data base.. ");
			//check if company email already exist:
			else if(comp.getEmail().equalsIgnoreCase(company.getEmail())){
				throw new CompanyAlreadyExistException("Add company fail, company email already exist in your data base.. ");
			}
		}
		companyRepository.save(company);
		return company;
		
	}

// UPDATE COMPANY
	//change the method to return String
	public String updateCompany(Company company) throws CompanyNotFoundException, CompanyUpdateFailException {
		
		Company updatedCompany = getOneCompany(company.getId());
		//check if company name was changed, cause its not allowed: 
		if(updatedCompany.getName().equalsIgnoreCase(company.getName())) {
			companyRepository.save(company);
			return "company update successfully";
		}else {
			throw new CompanyUpdateFailException("company update fail, company name change not allowed..");
		}
			
	}

// DELETE COMPANY
	
	public boolean deleteCompany(int companyID) {
		if(companyRepository.existsById(companyID)) {
			List<Coupon> companyCoupons = couponsRepository.findCouponsByCompanyId(companyID);
			
			for(int i=0; i<companyCoupons.size(); i++) {
				System.out.println("delte company coupon id: " + companyCoupons.get(i).getId());
				
				//delete first company coupons that was purchase and then delete coupon
				couponsRepository.deleteCouponPurchase(companyCoupons.get(i).getId());
				couponsRepository.deleteById(companyCoupons.get(i).getId());
				
			}
			//delete company 
			companyRepository.deleteById(companyID);
			return true;
		}
		System.out.println("fail to delete company, company id not found..");
		return false;
	}

// GET ALL COMPANIES
	
	public List<Company> getAllCompanies(){ 
		return companyRepository.findAll();
	}

// GET ONE COMPANY
	
	public Company getOneCompany(int companyID) throws CompanyNotFoundException {
		return companyRepository.findById(companyID).orElseThrow(()-> new CompanyNotFoundException("Cant find company by id: "+ companyID));
	}
	
// ADD CUSTOMER
	// i change the method to return Customer
	public Customer AddCustomer(Customer customer) throws CustomerExistException {
		List<Customer> customers = customerRepository.findAll();
		
		for(Customer cust: customers) {
			//making sure customer email not already exist:
			if(cust.getEmail().equalsIgnoreCase(customer.getEmail())) {
				throw new CustomerExistException("fail to add new customer, customer email already exist in your data base..");
			}
		}
		customerRepository.save(customer);
		return customer;
	}

// UPDATE CUSTOMER
	// i change the method to return String
	public String updateCustomer(Customer customer) throws CustomerNotFoundException {
		if(customerRepository.existsById(customer.getId())) {
			customerRepository.save(customer);
			return "customer update successfully";
		}else {
			throw new CustomerNotFoundException("Cant find customer !");
		}
	}

// DELETE CUSTOMER
	
	public boolean deleteCustomer(int customerID) { 
		if(customerRepository.existsById(customerID)) {
			//delete customer purchase coupons and then delete customer:
			customerRepository.deleteCouponsPurchaseByCustomerId(customerID); 
			customerRepository.deleteById(customerID); 
			System.out.println("deleted customer id: " + customerID);
			return true;
		}
		return false;
	}

// GET ALL CUSTOMERS
	
	public List<Customer> getAllCustomer(){
		return customerRepository.findAll();
	}
	
// GET ONE CUSTOMER
	
	public Customer getOneCustomer(int customerID) throws CustomerNotFoundException {
		return customerRepository.findById(customerID).orElseThrow(()->new CustomerNotFoundException("Cant find customer with id:" + customerID));
	}

	
//i add those 2 methods to admin cause a problem i had to display coupons in admin-> company/customer details(client side)
	
// GET ALL COMPANY COUPONS BY ID
	
	public List<Coupon> getAllCompanyCouponsById(int companyID){
		List<Coupon> companyCoupons = couponsRepository.findCouponsByCompanyId(companyID);
		return companyCoupons;
	}
	
// GET ALL CUSTOMER COUPONS BY ID
	public List<Coupon> getAllCustomerCouponsById(int customerID){
		List<Coupon> customerCoupons = couponsRepository.findAllCouponsByCustomerId(customerID);
		return customerCoupons;
	}
	
}
