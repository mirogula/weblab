package sk.stuba.fei.weblab.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;

import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient;
import sk.stuba.fei.weblab.web.model.Locale;
import sk.stuba.fei.weblab.web.model.RealSystem;
import sk.stuba.fei.weblab.web.model.RealSystemLocalizedDescription;
import sk.stuba.fei.weblab.web.model.RealSystemPicture;
import sk.stuba.fei.weblab.web.service.LocaleService;
import sk.stuba.fei.weblab.web.service.RealSystemService;
import sun.java2d.pisces.PiscesRenderingEngine;

@Named
@SessionScoped
//@RequestScoped
public class AddRealSystemBean implements Serializable {
	
	public static final String PICTURES_DIR = "/pictures";
	
	private MatlabAdapterClient maClient;
	
	private RealSystem newRealSystem;
	
	private Map<String, RealSystemLocalizedDescription> descriptions;
	
	private List<String> pictures;
	
	@EJB
	private RealSystemService rsService;
	
	@EJB
	private LocaleService localeService;
	
	@PostConstruct
	public void init() {
		
		newRealSystem = new RealSystem();
		descriptions = new LinkedHashMap<>();
		List<Locale> locales = localeService.findAll();
		for (Locale locale : locales) {
			RealSystemLocalizedDescription rsld = new RealSystemLocalizedDescription();
			rsld.setRealSystem(newRealSystem);
			rsld.setLocale(locale);
			descriptions.put(locale.getLanguageCode(), rsld);
		}
		pictures = new LinkedList<>();
		
//		pictures.add("Screenshot from 2013-05-19 20:49:38.png");
//		pictures.add("Screenshot from 2013-05-19 20:57:08.png");
//		pictures.add("Screenshot from 2013-05-19 20:57:34.png");

	}
	
	public void onLoad() {
		if(!FacesContext.getCurrentInstance().getPartialViewContext().isAjaxRequest()) {
			newRealSystem = new RealSystem();
			descriptions = new LinkedHashMap<>();
			List<Locale> locales = localeService.findAll();
			for (Locale locale : locales) {
				RealSystemLocalizedDescription rsld = new RealSystemLocalizedDescription();
				rsld.setRealSystem(newRealSystem);
				rsld.setLocale(locale);
				descriptions.put(locale.getLanguageCode(), rsld);
			}
			pictures = new LinkedList<>();
		}
	}

	
	public void submit() {
		try {
			List<RealSystemLocalizedDescription> rsldList = new LinkedList<>();
			for (RealSystemLocalizedDescription description : descriptions.values()) {
				rsldList.add(description);
			}
			List<RealSystemPicture> rsPictures = new LinkedList<>();
			for (String picName : pictures) {
				RealSystemPicture rsPicture = new RealSystemPicture();
				rsPicture.setName(picName);
				rsPicture.setRealSystem(newRealSystem);
				rsPictures.add(rsPicture);
			}
			newRealSystem.setAdditionDate(Calendar.getInstance());
			newRealSystem.setRealSystemLocalizedDescriptions(rsldList);
			newRealSystem.setRealSystemPictures(rsPictures);
			rsService.create(newRealSystem);
			Faces.redirect("user/listrealsystems.xhtml");
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		//return "/user/listrealsystems.xhtml";
	}
	
    public void handleFileUpload(FileUploadEvent event) {
    	String pictureName = event.getFile().getFileName();
    	String pictureFilePath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/")
    			+File.separator+PICTURES_DIR+File.separator+pictureName;
    	
    	System.out.println(pictureFilePath);
		try(FileOutputStream fos = new FileOutputStream(pictureFilePath)) {
			InputStream pictureInputStream = event.getFile().getInputstream();
			byte buffer[] = new byte[1024];
			int len;
			while ((len = pictureInputStream.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pictures.add(pictureName);
    } 

	public RealSystem getNewRealSystem() {
		return newRealSystem;
	}


	public void setNewRealSystem(RealSystem newRealSystem) {
		this.newRealSystem = newRealSystem;
	}


	public Map<String, RealSystemLocalizedDescription> getDescriptions() {
		return descriptions;
	}


	public void setDescriptions(Map<String, RealSystemLocalizedDescription> descriptions) {
		this.descriptions = descriptions;
	}


	public List<String> getPictures() {
		return pictures;
	}


	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

}
