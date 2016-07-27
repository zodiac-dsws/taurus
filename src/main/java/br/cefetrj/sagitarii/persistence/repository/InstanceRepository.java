package br.cefetrj.sagitarii.persistence.repository;

import java.util.List;

import br.cefetrj.sagitarii.persistence.entity.Instance;
import br.cefetrj.sagitarii.persistence.exceptions.DatabaseConnectException;
import br.cefetrj.sagitarii.persistence.exceptions.DeleteException;
import br.cefetrj.sagitarii.persistence.exceptions.InsertException;
import br.cefetrj.sagitarii.persistence.exceptions.NotFoundException;
import br.cefetrj.sagitarii.persistence.exceptions.UpdateException;
import br.cefetrj.sagitarii.persistence.infra.DaoFactory;
import br.cefetrj.sagitarii.persistence.infra.IDao;

public class InstanceRepository extends BasicRepository {

	public InstanceRepository() throws DatabaseConnectException {
		super();
		logger.debug("init");
	}

	
	public List<Instance> recoverFromCrash() throws Exception {
		logger.debug("recovering common instances" );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		List<Instance> pipes = null;
		try {
			pipes = fm.getList("select * from instances where status = 'PAUSED' or status = 'RUNNING' order by id_instance");
		} catch (NotFoundException e) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + pipes.size() + " instances.");
		return pipes;
	}
	
	
	public List<Instance> getHead( int howMany, int idFragment ) throws Exception {
		logger.debug("get first " + howMany + " records for fragment " + idFragment );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		List<Instance> pipes = null;
		try {
			String selectQuery = "select * from instances where status = 'PIPELINED' and type <> 'SELECT' and id_fragment = " + idFragment  
					+ " order by id_instance limit " + howMany;
			
			pipes = fm.getList( selectQuery );
			
			String update ="update instances set start_date_time = now(), status = 'RUNNING' where id_instance in (select id_instance from instances where status = 'PIPELINED' and type <> 'SELECT' and id_fragment = " + idFragment  
					+ " order by id_instance limit " + howMany + ")";
			
			fm.executeQuery( update, true );
		} catch (NotFoundException e) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + pipes.size() + " instances.");
		return pipes;
	}

	
	public List<Instance> getHeadJoin( int howMany, int idFragment ) throws Exception {
		logger.debug("get first " + howMany + " JOIN records for fragment " + idFragment );
		logger.debug("get first " + howMany + " records" );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		List<Instance> pipes = null;
		try {
			String selectQuery = "select * from instances where status = 'PIPELINED' and type = 'SELECT' and id_fragment = " + idFragment
					+ " order by id_instance limit " + howMany;
			
			pipes = fm.getList(selectQuery);

			String update ="update instances set status = 'RUNNING' where id_instance in (select id_instance from instances where status = 'PIPELINED' and type = 'SELECT' and id_fragment = " + idFragment
					+ " order by id_instance limit " + howMany + ")"; 
			fm.executeQuery( update, true );
			
		} catch (NotFoundException  e) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + pipes.size() + " instances.");
		return pipes;		
		
	}

	public List<Instance> getPipelinedList( int idFragment ) throws NotFoundException {
		logger.debug("get list" );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		List<Instance> pipes = null;
		try {
			pipes = fm.getList("select * from instances where status = 'PIPELINED' and id_fragment = " + idFragment);
		} catch (Exception e) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + pipes.size() + " instances.");
		return pipes;
	}
	
	public List<Instance> getList( int idFragment ) throws NotFoundException {
		logger.debug("get list" );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		List<Instance> pipes = null;
		try {
			pipes = fm.getList("select * from instances where id_fragment = " + idFragment);
		} catch (Exception e) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + pipes.size() + " instances.");
		return pipes;
	}
	
	public Instance insertInstance(Instance instance) throws InsertException {
		logger.debug("insert");
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		try {
			fm.insertDO(instance);
			commit();
		} catch (Exception e) {
			logger.error( e.getMessage() );
			rollBack();
			closeSession();
			throw new InsertException(e.getMessage());
		}
		closeSession();
		logger.debug("done");
		return instance;
	}

	
	public void insertInstanceList( List<Instance> pipes ) throws InsertException {
		logger.debug("insert list");
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		try {
			for ( Instance instance : pipes ) {
				fm.insertDO(instance);
			}
			commit();
		} catch (Exception e) {
			logger.error( e.getMessage() );
			rollBack();
			closeSession();
			throw new InsertException(e.getMessage());
		}
		closeSession();
		logger.debug("done");
	}
	
	public Instance getInstance(String serial) throws NotFoundException {
		logger.debug("retrieving instance by serial " + serial + "..." );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		Instance instance = null;
		try {
			instance = fm.getList("select * from instances where serial = '" + serial + "'").get(0);
		} catch ( NotFoundException e ) {
			closeSession();		
			throw e;
		} 
		logger.debug("done");
		closeSession();
		return instance;
	}
	
	public void deleteInstance(Instance instance) throws DeleteException {
		logger.debug("delete instance." );
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		try {
			fm.deleteDO(instance);
			commit();
		} catch (Exception e) {
			logger.error( e.getMessage() );
			rollBack();
			closeSession();
			throw new DeleteException( e.getMessage() );			
		}
		logger.debug("done.");
		closeSession();
	}
	
	public Instance getInstance(int idInstance) throws NotFoundException {
		logger.debug("retrieve");
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		Instance instance = null;
		try {
			instance = fm.getDO(idInstance);
		} catch ( Exception e ) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done");
		return instance;
	}
	
	public void updateInstance( Instance instance ) throws UpdateException {
		logger.debug("update");
		DaoFactory<Instance> df = new DaoFactory<Instance>();
		IDao<Instance> fm = df.getDao(this.session, Instance.class);
		try {
			instance.evaluateTime();
			fm.updateDO(instance);
			commit();
		} catch (Exception e) {
			e.printStackTrace();
			
			logger.error( e.getMessage() );
			rollBack();
			closeSession();
			throw new UpdateException( e.getMessage() );
		}
		closeSession();
		logger.debug("done");
	}

}
