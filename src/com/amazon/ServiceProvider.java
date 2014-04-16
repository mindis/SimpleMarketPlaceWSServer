package com.amazon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import com.amazon.db.DatabaseManager;
import com.amazon.dto.CategoryDTO;
import com.amazon.dto.ProductDTO;
import com.amazon.dto.ShoppingCartDTO;
import com.amazon.dto.SignUpDTO;
import com.amazon.dto.UserDTO;

/**
 * @author Amol
 * 
 */

@WebService
public class ServiceProvider {

	private static DatabaseManager dbManager = DatabaseManager.getInstance();

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public UserDTO authenticate(String username, String password) throws SQLException {
		UserDTO user = null;
		String sql = "select count(*) as count,identifier,email_id,first_name,last_name,LAST_LOGIN_TIME from user where EMAIL_ID = ? and PWD = ?";
		List<Object> values = new ArrayList<Object>();
		values.add(username);
		values.add(password);
		ResultSet result = dbManager.executeQuery(sql, values);
		while (result.next()) {
			if (result.getInt("count") > 0) {
				user = new UserDTO();
				user.setIdentifier(result.getInt("identifier"));
				user.setEmailId(result.getString("email_id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setLastLoginTime(result.getTimestamp("LAST_LOGIN_TIME").toString());
				updateLastLoginTime(username);
				break;
			}
		}
		return user;
	}

	/**
	 * @param username
	 * @throws SQLException
	 */
	private void updateLastLoginTime(String username) throws SQLException {
		String sql = "update user set LAST_LOGIN_TIME = ? where EMAIL_ID = ?";
		List<Object> values = new ArrayList<Object>();
		values.add(new Date());
		values.add(username);
		dbManager.executeUpdate(sql, values);
	}

	/**
	 * @param userId
	 * @throws SQLException
	 */
	public void checkout(int userId) throws SQLException {
		String sql = "update SHOPPING_CART set ORDER_STATUS = 'delivered' where USER_ID = ?";
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		dbManager.executeUpdate(sql, values);
	}

	/**
	 * @param shoppingCartDTO
	 * @throws SQLException
	 */
	public void addToCart(ShoppingCartDTO shoppingCartDTO) throws SQLException {
		String sql = "INSERT INTO SHOPPING_CART(USER_ID, PRODUCT_ID, ORDERED_QUANTITY, ORDER_STATUS) VALUES (?,?,?,?)";
		List<Object> values = new ArrayList<Object>();
		values.add(shoppingCartDTO.getUser().getIdentifier());
		ProductDTO[] products = shoppingCartDTO.getProducts();
		values.add(products[0].getIdentifier());
		values.add(products[0].getQuantity());
		values.add(shoppingCartDTO.getOrderStatus());
		dbManager.executeUpdate(sql, values);
		maintainProductCount(products[0].getIdentifier(), products[0].getQuantity(), "decrement");
	}

	public void removeFromCart(int userId, int productId) throws SQLException {
		String sql = "select ORDERED_QUANTITY from shopping_cart where USER_ID = " + userId + " and PRODUCT_ID ="
				+ productId + " and ORDER_STATUS = 'Pending'";
		ResultSet resultSet = dbManager.executeQuery(sql);
		int quantity = 0;
		while (resultSet.next()) {
			quantity = resultSet.getInt("ORDERED_QUANTITY");
		}
		String sql2 = "update SHOPPING_CART set ORDER_STATUS = 'deleted' where USER_ID = ? and PRODUCT_ID = ?";
		List<Object> values = new ArrayList<Object>();
		values.add(userId);
		values.add(productId);
		dbManager.executeUpdate(sql2, values);
		maintainProductCount(productId, quantity, "increment");
	}

	private void maintainProductCount(int productId, int quantity, String operation) throws SQLException {
		String sql = null;
		if (operation.equalsIgnoreCase("increment")) {
			sql = "update PRODUCT set QUANTITY = QUANTITY + ? where IDENTIFIER = ?";
		} else if (operation.equalsIgnoreCase("decrement")) {
			sql = "update PRODUCT set QUANTITY = QUANTITY - ? where IDENTIFIER = ?";
		}
		List<Object> values = new ArrayList<Object>();
		values.add(quantity);
		values.add(productId);
		dbManager.executeUpdate(sql, values);
	}

	public ShoppingCartDTO getShoppingCart(int userId) throws SQLException {
		String sql = "select p.IDENTIFIER as id,p.PRODUCT_NAME as productName, p.BRAND as brand, p.DESCRIPTION as description, "
				+ "p.PRICE as price, s.ORDERED_QUANTITY as orderedQuantity"
				+ " from shopping_cart s,product p where s.USER_ID = "
				+ userId
				+ " and s.PRODUCT_ID = p.IDENTIFIER and s.ORDER_STATUS = 'Pending'";

		ResultSet resultSet = dbManager.executeQuery(sql);

		List<ProductDTO> products = new ArrayList<ProductDTO>();
		while (resultSet.next()) {
			ProductDTO product = new ProductDTO();
			product.setIdentifier(resultSet.getInt("id"));
			product.setProductName(resultSet.getString("productName"));
			product.setBrand(resultSet.getString("brand"));
			product.setDescription(resultSet.getString("description"));
			product.setPrice(resultSet.getFloat("price"));
			product.setQuantity(resultSet.getInt("orderedQuantity"));
			products.add(product);
		}
		ShoppingCartDTO shoppingCart = new ShoppingCartDTO();
		shoppingCart.setProducts(products.toArray(new ProductDTO[products.size()]));
		return shoppingCart;
	}

	public int getShoppingCartItemCount(int userId) throws SQLException {
		String sql = "select count(*) as count from shopping_cart where USER_ID = " + userId
				+ " and ORDER_STATUS = 'Pending'";
		ResultSet resultSet = dbManager.executeQuery(sql);
		int count = 0;
		while (resultSet.next()) {
			count = resultSet.getInt("count");
		}
		return count;
	}

	/**
	 * @param productDTO
	 * @throws SQLException
	 */
	public void addProduct(ProductDTO productDTO) throws SQLException {
		String sql = "INSERT INTO PRODUCT(PRODUCT_NAME,BRAND,DESCRIPTION,PRICE,QUANTITY,CATEGORY_ID,PRODUCT_OWNER_ID,OWNER_INFORMATION) VALUES (?,?,?,?,?,?,?,?)";
		List<Object> values = new ArrayList<Object>();
		values.add(productDTO.getProductName());
		values.add(productDTO.getBrand());
		values.add(productDTO.getDescription());
		values.add(productDTO.getPrice());
		values.add(productDTO.getQuantity());
		values.add(productDTO.getCategoryId());
		values.add(productDTO.getProductOwnerId());
		values.add(productDTO.getSellerInformation());
		dbManager.executeUpdate(sql, values);
	}

	/**
	 * @param signUpDTO
	 * @return
	 * @throws SQLException
	 * @throws SignUpException
	 */
	public String[] createUser(SignUpDTO signUpDTO) throws SQLException {
		String[] message = new String[2];

		String sql = "select count(*) as count from user where EMAIL_ID = ?";
		List<Object> values = new ArrayList<Object>();
		values.add(signUpDTO.getEmailId());
		ResultSet results = dbManager.executeQuery(sql, values);
		if (results.next() && results.getInt("count") > 0) {
			message[0] = "error";
			message[1] = "User with same username already exists!!";
		} else {
			insertUser(signUpDTO);
			message[0] = "success";
			message[1] = "User Added Successfully!!";
		}

		return message;
	}

	/**
	 * @param signUpDTO
	 * @throws SQLException
	 */
	private void insertUser(SignUpDTO signUpDTO) throws SQLException {
		String sql = "insert into user(email_id,first_name,last_name,pwd) values (?,?,?,?)";
		List<Object> values = new ArrayList<Object>();
		values.add(signUpDTO.getEmailId());
		values.add(signUpDTO.getFirstname());
		values.add(signUpDTO.getLastname());
		values.add(signUpDTO.getPassword());

		dbManager.executeUpdate(sql, values);
	}

	/**
	 * @param categoryName
	 * @throws SQLException
	 */
	public void addCategory(String categoryName) throws SQLException {
		String sql = "INSERT INTO category(NAME) VALUES (?)";
		List<Object> values = new ArrayList<Object>();
		values.add(categoryName);

		dbManager.executeUpdate(sql, values);
	}

	public CategoryDTO[] getAllCategories() throws SQLException {
		String sql = "select identifier,name from category";
		List<CategoryDTO> categories = new ArrayList<CategoryDTO>();

		ResultSet resultSet = dbManager.executeQuery(sql);

		CategoryDTO defaultCategory = new CategoryDTO();
		defaultCategory.setCategoryName("All Categories");
		defaultCategory.setIdentifier(0);
		categories.add(defaultCategory);

		while (resultSet.next()) {
			CategoryDTO categoryDTO = new CategoryDTO();
			categoryDTO.setIdentifier(resultSet.getInt("identifier"));
			categoryDTO.setCategoryName(resultSet.getString("name"));
			categories.add(categoryDTO);
		}
		return categories.toArray(new CategoryDTO[categories.size()]);
	}

	public ProductDTO[] getProductsByCategoryId(int categoryId) throws SQLException {
		String sql = "select p.IDENTIFIER as IDENTIFIER, p.PRODUCT_NAME as PRODUCT_NAME, p.BRAND as BRAND,"
				+ " p.DESCRIPTION as DESCRIPTION, p.PRICE as PRICE, "
				+ "p.QUANTITY as QUANTITY,p.OWNER_INFORMATION as SELLER_INFORMATION, u.EMAIL_ID as OWNER_EMAIL_ID"
				+ " from product p, user u where p.CATEGORY_ID = " + categoryId
				+ " and u.IDENTIFIER = p.PRODUCT_OWNER_ID";

		return getProducts(sql);
	}

	public ProductDTO[] getAllProducts() throws SQLException {
		// String sql =
		// "select IDENTIFIER,PRODUCT_NAME,BRAND,DESCRIPTION,PRICE,QUANTITY from product";
		String sql = "select p.IDENTIFIER as IDENTIFIER, p.PRODUCT_NAME as PRODUCT_NAME, p.BRAND as BRAND,"
				+ " p.DESCRIPTION as DESCRIPTION, p.PRICE as PRICE, "
				+ "p.QUANTITY as QUANTITY,p.OWNER_INFORMATION as SELLER_INFORMATION, u.EMAIL_ID as OWNER_EMAIL_ID"
				+ " from product p, user u " + "where u.IDENTIFIER = p.PRODUCT_OWNER_ID";
		return getProducts(sql);
	}

	private ProductDTO[] getProducts(String sql) throws SQLException {
		List<ProductDTO> products = new ArrayList<ProductDTO>();

		ResultSet resultSet = dbManager.executeQuery(sql);
		while (resultSet.next()) {
			ProductDTO product = new ProductDTO();
			product.setIdentifier(resultSet.getInt("IDENTIFIER"));
			product.setProductName(resultSet.getString("PRODUCT_NAME"));
			product.setBrand(resultSet.getString("BRAND"));
			product.setDescription(resultSet.getString("DESCRIPTION"));
			product.setPrice(resultSet.getInt("PRICE"));
			product.setQuantity(resultSet.getInt("QUANTITY"));
			product.setProductOwnerName(resultSet.getString("OWNER_EMAIL_ID"));
			product.setSellerInformation(resultSet.getString("SELLER_INFORMATION"));
			// product.setCategoryId(resultSet.getInt("CATEGORY_ID"));
			products.add(product);

		}
		return products.toArray(new ProductDTO[products.size()]);
	}

	public ProductDTO[] getBoughtProducts(int userId) throws SQLException {
		String sql = "select p.identifier as IDENTIFIER, p.BRAND as BRAND, p.PRODUCT_NAME as PRODUCT_NAME, "
				+ "p.DESCRIPTION as DESCRIPTION,p.PRICE as PRICE, s.ORDERED_QUANTITY as QUANTITY "
				+ "from shopping_cart s, product p where USER_ID = " + userId
				+ " and ORDER_STATUS ='delivered' and p.identifier = s.product_id";

		return getHistoryProducts(sql);
	}

	public ProductDTO[] getSoldProducts(int userId) throws SQLException {
		String sql = "select p.identifier as IDENTIFIER, p.BRAND as BRAND, p.PRODUCT_NAME as PRODUCT_NAME, "
				+ "p.DESCRIPTION as DESCRIPTION, p.PRICE as PRICE,s.ORDERED_QUANTITY as QUANTITY from shopping_cart s, product p  "
				+ "where p.PRODUCT_OWNER_ID = " + userId
				+ " and order_status ='delivered' and p.identifier = s.product_id";
		return getHistoryProducts(sql);
	}

	private ProductDTO[] getHistoryProducts(String sql) throws SQLException {
		List<ProductDTO> products = new ArrayList<ProductDTO>();

		ResultSet resultSet = dbManager.executeQuery(sql);
		while (resultSet.next()) {
			ProductDTO product = new ProductDTO();
			product.setIdentifier(resultSet.getInt("IDENTIFIER"));
			product.setProductName(resultSet.getString("PRODUCT_NAME"));
			product.setBrand(resultSet.getString("BRAND"));
			product.setDescription(resultSet.getString("DESCRIPTION"));
			product.setPrice(resultSet.getInt("PRICE"));
			product.setQuantity(resultSet.getInt("QUANTITY"));
			// product.setCategoryId(resultSet.getInt("CATEGORY_ID"));
			products.add(product);

		}
		return products.toArray(new ProductDTO[products.size()]);
	}
}
