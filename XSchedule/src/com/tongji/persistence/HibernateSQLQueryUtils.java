package com.tongji.persistence;


import org.hibernate.Query;
import org.hibernate.Session;

public class HibernateSQLQueryUtils {
	public static int updateBySQL(Session session, final String sqlString, Object[] values){
		Query query = session.createSQLQuery(sqlString);
		for (int i = 0; i < values.length; i++) {
			query.setParameter(i, values[i]);
		}
		int result = query.executeUpdate();
		return result;
	}
	
	
}
