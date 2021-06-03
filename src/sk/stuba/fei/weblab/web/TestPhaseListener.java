package sk.stuba.fei.weblab.web;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class TestPhaseListener implements PhaseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void afterPhase(PhaseEvent evt) {
		System.out.println("after phase: "+evt.getPhaseId());
	}

	@Override
	public void beforePhase(PhaseEvent evt) {
		System.out.println("before phase: "+evt.getPhaseId());
		UIViewRoot viewRoot = evt.getFacesContext().getViewRoot();
		if(viewRoot != null) {
			printChildern(viewRoot);
		}
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
	
	private void printChildern(UIComponent component) {
		System.out.println("ID: "+component.getId()+", Family: "+component.getFamily()+", Class: "+component.getClass().getName());
		for (UIComponent child : component.getChildren()) {
			printChildern(child);
		}
	}

}
