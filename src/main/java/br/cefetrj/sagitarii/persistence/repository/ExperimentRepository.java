package br.cefetrj.sagitarii.persistence.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.cefetrj.sagitarii.persistence.entity.Experiment;
import br.cefetrj.sagitarii.persistence.entity.User;
import br.cefetrj.sagitarii.persistence.exceptions.DatabaseConnectException;
import br.cefetrj.sagitarii.persistence.exceptions.DeleteException;
import br.cefetrj.sagitarii.persistence.exceptions.InsertException;
import br.cefetrj.sagitarii.persistence.exceptions.NotFoundException;
import br.cefetrj.sagitarii.persistence.exceptions.UpdateException;
import br.cefetrj.sagitarii.persistence.infra.DaoFactory;
import br.cefetrj.sagitarii.persistence.infra.IDao;

public class ExperimentRepository extends BasicRepository {

	public ExperimentRepository() throws DatabaseConnectException {
		super();
		logger.debug("init");
	}


	public Set<Experiment> getList() throws NotFoundException {
		return getList( null );
	}
	
	public Set<Experiment> getList( User user ) throws NotFoundException {
		if ( user == null ) {
			logger.debug("get list for no user" );
		} else {
			logger.debug("get list for user " + user.getLoginName() );
		}
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		Set<Experiment> experiments = null;
		try {
			if ( user == null ) {
				experiments = new HashSet<Experiment>( fm.getList("select * from experiments") );
			} else {
				experiments = new HashSet<Experiment>( fm.getList("select * from experiments where id_user = " + user.getIdUser() ) );
			}
		} catch ( Exception e ) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + experiments.size() + " experiments.");
		
		for ( Experiment experiment : experiments ) {
			experiment.updateMetrics();
		}
		
		return experiments;
	}


	public List<Experiment> getRunning() throws NotFoundException {
		logger.debug("retrieve pendent" );
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		List<Experiment> running = null;
		try {
			running = fm.getList("select * from experiments where status = 'RUNNING' or status = 'PAUSED' ");
		} catch ( Exception e ) {
			closeSession();		
			throw e;
		} 
		closeSession();		
		logger.debug("done");
		for ( Experiment experiment : running ) {
			experiment.updateMetrics();
		}
		return running;
	}
	
	
	public void updateExperiment( Experiment experiment ) throws UpdateException {
		logger.debug("update");
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		try {
			fm.updateDO(experiment);
			commit();
		} catch (UpdateException e) {
			logger.error( e.getMessage() );
			rollBack();
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done");
	}
	
	public Experiment insertExperiment(Experiment experiment) throws InsertException {
		logger.debug("insert");
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		try {
			fm.insertDO(experiment);
			commit();
		} catch (InsertException e) {
			rollBack();
			closeSession();
			logger.error( e.getMessage() );
			throw e;
		}
		closeSession();
		logger.debug("done");
		return experiment;
	}
	
	
	public Experiment getExperiment(String tag) throws NotFoundException {
		logger.debug("retrieving experiment by TAG " + tag + "..." );
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		Experiment experiment = null;
		try {
			experiment = fm.getList("select * from experiments where tagExec = '" + tag + "'").get(0);
		} catch ( Exception e ) {
			closeSession();		
			throw e;
		} 
		logger.debug("done");
		closeSession();
		return experiment;
	}
	
	
	public Experiment getExperiment(int idExperiment) throws NotFoundException {
		logger.debug("retrieving experiment " + idExperiment + "...");
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		Experiment experiment = null;
		try {
			experiment = fm.getDO(idExperiment);
		} catch ( Exception e ) {
			closeSession();		
			throw e;
		} 
		closeSession();		
		logger.debug("done: " + experiment.getTagExec() );
		return experiment;
	}
	

	public void deleteExperiment(Experiment experiment) throws DeleteException {
		logger.debug("delete" );
		DaoFactory<Experiment> df = new DaoFactory<Experiment>();
		IDao<Experiment> fm = df.getDao(this.session, Experiment.class);
		try {
			fm.deleteDO(experiment);
			commit();
		} catch (DeleteException e) {
			rollBack();
			closeSession();
			logger.error( e.getMessage() );
			throw e;			
		}
		logger.debug("done");
		closeSession();
	}	
	
}
