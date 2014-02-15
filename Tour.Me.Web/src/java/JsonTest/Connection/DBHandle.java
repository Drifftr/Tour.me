/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package JsonTest.Connection;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 *
 * @author User
 */
public class DBHandle {
    	public static int setData(Connection con,String query ) throws Exception{
			java.sql.Statement stm=con.createStatement();					
			int res=stm.executeUpdate(query);
			return res;
	}
	
	public static ResultSet getData(Connection con,String query ) throws Exception{
			java.sql.Statement stm=con.createStatement();					
			ResultSet res=stm.executeQuery(query);
			return res;
	}

}
