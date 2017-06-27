package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.Pay;
import vn.giki.rest.utils.Utils;

@Service
public class PayDAOImpl implements PayDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public void save(int userId, String platform, String pakage, String product, String reciept, String dateStart, String dateEnd) throws SQLException {
		if (!isExists(userId, product)) {
			Connection connection = null;
			String sql = String.format("insert into pay(userId,platform,package,product,receipt,dateStart,dateEnd,lastCheckTime) values(?,?,?,?,?,?,?,?)");

			try {
				connection = dataSource.getConnection();
				PreparedStatement ps = connection.prepareStatement(sql);
				ps.setInt(1, userId);
				ps.setString(2, platform);
				ps.setString(3, pakage);
				ps.setString(4, product);
				ps.setString(5, reciept);
				ps.setString(6, dateStart);
				ps.setString(7, dateEnd);
				ps.setString(8, Utils.getDate());
				ps.executeUpdate();

				ps.close();

			} catch (Exception e) {
				throw new RuntimeException(e.getMessage());
			} finally {
				if (connection != null)
					connection.close();
			}
		}

	}

	@Override
	public boolean isExists(int userId, String product) throws SQLException {
		Connection connection = null;
		String sql = String.format("select count(*) as count from pay where userId=%d and product='%s'", userId,
				product);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt("count");
				if (count > 0) {
					rs.close();
					st.close();
					return true;
				}
			}

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

		return false;
	}

	@Override
	public List<Pay> getListPayByDate() throws SQLException {
		Connection connection = null;
		String sql = String.format("select * from pay where dateEnd>'%s' and lastCheckTime<>'%s' limit 10", Utils.getLastSevenDay(), Utils.getDate());
		List<Pay> result = new ArrayList<>();
		System.out.println(sql);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			Pay pay;
			while (rs.next()) {
				pay = new Pay();
				pay.setId(rs.getInt("id"));
				pay.setUserId(rs.getInt("userId"));
				pay.setPlatform(rs.getString("platform"));
				pay.setPackages(rs.getString("package"));
				pay.setProduct(rs.getString("product"));
				pay.setReceipt(rs.getString("receipt"));
				pay.setDateStart(rs.getString("dateStart"));
				pay.setDateEnd(rs.getString("dateEnd"));
				result.add(pay);
			}

			rs.close();
			st.close();
			return result;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public void updateLastCheck(int id) throws SQLException {
		Connection connection=null;
		String sql = String.format("update pay set lastCheckTime='%s' where id=%d", Utils.getDate(), id);
		System.out.println(sql);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			st.execute(sql);
			st.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
	}

	@Override
	public void updateDateEnd(int id, String dateEnd) throws SQLException {
		Connection connection=null;
		String sql = String.format("update pay set dateEnd='%s' where id=%d", dateEnd, id);
		System.out.println(sql);
		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			st.execute(sql);
			st.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
		
	}

}
