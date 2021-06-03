package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.SimulinkSchema;


@Stateless
public class SimulinkSchemaService extends GenericDataAccessService<SimulinkSchema> {
       
    public SimulinkSchemaService() {
        super(SimulinkSchema.class);

    }
}
