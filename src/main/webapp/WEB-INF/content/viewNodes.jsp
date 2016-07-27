<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ include file="../../header.jsp"%>

<script src="js/nodewebsocket.js"></script>


<div id="leftBox">
	<div id="bcbMainButtons" class="basicCentralPanelBar">
		<%@ include file="buttons.jsp"%>
	</div>

	<div id="basicCentralPanel">
	
		<div class="basicCentralPanelBar">
			<img src="img/node.png">
			<div class="basicCentralPanelBarText">Active Cores</div>
		</div>
			
		<div class="menuBarMain">
			<img alt="" onclick="back();" title="Back" class="button dicas" src="img/back.png" />
			<img onclick="clean();" title="Clean all workspaces" class="button dicas" src="img/clean.png"> 
			<img onclick="reloadWrappers();" title="Force nodes to reload all wrappers" class="button dicas" src="img/refresh.png"> 
			<img onclick="openMultipleConsole();" title="Open SSH terminal for all nodes" class="button dicas" src="img/bash.png">
		</div>

		<div id="nodeContainer"	style="height: 220px; display: table; width: 95%; margin: 0 auto">
			<div class="nodeBar" style="width: 95%;">
				<table id="coreTable" style="margin-top: 20px; width: 100%;">
				</table>
			</div>
		</div>

	</div>
</div>
<div id="rightBox">

	<%@ include file="commonpanel.jsp"%>

</div>


<script>

	$(document).ready(function() {
		reloadDicas();
		startWebSocket('${serverBaseUrl}');
	});
	
	function back() {
		window.history.back();
	}	
	
</script>

<%@ include file="../../footer.jsp"%>
