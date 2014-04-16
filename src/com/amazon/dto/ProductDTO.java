package com.amazon.dto;

public class ProductDTO {

	private int identifier;
	private String productName;
	private String brand;
	private String description;
	private double price;
	private int quantity;
	private int categoryId;
	private String productOwnerName;
	private String sellerInformation;
	private int productOwnerId;

	public ProductDTO() {

	}

	public ProductDTO(String productName, String brand, String description, double price, int quantity, int categoryId) {
		this.productName = productName;
		this.brand = brand;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
		this.categoryId = categoryId;
	}

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getProductOwnerName() {
		return productOwnerName;
	}

	public void setProductOwnerName(String productOwnerName) {
		this.productOwnerName = productOwnerName;
	}

	public String getSellerInformation() {
		return sellerInformation;
	}

	public void setSellerInformation(String sellerInformation) {
		this.sellerInformation = sellerInformation;
	}

	public int getProductOwnerId() {
		return productOwnerId;
	}

	public void setProductOwnerId(int productOwnerId) {
		this.productOwnerId = productOwnerId;
	}


}
