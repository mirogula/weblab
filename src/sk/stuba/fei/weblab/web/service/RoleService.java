package sk.stuba.fei.weblab.web.service;


import javax.ejb.Stateless;

import sk.stuba.fei.weblab.web.model.Role;


@Stateless
public class RoleService extends GenericDataAccessService<Role> {
       
    public RoleService() {
        super(Role.class);

    }
}
