package sk.stuba.fei.weblab.web.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIPanel;
import javax.inject.Named;

import org.primefaces.component.panel.Panel;

import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient;
import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient.MatlabAdapterStatus;
import sk.stuba.fei.weblab.web.model.RealSystem;
import sk.stuba.fei.weblab.web.service.RealSystemService;

@Named
@RequestScoped
public class RealSystemInfoBean {

	private List<RealSystem> realSystems;
	
	private Map<String, Panel> panels;
	
	private MatlabAdapterClient maClient;
	
	@EJB
	private RealSystemService rsService;
	
	
	@PostConstruct
	public void init() {
		try {
			panels = new LinkedHashMap<>();
			realSystems = rsService.findAll();
			for (RealSystem rs : realSystems) {
				Panel panel = new Panel();
				panel.setStyle("background-color: "+rs.getColor());
				panels.put(rs.getName(), panel);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String checkStatus(RealSystem rs) {
		try {
			maClient = new MatlabAdapterClient(rs.getUrlAddress());
			MatlabAdapterStatus maStatus = maClient.getStatus();
			return maStatus.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "ERROR";
		}
	}

	public List<RealSystem> getRealSystems() {
		return realSystems;
	}


	public void setRealSystems(List<RealSystem> realSystems) {
		this.realSystems = realSystems;
	}
	
	public String colorStyle(RealSystem rs) {
		return "width: 100px; height: 50px; background-color: "+rs.getColor();
	}

	public Map<String, Panel> getPanels() {
		return panels;
	}

	public void setPanels(Map<String, Panel> panels) {
		this.panels = panels;
	}
}
