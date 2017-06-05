package vn.giki.rest.utils;

public class SQLTemplate {
	public static final String GET_USER_PACKAGES = "select CASE WHEN up.user_id IS null THEN 0 ELSE 1 END as unlocks, up.status, p.id as package_id, p.name as package_name, (select count(*) from deck where package_id = p.id) as numberOfRoot, p.picturepath as picture_path from package as p left join userpack as up on p.id = up.packages_id and up.user_id = %d";
	public static final String GET_USER_PAYMENT = "select id, type, name, paymentStatus, paymentTime from user where id = %d";
	public static final String IS_USER_EXIST = "select id from user where id = %d";
	public static final String IS_PACKAGE_EXIST = "select id from package where id = '%s'";
	public static final String GET_USER_WORDS = "select * from word as w inner join userword as u on w.id = u.word_id where u.user_id = %d limit %d, %d";
	public static final String IS_DECK_EXIST = "select id from deck where id = '%s'";
	public static final String GET_USER_PACKAGE_DECKS = "select * from userdeck as u left join deck as d on u.decks_id = d.id where d.package_id = '%s' and u.user_id = %d";
	public static final String GET_USERS_HIGH_SCORES = "select id, name, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u order by total desc limit %d, %d";
	public static final String UPDATE_USER = "update user set %s where id = %d";
	public static final String GET_USER_DECK_WORDS = "select * from userword as u right join word as d on u.word_id = d.id  and u.user_id = %d where d.deck_id = '%s'";
	public static final String GET_TOKEN_CLIENT = "select tokenClient from user where id = %d";
	public static final String INSERT_GAME1 = "insert into usergame1 set %s";
}
