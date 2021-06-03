package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.RealSystemReservation;


@Stateless
public class RealSystemReservationService extends GenericDataAccessService<RealSystemReservation> {
       
    public RealSystemReservationService() {
        super(RealSystemReservation.class);

    }
}
