package sk.stuba.fei.weblab.web.controller;

import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;

@Named
@RequestScoped
public class TestBean {
	
//	@ManagedProperty("#{param.text}")
	private String text;
	private String text1;
	
//	@PostConstruct
	public void init() {
		System.out.println(text);
		System.out.println(text1);
		
		System.out.println("is post-back request: "+FacesContext.getCurrentInstance().isPostback());
		
		UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
		if(viewRoot != null) {
			printChildern(viewRoot, "");
		}
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		System.out.println("text from backing bean: "+text);
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}
	
	public String testAction() {
		System.out.println("testAction() executed!!!");
		
		return null;
	}

	
	public void testActionListener(ActionEvent evt) {
		System.out.println("testActionListener() executed!!!");
		
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		for (Entry<String, Object> sessionEntry : sessionMap.entrySet()) {
			System.out.println("name: "+sessionEntry.getKey()+", value type: "+sessionEntry.getValue().getClass().getName());
		}
	}
	
	
	private void printChildern(UIComponent component, String padding) {
		System.out.println(padding+"ID: "+component.getId()+", Family: "+component.getFamily()+", Class: "+component.getClass().getName());
		Map<String, Object> attrs = component.getAttributes();
		if(!attrs.isEmpty()) {
			System.out.println(padding+"ATTRIBUTES:");
			for (Entry<String, Object> attrEntry : attrs.entrySet()) {
				System.out.println(padding+"name: "+attrEntry.getKey()+", value: "+attrEntry.getValue().getClass().getName());
			}
		}
		for (UIComponent child : component.getChildren()) {
			printChildern(child, padding+"\t");
		}
	}

}
