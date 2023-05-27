package example.Coupon_Project_3.beans;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity
@Table(name = "coupons")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	private Company company;
	private int amount;
	@Column(name = "category")
	private Category category;
	private String title;
	private String description;
	private String image;
	private Date startDate; 
	private Date endDate; 
	private double price;
	
	//constructor
	
	public Coupon() {}
	
	public Coupon(int id, Company company, Category category, String title, String description,
				Date startDate, Date endDate, int amount, double price, String image) {
		
		this.id = id;
		this.company = company;
		this.category = category;
		this.title = title;
		this.description = description;
		this.startDate = startDate;
		this.endDate = endDate;
		this.amount = amount;
		this.price = price;
		this.image = image;
	}
	
		
	public Coupon(Company company, Category category, String title, String description,
				Date startDate, Date endDate, int amount, double price, String image) {
		
			this.company = company;
			this.category = category;
			this.title = title;
			this.description = description;
			this.startDate = startDate;
			this.endDate = endDate;
			this.amount = amount;
			this.price = price;
			this.image = image;
	}

	//getters setters
	public int getId() {
		return id;
	}


	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) { // add this set caues maybe it will solve my problem..
		this.company = company;
	}
	
	public void setAmount(int num) {
		this.amount = num;
	}


	public int getAmount() {
		return amount;
	}


	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}


	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public Date getStartDate() {
		return startDate;
	}


	public Date getEndDate() {
		return endDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


//	company : "+company.getId()+
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		return "< id: "+id+"   company : "+company.getId()+"   Category: "+category+"   Title: "+title+"   Description: "+description+
				"   Start Date: "+sdf.format(startDate)+"   End Date: "+sdf.format(endDate)+"   Amount: "+amount+"   Price: "+price+"   Image: "+ image + "> \n";
	}
}
