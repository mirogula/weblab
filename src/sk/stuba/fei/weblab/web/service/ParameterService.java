package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.Parameter;


@Stateless
public class ParameterService extends GenericDataAccessService<Parameter> {
       
    public ParameterService() {
        super(Parameter.class);

    }
}
