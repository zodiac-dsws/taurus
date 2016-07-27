package br.cefetrj.sagitarii.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.core.instances.InstanceList;
import br.cefetrj.sagitarii.core.instances.InstanceListContainer;
import br.cefetrj.sagitarii.core.types.ActivityType;
import br.cefetrj.sagitarii.core.types.ExperimentStatus;
import br.cefetrj.sagitarii.core.types.FragmentStatus;
import br.cefetrj.sagitarii.persistence.entity.Experiment;
import br.cefetrj.sagitarii.persistence.entity.Fragment;
import br.cefetrj.sagitarii.persistence.entity.Instance;
import br.cefetrj.sagitarii.persistence.exceptions.NotFoundException;
import br.cefetrj.sagitarii.persistence.services.InstanceService;

public class InstanceBuffer {
	private int bufferSize;
	private Queue<Instance> instanceInputBuffer;
	private Queue<Instance> instanceJoinInputBuffer;
	private Queue<Instance> instanceOutputBuffer;
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	private List<Experiment> runningExperiments;
	
	public List<Experiment> getRunningExperiments() {
		return new ArrayList<Experiment>( runningExperiments );
	}
	
	public boolean isEmpty() {
		return ( getInstanceJoinInputBufferSize() == 0 ) && ( getInstanceInputBufferSize() == 0 ); 
	}
	
	private synchronized void processAndInclude( List<Instance> preBuffer ) {
		logger.debug("collecting instances from database to buffers...");
		for( Instance instance : preBuffer ) {
			if( instance.getType().isJoin() ) {
				logger.debug("JB > " + instance.getSerial() + " " + instance.getType() );
				instanceJoinInputBuffer.add(instance);
			} else {
				logger.debug("CB > " + instance.getSerial() + " " + instance.getType() );
				instanceInputBuffer.add(instance);
			}
		}
		logger.debug("done.");
	}
	
	public synchronized void loadBuffers() throws Exception {
		int runningExperimentCount = getRunningExperiments().size(); 
		if ( runningExperimentCount == 0 ) return;
		int sliceSize;

		if ( getInstanceInputBufferSize() < ( bufferSize / 3 ) ) {
			InstanceListContainer listContainer = new InstanceListContainer();
			logger.debug("check COMMON buffer because buffer is at " + getInstanceInputBufferSize() );
			sliceSize = ( bufferSize - getInstanceInputBufferSize() ) / runningExperimentCount + 1;
			for ( Experiment experiment : getRunningExperiments() ) {
				
				if ( experiment.getStatus() != ExperimentStatus.PAUSED ) {
					logger.debug("loading Common Instances for experiment " + experiment.getTagExec() + ". Slice size: " + sliceSize );
					List<Instance> common = loadCommonBuffer( sliceSize, experiment );
					if ( common != null ) {
						logger.debug("found " + common.size() + " instances. Adding to container...");
						listContainer.addList( new InstanceList(common, experiment.getTagExec()) );
					}
				} else {
					logger.debug("experiment " + experiment.getTagExec() + " is paused. will ignore..." );
				}
				
			}
			try {
				if ( listContainer.size() > 0 ) {
					logger.debug("container have " + listContainer.size() + " instance list. Merging...");
					List<Instance> instances = listContainer.merge();
					instanceInputBuffer.addAll( instances );
				} else {
					logger.error("container is empty. Something VERY wrong happen. ");
				}
				logger.debug("common buffer size: " + instanceInputBuffer.size() );
			} catch ( Exception e ) {
				e.printStackTrace();
			}
			
		}
	
		if ( getInstanceJoinInputBufferSize() < ( bufferSize / 5 ) ) {
			InstanceListContainer listContainerS = new InstanceListContainer();
			logger.debug("check SELECT buffer...");
			sliceSize = ( bufferSize - getInstanceJoinInputBufferSize() ) / runningExperimentCount + 1;
			for ( Experiment experiment : getRunningExperiments() ) {
				logger.debug(" > " + experiment.getTagExec() + " " + experiment.getStatus() );
				if ( experiment.getStatus() != ExperimentStatus.PAUSED ) {
					logger.debug("loading SELECT Instances for experiment " + experiment.getTagExec() );
					List<Instance> select = loadJoinBuffer( sliceSize, experiment); 
					if ( select != null ) {
						listContainerS.addList( new InstanceList(select, experiment.getTagExec()) );
					} 
				} else {
					logger.debug("experiment " + experiment.getTagExec() + " is paused. will ignore..." );
				}
			}
			if ( listContainerS.size() > 0 ) {
				instanceJoinInputBuffer.addAll( listContainerS.merge() );
			}
			logger.debug("SELECT buffer size: " + instanceJoinInputBuffer.size() );
		}
		
	}
	
	
	public int getInstanceInputBufferSize() {
		return instanceInputBuffer.size();
	}
	
	public int getInstanceJoinInputBufferSize() {
		return instanceJoinInputBuffer.size();
	}
	
	private synchronized Instance getNextInstance( String macAddress) {
		Instance instance = getNextInstance( getRunningExperiments(), macAddress );
		if ( instance != null ) {
			logger.debug("serving instance " + instance.getSerial() + " to " + macAddress );
		} else {
			logger.debug("null instance");
		}
		return instance;
	}
	
	public synchronized Instance getNextInstance( List<Experiment> runningExperiments, String macAddress ) {
		
		this.runningExperiments = runningExperiments;
		Instance next = instanceInputBuffer.poll();
		if ( next != null ) {
			if ( next.getType() == ActivityType.SELECT ) {
				logger.error("SELECT type Instance in common buffer!");
			} else {
				if ( hasOwner(next) ) {
					instanceOutputBuffer.add( next );
				} else {
					return getNextInstance( macAddress );
				}
			}
		} else {
			//logger.debug("empty output buffer.");
		}
		return next;
	}
	
	public void setBufferSize( int bufferSize ) {
		this.bufferSize = bufferSize;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}

	public InstanceBuffer() {
		this.instanceInputBuffer = new LinkedList<Instance>();
		this.instanceJoinInputBuffer = new LinkedList<Instance>();
		this.instanceOutputBuffer = new LinkedList<Instance>();
		this.bufferSize = 300;
	}
	
	public synchronized void removeFromOutputBuffer( Instance instance ) {
		for ( Instance pipe : getInstanceOutputBuffer() ) {
			if ( pipe.getSerial().equals( instance.getSerial() ) ) {
				instanceOutputBuffer.remove( pipe );
				break;
			}
		}
	}
	
	public Instance getIntanceFromOutputBuffer( String instanceSerial ) {
		for ( Instance pipe : getInstanceOutputBuffer() ) {
			if ( pipe.getSerial().equals( instanceSerial ) ) {
				return pipe;
			}
		}
		return null;
	}
	
	public synchronized boolean experimentIsStillQueued( Experiment exp ) {
		for( Fragment frag : exp.getFragments() ) {
			for( Instance pipe : getInstanceOutputBuffer()  ) {
				if( pipe.getIdFragment() == frag.getIdFragment() ) {
					return true;
				}
			}
			
			for( Instance pipe : instanceInputBuffer  ) {
				if( pipe.getIdFragment() == frag.getIdFragment() ) {
					return true;
				}
			}
			for( Instance pipe : instanceJoinInputBuffer  ) {
				if( pipe.getIdFragment() == frag.getIdFragment() ) {
					return true;
				}
			}
		}
		return false;
	}
	
	public synchronized void reloadAfterCrash( List<Experiment> runningExperiments ) {
		this.runningExperiments = runningExperiments;
		logger.debug("after crash reloading " + runningExperiments.size() + " experiments.");
		try {
			try {
				InstanceService instanceService = new InstanceService();
				processAndInclude( instanceService.recoverFromCrash() );
				logger.debug( getInstanceInputBufferSize() + " common instances recovered.");
				logger.debug( getInstanceJoinInputBufferSize() + " JOIN instances recovered.");
			} catch ( NotFoundException e ) {
				logger.debug("no instances to recover");
			}
			
		} catch ( Exception e) {
			logger.error( e.getMessage() );
		} 
		logger.debug("after crash reload done.");
	}
	
	public synchronized Instance getNextJoinInstance( String macAddress ) {
		Instance next = instanceJoinInputBuffer.poll();
		if ( next != null ) {
			logger.debug("serving SELECT instance " + next.getSerial() + " to " + macAddress );
			instanceOutputBuffer.add(next);
		}
		return next;
	}

	public synchronized void returnToBuffer( Instance instance ) {
		logger.debug("instance refund: " + instance.getSerial() );
		if ( instanceOutputBuffer.remove( instance ) ) {
			if ( instance.getType().isJoin() ) {
				instanceJoinInputBuffer.add( instance );
				logger.debug(" > to the join buffer" );
			} else {
				instanceInputBuffer.add( instance );
				logger.debug(" > to the common buffer" );
			}
		}
	}
	
	
	/**
	 *	Discard instances in buffer that have no experiments (deleted) 
	 */
	private synchronized boolean hasOwner( Instance instance ) {
		for ( Experiment exp : getRunningExperiments() ) {
			for( Fragment frag : exp.getFragments() ) {
				if ( instance.getIdFragment() == frag.getIdFragment() ) {
					return true;
				}
			}
		}
		logger.warn("owner of instance " + instance.getSerial() + " not found. Will discard this instance.");
		return false;
	}
	
	public Queue<Instance> getInstanceInputBuffer() {
		return new LinkedList<Instance>( instanceInputBuffer );
	}

	public Queue<Instance> getInstanceOutputBuffer() {
		return new LinkedList<Instance>( instanceOutputBuffer );
	}

	public Queue<Instance> getInstanceJoinInputBuffer() {
		return new LinkedList<Instance>( instanceJoinInputBuffer );
	}
	
	private synchronized Fragment getRunningFragment( Experiment experiment ) {
		for ( Fragment frag : experiment.getFragments() ) {
			if ( frag.getStatus() == FragmentStatus.RUNNING ) {
				return frag;
			}
		}
		return null;
	}
	
	private List<Instance> loadJoinBuffer( int count, Experiment experiment ) {
		List<Instance> preBuffer = null;
		logger.debug("loading SELECT buffer. current size: " + instanceJoinInputBuffer.size());
		try {
			Fragment running = getRunningFragment( experiment );
			if ( running == null ) {
				logger.debug("no SELECT fragments running");
			} else {
				logger.debug("running SELECT fragment found: " + running.getSerial() );
				try {
					InstanceService ps = new InstanceService();
					preBuffer = ps.getHeadJoin( count, running.getIdFragment() );
					logger.debug("found " + preBuffer.size() + " Instances for experiment " + experiment.getTagExec() + " Fragment " +
					 running.getSerial() );
				} catch (NotFoundException e) {
					logger.debug("no more SELECT instances found in database for experiment " + experiment.getTagExec() +
							" Fragment " + running.getSerial() );
				}
			}
		} catch (Exception e) {
			logger.error( e.getMessage() );
		}
		return preBuffer;
	}

	private List<Instance> loadCommonBuffer( int count, Experiment experiment ) {
		List<Instance> preBuffer = null;
		logger.debug("loading common buffer...");
		try {
			Fragment running = getRunningFragment( experiment );
			if ( running == null ) {
				logger.debug("no fragments running");
			} else {
				logger.debug("running fragment found: " + running.getSerial() );
				InstanceService ps = new InstanceService();
				preBuffer = ps.getHead( count, running.getIdFragment() );
			}
		} catch (NotFoundException e) {
			logger.debug("no running instances found for experiment " + experiment.getTagExec() );
		} catch ( Exception e) {
			logger.error( e.getMessage() );
		} 
		return preBuffer;
	}
	
	

}
