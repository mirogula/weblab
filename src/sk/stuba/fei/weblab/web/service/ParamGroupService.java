package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.ParamGroup;


@Stateless
public class ParamGroupService extends GenericDataAccessService<ParamGroup> {
       
    public ParamGroupService() {
        super(ParamGroup.class);

    }
}
