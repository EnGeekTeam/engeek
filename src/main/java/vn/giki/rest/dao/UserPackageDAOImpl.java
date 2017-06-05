package vn.giki.rest.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.giki.rest.entity.UserPack;
import vn.giki.rest.utils.Utils;

@Service
public class UserPackageDAOImpl implements UserPackageDAO {

	@Autowired
	private Connection connection;
	
	@Override
	public void save(String packageId, int userId) throws Exception {
		
		if (!isExistsUserPack(packageId, userId)){
			String sqlInsertPackage = "insert into userpack(createAt,status,packages_id,user_id) values (?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sqlInsertPackage);
			ps.setString(1,  Utils.getDate());
			ps.setInt(2, 0);
			ps.setString(3, packageId);
			ps.setInt(4, userId);
			ps.executeUpdate();
			ps.close();
		} else {
			throw new Exception("Package is exists!");
		}
	}

	@Override
	public List<UserPack> getList(int userId, String packageId) {
		
		return null;
	}
	
	
	private boolean isExistsUserPack(String packageId, int userId) throws SQLException{
		String sql = String.format("select count(*) as count from userpack where user_id = %d and packages_id = '%s'", userId, packageId );
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		
		if (rs.next()){
			int count = rs.getInt("count");
			if (count>0){
				return true;
			}
		}
		
		return false;
		
	}

}
