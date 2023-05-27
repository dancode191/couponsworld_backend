package example.Coupon_Project_3.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.spi.CleanableThreadContextMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.Coupon_Project_3.beans.Category;
import example.Coupon_Project_3.beans.Company;
import example.Coupon_Project_3.beans.Coupon;
import example.Coupon_Project_3.db.CompanyRepository;
import example.Coupon_Project_3.db.CouponsRepository;
import example.Coupon_Project_3.db.CustomerRepository;
import example.Coupon_Project_3.exceptions.CompanyAlreadyExistException;
import example.Coupon_Project_3.exceptions.CompanyNotFoundException;
import example.Coupon_Project_3.exceptions.CouponExistException;
import example.Coupon_Project_3.exceptions.CouponIDNotExist;
import example.Coupon_Project_3.exceptions.InvalidToken;
import example.Coupon_Project_3.exceptions.ListIsEmpthyException;
import example.Coupon_Project_3.exceptions.LogInFailException;
import example.Coupon_Project_3.exceptions.UserNotFoundException;

@Service
public class CompanyService extends ClientService{
	
	private int companyID;

	//getting access to repositories from ClientService
	public CompanyService(CompanyRepository companyRepository, CouponsRepository couponsRepository, CustomerRepository customerRepository) {
		super(companyRepository, couponsRepository, customerRepository);
		
	}
	
	public int getCompanyID() {
		return companyID;
	}



// LOG-IN
	@Override
	public int login(String email, String password) throws LogInFailException {	
		//if company login return null (cause its not exist in DB) it will throw exception
		if((companyRepository.login(email, password)) == null) {	
			throw new LogInFailException("wrong email or password.. please try again");
		}
		//if it pass it return company id:
		companyID = companyRepository.login(email, password);
		System.out.println("welcome company id: "+ companyID );
		return companyID;
	}

	
// GET USER
	public Company getUser(String email, String password) throws UserNotFoundException{
		return companyRepository.findByEmailAndPassword(email, password).orElseThrow(()-> new UserNotFoundException("user not found !"));
	}
	


// ADD COUPON
	public Coupon addCoupon(Coupon coupon) throws CouponExistException {

		List<Coupon> companyCoupons = couponsRepository.findCouponsByCompanyId(companyID);
		
		for(Coupon coup : companyCoupons) {
			//check if coupon title already exist in company coupons list:
			if(coup.getTitle().equalsIgnoreCase(coupon.getTitle())) {
				throw new CouponExistException("coupon with title: "+coupon.getTitle()+" already exist in your company coupons list");
			}
		}
		
		 couponsRepository.save(coupon);
		 return coupon;
	 }
	
	
// UPDATE COUPON	
	public Coupon updateCoupn(Coupon coupon) throws CouponIDNotExist {
		List<Coupon> companyCoupons = couponsRepository.findCouponsByCompanyId(companyID);
		
		for(Coupon coup : companyCoupons) {
			if(coup.getId() == coupon.getId()) {
				
				couponsRepository.save(coupon);
				
				return coupon;			
			}
		}

		throw new CouponIDNotExist("coupon ID not exist in your company coupons");
	}
	
	
// DELETE COUPON 
	public String deleteCoupon(int couponID) throws CouponIDNotExist {
		List<Coupon> companyCoupons = couponsRepository.findCouponsByCompanyId(companyID); 
		
		for(Coupon coup : companyCoupons) {
			if(coup.getId() == couponID) {
			// we delete coupon purchase history first and then the coupon itself:
			couponsRepository.deleteCouponPurchase(couponID);
			couponsRepository.deleteById(couponID);
				System.out.println("coupon id: "+couponID+" delted!!");
				return "coupon deleted successfully";				
			}
		}
		throw new CouponIDNotExist("coupon ID not exist in your company coupons");
	}
	
	
// GET ONE COUPON
	public Coupon getOneCoupon(int couponID) throws CouponIDNotExist {
		return couponsRepository.findById(couponID).orElseThrow(()-> new CouponIDNotExist("Cant find coupon id: " + couponID));
	}
	
	
// GET ALL COUPONS
	public List<Coupon> getAllCoupons(){
		List<Coupon> companyCoupons = couponsRepository.findCouponsByCompanyId(companyID);
		return companyCoupons;
	}
	
	
// GET ALL COUPONS BY CATEGORY
	public List<Coupon> getAllCouponsByCategory(Category category) throws ListIsEmpthyException{

		List<Coupon> companyCoupons = couponsRepository.findByCompanyIdAndCategory(companyID, category.ordinal());
		//check if company coupons list by a specific category is empty
		if(companyCoupons.size() >=1) 
			return companyCoupons;
		
		else
			throw new ListIsEmpthyException("no results found, no coupon found from category: " + category);
	}
	
	
// GET ALL COUPONS BY MAX PRICE      
	public List<Coupon> getAllCouponsByMaxPrice(double maxPrice) throws ListIsEmpthyException{

		List<Coupon> companyCoupons = couponsRepository.findByCompanyIdAndMaxPrice(companyID, maxPrice);
		//check if company coupons list by max price is empty
		if(companyCoupons.size() >=1) 
			return companyCoupons;
		else
			throw new ListIsEmpthyException("no results found, no coupon found under price: " + maxPrice + "$");
	}
	
	
// GET COMPANY DETAILS
	public Company getCompanyDetails() throws CompanyNotFoundException {
		 Company companyDetails = companyRepository.findById(companyID).orElseThrow(()-> new CompanyNotFoundException("Cant find company by id: "+ companyID));
		 return companyDetails;
	}
	
//i add away for customer to sign-up
	public Company signUp(Company company) throws CompanyAlreadyExistException {
		//maybe this email verify check is not needed cause we already check it in the verify step..
		verifyCompanyEmail(company.getEmail());
		
		companyRepository.save(company);
		return company;
	}
	
	public void verifyCompanyEmail(String email) throws CompanyAlreadyExistException {
		List<Company> companies = companyRepository.findAll();
		
		for(Company comp: companies) {
			if(comp.getEmail().equalsIgnoreCase(email)) {
				throw new CompanyAlreadyExistException("fail to add new company, company email already exist in our data base..");
			}
		}
	}
	
	public void comparingTokens(String token, String companyTmpToken) throws InvalidToken {
		if(token.equals(companyTmpToken)) {
			System.out.println("tokens are match!!");
		}else {
			throw new InvalidToken("invalid token..");
		}
	}

}
