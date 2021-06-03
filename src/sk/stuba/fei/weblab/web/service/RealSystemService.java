package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.RealSystem;


@Stateless
public class RealSystemService extends GenericDataAccessService<RealSystem> {
       
    public RealSystemService() {
        super(RealSystem.class);

    }
}
