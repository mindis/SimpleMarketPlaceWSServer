package com.amazon.dto;


public class ShoppingCartDTO {
	private UserDTO user;
	private ProductDTO[] products;
	private String orderStatus;

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public ProductDTO[] getProducts() {
		return products;
	}

	public void setProducts(ProductDTO[] products) {
		this.products = products;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


			
}
