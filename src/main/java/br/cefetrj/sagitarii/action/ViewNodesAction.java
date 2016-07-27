
package br.cefetrj.sagitarii.action;

import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

import br.cefetrj.sagitarii.federation.federates.SagitariiFederate;
import br.cefetrj.sagitarii.federation.objects.CoreObject;
import br.cefetrj.sagitarii.federation.objects.TeapotObject;

@Action (value = "viewNodes", results = { @Result (location = "viewNodes.jsp", name = "ok") 
}, interceptorRefs= { @InterceptorRef("seguranca")	 } ) 

@ParentPackage("default")
public class ViewNodesAction extends BasicActionClass {
	private List<TeapotObject> nodes;
	private List<CoreObject> cores;
	
	
	public String execute () {
		
		try {
			nodes = SagitariiFederate.getInstance().getTeapotClass().getNodes();
			cores =  SagitariiFederate.getInstance().getCoreClass().getCores();
		} catch ( Exception e ) {
			
		}
		return "ok";
	}

	public List<TeapotObject> getNodes() {
		return nodes;
	}
	
	public List<CoreObject> getCores() {
		return cores;
	}

}
