package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vn.giki.rest.entity.Package;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageDAOImpl implements PackageDAO {

	@Autowired
	private DataSource dataSource;

	@Override
	public boolean isExists(String packageId) throws SQLException {
		Connection connection = null;
		String sql = String.format("select count(*) as count from package where id = '%s'", packageId);

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				int count = rs.getInt("count");
				if (count > 0) {
					return true;
				}
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}

		return false;
	}

	@Override
	public List<Package> getAll() throws SQLException {
		Connection connection = null;
		List<Package> result = new ArrayList<>();
		String sql = String.format("select p.id,p.name,p.orders from package p order by orders");

		try {
			connection = dataSource.getConnection();
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(sql);
			Package packages;
			while (rs.next()) {
				packages = new Package();
				packages.setId(rs.getString("id"));
				packages.setName(rs.getString("name"));
				packages.setOrders(rs.getInt("orders"));
				result.add(packages);
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (connection != null)
				connection.close();
		}
		return result;
	}

}
