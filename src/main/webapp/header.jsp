<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<title>Sagitarii</title>
		<link rel="stylesheet" href="css/jquery.dataTables.css" type="text/css"/>
		<link rel="stylesheet" href="css/style.css?id=450" type="text/css"/>
		<link rel="stylesheet" href="css/codemirror.css" type="text/css" />
		<link rel="stylesheet" href="css/tipTip.css" type="text/css"/>	


		<script src="js/jquery-1.11.0.min.js"></script>
		<script src="js/jquery.tipTip.minified.js"></script>	
		<script src="js/script.js?id=450"></script>
		<script src="js/jquery.dataTables.js"></script>
		<script src="js/cytoscape.min.js"></script>
		<script src="js/nodes.js?id=450"></script>
		
		<script type="text/javascript" src="js/codemirror.js"></script>
		<script type="text/javascript" src="js/xml.js"></script>
		<script type="text/javascript" src="js/sql.js"></script>
		<script type="text/javascript" src="js/shell.js"></script>
		
	</head>
	
	<body>
		<div id="mainContainer" >
		
			<div id="topBar"> 
			
				<div style="margin-left:10px;float:left;margin-top:5px;height:70px;width:170px;" id="systemLogo">
					<img src="img/logos/new-logo.png" style="background-color:white;padding-right:10px;padding-left:10px; height:67px;" > 
				</div>
			
				<div class="userBoard" style="background-color:#FFF; border:0px; height: 30px; margin-top: 0; position: absolute; right: 13px; top: 4px; width: 200px;">
					<div style="float:left; width:160px">
						<div class="userBoardT1" style="color:#ADADAD;text-align:right">
							<a target="__BLANK" href="upload.zip">Data Upload Tool v1.0</a>
						</div>
					</div>							
				</div>

			</div>

			<div id="msgBar">
				<div id="msgBarBody" >
					<span id="msgText"></span> 
				</div>
			</div>

			<div id="centerContainer" > 
				<div id="blockAllPanel"></div>
			
			
