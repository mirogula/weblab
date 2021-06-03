package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.RealSystemLocalizedDescription;


@Stateless
public class RealSystemLocalizedDescriptionService extends GenericDataAccessService<RealSystemLocalizedDescription> {
       
    public RealSystemLocalizedDescriptionService() {
        super(RealSystemLocalizedDescription.class);

    }
}
