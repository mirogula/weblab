package sk.stuba.fei.weblab.web.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.omnifaces.util.Messages;

import sk.stuba.fei.weblab.web.model.Locale;
import sk.stuba.fei.weblab.web.model.Role;
import sk.stuba.fei.weblab.web.model.User;
import sk.stuba.fei.weblab.web.service.LocaleService;
import sk.stuba.fei.weblab.web.service.RoleService;
import sk.stuba.fei.weblab.web.service.UserService;

@Named
@RequestScoped
public class RegisterBean {

    private User user;

    @EJB
    private UserService userService;
    
    @EJB
    private LocaleService localeService;
    
    @EJB
    private RoleService roleService;
    
    @Inject
    LanguageBean lang;

    @PostConstruct
    public void init() {
        user = new User();
    }

    public void submit() {
        try {
        	user.setPassword(new Sha256Hash(user.getPassword()).toHex());
        	Map<String, Object> queryParamsMap = new HashMap<String, Object>();
        	queryParamsMap.put("languageCode", lang.getLocale().getLanguage());
        	Locale locale = (Locale)localeService.findWithNamedQuery("Locale.findByLanguageCode", queryParamsMap, 1).get(0);
        	user.setLocale(locale);
        	queryParamsMap = new HashMap<String, Object>();
        	queryParamsMap.put("roleName", "user");
        	Role roleUser = (Role)roleService.findWithNamedQuery("Role.findByRoleName", queryParamsMap, 1).get(0);
        	List<Role> roles = new ArrayList<>();
        	roles.add(roleUser);
        	user.setRoles(roles);
        	user.setSignupDate(Calendar.getInstance());
        	
        	
            userService.create(user);
            Messages.addGlobalInfo("Registration suceed, new user ID is: {0}", user.getId());
        }
        catch (RuntimeException e) {
            Messages.addGlobalError("Sign up failed: {0}", e.getMessage());
            e.printStackTrace(); // TODO: logger.
        }
    }

    public User getUser() {
        return user;
    }

}