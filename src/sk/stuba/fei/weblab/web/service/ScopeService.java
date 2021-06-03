package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.Scope;


@Stateless
public class ScopeService extends GenericDataAccessService<Scope> {
       
    public ScopeService() {
        super(Scope.class);

    }
}
