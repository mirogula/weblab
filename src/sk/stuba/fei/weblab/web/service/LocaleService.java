package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.Locale;


@Stateless
public class LocaleService extends GenericDataAccessService<Locale> {
       
    public LocaleService() {
        super(Locale.class);

    }
}
