package br.cefetrj.sagitarii.persistence.services;

import java.util.List;

import br.cefetrj.sagitarii.persistence.entity.Fragment;
import br.cefetrj.sagitarii.persistence.exceptions.DatabaseConnectException;
import br.cefetrj.sagitarii.persistence.exceptions.InsertException;
import br.cefetrj.sagitarii.persistence.exceptions.NotFoundException;
import br.cefetrj.sagitarii.persistence.exceptions.UpdateException;
import br.cefetrj.sagitarii.persistence.repository.FragmentRepository;

public class FragmentService {
	private FragmentRepository rep;
	
	public FragmentService() throws DatabaseConnectException {
		this.rep = new FragmentRepository();
	}
	

	public void close() {
		rep.closeSession();
	}
	
	public void newTransaction() {
		if( !rep.isOpen() ) {
			rep.newTransaction();
		}
	}

	public int insertFragment(Fragment fragment) throws InsertException {
		newTransaction();
		rep.insertFragment( fragment );
		return 0;
	}
	
	public void updateFragment( Fragment fragment ) throws UpdateException {
		newTransaction();
		Fragment oldFragment;
		try {
			oldFragment = rep.getFragment( fragment.getIdFragment() );
		} catch (NotFoundException e) {
			throw new UpdateException( e.getMessage() );
		}
		oldFragment.setStatus( fragment.getStatus() );
		oldFragment.setRemainingInstances( fragment.getRemainingInstances() );
		oldFragment.setTotalInstances( fragment.getTotalInstances() );
		rep.newTransaction();
		rep.updateFragment(oldFragment);
	}
	
	public void insertFragmentList( List<Fragment> fragmentList ) throws InsertException {
		rep.insertFragmentList( fragmentList );
	}
	
	public List<Fragment> getList( int idExperiment ) throws NotFoundException {
		newTransaction();
		List<Fragment> frags = rep.getList( idExperiment );
		return frags;
	}

}
