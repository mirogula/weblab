package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.RealSystemPicture;


@Stateless
public class RealSystemPictureService extends GenericDataAccessService<RealSystemPicture> {
       
    public RealSystemPictureService() {
        super(RealSystemPicture.class);

    }
}
