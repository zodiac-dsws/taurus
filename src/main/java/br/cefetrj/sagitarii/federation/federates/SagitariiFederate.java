package br.cefetrj.sagitarii.federation.federates;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.cefetrj.sagitarii.core.InstanceBuffer;
import br.cefetrj.sagitarii.core.types.ExperimentStatus;
import br.cefetrj.sagitarii.core.types.InstanceStatus;
import br.cefetrj.sagitarii.federation.Environment;
import br.cefetrj.sagitarii.federation.RTIAmbassadorProvider;
import br.cefetrj.sagitarii.federation.classes.CoreClass;
import br.cefetrj.sagitarii.federation.classes.FinishedInstanceInteractionClass;
import br.cefetrj.sagitarii.federation.classes.RequestTaskInteractionClass;
import br.cefetrj.sagitarii.federation.classes.RunInstanceInteractionClass;
import br.cefetrj.sagitarii.federation.classes.SagitariiClass;
import br.cefetrj.sagitarii.federation.classes.TeapotClass;
import br.cefetrj.sagitarii.federation.objects.CoreObject;
import br.cefetrj.sagitarii.misc.PathFinder;
import br.cefetrj.sagitarii.misc.ZipUtil;
import br.cefetrj.sagitarii.persistence.entity.Experiment;
import br.cefetrj.sagitarii.persistence.entity.Instance;
import br.cefetrj.sagitarii.persistence.services.InstanceService;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;

public class SagitariiFederate {
	private static SagitariiFederate instance;
	
	private Logger logger = LogManager.getLogger( this.getClass().getName() );
	private String rootPath;
	private SagitariiClass sagitariiClass;
	private TeapotClass teapotClass;
	private CoreClass coreClass;
	private RequestTaskInteractionClass requestTaskInteractionClass;
	private RunInstanceInteractionClass runInstanceInteractionClass;
	private FinishedInstanceInteractionClass finishedInstanceInteractionClass;

	public FinishedInstanceInteractionClass getFinishedInstanceInteractionClass() {
		return finishedInstanceInteractionClass;
	}
	
	public RequestTaskInteractionClass getRequestTaskInteractionClass() {
		return requestTaskInteractionClass;
	}
	
	public RunInstanceInteractionClass getRunInstanceInteractionClass() {
		return runInstanceInteractionClass;
	}
	
	// ==== OLD SAGITARII ========================
	private InstanceBuffer instanceBuffer;
	private List<Experiment> runningExperiments;
	
	// ========================= OLD SAGITARII STUFF ================================
	
	public void loadBuffers() throws Exception {
		instanceBuffer.loadBuffers();
	}
	
	public void setMaxInputBufferCapacity(int maxInputBufferCapacity) {
		instanceBuffer.setBufferSize( maxInputBufferCapacity );
	}
	
	public int getMaxInputBufferCapacity() {
		return instanceBuffer.getBufferSize();
	}
	
	public synchronized List<Experiment> getRunningExperiments() {
		return new ArrayList<Experiment>( runningExperiments );
	}
	
	public void setRunningExperiments(List<Experiment> runningExperiments) {
		this.runningExperiments = runningExperiments;
	}
	
	public synchronized void addRunningExperiment( Experiment experiment ) throws Exception {
		boolean found = false;
		for ( Experiment exp : runningExperiments ) {
			if ( exp.getTagExec().equalsIgnoreCase( experiment.getTagExec() ) ) {
				found = true;
			}
		}
		if ( !found && ( experiment.getStatus() == ExperimentStatus.RUNNING ) ) {
			runningExperiments.add( experiment );
			//updateFragments();
		}
	}	
	
	public boolean isRunning() {
		return ( runningExperiments.size() > 0 );
	}	
	
	public void reloadAfterCrash() {
		instanceBuffer.reloadAfterCrash( runningExperiments );
	}	
	
	public synchronized void finishInstance( ParameterHandleValueMap theParameters ) throws Exception {
		String nodeSerial = finishedInstanceInteractionClass.getNodeSerial( theParameters );
		String instanceSerial = finishedInstanceInteractionClass.getInstanceSerial( theParameters );
		
		logger.debug("Node " + nodeSerial + " finished instance " + instanceSerial );
		
		Instance instance = instanceBuffer.getIntanceFromOutputBuffer(instanceSerial);
		if ( instance != null ) {
			finishInstance( instance );
		} else {
			logger.error("instance " + instanceSerial + " is not in output buffer.");
		}
		

	}
	
	private void finishInstance( Instance instance ) {
		logger.debug("instance " + instance.getSerial() + " is finished by " + instance.getExecutedBy() +
				". execution time: " + instance.getElapsedTime() );
		try {
			// Set as finished (database)
			InstanceService instanceService = new InstanceService();
			instanceService.finishInstance( instance );
			// Remove from output buffer if any
			instanceBuffer.removeFromOutputBuffer( instance );
		} catch ( Exception e ) {
			logger.error( e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public synchronized Instance getNextInstance(String macAddress) {
		return instanceBuffer.getNextInstance( runningExperiments, macAddress );
	}

	
	public synchronized void returnToBuffer( Instance instance ) {
		instance.triedAgain();
		instanceBuffer.returnToBuffer(instance);
	}
	
	public synchronized Instance getNextJoinInstance( String macAddress) {
		return instanceBuffer.getNextJoinInstance( macAddress);
	}	
	
	
	// ==============================================================================

	public static SagitariiFederate getInstance() throws Exception {
		if ( instance == null ) {
			instance = new SagitariiFederate();
		}
		return instance;
	}
	
	public void finishFederationExecution() throws Exception {
		logger.debug( "Will try to finish Federation execution" );
		RTIambassador rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();

		rtiamb.resignFederationExecution( ResignAction.DELETE_OBJECTS );
		try	{
			rtiamb.destroyFederationExecution( "Sagitarii" );
			logger.debug( "Destroyed Federation" );
		} catch( FederationExecutionDoesNotExist dne ) {
			logger.debug( "No need to destroy federation, it doesn't exist" );
		} catch( FederatesCurrentlyJoined fcj ){
			logger.debug( "Didn't destroy federation, federates still joined" );
		}		
	}
	
	
	private SagitariiFederate( ) throws Exception {
		instanceBuffer = new InstanceBuffer();
		runningExperiments = new ArrayList<Experiment>();
		rootPath = PathFinder.getInstance().getPath();
	}
	
	private void startFederate() {
		logger.debug("Starting Sagitarii");
		try {

			Map<String, String> newenv = new HashMap<String, String>();
			newenv.put("RTI_HOME", "");
			//newenv.put("RTI_RID_FILE", rootPath + "/rti.RID" );
			Environment.setEnv( newenv );
			
			RTIambassador rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();
			
			try	{
				URL[] modules = new URL[]{
					(new File( rootPath + "/foms/HLAstandardMIM.xml" ) ).toURI().toURL()
				};
				rtiamb.createFederationExecution("Sagitarii", modules );
			} catch( FederationExecutionAlreadyExists exists ) {
				logger.debug("Federation already exists. Bypassing...");
			}
			
			join();
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private String fillInstanceID( Instance instance ) {
		String content = instance.getContent();
		try {
			content = instance.getContent().replace("%ID_PIP%", String.valueOf( instance.getIdInstance() ) );
		} catch ( Exception e ) {
			logger.error("Error setting Instance ID to instance content tag.");
		}
		return content.replace("##TAG_ID_INSTANCE##", String.valueOf( instance.getIdInstance() ) );
	}	
	
	private String encode( Instance instance ) {
		instance.setStartDateTime( Calendar.getInstance().getTime() );
		instance.setStatus( InstanceStatus.WAITING );
		String content = fillInstanceID ( instance );
		instance.setContent( content );
		byte[] respCompressed = ZipUtil.compress( content );
		String respHex = ZipUtil.toHexString( respCompressed );
		logger.debug( " > instance "+ instance.getSerial() + " compressed and control tags replaced." );	
		return respHex;
	}
	
	public void sendInstancesToNode( ParameterHandleValueMap theParameters ) throws Exception {
		String nodeSerial = requestTaskInteractionClass.getNodeSerial( theParameters );
		String taskType = requestTaskInteractionClass.getTaskType( theParameters );
		
		if ( !nodeSerial.equals("") ) {
			CoreObject core = coreClass.getCore( nodeSerial );
			if ( core != null ) {
				Instance instance = null;
				
				if ( taskType.equals("teapot")) {
					instance = getNextInstance( core.getOwnerNode() );
				}
				
				if ( taskType.equals("nunki")) {
					instance = getNextJoinInstance( core.getOwnerNode() );
				}

				if ( instance != null ) {
					String hexEncodedInstance = encode( instance );
					runInstanceInteractionClass.send( hexEncodedInstance, nodeSerial );
				} else {
					// no instance
				}
				
			}
			
		}
	}
	
	public void startServer() throws Exception {
		logger.debug("starting server...");
		startFederate();
		if ( sagitariiClass == null ) {
			
			sagitariiClass = new SagitariiClass();
			// Publish server attributes
			sagitariiClass.publish();
			// Create a new Server Object
			sagitariiClass.createNew();
			// Send Server attributes to the RTI.  
			sagitariiClass.updateAttributeValues();
			
			// Subscribe to Teapot Node Updates
			teapotClass = new TeapotClass();
			teapotClass.subscribe();
			
			// Subscribe to Cores updates
			coreClass = new CoreClass();
			coreClass.subscribe();

			// Interactions
			// Subscribe to listen to task rrequests from Cores.
			requestTaskInteractionClass = new RequestTaskInteractionClass();
			requestTaskInteractionClass.subscribe();
			// Publish run instance command
			runInstanceInteractionClass = new RunInstanceInteractionClass();
			runInstanceInteractionClass.publish();
			
			// Subscribe to know when a core finished an instance
			finishedInstanceInteractionClass = new FinishedInstanceInteractionClass();
			finishedInstanceInteractionClass.subscribe();
			
			logger.debug("done.");
			
		} else {
			logger.warn("server is already running an instance");
		}
	}
	
	public SagitariiClass getSagitariiClass() {
		return sagitariiClass;
	}
	
	public TeapotClass getTeapotClass() {
		return teapotClass;
	}
	
	public CoreClass getCoreClass() {
		return coreClass;
	}
	
	private void join() throws Exception {
		RTIambassador rtiamb = RTIAmbassadorProvider.getInstance().getRTIAmbassador();

		logger.debug("joing Federation Execution using FOM SagitariiFederation.xml...");
		URL[] joinModules = new URL[]{
		    (new File(rootPath +  "/foms/SagitariiFederation.xml")).toURI().toURL()
		};
		rtiamb.joinFederationExecution( "Sagitarii", "SagitariiType", "Sagitarii", joinModules );           
	}
	
}
