package sk.stuba.fei.weblab.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.primefaces.component.panel.Panel;
import org.primefaces.event.DragDropEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.LineChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient;
import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterException;

//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@Named
@SessionScoped
public class LinechartBean implements Serializable {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LinechartBean.class);
	

	private List<CartesianChartModel> models;
	private List<LineChartSeries> series;
	private int time = 0;
	private List<Double> xValuesWindowSizes;
	private boolean running = false;
	private List<Double> minX;
	private List<Double> maxX;
//	private transient Map<String, LineChart> linechartComponents;
	
	private boolean errorDialogVisible = false;
	private String errorDialogText;
	
//	private String matlabAdapterLocation = "http://192.168.56.101:8080";
	private String matlabAdapterLocation = "http://147.175.125.59:8080";
//	private String modelLocation = "vrmaglev";
	private String modelLocation = "schema_simple";
	private String modelName;
	
	private String setParamObjectName = "Generator/Pulse Generator";
	private String setParamParamName = "Amplitude";
	private String setParamParamValue = "0.8";
	
//	private boolean buildModel = false;
	private boolean buildModel = true;
	
//	private Map<String, Map<Double, double[][]>> graphDataAccumulator;
	private LinkedList<Map<String, Map<Double, double[][]>>> graphDataAccumulator;

	public LinechartBean() {
		models = new ArrayList<CartesianChartModel>();
		models.add(new CartesianChartModel());
		models.add(new CartesianChartModel());
		series = new ArrayList<LineChartSeries>();
		series.add(new LineChartSeries());
		series.add(new LineChartSeries());
		series.add(new LineChartSeries());
		series.add(new LineChartSeries());

		
		series.get(0).setLabel("hodnota 1");
		series.get(1).setLabel("hodnota 2");
		series.get(2).setLabel("hodnota 3");
		series.get(3).setLabel("hodnota 4");
		
		series.get(0).set(time, (int)Math.random()*1);
		series.get(1).set(time, (int)(Math.random()*1));
		series.get(2).set(time, (int)Math.random()*10);
		series.get(3).set(time, (int)(Math.random()*10));
		
		models.get(0).addSeries(series.get(0));
		models.get(0).addSeries(series.get(1));
		models.get(1).addSeries(series.get(2));
		models.get(1).addSeries(series.get(3));
		
		xValuesWindowSizes = new ArrayList<Double>();
		xValuesWindowSizes.add(Double.valueOf(2));
		xValuesWindowSizes.add(Double.valueOf(15));
		
		minX = new ArrayList<Double>();
		maxX = new ArrayList<Double>();
		minX.add(Double.valueOf(0));
		maxX.add(xValuesWindowSizes.get(0));
		minX.add(Double.valueOf(0));
		maxX.add(xValuesWindowSizes.get(1));
		
//		linechartComponents = new HashMap<String,LineChart>();
		
	}
	

//	public LineChart getLinechartComponent() {
//		return linechartComponents;
//	}
//
//
//	public void setLinechartComponents(LineChart linechartComponents) {
//		this.linechartComponents = linechartComponents;
//	}

	


	public boolean isRunning() {
		return running;
	}


	public void setRunning(boolean running) {
		this.running = running;
		
		Response response = null;
		if(running == true) {
			graphDataAccumulator = new LinkedList<Map<String, Map<Double,double[][]>>>();
			series.get(0).getData().clear();
			series.get(1).getData().clear();
			
			modelName = modelLocation;
			int lastSeparatorPosition = modelLocation.lastIndexOf("/");
			if(lastSeparatorPosition != -1) {
				modelName = modelLocation.substring(lastSeparatorPosition+1);
			}
			response = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/startSimulation?modelName="+modelName).get();
		} else {
			response = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/stopSimulation").get();
		}
		
		if(response.getStatus() != Status.OK.getStatusCode()) {
			errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
			errorDialogVisible = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
					"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
		}
	
	}


	public double getTime() {
		return time;
	}


	public void setTime(double time) {
		this.time = (int)time;
	}

	public List<CartesianChartModel> getModel() {
		return models;
	}
	
	public void updateModel() {
		time++;
		
//		series.get(0).set(time, (int)(Math.random()*10));
//		series.get(1).set(time, (int)(Math.random()*10));
		series.get(2).set(time, (int)(Math.random()*10));
		series.get(3).set(time, (int)(Math.random()*10));
//		series.get(0).getData().remove(time-xValuesWindowSizes.get(0));
//		series.get(1).getData().remove(time-xValuesWindowSizes.get(0));
		series.get(2).getData().remove(time-xValuesWindowSizes.get(1));
		series.get(3).getData().remove(time-xValuesWindowSizes.get(1));
		
		
//		if(time>X_VALUES_WINDOW) {
////			LineChart linechart = (LineChart)FacesContext
////					.getCurrentInstance().getViewRoot().findComponent("linechart");
////			linechart.setMinX(time-CHART_SIZE);
////			linechart.setMaxX(time);
//			
////			Map<String, Object> linechartAttributes = 
////				FacesContext.getCurrentInstance().getViewRoot().findComponent("linechart").getAttributes();
////			linechartAttributes.put("minX", time-CHART_SIZE);
////			linechartAttributes.put("maxX", time);
//			
//			linechartComponents.get(0).setMinX(time-X_VALUES_WINDOW);
//			linechartComponents.get(0).setMaxX(time);
//			linechartComponents.get(1).setMinX(time-X_VALUES_WINDOW);
//			linechartComponents.get(1).setMaxX(time);
//		}
		

		if(time>xValuesWindowSizes.get(1)) {
			minX.set(1, time-xValuesWindowSizes.get(1));
			maxX.set(1, Double.valueOf(time));
		}
		
//		System.out.println(System.getProperty("user.dir"));
//		System.out.println(System.getProperty("catalina.base"));
//		System.out.println(System.getProperty("catalina.home"));
//		LOGGER.trace("time: " + time);
		
		
// ---------------------------------------------------------------------------------------------------
		
//		List<Object> providers = new ArrayList<Object>();
//		providers.add(new JacksonJaxbJsonProvider());
//		
//		WebClient client = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/getGraphData", providers);
//		client.accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN);
//		Response response = client.get();
//		if(response.getStatus() != Status.OK.getStatusCode()) {
//			running = false;
//			WebClient.create("http://192.168.56.101:8080/MatlabAdapter/rest/matlab/stopSimulation").get();
//			errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
//			errorDialogVisible = true;
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
//					"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
//			return;
//		}
//		
//		Map<String, Map<Double, Double[][]>> graphData = null;
//		try {
//			graphData = (Map<String, Map<Double, Double[][]>>) response.getEntity();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}


		
		try {
			Map<String, Map<Double, double[][]>> graphData; //= new HashMap<String, Map<Double,double[][]>>();
			List<Object> providers = new ArrayList<Object>();
			providers.add(new JacksonJaxbJsonProvider());
			
			WebClient client = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/getGraphData", providers);
			client = client.accept(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN).type(MediaType.TEXT_PLAIN);
	//		client = client.accept(MediaType.TEXT_PLAIN).type(MediaType.TEXT_PLAIN);
			
			String graphDataPlainText = client.get(String.class);
//			Response response = client.getResponse();
//			System.out.println(response.getStatus());
			
//			System.out.println(graphDataPlainText);
			
//			return;
			
			
			
			
			
		
			ObjectMapper mapper = new ObjectMapper();
//			Map<String, Map<Double, Object>> tmpGraphData = 
//					mapper.readValue(graphDataPlainText, new TypeReference<Map<String, Map<String, Map<Double, Object>>>>() {});
//			for (Entry<String, Map<Double, Object>> scope : tmpGraphData.entrySet()) {
//				graphData.put(scope.getKey(), new HashMap<Double, double[][]>());
//				for (Entry<Double, Object> scopeInput : scope.getValue().entrySet()) {
//					Object rawGraphData = scopeInput.getValue();
//					if(rawGraphData instanceof double[]) {
//						double[] rawGraphDataArray = (double[])rawGraphData;
//						double [][] graphDataArray = new double[rawGraphDataArray.length][1];
//						for(int i=0; i<rawGraphDataArray.length; i++) {
//							graphDataArray[i][0] = rawGraphDataArray[i];
//						}	
//						graphData.get(scope.getKey()).put(scopeInput.getKey(), graphDataArray);
//					} else if(rawGraphData instanceof double[][]) {
//						graphData.get(scope.getKey()).put(scopeInput.getKey(), (double[][])rawGraphData);
//					} else {
//						System.out.println("test");
//						graphData.get(scope.getKey()).put(scopeInput.getKey(), null);
//					}
//				}
//				
//			}
			graphData = mapper.readValue(graphDataPlainText, new TypeReference<Map<String, Map<Double,double[][]>>>() {});
		
			
//			printData(graphData);
		
			graphDataAccumulator.add(graphData);
			
			
		
//		double maxTime = 0;
//		double[][] doubleArray = graphData.get("vrmaglev/Scope").get(1.0);
//		for (String scopeName : graphData.keySet()) {
//			for(int i=0; i<doubleArray[0].length; i++) {
//				series.get(0).set(doubleArray[0][i], doubleArray[1][i]);
//				series.get(1).set(doubleArray[0][i], doubleArray[2][i]);
//				
//				maxTime = doubleArray[0][i];
//
////				LOGGER.info(doubleArray[1][i].toString());
//			}	
//		}
			
			
			
			
			
			double maxTime = 0;
//			double[][] doubleArray = graphData.get("schema_simple/Scope").get(1.0);
			double[][] doubleArray = graphData.get("udaq28LT_iov1/Temperature").get(1.0);
			for (String scopeName : graphData.keySet()) {
				for(int i=0; i<doubleArray[0].length; i++) {
					series.get(0).set(doubleArray[0][i], doubleArray[1][i]);
					series.get(1).set(doubleArray[0][i], doubleArray[2][i]);
					
					maxTime = doubleArray[0][i];

//					LOGGER.info(doubleArray[1][i].toString());
				}	
			}
	
	
//// --------------------------------------------------------------------------------------------------------------------
//		
//
////		double[][] timeSeries = graphData.get(modelName+"/Scope").get(1.0);
////		for (String scopeName : graphData.keySet()) {
////			for(int i=0; i<timeSeries[0].length; i++) {
////				series.get(0).set(timeSeries[0][i], timeSeries[1][i]);
////				series.get(1).set(timeSeries[0][i], timeSeries[2][i]);
////				
//////				maxTime = timeSeries[0][i];
////
//////				LOGGER.info(doubleArray[1][i].toString());
////			}	
////		}
////		double maxTime = timeSeries[0][timeSeries[0].length-1];
//		
//		
//			double[][] timeSeries = graphData.get(modelName+"/Temperature").get(1.0);
//			if(timeSeries == null) {
//				return;
//			}
//			for (String scopeName : graphData.keySet()) {
//				for (Double inputId : graphData.get(scopeName).keySet()) {
//					for(int i=0; i<timeSeries[0].length; i++) {
//						series.get(0).set(timeSeries[0][i], timeSeries[1][i]);
//						series.get(1).set(timeSeries[0][i], timeSeries[2][i]);
//						
//		//				LOGGER.info(doubleArray[1][i].toString());
//					}
//				}
//			}
//			double maxTime = timeSeries[0][timeSeries[0].length-1];
//		
//// ------------------------------------------------------------------------------------------------------------------		
//
////		double maxTime = 0;
////		int timeSeriesCount = 0;
////		for (Entry<String, Map<Double, double[][]>> scopesEntry : graphData.entrySet()) {
////			for (Entry<Double, double[][]> inputsEntry : scopesEntry.getValue().entrySet()) {
////				double[][] timeSeries = inputsEntry.getValue();
////				for (int i=1; i < timeSeries.length; i++) {
////					timeSeriesCount++;
////					for (int j=0; j < timeSeries.length; j++) {
////						series.get(timeSeriesCount-1).set(timeSeries[0][j], timeSeries[i][j]);
////					}
////				}
////			}	
////		}
//
//
//		
//		
			series.get(0).getData().remove(maxTime-xValuesWindowSizes.get(0));
			series.get(1).getData().remove(maxTime-xValuesWindowSizes.get(0));
			if(maxTime>xValuesWindowSizes.get(0)) {
				minX.set(0, maxTime-xValuesWindowSizes.get(0));
				maxX.set(0, maxTime);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public List<Double> getMinX() {
		return minX;
	}


	public void setMinX(List<Double> minX) {
		this.minX = minX;
	}


	public List<Double> getMaxX() {
		return maxX;
	}


	public void setMaxX(List<Double> maxX) {
		this.maxX = maxX;
	}
	
	public void onDrop(DragDropEvent event) {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		System.out.println(event.getDragId());
		Panel panel = (Panel)FacesContext.getCurrentInstance().getViewRoot().findComponent(event.getDragId());
		System.out.println(panel.getStyle());
	}
	
//	public String handleCommand(String command, String[] params) {
//		List<String> commandList = new ArrayList<String>();
//		commandList.add(command);
//		commandList.addAll(Arrays.asList(params));
//		ProcessBuilder pb = new ProcessBuilder(commandList);
//		String output = "";
//		try {
//			Process p = pb.start();
//			BufferedReader brInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
////			BufferedReader brError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
//			String inputLine;
//			String errorLine;
////			while(((inputLine = brInput.readLine()) != null) && ((errorLine = brError.readLine()) != null)) {
//			while((inputLine=brInput.readLine()) != null) { 
//					output+=inputLine+"\n";
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		output = output.replaceAll("\n", "<br />");
//		
//		return output;
//		
//	}
	
	
	public String handleCommand(String command, String[] params) {
		String output = "";
		try {
//			List<String> commandList = new ArrayList<String>();
//			commandList.add(command);
//			commandList.addAll(Arrays.asList(params));
			StringBuilder fullCommand = new StringBuilder(command);
			fullCommand.append(" ");
			for (int i=0; i<params.length; i++) {
				fullCommand.append(params[i]+" ");
			}
			
			WebClient client = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/").path("executeCommandWithOutput").query("command", fullCommand.toString());
			client.accept(MediaType.TEXT_PLAIN).type(MediaType.TEXT_PLAIN);
			
			output = client.get(String.class);
			
			Response response = client.getResponse();
			
			System.out.println(fullCommand.toString());
			System.out.println(output);
			System.out.println(response.getStatus());
			
			if(response.getStatus() != Status.OK.getStatusCode()) {
				errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
				errorDialogVisible = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
						"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
				return null;
			}
	
			
			output = output.replaceAll("\n", "<br />");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return output;
		
	}


	public boolean isErrorDialogVisible() {
		return errorDialogVisible;
	}


	public void setErrorDialogVisible(boolean errorDialogVisible) {
		this.errorDialogVisible = errorDialogVisible;
	}


	public String getErrorDialogText() {
		return errorDialogText;
	}


	public void setErrorDialogText(String errorDialogText) {
		this.errorDialogText = errorDialogText;
	}
	
	public String getModelLocation() {
		return modelLocation;
	}


	public void setModelLocation(String modelName) {
		this.modelLocation = modelName;
	}


	public String getMatlabAdapterLocation() {
		return matlabAdapterLocation;
	}


	public void setMatlabAdapterLocation(String matlabAdapterLocation) {
		this.matlabAdapterLocation = matlabAdapterLocation;
	}


	public String getSetParamObjectName() {
		return setParamObjectName;
	}


	public void setSetParamObjectName(String setParamObjectName) {
		this.setParamObjectName = setParamObjectName;
	}


	public String getSetParamParamName() {
		return setParamParamName;
	}


	public void setSetParamParamName(String setParamParamName) {
		this.setParamParamName = setParamParamName;
	}


	public String getSetParamParamValue() {
		return setParamParamValue;
	}


	public void setSetParamParamValue(String setParamParamValue) {
		this.setParamParamValue = setParamParamValue;
	}


	public boolean isBuildModel() {
		return buildModel;
	}


	public void setBuildModel(boolean buildModel) {
		this.buildModel = buildModel;
	}


//	public void connect() {
////		String connectionClass = "sk.stuba.fei.weblab.matlabadapter.matlab.JMatlinkConnection";
//		String connectionClass = "sk.stuba.fei.weblab.matlabadapter.matlab.MatlabcontrolConnection";
//		try {
//			Response response = WebClient
//					.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/connect?connectionClass="+connectionClass).get();
//		
//			if(response.getStatus() != Status.OK.getStatusCode()) {
//				errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
//				errorDialogVisible = true;
//				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
//						"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
//			}
//		} catch (Exception e) {
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Exception", e.getMessage()));
//		}
//	}
	
	public void connect() {
		MatlabAdapterClient maClient = 
				new MatlabAdapterClient(matlabAdapterLocation);
		try {
			maClient.connect(MatlabAdapterClient.CONNECTION_CLASS_MATLABCONTROL);
		} catch (MatlabAdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void openModel() {
//		Response response = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/openModel?modelName="+modelLocation+"&build="+buildModel).get();
//		
//		if(response.getStatus() != Status.OK.getStatusCode()) {
//			errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
//			errorDialogVisible = true;
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
//					"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
//		}
//	}

	public void openModel() {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		WebResource baseResource = client.resource(matlabAdapterLocation+"/MatlabAdapter/rest/matlab");
		WebResource openModelResource = baseResource.path("openModel");
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("modelName", modelLocation);
		params.add("build", Boolean.toString(buildModel));
		ClientResponse cResponse = openModelResource.queryParams(params).accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		
		if(cResponse.getStatus() != Status.OK.getStatusCode()) {
			errorDialogText = "response status: "+Status.fromStatusCode(cResponse.getStatus())+" ("+cResponse.getStatus()+")";
			errorDialogVisible = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
					"response status: "+Status.fromStatusCode(cResponse.getStatus())+" ("+cResponse.getStatus()+")"));
		}
	}
	
	public void closeModel() {
		Response response = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/closeModel?modelName="+modelLocation).get();
		
		if(response.getStatus() != Status.OK.getStatusCode()) {
			errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
			errorDialogVisible = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
					"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
		}
	}
	
	public void disconnect() {
		Response response = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/disconnect").get();
		
		if(response.getStatus() != Status.OK.getStatusCode()) {
			errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
			errorDialogVisible = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
					"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
		}
	}
	
	public void dumpGraphData() throws IOException {
		Map<String, Map<Double, double[][]>> graphDataDump = new HashMap<String, Map<Double,double[][]>>();
		System.out.println(graphDataAccumulator.size());
		try {
			for (Map<String, Map<Double, double[][]>> graphData : graphDataAccumulator) {
				for (String scope : graphData.keySet()) {
					if (!graphDataDump.containsKey(scope)) {
						graphDataDump.put(scope, graphData.get(scope));
					} else {
						for (Double scopeInputNum : graphData.get(scope).keySet()) {
							double[][] dataDelta = graphData.get(scope).get(scopeInputNum);
							double[][] dataAccumulated = graphDataDump.get(scope).get(scopeInputNum);
							if(dataAccumulated == null) {
								dataAccumulated = new double[dataDelta.length][dataDelta[0].length];
								graphDataDump.get(scope).put(scopeInputNum, dataAccumulated);
							}
							
		
							for(int i=0; i<dataAccumulated.length; i++) {
								double[] dataNew = new double[dataAccumulated[i].length+dataDelta[i].length];
								System.arraycopy(dataAccumulated[i], 0, dataNew, 0, dataAccumulated[i].length);
								System.arraycopy(dataDelta[i], 0, dataNew, dataAccumulated[i].length, dataDelta[i].length);
								dataAccumulated[i] = dataNew;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		File graphDataFile = new File(System.getProperty("user.home")+File.separator+"graphData"+File.separator+"graphData.dat");
		graphDataFile.delete();
		graphDataFile.createNewFile();
		try (ObjectOutputStream oos = 
				new ObjectOutputStream(new FileOutputStream(graphDataFile))) {
			oos.writeObject(graphDataDump);
			graphDataDump = null;
		}
	}
	
	
	public void executeSetParam() {
//		Response response = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/setParam?modelName="
//		+modelName+"&object="+setParamObjectName+"&name="+setParamParamName+"&value="+setParamParamValue).get();
		
//		WebClient client = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/setParamValue");
//		client.query("modelName", modelName).query("blockName", setParamObjectName).
//			query("paramName", setParamParamName).query("value", setParamParamValue);
		try {
			WebClient client = WebClient.create(matlabAdapterLocation+"/MatlabAdapter/rest/matlab/setParam");
			client.query("modelName", modelLocation)
				.query("object",setParamObjectName)
				.query("name", setParamParamName)
				.query("value", setParamParamValue);
			
			Response response = client.get();
			
			if(response.getStatus() != Status.OK.getStatusCode()) {
				errorDialogText = "response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")";
				errorDialogVisible = true;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("MATLAB REST ERROR", 
						"response status: "+Status.fromStatusCode(response.getStatus())+" ("+response.getStatus()+")"));
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void printData(Map<String, Map<Double, double[][]>> data) {
		for (String scopeName : data.keySet()) {
			System.out.println("scope name: "+scopeName+"\n");
			Map<Double, double[][]> scope = data.get(scopeName);
			for (Double portNumber : scope.keySet()) {
				System.out.println("\t port number: "+portNumber);
				double[][] portData = scope.get(portNumber);
				printDoubleArray(portData);
			}
			System.out.println();
		}
		System.out.println("----------------------------------------------------------------------------------------------------------");
	}
	
	private static void printDoubleArray(double[][] dArray) {
		for (int i = 0; i < dArray.length; i++) {
			System.out.print("\t\t");
			for (int j = 0; j < dArray[i].length; j++) {
				System.out.print(dArray[i][j]+",  ");
			}
			System.out.println();
		}
	}
	
}
