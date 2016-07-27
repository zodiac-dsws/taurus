package br.cefetrj.sagitarii.persistence.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.cefetrj.sagitarii.persistence.entity.Activity;
import br.cefetrj.sagitarii.persistence.entity.Fragment;
import br.cefetrj.sagitarii.persistence.entity.Relation;
import br.cefetrj.sagitarii.persistence.exceptions.DatabaseConnectException;
import br.cefetrj.sagitarii.persistence.exceptions.InsertException;
import br.cefetrj.sagitarii.persistence.exceptions.NotFoundException;
import br.cefetrj.sagitarii.persistence.exceptions.UpdateException;
import br.cefetrj.sagitarii.persistence.infra.DaoFactory;
import br.cefetrj.sagitarii.persistence.infra.IDao;

public class FragmentRepository extends BasicRepository {

	public FragmentRepository() throws DatabaseConnectException {
		super();
		logger.debug("init");
	}
	
	public List<Fragment> getList( int idExperiment ) throws NotFoundException {
		logger.debug("get fragment list" );
		DaoFactory<Fragment> df = new DaoFactory<Fragment>();
		IDao<Fragment> fm = df.getDao(this.session, Fragment.class);
		List<Fragment> fragments = null;
		try {
			fragments = fm.getList("select * from fragments where id_experiment = " + idExperiment);
		} catch (Exception e) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done: " + fragments.size() + " fragments.");
		return fragments;
	}

	
	public Fragment insertFragment(Fragment fragment) throws InsertException {
		logger.debug("insert");
		DaoFactory<Fragment> df = new DaoFactory<Fragment>();
		IDao<Fragment> fm = df.getDao(this.session, Fragment.class);
		try {
			fm.insertDO(fragment);
			commit();
		} catch (Exception e) {
			logger.error( e.getMessage() );
			rollBack();
			closeSession();
			throw new InsertException(e.getMessage());
		}
		closeSession();
		logger.debug("done");
		return fragment;
	}

	
	public void insertFragmentList( List<Fragment> fragmentList ) throws InsertException {
		logger.debug("insert");
		DaoFactory<Fragment> df = new DaoFactory<Fragment>();
		IDao<Fragment> fm = df.getDao(this.session, Fragment.class);
		try {
			for ( Fragment fragment : fragmentList ) {
				
				DaoFactory<Relation> dfr = new DaoFactory<Relation>();
				IDao<Relation> fmr = dfr.getDao(this.session, Relation.class);
				
				logger.debug("fragment " + fragment.getSerial() + " : ");
				for ( Activity act : fragment.getActivities() ) {
					logger.debug(" > activity " + act.getTag() );
					Set<Relation> inputRelations = new HashSet<Relation>();
					for ( Relation rel : act.getInputRelations() ) {
						logger.debug("  > input " + rel.getName() );
						inputRelations.add( fmr.getDO( rel.getIdTable() ) );
					}
					act.setInputRelations(inputRelations);
				}
				fm.insertDO(fragment);
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
	
	public void updateFragment( Fragment fragment ) throws UpdateException {
		logger.debug("update");
		DaoFactory<Fragment> df = new DaoFactory<Fragment>();
		IDao<Fragment> fm = df.getDao(this.session, Fragment.class);
		try {
			fm.updateDO(fragment);
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
	
	public Fragment getFragment(int idFragment) throws NotFoundException {
		logger.debug("retrieve");
		DaoFactory<Fragment> df = new DaoFactory<Fragment>();
		IDao<Fragment> fm = df.getDao(this.session, Fragment.class);
		Fragment fragment = null;
		try {
			fragment = fm.getDO(idFragment);
		} catch ( Exception e ) {
			closeSession();
			throw e;
		}
		closeSession();
		logger.debug("done");
		return fragment;
	}

}
