package com.amazon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author Amol
 * 
 */
public class DatabaseManager {

	private Connection conn = null;

	private static DatabaseManager instance = null;

	private DatabaseManager() {
		conn = createConnection();
	}

	public static DatabaseManager getInstance() {
		if (instance == null) {
			instance = new DatabaseManager();
		}
		return instance;
	}

	/**
	 * @return connection
	 */
	private Connection createConnection() {

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/amazon273", "root", "root");
			System.out.println("Connection created successfully!!");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;
	}

	/**
	 * @param sql
	 * @param values
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sql, List<Object> values) throws SQLException {
		PreparedStatement pStatement = getPreparedStatement(sql, values);
		ResultSet results = pStatement.executeQuery();
		return results;
	}

	public void executeUpdate(String sql, List<Object> values) throws SQLException {
		PreparedStatement pStatement = getPreparedStatement(sql, values);
		pStatement.executeUpdate();
	}

	public ResultSet executeQuery(String sql) throws SQLException {
		Statement stmt = conn.createStatement();
		ResultSet results = stmt.executeQuery(sql);
		return results;
	}

	private PreparedStatement getPreparedStatement(String sql, List<Object> values) throws SQLException {
		PreparedStatement pStatement = conn.prepareStatement(sql);
		for (int i = 1; i <= values.size(); i++) {
			pStatement.setObject(i, values.get(i - 1));
		}
		return pStatement;
	}


}
