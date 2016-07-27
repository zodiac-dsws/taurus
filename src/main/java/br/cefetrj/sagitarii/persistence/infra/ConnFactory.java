package br.cefetrj.sagitarii.persistence.infra;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class ConnFactory {
	private static SessionFactory factory;
	private static String myClass = "br.cefetrj.infra.database.ConnFactory";
	private static String userName;
	private static String password;
	private static String databaseName;	
	
	public static void setCredentials( String user, String passwd, String database ) {
		userName = user;
		password = passwd;
		databaseName = database;
	}
	
	private static void doLog( String s ) {
		System.out.println(myClass + " " + s);
	}

	public static Session getSession() {
		if ( factory == null ) {
			
			try { 
				doLog("starting Hibernate");
				
				Configuration cfg1 = new Configuration();
				cfg1.configure("hibernate.cfg.xml");
				//String url = "jdbc:postgresql://localhost/" + databaseName + "?ApplicationName=Sagitarii";
				cfg1.setProperty("hibernate.connection.username", userName);
				//cfg1.setProperty("hibernate.connection.url", url);
			 	cfg1.setProperty("hibernate.connection.password", password);
				
				StandardServiceRegistryBuilder serviceRegistryBuilder1 = new StandardServiceRegistryBuilder();
				serviceRegistryBuilder1.applySettings( cfg1.getProperties() );
				ServiceRegistry serviceRegistry1 = serviceRegistryBuilder1.build();	
				
				factory = cfg1.buildSessionFactory(serviceRegistry1);			
				
			} catch (Throwable ex) { 
				doLog("fail: " + ex.getMessage() );
				ex.printStackTrace();
				return null;
			}
		} 
		
		Session session = factory.openSession();
		return session;
	}

	
}
