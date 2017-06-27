package vn.giki.rest.dao;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import vn.giki.rest.entity.Badge;
import vn.giki.rest.entity.BadgeInfo;
import vn.giki.rest.entity.ProgressInfo;

public interface ProgressDAO {
	List<Badge> allBadges(int userID) throws Exception;
	ProgressInfo allTimeData(int userID)throws Exception;
	BadgeInfo allTimeBadge (int userID) throws Exception;
	List<Badge> bestBadge (int userID) throws Exception;
	void saveBadge (int userId, int get_level, String type) throws Exception;
}
