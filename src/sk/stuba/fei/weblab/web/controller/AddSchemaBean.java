package sk.stuba.fei.weblab.web.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.primefaces.event.FlowEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient;
import sk.stuba.fei.weblab.paramproperties.ParamProperties;
import sk.stuba.fei.weblab.web.model.Locale;
import sk.stuba.fei.weblab.web.model.Parameter;
import sk.stuba.fei.weblab.web.model.RealSystem;
import sk.stuba.fei.weblab.web.model.RealSystemLocalizedDescription;
import sk.stuba.fei.weblab.web.model.SimulinkSchema;
import sk.stuba.fei.weblab.web.model.SimulinkSchemaLocalizedDescription;
import sk.stuba.fei.weblab.web.service.LocaleService;
import sk.stuba.fei.weblab.web.service.RealSystemService;

@Named
@RequestScoped
public class AddSchemaBean {

	private RealSystem realSystem;
	
	private SimulinkSchema simulinkSchema;
	
	private List<Parameter> parameters;
	
	private List<String> paramGroupsNames;
	
	private Map<String, String> paramGroupMap;
	
	private Map<String,  String> paramGroupMapSelectedValue;
	
	private Map<String, SimulinkSchemaLocalizedDescription> descriptions;
	
	private MatlabAdapterClient maClient;
	
	private TreeNode root;
	
	private TreeNode[] selectedNodes;
	
	@EJB
	private RealSystemService rsService;
	
	@EJB
	private LocaleService localeService;
	
	@PostConstruct
	public void init() {
		String rsId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("realSystemId");
		if(rsId != null && !rsId.equals("")) {
			realSystem = rsService.find(Integer.parseInt(rsId));
			simulinkSchema = new SimulinkSchema();
			simulinkSchema.setRealSystem(realSystem);
			
			root = new DefaultTreeNode("Root", null);
	        TreeNode node0 = new DefaultTreeNode("Node 0", root);  
	        TreeNode node1 = new DefaultTreeNode("Node 1", root);  
	        TreeNode node2 = new DefaultTreeNode("Node 2", root);  
	  
	        TreeNode node00 = new DefaultTreeNode("Node 0.0", node0);  
	        TreeNode node01 = new DefaultTreeNode("Node 0.1", node0);  
	  
	        TreeNode node10 = new DefaultTreeNode("Node 1.0", node1);  
	        TreeNode node11 = new DefaultTreeNode("Node 1.1", node1);  
	  
	        TreeNode node000 = new DefaultTreeNode("Node 0.0.0", node00);  
	        TreeNode node001 = new DefaultTreeNode("Node 0.0.1", node00);  
	        TreeNode node010 = new DefaultTreeNode("Node 0.1.0", node01);  
	  
	        TreeNode node100 = new DefaultTreeNode("Node 1.0.0", node10);  
		}
		descriptions = new LinkedHashMap<>();
		List<Locale> locales = localeService.findAll();
		for (Locale locale : locales) {
			SimulinkSchemaLocalizedDescription ssld = new SimulinkSchemaLocalizedDescription();
			ssld.setSimulinkSchema(simulinkSchema);
			ssld.setLocale(locale);
			descriptions.put(locale.getLanguageCode(), ssld);
		}
		
		parameters = new LinkedList<>();
		Parameter p = new Parameter();
		p.setDisplayName("Kp:");
		p.setSimulinkParamName("Kp");
		p.setSimulinkBlockName("Discrete-Time PID");
		p.setDefaultValue("1");
		parameters.add(p);
		
		p = new Parameter();
		p.setDisplayName("Ki:");
		p.setSimulinkParamName("Ki");
		p.setSimulinkBlockName("Discrete-Time PID");
		p.setDefaultValue("10");
		parameters.add(p);
		
		p = new Parameter();
		p.setDisplayName("Kd:");
		p.setSimulinkParamName("Kd");
		p.setSimulinkBlockName("Discrete-Time PID");
		p.setDefaultValue("0.03");
		parameters.add(p);
		
		p = new Parameter();
		p.setDisplayName("Amplitude:");
		p.setSimulinkParamName("Amplitude");
		p.setSimulinkBlockName("Signal Generator");
		p.setDefaultValue("0.1");
		parameters.add(p);
		
		p = new Parameter();
		p.setDisplayName("Frequency:");
		p.setSimulinkParamName("Frequency");
		p.setSimulinkBlockName("Signal Generator");
		p.setDefaultValue("7");
		parameters.add(p);
	}
	
	public void onLoad() {
		try {
	//		if(!FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest()) {
			System.out.println(simulinkSchema);
			String rsId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("realSystemId");
			System.out.println(rsId);
			if(rsId != null && !rsId.equals("")) {
				realSystem = rsService.find(Integer.parseInt(rsId));
				
				simulinkSchema = new SimulinkSchema();
				simulinkSchema.setName("aaaaa");
				System.out.println(simulinkSchema.getName());
				
				simulinkSchema.setRealSystem(realSystem); 
			}
			root = new DefaultTreeNode("Root", null);
			
			TreeNode node3 = new DefaultTreeNode("Discrete-Time PID", root);  
	        TreeNode node30 = new DefaultTreeNode("Kp:", node3);  
	        TreeNode node31 = new DefaultTreeNode("Ki:", node3); 
	        TreeNode node32 = new DefaultTreeNode("Kd:", node3);  
	        TreeNode node33 = new DefaultTreeNode("Sample time:", node3);  
	
	        TreeNode node4 = new DefaultTreeNode("Signal Generator", root);
	        TreeNode node40 = new DefaultTreeNode("Wave form:", node4);  
	        TreeNode node41 = new DefaultTreeNode("Time (t):", node4); 
	        TreeNode node42 = new DefaultTreeNode("Amplitude:", node4);  
	        TreeNode node43 = new DefaultTreeNode("Frequency:", node4); 
	        TreeNode node44 = new DefaultTreeNode("Units:", node4); 
	        TreeNode node45 = new DefaultTreeNode("Interpret vector parameters as 1-D", node4); 
	        
	        TreeNode node5 = new DefaultTreeNode("Magnetic Levitation Plant Model", root); 
	        TreeNode node50 = new DefaultTreeNode("1//m", node5);  
	        TreeNode node501 = new DefaultTreeNode("Gain:", node50);
	        TreeNode node502 = new DefaultTreeNode("Multiplication:", node50);  
	        TreeNode node503 = new DefaultTreeNode("Parameter minimum:", node50);  
	        TreeNode node504 = new DefaultTreeNode("Parameter maximum:", node50);  
	        TreeNode node505 = new DefaultTreeNode(" Parameter data type:", node50);  
	        TreeNode node506 = new DefaultTreeNode("Output minimum:", node50);  
	        TreeNode node507 = new DefaultTreeNode("Output maximum:", node50);  
	        TreeNode node508 = new DefaultTreeNode("Output data type:", node50);  
	        TreeNode node509 = new DefaultTreeNode("Lock output data type setting against changes by the fixed-point tools", node50);  
	        TreeNode node5010 = new DefaultTreeNode("Integer rounding mode:", node50);  
	        TreeNode node5011 = new DefaultTreeNode("Saturate on integer overflow", node50);  
	        TreeNode node5012 = new DefaultTreeNode("Sample time (-1 for inherited):", node50);
			
	        TreeNode node0 = new DefaultTreeNode("Discrete-Time PID", root);  
	        TreeNode node00 = new DefaultTreeNode("Kp:", node0);  
	        TreeNode node01 = new DefaultTreeNode("Ki:", node0); 
	        TreeNode node02 = new DefaultTreeNode("Kd:", node0);  
	        TreeNode node03 = new DefaultTreeNode("Sample time:", node0);  
	
	        TreeNode node2 = new DefaultTreeNode("Signal Generator", root);
	        TreeNode node20 = new DefaultTreeNode("Wave form:", node2);  
	        TreeNode node21 = new DefaultTreeNode("Time (t):", node2); 
	        TreeNode node22 = new DefaultTreeNode("Amplitude:", node2);  
	        TreeNode node23 = new DefaultTreeNode("Frequency:", node2); 
	        TreeNode node24 = new DefaultTreeNode("Units:", node2); 
	        TreeNode node25 = new DefaultTreeNode("Interpret vector parameters as 1-D", node2); 
	        
	        TreeNode node1 = new DefaultTreeNode("Magnetic Levitation Plant Model", root); 
	        TreeNode node10 = new DefaultTreeNode("1//m", node1);  
	        TreeNode node101 = new DefaultTreeNode("Gain:", node10);
	        TreeNode node102 = new DefaultTreeNode("Multiplication:", node10);  
	        TreeNode node103 = new DefaultTreeNode("Parameter minimum:", node10);  
	        TreeNode node104 = new DefaultTreeNode("Parameter maximum:", node10);  
	        TreeNode node105 = new DefaultTreeNode(" Parameter data type:", node10);  
	        TreeNode node106 = new DefaultTreeNode("Output minimum:", node10);  
	        TreeNode node107 = new DefaultTreeNode("Output maximum:", node10);  
	        TreeNode node108 = new DefaultTreeNode("Output data type:", node10);  
	        TreeNode node109 = new DefaultTreeNode("Lock output data type setting against changes by the fixed-point tools", node10);  
	        TreeNode node1010 = new DefaultTreeNode("Integer rounding mode:", node10);  
	        TreeNode node1011 = new DefaultTreeNode("Saturate on integer overflow", node10);  
	        TreeNode node1012 = new DefaultTreeNode("Sample time (-1 for inherited):", node10);  
	        
			TreeNode node6 = new DefaultTreeNode("Discrete-Time PID", root);  
	        TreeNode node60 = new DefaultTreeNode("Kp:", node6);  
	        TreeNode node61 = new DefaultTreeNode("Ki:", node6); 
	        TreeNode node62 = new DefaultTreeNode("Kd:", node6);  
	        TreeNode node63 = new DefaultTreeNode("Sample time:", node6);  
	
	        TreeNode node7 = new DefaultTreeNode("Signal Generator", root);
	        TreeNode node70 = new DefaultTreeNode("Wave form:", node7);  
	        TreeNode node71 = new DefaultTreeNode("Time (t):", node7); 
	        TreeNode node72 = new DefaultTreeNode("Amplitude:", node7);  
	        TreeNode node73 = new DefaultTreeNode("Frequency:", node7); 
	        TreeNode node74 = new DefaultTreeNode("Units:", node7); 
	        TreeNode node75 = new DefaultTreeNode("Interpret vector parameters as 1-D", node7); 
	        
	        TreeNode node8 = new DefaultTreeNode("Magnetic Levitation Plant Model", root); 
	        TreeNode node80 = new DefaultTreeNode("1//m", node8);  
	        TreeNode node801 = new DefaultTreeNode("Gain:", node80);
	        TreeNode node802 = new DefaultTreeNode("Multiplication:", node80);  
	        TreeNode node803 = new DefaultTreeNode("Parameter minimum:", node80);  
	        TreeNode node804 = new DefaultTreeNode("Parameter maximum:", node80);  
	        TreeNode node805 = new DefaultTreeNode(" Parameter data type:", node80);  
	        TreeNode node806 = new DefaultTreeNode("Output minimum:", node80);  
	        TreeNode node807 = new DefaultTreeNode("Output maximum:", node80);  
	        TreeNode node808 = new DefaultTreeNode("Output data type:", node80);  
	        TreeNode node809 = new DefaultTreeNode("Lock output data type setting against changes by the fixed-point tools", node80);  
	        TreeNode node8010 = new DefaultTreeNode("Integer rounding mode:", node80);  
	        TreeNode node8011 = new DefaultTreeNode("Saturate on integer overflow", node80);  
	        TreeNode node8012 = new DefaultTreeNode("Sample time (-1 for inherited):", node80);
			
	        
			descriptions = new LinkedHashMap<>();
			List<Locale> locales = localeService.findAll();
			for (Locale locale : locales) {
				SimulinkSchemaLocalizedDescription ssld = new SimulinkSchemaLocalizedDescription();
				ssld.setSimulinkSchema(simulinkSchema);
				ssld.setLocale(locale);
				descriptions.put(locale.getLanguageCode(), ssld);
			}
			
			parameters = new LinkedList<>();
			Parameter p = new Parameter();
			p.setDisplayName("Kp:");
			p.setSimulinkParamName("Kp");
			p.setSimulinkBlockName("Discrete-Time PID");
			p.setDefaultValue("1");
			parameters.add(p);
			
			p = new Parameter();
			p.setDisplayName("Ki:");
			p.setSimulinkParamName("Ki");
			p.setSimulinkBlockName("Discrete-Time PID");
			p.setDefaultValue("10");
			parameters.add(p);
			
			p = new Parameter();
			p.setDisplayName("Kd:");
			p.setSimulinkParamName("Kd");
			p.setSimulinkBlockName("Discrete-Time PID");
			p.setDefaultValue("0.03");
			parameters.add(p);
			
			p = new Parameter();
			p.setDisplayName("Amplitude:");
			p.setSimulinkParamName("Amplitude");
			p.setSimulinkBlockName("Signal Generator");
			p.setDefaultValue("0.1");
			parameters.add(p);
			
			p = new Parameter();
			p.setDisplayName("Frequency:");
			p.setSimulinkParamName("Frequency");
			p.setSimulinkBlockName("Signal Generator");
			p.setDefaultValue("7");
			parameters.add(p);
			
			
			paramGroupMap = new LinkedHashMap<>();
			paramGroupMap.put("Regulator params", "Regulator params");
			paramGroupMap.put("Levitation params", "Levitation params");
			
			paramGroupMapSelectedValue = new LinkedHashMap<>();
			for (Parameter param : parameters) {
				paramGroupMapSelectedValue.put(param.getSimulinkParamName(), "Regulator params");
			}
			
			System.out.println(parameters.get(1).getDisplayName());

//		}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadSchema() {
		
	}
	
    public String onFlowProcess(FlowEvent event) {  
    	
    	return event.getNewStep();
//    	return "confirm";
    }
    
    public void submit() {
    	
    	try {
			Faces.redirect("/user/realsystemdetails.xhtml?"+"id="+realSystem.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    

	public RealSystem getRealSystem() {
		return realSystem;
	}

	public void setRealSystem(RealSystem realSystem) {
		this.realSystem = realSystem;
	}

	public SimulinkSchema getSimulinkSchema() {
		return simulinkSchema;
	}

	public void setSimulinkSchema(SimulinkSchema simulinkSchema) {
		this.simulinkSchema = simulinkSchema;
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode paramsRoot) {
		this.root = paramsRoot;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}

	public Map<String, String> getParamGroupMap() {
		return paramGroupMap;
	}

	public void setParamGroupMap(Map<String, String> paramGroupMap) {
		this.paramGroupMap = paramGroupMap;
	}

	public Map<String,  String> getParamGroupMapSelectedValue() {
		return paramGroupMapSelectedValue;
	}

	public void setParamGroupMapSelectedValue(
			Map<String,  String> paramGroupMapSelectedValue) {
		this.paramGroupMapSelectedValue = paramGroupMapSelectedValue;
	}

	public List<String> getParamGroupsNames() {
		return paramGroupsNames;
	}

	public void setParamGroupsNames(List<String> paramGroupsNames) {
		this.paramGroupsNames = paramGroupsNames;
	}

	public Map<String, SimulinkSchemaLocalizedDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(
			Map<String, SimulinkSchemaLocalizedDescription> descriptions) {
		this.descriptions = descriptions;
	}
}
