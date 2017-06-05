package vn.giki.rest.dao;

import java.sql.SQLException;

public interface PackageDAO {
	boolean isExists(String packageId) throws SQLException;
}
