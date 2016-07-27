package br.cefetrj.sagitarii.core;

import br.cefetrj.sagitarii.federation.federates.SagitariiFederate;

/**
 * Main system heartbeat
 * @author Carlos Magno Abreu
 *
 */
public class MainHeartBeat implements Runnable {
	
    @Override
    public void run() {
    	try {
    		SagitariiFederate.getInstance().loadBuffers();
    	} catch ( Exception e ) {
    		
    	}
    }
	

}
