package sk.stuba.fei.weblab.web.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import sk.stuba.fei.weblab.web.model.Locale;
import sk.stuba.fei.weblab.web.model.RealSystem;
import sk.stuba.fei.weblab.web.model.RealSystemLocalizedDescription;
import sk.stuba.fei.weblab.web.service.RealSystemLocalizedDescriptionService;
import sk.stuba.fei.weblab.web.service.RealSystemService;

@Named
@SessionScoped
public class RealSystemDetailsBean implements Serializable {

	private RealSystem realSystem;
	
	private String description;
	
	private Integer realSystemId;
	
	@EJB
	private RealSystemService rsService;
	
	@EJB
	private RealSystemLocalizedDescriptionService rsldService;
	
	@PostConstruct
	public void init() {
		String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
		System.out.println(id);
		if(id != null && !id.equals("")) {
			realSystemId = Integer.parseInt(id);
			realSystem = rsService.find(realSystemId);
			String lang = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
			for (RealSystemLocalizedDescription rsld : realSystem.getRealSystemLocalizedDescriptions()) {
				if(rsld.getLocale().getLanguageCode().equalsIgnoreCase(lang)) {
					description = rsld.getDescription();
					break;
				}
			}
		}
		System.out.println(description);
	}
	
	public void onLoad() {
//		if(!FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest()) {
			String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			System.out.println(id);
			if(id != null && !id.equals("")) {
				realSystemId = Integer.parseInt(id);
			}
				
			realSystem = rsService.find(realSystemId);
			String lang = FacesContext.getCurrentInstance().getViewRoot().getLocale().getLanguage();
			for (RealSystemLocalizedDescription rsld : realSystem.getRealSystemLocalizedDescriptions()) {
				if(rsld.getLocale().getLanguageCode().equalsIgnoreCase(lang)) {
					description = rsld.getDescription();
					break;
				}
			}
			
			System.out.println(description);
//		}
	}

	public RealSystem getRealSystem() {
		return realSystem;
	}

	public void setRealSystem(RealSystem realSystem) {
		this.realSystem = realSystem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getRealSystemId() {
		return realSystemId;
	}

	public void setRealSystemId(Integer realSystemId) {
		this.realSystemId = realSystemId;
	}
}
