package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import vn.giki.rest.entity.Package;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageDAOImpl implements PackageDAO {

	@Autowired
	private Connection connection;

	@Override
	public boolean isExists(String packageId) throws SQLException {
		String sql = String.format("select count(*) as count from package where id = '%s'", packageId);
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		if (rs.next()) {
			int count = rs.getInt("count");
			if (count > 0) {
				return true;
			}
		}

		return false;
	}

	@Override
	public List<Package> getAll() throws SQLException {
		List<Package> result = new ArrayList<>();
		String sql = String.format("select p.id,p.name,p.orders from package p order by orders");
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
		st.close();
		return result;
	}

}
