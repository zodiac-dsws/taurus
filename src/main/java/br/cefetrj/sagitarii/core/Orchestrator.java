package br.cefetrj.sagitarii.core;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.cefetrj.sagitarii.core.config.Configurator;
import br.cefetrj.sagitarii.core.types.UserType;
import br.cefetrj.sagitarii.federation.federates.SagitariiFederate;
import br.cefetrj.sagitarii.persistence.entity.Experiment;
import br.cefetrj.sagitarii.persistence.entity.User;
import br.cefetrj.sagitarii.persistence.exceptions.NotFoundException;
import br.cefetrj.sagitarii.persistence.infra.ConnFactory;
import br.cefetrj.sagitarii.persistence.services.ExperimentService;
import br.cefetrj.sagitarii.persistence.services.UserService;

@WebListener
public class Orchestrator implements ServletContextListener {
	private ScheduledExecutorService scheduler;
	
    private void loggerDebug( String log ) {
    	System.out.println( log );
    }
    
    private void loggerError( String log ){
    	System.out.println( log );
    }

	@Override
    public void contextInitialized(ServletContextEvent event) {
    	loggerDebug("--------------------------------------");
    	loggerDebug("Sagitarii Workflow Data Science System");
    	loggerDebug("CEFET-RJ                          2015");
    	loggerDebug("Carlos M. Abreu magno.mabreu@gmail.com");
    	loggerDebug("--------------------------------------");
    	
    	ServletContext context = event.getServletContext();
    	System.setProperty("rootPath", context.getRealPath("/") );

    	UserService us;
    	try {
    		int interval = 5;
    		int maxInputBufferCapacity = 500;
       
			Configurator config = Configurator.getInstance("config.xml");
			interval = config.getPoolIntervalSeconds();

			maxInputBufferCapacity = config.getMaxInputBufferCapacity();
			
			String user = config.getUserName();
			String passwd = config.getPassword();
			String database = config.getDatabaseName();
    		
    		ConnFactory.setCredentials(user, passwd, database);

   		
			us = new UserService();
			try {
				us.getList().size();
			} catch (NotFoundException ignored ) {
				// No users found. We need an Admin!
				User usr = new User();
				usr.setFullName("System Administrator");
				usr.setLoginName("admin");
				usr.setType( UserType.ADMIN );
				usr.setPassword("admin");
				usr.setUserMail("no.mail@localhost");
				us.newTransaction();
				us.insertUser(usr);
				loggerDebug("System Administrator created");
			}
			
			loggerDebug("check for interrupted work");	
			try {
				ExperimentService ws = new ExperimentService();
				List<Experiment> running = ws.getRunning();
				SagitariiFederate.getInstance().setRunningExperiments( running );
				SagitariiFederate.getInstance().reloadAfterCrash();
				loggerDebug("found " + SagitariiFederate.getInstance().getRunningExperiments().size() + " running experiments");	
			} catch ( NotFoundException e ) {
				loggerDebug("no running experiments found");	
			}			

			loggerDebug("Buffer cabacity " + maxInputBufferCapacity );
	        SagitariiFederate.getInstance().setMaxInputBufferCapacity( maxInputBufferCapacity );
	        SagitariiFederate.getInstance().startServer();
	        
	        scheduler = Executors.newSingleThreadScheduledExecutor();
			MainHeartBeat as = new MainHeartBeat();
	        scheduler.scheduleAtFixedRate(as, 20, interval , TimeUnit.SECONDS);
			
		} catch (Exception e) { 
			System.out.println( e.getMessage() );
			loggerError( e.getMessage() );
			//e.printStackTrace(); 
		}
        
        
	}
	
	@Override
    public void contextDestroyed(ServletContextEvent event) {
		loggerDebug("shutdown");
		scheduler.shutdownNow();
    }
}
