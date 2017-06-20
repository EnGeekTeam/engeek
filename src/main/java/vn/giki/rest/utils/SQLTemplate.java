package vn.giki.rest.utils;

public class SQLTemplate {
	public static final String GET_USER_PACKAGES = "select CASE WHEN up.user_id IS null THEN 0 ELSE 1 END as unlocks, up.status, p.id as package_id, p.name as package_name, (select count(*) from deck where package_id = p.id) as numberOfRoot, p.picturepath as picture_path from package as p left join userpack as up on p.id = up.packages_id and up.user_id = %d ORDER BY p.orders";
	public static final String GET_USER_PAYMENT = "select id, type, name, paymentStatus, paymentTime, expiredDate from user where id = %d";
	public static final String GET_USER_WORDS = "select * from word as w inner join userword as u on w.id = u.word_id where u.user_id = %d limit %d, %d";
	public static final String GET_USER_PACKAGE_DECKS = "select CASE WHEN ud.user_id IS null THEN 0 ELSE 1 END as unlocks,d.package_id, ud.status, d.id as deck_id, d.name as deck_name, (select count(*) from word where deck_id = d.id) as numberOfRoot,(select COUNT(*) as count FROM word as w INNER JOIN userword AS uw ON w.id = uw.word_id WHERE w.deck_id= d.id and uw.user_id = ud.user_id) as learned , d.picturepath as picture_path from deck as d left join userdeck as ud on d.id = ud.decks_id and ud.user_id = %d WHERE d.package_id = '%s'";
	public static final String GET_USERS_HIGH_SCORES = "select id, name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u order by total desc limit %d, %d";
	public static final String GET_LIST_FRIEND = "select id, googleId,name, avatarUrl, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u where googleId in (%s) order by total desc limit %d, %d";
	public static final String UPDATE_USER = "update user set %s where id = %d";
	public static final String GET_USER_DECK_WORDS = "select w.id,w.absoluteFrequency,w.antonym,w.audioPath,w.name, w.meaning,w.phonetics,w.wordLink,w.deck_id,w.description,w.example from word as w left join userword as uw on w.id = uw.word_id and uw.user_id = %d WHERE w.deck_id = '%s'";
	public static final String GET_TOKEN_CLIENT = "select tokenClient from user where id = %d";
	public static final String INSERT_GAME1 = "insert into usergame1 set %s";
	public static final String LIST_WORD_GAME1 = "select u.user_id, u.word_id, w.antonym from userword as u, word as w where u.word_id = w.id";
	public static final String INSERT_GAME2 = "insert into usergame2 set %s";
	public static final String INSERT_GAME3 = "insert into usergame3 set %s";
	public static final String GET_DATA_GAME3 = "select word.name, word.meaning, word.example from word, userword where word.id=userword.word_id and userword.user_id = %d order by RAND() Limit 10 ";
	public static final String IS_USER_EXIST = "select count(*) as count from user where id = %d";
	public static final String GET_DATA_GAME2 = "SELECT word.sysnonym FROM word WHERE word.id = '%s'";
	public static final String UPDATE_TOTAL_TIME = "update user set user.total_time = %d where user.id = %d";
	public static final String LIST_LEARNED_WORD = "select userword.word_id, month(userword.createdAt) as month from userword where userword.user_id = %d order by month";
	public static final String GET_USERS_SCORES = "select id, name, (select max(score) from usergame1 where user_id = u.id) as game1_max_score, (select max(score) from usergame2 where user_id = u.id) as game2_max_score, (select max(score) from usergame3 where user_id = u.id) as game3_max_score, (ifnull((select max(score) from usergame1 where user_id = u.id),0) + ifnull((select max(score) from usergame2 where user_id = u.id),0) +ifnull((select max(score) from usergame3 where user_id = u.id),0)) as total from user as u where u.id = %d";
	
}

