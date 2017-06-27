package vn.giki.rest.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import vn.giki.rest.utils.Response;

@RestController
@RequestMapping("/product")
@Api(tags={"ProductAPI"})
public class ProductAPI {
	@Autowired
	private DataSource dataSource;
	
	@GetMapping()
	public Map<String, Object> listProduct (@RequestParam (required=true) ArrayList<String>listProType) throws SQLException{
		Response res = new Response();
		Connection con = null;
		StringBuilder para = new StringBuilder();
		for(String s: listProType){
			para.append("'"+s+"'");
			para.append(",");
		}
		String value = para.toString().substring(0, para.lastIndexOf(","));
		
		try{
			con = dataSource.getConnection();
			String sql = String.format("Select * from product where product.type in (%s)", value);
			return res.execute(sql, con).renderArrayResponse();
		}catch(Exception e){
			return res.setThrowable(e).renderArrayResponse();
		}finally{
			if(con!=null)
				con.close();
		}
	}

}
