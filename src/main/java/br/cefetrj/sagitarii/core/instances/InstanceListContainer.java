package br.cefetrj.sagitarii.core.instances;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sagitarii.misc.Activation;
import br.cefetrj.sagitarii.misc.XMLParser;
import br.cefetrj.sagitarii.persistence.entity.Instance;

public class InstanceListContainer {
	private List<InstanceList> lists;
	
	public void clear() {
		lists.clear();
	}
	
	public int size() {
		return lists.size();
	}
	
	public void addList( InstanceList list ) {
		lists.add( list );
	}
	
	public InstanceListContainer() {
		lists = new ArrayList<InstanceList>();
	}
	
	private int getBiggerSize() {
		int res = 0;
		for ( InstanceList list : lists ) {
			if ( list.size() > res ) {
				res = list.size();
			}
		}
		return res;
	}
	
	public List<Instance> merge() {
		List<Instance> res = new ArrayList<Instance>();
	    int bigger = getBiggerSize();
	    for ( int index = 0; index < bigger; index++  ) {
	    	for ( InstanceList list : lists ) {
	    		if ( index < list.size() ) {
	    			Instance instance = list.get(index); 
	    			res.add( instance );
	    		}
	    	}
	    }
	    return optimize( res );
	}
	
	private String getHashSHA1( byte[] subject ) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] digest = md.digest( subject );
		
		String result = "";
		for (int i=0; i < digest.length; i++) {
			result +=
				Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
	
	private String getInstanceHash( Instance instance ) {
		String hash = null;
		try {
			List<Activation> activations = new XMLParser().parseActivations( instance.getContent() );
			StringBuilder sb = new StringBuilder();
			for ( Activation act : activations ) {
				sb.append( act.getExecutor() );
			}
			hash = getHashSHA1( sb.toString().getBytes() );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}
	
	private List<Instance> optimize( List<Instance> instances ) {
		/*
		try {
	    	for ( Instance instance : instances ) {
	    		String hash = getInstanceHash( instance );
	    		Accumulator acc = AgeCalculator.getInstance().getAccumulator(hash);
	    		if ( acc != null ) {
	    			 // Do something with estimated time: instance.getSerial() // acc.getAverageAgeAsText() 
	    		} else {
	    			// Accumulator not found
	    		}
	    	}
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		*/
		return instances;
	}
	

}
