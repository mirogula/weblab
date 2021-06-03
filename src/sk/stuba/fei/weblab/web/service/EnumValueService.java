package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.EnumValue;


@Stateless
public class EnumValueService extends GenericDataAccessService<EnumValue> {
       
    public EnumValueService() {
        super(EnumValue.class);

    }
}
