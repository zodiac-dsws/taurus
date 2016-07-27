
package br.cefetrj.sagitarii.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;

@Action (value = "indexRedir", results = { @Result (location = "workflows.jsp", name = "ok") 
}, interceptorRefs= { @InterceptorRef("seguranca")} ) 

@ParentPackage("default")
public class IndexRedirAction extends BasicActionClass {

	
	public String execute () {

		
		return "ok";
	}


}
