<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../../header.jsp" %>

				<div id="leftBox" > 
				
					<div id="bcbMainButtons" class="basicCentralPanelBar">
						<%@ include file="buttons.jsp" %>
					</div>
					
					<div id="basicCentralPanel">
					
						<div class="basicCentralPanelBar">
							<img src="img/workflow.png">
							<div class="basicCentralPanelBarText">Manage Workflows</div>
						</div>
						
						
						<div class="menuBarMain">
							<img onclick="showNewPannel();" title="New Workflow" class="button dicas" src="img/add.png">
							<img onclick="importWf();" title="Import Workflow from XML" class="button dicas" src="img/xml.png">
						</div>


						<div id="newPannel" style="display:none; height:100px; width:95%; margin:0 auto;margin-top:10px;margin-bottom:10px;">
							<form action="doNewWf" method="POST" id="formPost">
								<table>
									<tr>
										<td class="tableCellFormLeft">Label</td>
										<td class="tableCellFormRight"> 
											<input id="wfTag" name="wf.tag" class="tableCellFormInputText" type="text"> 
										</td>
									</tr>

									<tr>
										<td class="tableCellFormLeft">Description</td>
										<td class="tableCellFormRight"> 
											<input id="wfDescription" name="wf.description" class="tableCellFormInputText" type="text"> 
										</td>
									</tr>
								</table>
							</form>
							<div onclick="doPost()" class="basicButton">Create</div>							
							<div onclick="cancelNewPanel()" class="basicButton">Cancel</div>							
						
						</div>

						<div id="editPannel" style="display:none; height:100px; width:95%; margin:0 auto;margin-top:10px;margin-bottom:10px;">
							<form action="doEditWf" method="POST" id="formPostEdit">
							<input id="wfIdEdt" name="wf.idWorkflow" type="hidden">
								<table>
									<tr>
										<td class="tableCellFormLeft">Label</td>
										<td class="tableCellFormRight"> 
											<input id="wfTagEdt" name="wf.tag" class="tableCellFormInputText" type="text"> 
										</td>
									</tr>

									<tr>
										<td class="tableCellFormLeft">Description</td>
										<td class="tableCellFormRight"> 
											<input id="wfDescriptionEdt" name="wf.description" class="tableCellFormInputText" type="text"> 
										</td>
									</tr>
								</table>
							</form>
							<div onclick="doPostEdit()" class="basicButton">Update</div>							
							<div onclick="cancelEditPanel()" class="basicButton">Cancel</div>							
						
						</div>



						<div style="margin : 0 auto; width : 95%; margin-top:10px;"  >
							<table class="tableForm"  id="example" >
								<thead>
									<tr>
										<th>Description</th>
										<th>Label</th>
										<th>Owner</th>
										<th>Exps</th>
										<th>&nbsp;</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="workflow" items="${wfList}">
										<tr>
											<td>${workflow.description}</td>
											<td>${workflow.tag}</td>
											<td>${workflow.owner.loginName}</td>
											<td>${fn:length(workflow.experiments)}</td>
											<td>
												<img class="miniButton dicas" title="Export to XML" onclick="exportWf('${workflow.tag}')" src="img/xml.png">

												<c:if test="${loggedUser.type == 'ADMIN'}">
													<img class="miniButton dicas" title="Delete" onclick="deleteWf('${workflow.idWorkflow}','${workflow.tag}')" src="img/delete.png">
													<img class="miniButton dicas" title="Edit" onclick="edit('${workflow.idWorkflow}','${workflow.tag}','${workflow.description}');" src="img/edit.png">
													<img class="miniButton dicas" title="Details" onclick="viewWorkflow('${workflow.idWorkflow}')" src="img/search.png">
													<img class="miniButton dicas" title="Manage Activities" onclick="activity('${workflow.idWorkflow}')" src="img/family3.png">
													<span style="float:right; height: 20px; margin-left:0px; margin-right: 8px; border-right:1px dotted #ADADAD" >&nbsp;</span>
												</c:if>

												<c:if test="${fn:length(workflow.activitiesSpecs) != '' }">
													<img class="miniButton dicas" style="margin-right:0px" title="Generate New Experiment" onclick="newExperiment('${workflow.idWorkflow}')" src="img/add.png">
												</c:if>
												<c:if test="${fn:length(workflow.activitiesSpecs) == '' }">
													<img class="miniButton dicas" style="margin-right:0px;opacity:0.3;border-right:1px dotted #ADADAD" title="New Experiment: Missing Activities" src="img/add.png">
												</c:if>
												
											</td>
											
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>

					</div>												
					
				</div>
				
				<div id="rightBox"> 
					<%@ include file="commonpanel.jsp" %>
				</div>
				
				
<script>

	function doPost() {
		var tag = $("#wfTag").val();
		var desc = $("#wfDescription").val();
		if ( (tag == '') || ( desc == '' ) ) {
			showMessageBox('Please fill all required fields.');
			return;
		} 
		if ( tag.indexOf(' ') >= 0 ) {
			$("#wfTag").focus();
			showMessageBox("The Workflow Tag cannot have white spaces.");
			return false;
		}
		$("#formPost").submit();
	}

	function doPostEdit() {
		var tag = $("#wfTagEdt").val();
		var desc = $("#wfDescriptionEdt").val();
		if ( (tag == '') || ( desc == '' ) ) {
			showMessageBox('Please fill all required fields.');
			return;
		} 
		if ( tag.indexOf(' ') >= 0 ) {
			$("#wfTagEdt").focus();
			showMessageBox("The Workflow Tag cannot have white spaces.");
			return false;
		}
		$("#formPostEdit").submit();
	}

	function edit(id, tag, description) {
		$("#wfIdEdt").val(id);
		$("#wfTagEdt").val(tag);
		$("#wfDescriptionEdt").val(description);
		$("#editPannel").css("display","block");
	}
	
	function deleteWf(idWf, tag) {
		showDialogBox( "The Workflow "+tag+" will be deleted. Are you sure?", "deleteWorkflow?idWorkflow=" + idWf );
	}

	function cancelNewPanel() {
		$("#newPannel").css("display","none");
	}
	
	function cancelEditPanel() {
		$("#editPannel").css("display","none");
	}

	function viewWorkflow(idWf) {
		window.location.href="viewWorkflow?idWorkflow=" + idWf;
	}
	
	function exportWf(idWf) {
		window.location.href="getWorkflowXML?workflowAlias=" + idWf;
	}
	
	function newExperiment(idWf) {
		window.location.href="newExperiment?idWorkflow=" + idWf;
	}
	
	function activity(idWf) {
		window.location.href="actManager?idWorkflow=" + idWf;
	}
	
	function importWf(idWf) {
		window.location.href="importWorkflowXML";
	}

	function showNewPannel() {
		$("#newPannel").css("display","block");
	}

	$(document).ready(function() {
		$('#example').dataTable({
	        "oLanguage": {
	            "sUrl": "js/pt_BR.txt"
	        },	
	        "iDisplayLength" : 10,
			"bLengthChange": false,
			"fnInitComplete": function(oSettings, json) {
				doTableComplete();
			},
			"bAutoWidth": false,
			"sPaginationType": "full_numbers",
			"aoColumns": [ 
						  { "sWidth": "30%" },
						  { "sWidth": "25%" },
						  { "sWidth": "10%" },
						  { "sWidth": "5%" },
						  { "sWidth": "30%" }]						
		} ).fnSort( [[0,'desc']] );
	} );	
	
</script>				
				
<%@ include file="../../footer.jsp" %>
				