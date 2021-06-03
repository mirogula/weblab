package sk.stuba.fei.weblab.matlabadapterclient;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response.Status;

import sk.stuba.fei.weblab.paramproperties.ParamProperties;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.MultivaluedMapImpl;



public class MatlabAdapterClient {
	
	public static enum MatlabAdapterStatus {
		OFFLINE, ONLINE, CONNECTED_TO_MATLAB
	}
	
	public static final String CONNECTION_CLASS_MATLABCONTROL = 
			"sk.stuba.fei.weblab.matlabadapter.matlab.MatlabcontrolConnector";
	public static final String CONNECTION_CLASS_JMATLINK = 
			"sk.stuba.fei.weblab.matlabadapter.matlab.JMatlinkConnection";
	
	private static final String MATLAB_ADAPTER_ERROR_PREFIX = "matlabadapterError";
	private static final String ERROR_FIELD_SEPARATOR = ":;:";
	
	// general unspecified error
	public static final int ERROR_CODE_GENERAL = 0;
	// when MatlabFacade can not recognize or create class from class name provided by the user
	public static final int ERROR_CODE_INIT_REFLECTION = 1;
	// when MatlabAdapter can not properly comunicate with matlab, because of some internal error in connection with matlab
	public static final int ERROR_CODE_MATLAB_COMM = 2;
	// when user is trying to connect to matlab and MatlabFacade is already connected
	public static final int ERROR_CODE_ALREADY_CONNECTED = 3;
	// when user is trying to communicate with matlab without connecting first
	public static final int ERROR_CODE_NOT_CONNECTED = 4;
	// when upload of model file fails
	public static final int ERROR_CODE_MODEL_UPLOAD_FAILED = 5;
	
	public static final int ERROR_CODE_COMPRESSION_FAILED = 6;
	
	public static final int ERROR_CODE_CANNOT_DELETE_CONTENT = 7;
	
	private final WebResource baseResource;
	
	
	public MatlabAdapterClient(String matlabAdapterURL) {
		ClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
		Client client = Client.create(clientConfig);
		baseResource = client.resource(matlabAdapterURL);		
	}
	
	public MatlabAdapterStatus getStatus() {
		int timeout = 1000;
		boolean isOnline;
		String hostName = baseResource.getURI().getHost();
		try {
			isOnline = InetAddress.getByName(hostName).isReachable(timeout);
		} catch (IOException e) {
			isOnline = false;
		}
		if(!isOnline) {
			// server is offline
			return MatlabAdapterStatus.OFFLINE;
		} 
		WebResource isConnectedResource = baseResource.path("isMatlabRunning");
		ClientResponse cResponse;
		try {
			cResponse = isConnectedResource.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		} catch (Exception e) {
			// server is online, but web server is offline
			return MatlabAdapterStatus.OFFLINE;
		}
		String isConnected = cResponse.getEntity(String.class);
		if(!Boolean.parseBoolean(isConnected)) {
			// web server is online
			return MatlabAdapterStatus.ONLINE;
		} else {
			// web server is online, and MatlabAdapter is connected to matlab right now
			return MatlabAdapterStatus.CONNECTED_TO_MATLAB;
		}
	}
	
	
	public void connect(String connectionClass) throws MatlabAdapterException, ClientHandlerException {
		WebResource connectResource = baseResource.path("startMatlab");
		ClientResponse cResponse = connectResource.queryParam("connectionClass", connectionClass)
				.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
	public void openModel(String modelName, boolean build) throws MatlabAdapterException, ClientHandlerException {
		WebResource openModelResource = baseResource.path("openModel");
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("modelName", modelName);
		params.add("build", Boolean.toString(build));
		ClientResponse cResponse = openModelResource.queryParams(params).accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
	public void startSimulation(String modelName) throws MatlabAdapterException, ClientHandlerException {
		WebResource startSimResource = baseResource.path("startSimulation");
		ClientResponse cResponse = startSimResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}

	public void togglePauseSimulation() throws MatlabAdapterException, ClientHandlerException {
		WebResource togglePauseSimResource = baseResource.path("togglePauseSimulation");
		ClientResponse cResponse = togglePauseSimResource.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
	/**
	 * @param modelName
	 * @param blockName
	 * @param paramName
	 * @param paramValue
	 * @return new value of parameter, if some unexpected error happened it may return null, empty string or old value
	 * @throws MatlabAdapterException
	 * @throws ClientHandlerException
	 */
	public String setParamValue(String modelName, String blockName, 
			String paramName, String paramValue) throws MatlabAdapterException, ClientHandlerException {
		WebResource setParamValueResource = baseResource.path("setParamValue");
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("modelName", modelName);
		params.add("blockName", blockName);
		params.add("paramName", paramName);
		params.add("paramValue", paramValue);
		ClientResponse cResponse = setParamValueResource.queryParams(params).accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
			return cResponse.getEntity(String.class);
		} else {
			return null;
		}
	}
	
	public Map<String, Map<String, String>> setParamsValues(String modelName, Map<String, 
			Map<String, String>> blocksParamsValuesMap) throws MatlabAdapterException, ClientHandlerException {
		WebResource setParamsValuesResource = baseResource.path("setParamsValues");
		ClientResponse cResponse = setParamsValuesResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, blocksParamsValuesMap);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
			return cResponse.getEntity(new GenericType<Map<String, Map<String, String>>>() {});
		} else {
			return null;
		}
	}
	
	/**
	 * @param modelName
	 * @param blockName
	 * @param paramName
	 * @return parameter value as String (may be empty string if parameter does not exist), or null if some unexpected error happened
	 * @throws MatlabAdapterException
	 * @throws ClientHandlerException
	 */
	public String getParamValue(String modelName, String blockName, 
			String paramName) throws MatlabAdapterException, ClientHandlerException {
		WebResource getParamValueResource = baseResource.path("getParamValue");
		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("modelName", modelName);
		params.add("blockName", blockName);
		params.add("paramName", paramName);
		ClientResponse cResponse = getParamValueResource.queryParams(params).accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
			return cResponse.getEntity(String.class);
		} else {
			return null;
		}
	}
	
	public Map<String, Map<String, String>> getParamsValues(String modelName, 
			Map<String, List<String>> blocksParamsMap) throws MatlabAdapterException, ClientHandlerException {
		WebResource getParamValueResource = baseResource.path("getParamsValues");
		ClientResponse cResponse = getParamValueResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_JSON_TYPE)
				.type(MediaType.APPLICATION_JSON_TYPE).post(ClientResponse.class, blocksParamsMap);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
			return cResponse.getEntity(new GenericType<Map<String, Map<String, String>>>() {});
		} else {
			return null;
		}
	}
	
//	public Map<String, Map<Double, double[][]>> getGraphData() throws MatlabAdapterException, ClientHandlerException {
//		WebResource getGraphDataResource = baseResource.path("getGraphData");
//		ClientResponse cResponse = getGraphDataResource.
//				accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
//		checkForErrors(cResponse);
//		if(cResponse.hasEntity()) {
//			return cResponse.getEntity(new GenericType<Map<String, Map<Double, double[][]>>>() {});
//		} else {
//			return null;
//		}
//	}
	
//	public String getGraphData() throws MatlabAdapterException, ClientHandlerException {
//		WebResource getGraphDataResource = baseResource.path("getGraphData");
//		ClientResponse cResponse = getGraphDataResource.
//				accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
//		checkForErrors(cResponse);
//		if(cResponse.hasEntity()) {
//	//		return cResponse.getEntity(new GenericType<Map<String, Map<Double, Object>>>() {});
//			return cResponse.getEntity(String.class);
//		} else {
//			return null;
//		}
//	}
	
	public Map<String, Map<String, List<List<Double>>>> getGraphData() throws MatlabAdapterException,
			ClientHandlerException {
		WebResource getGraphDataResource = baseResource.path("getGraphData");
		ClientResponse cResponse = getGraphDataResource.accept(
				MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
		if (cResponse.hasEntity()) {
			Map<String, Map<String, List<?>>> graphDataInput = 
					cResponse.getEntity(new GenericType<Map<String, Map<String, List<?>>>>() {});
			Map<String, Map<String, List<List<Double>>>> graphDataOutput = normalizeGraphData(graphDataInput);		
			return graphDataOutput;
		} else {
			return null;
		}
	}
	
	public String executeCommandWithOutput(String command) throws MatlabAdapterException, ClientHandlerException {
		WebResource execCommandResource = baseResource.path("executeCommandWithOutput");
		ClientResponse cResponse = execCommandResource.queryParam("command", command)
				.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
			return cResponse.getEntity(String.class);
		} else {
			return null;
		}
	}
	
	public void stopSimulation() throws MatlabAdapterException, ClientHandlerException {
		WebResource stopSimResource = baseResource.path("stopSimulation");
		ClientResponse cResponse = stopSimResource.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
	public void closeModel(String modelName) throws MatlabAdapterException, ClientHandlerException {
		WebResource closeModelResource = baseResource.path("closeModel");
		ClientResponse cResponse = closeModelResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
	public void disconnect() throws MatlabAdapterException, ClientHandlerException {
		WebResource disconnectResource = baseResource.path("stopMatlab");
		ClientResponse cResponse = disconnectResource.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
//	public void uploadModel(File modelFile, String modelName) throws MatlabAdapterException, ClientHandlerException {
//		WebResource uploadModelResource = baseResource.path("uploadModel");
//		FileDataBodyPart fdbp = new FileDataBodyPart("file", modelFile, MediaType.APPLICATION_OCTET_STREAM_TYPE);
//		FormDataMultiPart form = new FormDataMultiPart();
//		form.field("file", modelFile, MediaType.MULTIPART_FORM_DATA_TYPE);
//		form.bodyPart(fdbp);
//		ClientResponse cResponse = uploadModelResource.accept(MediaType.TEXT_PLAIN_TYPE).
//				type(MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, form);
//		checkForErrors(cResponse);
//	}
	
	public void uploadModel(String modelFilePath, String modelName) throws MatlabAdapterException, ClientHandlerException {
		WebResource uploadModelResource = baseResource.path("uploadModel");
		ClientResponse cResponse = uploadModelResource.queryParam("modelName", modelName).accept(MediaType.TEXT_PLAIN_TYPE)
				.type(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class, new File(modelFilePath));
		checkForErrors(cResponse);
	}

	public void uploadInitScript(String scriptFilePath, String forModel) throws MatlabAdapterException, ClientHandlerException {
		WebResource uploadInitScriptResource = baseResource.path("uploadInitScript");
		ClientResponse cResponse = uploadInitScriptResource.queryParam("forModel", forModel).accept(MediaType.TEXT_PLAIN_TYPE)
				.type(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class, new File(scriptFilePath));
		checkForErrors(cResponse);
	}
	
	public void uploadCleanupScript(String scriptFilePath, String forModel) throws MatlabAdapterException, ClientHandlerException {
		WebResource uploadCleanupScriptResource = baseResource.path("uploadCleanupScript");
		ClientResponse cResponse = uploadCleanupScriptResource.queryParam("forModel", forModel).accept(MediaType.TEXT_PLAIN_TYPE)
				.type(MediaType.APPLICATION_OCTET_STREAM_TYPE).post(ClientResponse.class, new File(scriptFilePath));
		checkForErrors(cResponse);
	}
	
	public void deleteModel(String modelName) throws MatlabAdapterException, ClientHandlerException {
		WebResource deleteModelResource = baseResource.path("deleteModel");
		ClientResponse cResponse = deleteModelResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
//	public boolean isMatlabRunning() throws MatlabAdapterException, ClientHandlerException  {
//		WebResource isMatlabRunningResource = baseResource.path("isMatlabRunning");
//		ClientResponse cResponse = isMatlabRunningResource.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);
//		checkForErrors(cResponse);
//		return cResponse.getEntity(Boolean.class);
//	}
	
	public void generateWebView(String modelName, String localWebViewDir) throws MatlabAdapterException, ClientHandlerException {
		WebResource generateWebViewResource = baseResource.path("generateWebView");
		ClientResponse cResponse = generateWebViewResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_OCTET_STREAM_TYPE)
				.get(ClientResponse.class);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
//			MultivaluedMap<String, String> headers = cResponse.getHeaders();
//			for (Entry<String, List<String>> headerEntry : headers.entrySet()) {
//				System.out.println(headerEntry.getKey()+" | "+headerEntry.getValue());
//				System.out.println();
//			}
			
//			InputStream downloadFileInputStream = cResponse.getEntityInputStream();
//			String zipFilePath = webViewDir+File.separator+modelName+".zip";
//			writeToFile(downloadFileInputStream, zipFilePath);
//			downloadFileInputStream.close();
//			ZipExtractor ze = new ZipExtractor(webViewDir, zipFilePath);
//			ze.decompress();
			
			try {
				ZipExtractor.decompress(localWebViewDir, cResponse.getEntityInputStream());
			} catch (Exception e) {
				throw new ClientHandlerException(e);
			}
		}
	}
	
	public void cleanRemoteWebViewTmpDir() throws MatlabAdapterException, ClientHandlerException {
		WebResource cleanWebViewDirResource = baseResource.path("cleanWebViewTmpDir");
		ClientResponse cResponse = cleanWebViewDirResource.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
	}
	
	public Map<String, List<ParamProperties>> getModelInfo(String modelName) throws MatlabAdapterException, ClientHandlerException, IOException {
		WebResource getModelInfoResource = baseResource.path("getModelInfo");
		ClientResponse cResponse = getModelInfoResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE, MediaType.APPLICATION_OCTET_STREAM_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
//			Map<String, List<ParamProperties>> modelInfo;		
//			try(ObjectInputStream ois = new ObjectInputStream(cResponse.getEntityInputStream())) {
//				modelInfo = (Map<String, List<ParamProperties>>)ois.readObject();				
//			} catch (Exception e) {
//				throw new ClientHandlerException(e);
//			}
//			return modelInfo;
			
			return cResponse.getEntity(new GenericType<Map<String, List<ParamProperties>>>() {});
			
		} else {
			return null;
		}
	}
	
	public Boolean isModelOpened(String modelName) throws MatlabAdapterException, ClientHandlerException {
		WebResource isModelOpenedResource = baseResource.path("isModelOpened");
		ClientResponse cResponse = isModelOpenedResource.queryParam("modelName", modelName)
				.accept(MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
		checkForErrors(cResponse);
		if(cResponse.hasEntity()) {
			String result = cResponse.getEntity(String.class);
			return Boolean.parseBoolean(result);
		} else {
			return null;
		}
	}
	
	private static void writeToFile(InputStream downloadInputStream, 
			String downloadedFilePath) throws IOException {
		OutputStream out = null;;
		try {
			out = new FileOutputStream(new File(downloadedFilePath));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = downloadInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
		} finally {
			if(out != null) {
				out.flush();
				out.close();
			}
		}
	}
	
	private static void checkForErrors(ClientResponse cResponse) throws MatlabAdapterException {
		if(cResponse.getStatus() != Status.OK.getStatusCode()) {
			MatlabAdapterException maException = new MatlabAdapterException();
			maException.setHttpStatusCode(cResponse.getStatus());
			if(cResponse.hasEntity()) {
				String errorMessage = cResponse.getEntity(String.class);
				if(errorMessage.startsWith(MATLAB_ADAPTER_ERROR_PREFIX)) {
					String errorMessageParts[] = errorMessage.split(ERROR_FIELD_SEPARATOR);		
					maException.setErrorCode(Integer.parseInt(errorMessageParts[1]));
					maException.setErrorMessage(errorMessageParts[2]);
					maException.setExceptionMessage(errorMessageParts[3]);
					maException.setExceptionStackTrace(errorMessageParts[4]);
				} else {
					maException.setErrorMessage(errorMessage);
				}
			}
			throw maException;
		}
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Map<String, List<List<Double>>>> normalizeGraphData(Map<String, Map<String, List<?>>> graphDataInput) {
		Map<String, Map<String, List<List<Double>>>> graphDataOutput = new HashMap<String, Map<String, List<List<Double>>>>();
		for (Entry<String, Map<String, List<?>>> graphDataEntrySet : graphDataInput.entrySet()) {
			Map<String, List<List<Double>>> scopeDataOutput = new HashMap<String, List<List<Double>>>();
			graphDataOutput.put(graphDataEntrySet.getKey(), scopeDataOutput);
			for (Entry<String, List<?>> scopesEntrySet : graphDataEntrySet.getValue().entrySet()) {
				List<?> scopeInputData = scopesEntrySet.getValue();
				if(Double.class.isInstance(scopeInputData.get(0))) {
					List<List<Double>> outputData = new ArrayList<List<Double>>();
					for (Object value : scopeInputData) {
						Double doubleValue = (Double)value;
						List<Double> valueList = new ArrayList<>();
						valueList.add(doubleValue);
						outputData.add(valueList);
					}
					scopeDataOutput.put(scopesEntrySet.getKey(), outputData);
				} else {
					scopeDataOutput.put(scopesEntrySet.getKey(), (List<List<Double>>)scopeInputData);
				}
			}
		}
		
		return graphDataOutput;
	}

}
