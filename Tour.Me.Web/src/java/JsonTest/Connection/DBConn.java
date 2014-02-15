/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package JsonTest.Connection;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author User
 */
public class DBConn {
   	public Connection getConnection() throws Exception{		
		Class.forName("com.mysql.jdbc.Driver");
		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","kaputa");
		return con;		
	}
    
}
