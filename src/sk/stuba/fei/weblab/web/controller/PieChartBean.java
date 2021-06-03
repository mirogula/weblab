package sk.stuba.fei.weblab.web.controller;

import java.io.Serializable;


import java.io.Serializable;  

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.PieChartModel;  

@ManagedBean
@ViewScoped
public class PieChartBean implements Serializable {  
  
    private PieChartModel livePieModel;  
  
    public PieChartBean() {  
        createLivePieModel();  
    }  
  
    public PieChartModel getLivePieModel() {  
        int random1 = (int)(Math.random() * 1000);  
        int random2 = (int)(Math.random() * 1000);  
  
        livePieModel.getData().put("Candidate 1", random1);  
        livePieModel.getData().put("Candidate 2", random2);  
  
        return livePieModel;  
    }  
  
    private void createLivePieModel() {  
        livePieModel = new PieChartModel();  
  
        livePieModel.set("Candidate 1", 540);  
        livePieModel.set("Candidate 2", 325);  
    } 
}
