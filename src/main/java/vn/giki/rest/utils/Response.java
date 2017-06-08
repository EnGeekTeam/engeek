package vn.giki.rest.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Response {
	private Throwable throwable;
	private List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

	public Response() {
		super();
	}

	public Response(Throwable throwable) {
		super();
		this.throwable = throwable;
	}

	public List<Map<String, Object>> getResult() {
		return result;
	}

	public void setResult(List<Map<String, Object>> result) {
		this.result = result;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public Response setThrowable(Throwable throwable) {
		this.throwable = throwable;
		return this;
	}

	public Map<String, Object> renderResponse() {
		String error, message;
		if (throwable == null) {
			error = "0";
			message = "Success";
		} else {
			error = "1";
			message = throwable.getMessage();
		}
		Map<String, Object> result = new TreeMap<>();
		result.put("data", this.result.size() < 2 ? (this.result.size() == 0 ? new HashMap<>() : this.result.get(0))
				: this.result);
		result.put("error", error);
		result.put("message", message);
		return result;
	}

	public Map<String, Object> renderResponsePlus(HashMap<String, Object> data, String title) {
		String error, message;
		if (throwable == null) {
			error = "0";
			message = "Success";
		} else {
			error = "1";
			message = throwable.getMessage();
		}
		
		System.out.println("++: " + data.size());
		
		Map<String, Object> result = new TreeMap<>();

		HashMap<String, Object> rs = new HashMap<>();
		rs.put(title, data);
		rs.put("data", this.result);
		result.put("data", rs);

		result.put("error", error);
		result.put("message", message);
		return result;
	}

	public Map<String, Object> renderArrayResponse() {
		String error, message;
		if (throwable == null) {
			error = "0";
			message = "Success";
		} else {
			error = "1";
			message = throwable.getMessage();
		}
		Map<String, Object> result = new TreeMap<>();
		result.put("data", this.result.size() == 0 ? "[]" : this.result);
		result.put("error", error);
		result.put("message", message);
		return result;
	}

	public Response execute(String sql, Connection connection) throws SQLException {
		System.out.println(sql);
		if (sql.toLowerCase().contains("select")) {
			return executeQuery(sql, connection);
		} else {
			return executeUpdate(sql, connection);
		}
	}

	private Response executeQuery(String sql, Connection connection) throws SQLException {
		PreparedStatement st = connection.prepareStatement(sql);
		ResultSet rs = st.executeQuery();
		ResultSetMetaData meta = rs.getMetaData();
		int numberOfColumn = meta.getColumnCount();
		String[] template = new String[numberOfColumn];
		for (int i = 0; i < numberOfColumn; i++) {
			template[i] = meta.getColumnLabel(i + 1);
			System.out.println(template[i]);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> temp = null;
		while (rs.next()) {
			temp = new TreeMap<>();
			for (String columnName : template) {
				temp.put(columnName, rs.getObject(columnName));
			}
			list.add(temp);
		}
		this.result = list;
		return this;
	}
	

	private Response executeUpdate(String sql, Connection connection) throws SQLException {
		PreparedStatement st = connection.prepareStatement(sql);
		int i = st.executeUpdate();
		if (i == 0) {
			throw new SQLException("Operation fail");
		}
		return this;
	}

	public Response execute(PreparedStatement st, Connection connection) throws SQLException {
		if (!st.execute()) {
			if (st.getUpdateCount() == 0) {
				throw new SQLException("Operation fail");
			}
			return this;
		}
		ResultSet rs = st.getResultSet();
		ResultSetMetaData meta = rs.getMetaData();
		int numberOfColumn = meta.getColumnCount();
		String[] template = new String[numberOfColumn];
		for (int i = 0; i < numberOfColumn; i++) {
			template[i] = meta.getColumnLabel(i + 1);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> temp = null;
		while (rs.next()) {
			temp = new TreeMap<>();
			for (String columnName : template) {
				temp.put(columnName, rs.getObject(columnName));
			}
			list.add(temp);
		}
		this.result = list;
		return this;
	}

}
