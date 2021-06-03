package sk.stuba.fei.weblab.web.controller;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.sun.jersey.api.client.ClientHandlerException;

import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient;
import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterClient.MatlabAdapterStatus;
import sk.stuba.fei.weblab.matlabadapterclient.MatlabAdapterException;

@Named
@SessionScoped
public class ExperimentBean implements Serializable {

	
	private TreeNode root;
	
	private TreeNode[] selectedNodes;
	
	private MatlabAdapterClient maClient;
	
	private boolean renderPanels = false;
	
	@PostConstruct
	public void init() {
		root = new DefaultTreeNode("Root", null);
        TreeNode node0 = new DefaultTreeNode("Scope", root);  
  
        TreeNode node00 = new DefaultTreeNode("input port: 0", node0);   
  
        TreeNode node000 = new DefaultTreeNode("timeseries: 0", node00);  
        TreeNode node001 = new DefaultTreeNode("timeseries: 1", node00);
	}

	public TreeNode getRoot() {
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
	
	
	
	
	public String openConnection(String url) {
		
		
		
//		maClient = new MatlabAdapterClient(url);
//		try {
////			if(maClient.getStatus() == MatlabAdapterStatus.CONNECTED_TO_MATLAB) {
////				return "/user/experiment?faces-redirect=true";
////			}
//			maClient.connect(MatlabAdapterClient.CONNECTION_CLASS_MATLABCONTROL);
//		} catch (ClientHandlerException e) {
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "nepodarilo sa nadviazať spojenie so vzdialenou sústavou", e.getMessage());
//			FacesContext.getCurrentInstance().addMessage(null, message);
//			return null;
//		} catch (MatlabAdapterException e) {
//			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "nepodarilo sa spustiť inštanciu matlabu", e.getMessage());
//			FacesContext.getCurrentInstance().addMessage(null, message);
//			System.out.println(e.getHttpStatusCode());
//			System.out.println(e.getErrorCode());
//			System.out.println(e.getErrorMessage());
//			System.out.println(e.getExceptionMessage());
//			System.out.println(e.getExceptionStackTrace());
//			return null;
//		}
		
		return "/user/experiment?faces-redirect=true";
	}
	
	public String closeConnection() {
		try {
			maClient.disconnect();
		} catch (ClientHandlerException | MatlabAdapterException e) {
			e.printStackTrace();
		} finally {
			return "/user/listrealsystems?faces-redirect=true";
		}
	}
	
	public void openBlockDiagram(String name) {
		try {
			maClient.openModel(name, true);
			renderPanels = true;
		} catch (ClientHandlerException | MatlabAdapterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
