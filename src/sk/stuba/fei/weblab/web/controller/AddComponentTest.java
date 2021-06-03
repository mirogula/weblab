package sk.stuba.fei.weblab.web.controller;

import java.io.Serializable;
import java.util.Collection;

import javax.el.MethodExpression;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;

import org.primefaces.component.panel.Panel;


@ManagedBean
@ViewScoped
public class AddComponentTest implements Serializable {

//	private transient UIComponent rootComponent;
	private int i = 0;
	private int componentIdToRemove;

	
//	public UIComponent getRootComponent() {
//		return rootComponent;
//	}
//
//	public void setRootComponent(UIComponent rootComponent) {
//		this.rootComponent = rootComponent;
//	}
	
	public int getComponentIdToRemove() {
		return componentIdToRemove;
	}

	public void setComponentIdToRemove(int componentIdToremove) {
		this.componentIdToRemove = componentIdToremove;
	}
	
	public void addComponent() {
		System.out.println("test");	
		Panel p = new Panel();
		p.setHeader("test panel "+i);
		p.setId("panel"+i);
		p.setClosable(true);
		AjaxBehavior ab = new AjaxBehavior();
		FacesContext fctx = FacesContext.getCurrentInstance(); 
		MethodExpression me = fctx.getApplication().getExpressionFactory().createMethodExpression(fctx.getELContext(), 
				"#{addComponentTest.removeComponentTwo(panel"+i+")}", null, null);
//		ab.setListener(me);
//		ab.addAjaxBehaviorListener(new AjaxBehaviorListener() {
//			
//			@Override
//			public void processAjaxBehavior(AjaxBehaviorEvent arg0)
//					throws AbortProcessingException {
//				System.out.println("hello from procesAjaxBehavior");
//			}
//		});
		p.addClientBehavior("close", ab);
		//rootComponent.getChildren().add(p);
		FacesContext.getCurrentInstance().getViewRoot().findComponent("rootPanel").getChildren().add(p);
		i++;
	}
	
	public void removeComponent() {
		UIComponent component = FacesContext.getCurrentInstance()
				.getViewRoot().findComponent("panel"+componentIdToRemove);
		if(component == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", 
					"component with name '"+"test panel "+componentIdToRemove+"' does not exist");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		//rootComponent.getChildren().remove(component);
		FacesContext.getCurrentInstance().getViewRoot().findComponent("rootPanel").getChildren().remove(component);
	}
	
	public void removeComponentTwo(String componentId) {
		System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
		UIComponent component = FacesContext.getCurrentInstance()
				.getViewRoot().findComponent(componentId);
		if(component == null) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR:", 
					"component with id '"+componentId+"' does not exist");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return;
		}
		//rootComponent.getChildren().remove(component);
		FacesContext.getCurrentInstance().getViewRoot().findComponent("rootPanel").getChildren().remove(component);
	}

	
}
