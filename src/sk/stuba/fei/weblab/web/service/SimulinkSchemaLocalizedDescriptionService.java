package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.SimulinkSchemaLocalizedDescription;


@Stateless
public class SimulinkSchemaLocalizedDescriptionService extends GenericDataAccessService<SimulinkSchemaLocalizedDescription> {
       
    public SimulinkSchemaLocalizedDescriptionService() {
        super(SimulinkSchemaLocalizedDescription.class);

    }
}
