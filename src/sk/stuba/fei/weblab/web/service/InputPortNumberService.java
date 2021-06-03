package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.InputPortNumber;


@Stateless
public class InputPortNumberService extends GenericDataAccessService<InputPortNumber> {
       
    public InputPortNumberService() {
        super(InputPortNumber.class);

    }
}
