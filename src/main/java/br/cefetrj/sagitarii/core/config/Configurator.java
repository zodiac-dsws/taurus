package br.cefetrj.sagitarii.core.config;

import java.io.InputStream;
import java.lang.management.ManagementFactory;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Configurator {

	private int poolIntervalSeconds;
	private int fileReceiverPort;
	private int fileReceiverChunkBufferSize;
	private int pseudoClusterIntervalSeconds;
	private Document doc;
	private int pseudoMaxTasks;
	private int maxInputBufferCapacity;
	private int mainNodesQuant;
	private static Configurator instance;
	private char CSVDelimiter;
	private long firstDelayLimitSeconds;
	private boolean useDynamicLoadBalancer;
	private String userName;
	private String password;
	private String databaseName;
	private String hadoopConfigPath;
	
	public String getHadoopConfigPath() {
		return hadoopConfigPath;
	}
	
	public String toXml() {
		StringBuilder xmlData = new StringBuilder();
		xmlData.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		xmlData.append("<configuration><sagitarii>");
		xmlData.append("<poolIntervalSeconds>"+poolIntervalSeconds+"</poolIntervalSeconds>");
		xmlData.append("<pseudoClusterIntervalSeconds>"+pseudoClusterIntervalSeconds+"</pseudoClusterIntervalSeconds>");
		xmlData.append("<pseudoMaxTasks>"+pseudoMaxTasks+"</pseudoMaxTasks>");
		xmlData.append("<maxInputBufferCapacity>"+maxInputBufferCapacity+"</maxInputBufferCapacity>");
		xmlData.append("<mainNodesQuant>"+mainNodesQuant+"</mainNodesQuant>");
		xmlData.append("<fileReceiverPort>"+fileReceiverPort+"</fileReceiverPort>");
		xmlData.append("<fileReceiverChunkBufferSize>"+fileReceiverChunkBufferSize+"</fileReceiverChunkBufferSize>");
		xmlData.append("<firstDelayLimitSeconds>"+firstDelayLimitSeconds+"</firstDelayLimitSeconds>");
		xmlData.append("<CSVDelimiter>"+CSVDelimiter+"</CSVDelimiter>");
		xmlData.append("<useDynamicLoadBalancer>"+useDynamicLoadBalancer+"</useDynamicLoadBalancer>");
		xmlData.append("<databaseName>"+databaseName+"</databaseName>");
		xmlData.append("<userName>"+userName+"</userName>");
		xmlData.append("<password>"+password+"</password>");
		xmlData.append("</sagitarii></configuration>");		
		return xmlData.toString();
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public boolean useDynamicLoadBalancer() {
		return this.useDynamicLoadBalancer;
	}
	
	public long getFirstDelayLimitSeconds() {
		return firstDelayLimitSeconds;
	}
	
    public double getProcessCpuLoad() {
    	try {
	        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
	        ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
	        AttributeList list = mbs.getAttributes(name, new String[]{ "SystemCpuLoad" });
	        if (list.isEmpty())  return 0;
	        Attribute att = (Attribute)list.get(0);
	        Double value = (Double)att.getValue();
	        if (value == -1.0) return 0; 
	        return ((int)(value * 1000) / 10.0);
    	} catch (MalformedObjectNameException | ReflectionException | InstanceNotFoundException e) {
    		return 0;
    	}
    }   	

	public char getCSVDelimiter() {
		return CSVDelimiter;
	}
	
	public int getFileReceiverChunkBufferSize() {
		return fileReceiverChunkBufferSize;
	}
	
	public int getFileReceiverPort() {
		return fileReceiverPort;
	}
	
	public int getMaxInputBufferCapacity() {
		return maxInputBufferCapacity;
	}
	
	public int getMainNodesQuant() {
		return mainNodesQuant;
	}
	
	public int getPseudoMaxTasks() {
		return pseudoMaxTasks;
	}

	public int getPseudoClusterIntervalSeconds() {
		return pseudoClusterIntervalSeconds;
	}

	public int getPoolIntervalSeconds() {
		return poolIntervalSeconds;
	}

	private String getTagValue(String sTag, Element eElement) throws Exception{
		try {
			NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	        Node nValue = (Node) nlList.item(0);
			return nValue.getNodeValue();
		} catch ( Exception e ) {
			System.out.println("Error: Element " + sTag + " not found in configuration file.");
			throw e;
		}
	 }
	
	public String getValue(String container, String tagName) {
		String tagValue = "";
		try {
			NodeList postgis = doc.getElementsByTagName(container);
			Node pgconfig = postgis.item(0);
			Element pgElement = (Element) pgconfig;
			tagValue = getTagValue(tagName, pgElement) ; 
		} catch ( Exception e ) {
		}
		return tagValue;
	}

	public long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	public long getTotalMemory() {
		return Runtime.getRuntime().totalMemory();
	}
	
	public static Configurator getInstance() throws Exception {
		if ( instance == null ) {
			throw new Exception("Configurator not initialized");
		}
		return instance;
	}
	
	public static Configurator getInstance( String file ) throws Exception {
		if ( instance == null ) {
			instance = new Configurator(file);
		}
		return instance;
	}
	
	private Configurator(String file) throws Exception {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			loadMainConfig();
		  } catch (Exception e) {
				System.out.println("Error: XML file " + file + " not found.");
		  }			
	}
	
	
	public void loadMainConfig()  {
		NodeList mapconfig = doc.getElementsByTagName("sagitarii");
		Node mpconfig = mapconfig.item(0);
		Element mpElement = (Element) mpconfig;
		try {
			poolIntervalSeconds = Integer.valueOf( getTagValue("poolIntervalSeconds", mpElement) );
			pseudoClusterIntervalSeconds = Integer.valueOf( getTagValue("pseudoClusterIntervalSeconds", mpElement) );
			pseudoMaxTasks = Integer.valueOf( getTagValue("pseudoMaxTasks", mpElement) );
			maxInputBufferCapacity = Integer.valueOf( getTagValue("maxInputBufferCapacity", mpElement) );
			mainNodesQuant = Integer.valueOf( getTagValue("mainNodesQuant", mpElement) );
			fileReceiverPort = Integer.valueOf( getTagValue("fileReceiverPort", mpElement) );
			fileReceiverChunkBufferSize = Integer.valueOf( getTagValue("fileReceiverChunkBufferSize", mpElement) );
			firstDelayLimitSeconds = Long.valueOf( getTagValue("firstDelayLimitSeconds", mpElement) );
			
			userName = getTagValue("userName", mpElement);
			password = getTagValue("password", mpElement);
			databaseName = getTagValue("databaseName", mpElement);
			hadoopConfigPath = getTagValue("hadoopConfigPath", mpElement);
			useDynamicLoadBalancer = Boolean.valueOf( getTagValue("useDynamicLoadBalancer", mpElement).toLowerCase() );
			
			CSVDelimiter = getTagValue("CSVDelimiter", mpElement).charAt(0);
		} catch ( Exception e ) {
			System.out.println( e.getMessage() );
		}
	}

	public void setPoolIntervalSeconds(int poolIntervalSeconds) {
		this.poolIntervalSeconds = poolIntervalSeconds;
	}

	public void setPseudoClusterIntervalSeconds(int pseudoClusterIntervalSeconds) {
		this.pseudoClusterIntervalSeconds = pseudoClusterIntervalSeconds;
	}

	public void setPseudoMaxTasks(int pseudoMaxTasks) {
		this.pseudoMaxTasks = pseudoMaxTasks;
	}

	public void setMaxInputBufferCapacity(int maxInputBufferCapacity) {
		this.maxInputBufferCapacity = maxInputBufferCapacity;
	}

	public void setMainNodesQuant(int mainNodesQuant) {
		this.mainNodesQuant = mainNodesQuant;
	}

	public static void setInstance(Configurator instance) {
		Configurator.instance = instance;
	}

	public void setFileReceiverPort(int fileReceiverPort) {
		this.fileReceiverPort = fileReceiverPort;
	}

	public void setFileReceiverChunkBufferSize(int fileReceiverChunkBufferSize) {
		this.fileReceiverChunkBufferSize = fileReceiverChunkBufferSize;
	}

	public void setCSVDelimiter(char cSVDelimiter) {
		CSVDelimiter = cSVDelimiter;
	}

	public void setFirstDelayLimitSeconds(long firstDelayLimitSeconds) {
		this.firstDelayLimitSeconds = firstDelayLimitSeconds;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	
	
	
	
	
}
