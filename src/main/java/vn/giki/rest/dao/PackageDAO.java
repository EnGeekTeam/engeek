package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.List;

import vn.giki.rest.entity.Package;

public interface PackageDAO {
	boolean isExists(String packageId) throws SQLException;
	
	List<Package> getAll() throws SQLException;
}
