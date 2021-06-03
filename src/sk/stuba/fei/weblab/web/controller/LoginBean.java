package sk.stuba.fei.weblab.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import sk.stuba.fei.weblab.web.model.Locale;
import sk.stuba.fei.weblab.web.model.Role;
import sk.stuba.fei.weblab.web.model.User;
import sk.stuba.fei.weblab.web.service.LocaleService;
import sk.stuba.fei.weblab.web.service.RoleService;
import sk.stuba.fei.weblab.web.service.UserService;

@Named
@RequestScoped
public class LoginBean {
	
    public static final String HOME_URL = "index.xhtml";

    private String username;
    private String password;
    
    @EJB
    private UserService userService;
//    
//    @Inject
//    private LocaleService localeService;
//    
//    @Inject 
//    private RoleService roleService;
//    
    @Inject
    private LanguageBean lang;
    

    public void submit() {
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password, false));
            
            
//            Map<String, Object> queryParamsMap = new HashMap<String, Object>();
//            queryParamsMap.put("username", username);
//            if(userService.findWithNamedQuery("User.findByName", queryParamsMap).isEmpty()) {
//            	User user = new User();
//            	user.setUsername(username);
//            	user.setPassword("");
//            	queryParamsMap = new HashMap<String, Object>();
//            	queryParamsMap.put("languageCode", lang.getLocale().getLanguage());
//            	Locale locale = (Locale)localeService.findWithNamedQuery("Locale.findByLanguageCode", queryParamsMap, 1).get(0);
//            	user.setLocale(locale);
//            	user.setEmail(username+"@is.stuba.sk");
//            	user.setSignupDate(Calendar.getInstance());
//            	queryParamsMap = new HashMap<String, Object>();
//            	queryParamsMap.put("roleName", "user");
//            	Role roleUser = (Role)roleService.findWithNamedQuery("Role.findByRoleName", queryParamsMap, 1).get(0);
//            	List<Role> roles = new ArrayList<>();
//            	roles.add(roleUser);
//            	user.setRoles(roles);
//            }
            Map<String, Object> queryParamsMap = new HashMap<>();
            queryParamsMap.put("username", username);
            User user = (User)userService.findWithNamedQuery("User.findByUsername", queryParamsMap,1).get(0);
//            lang.setLocale(new java.util.Locale(user.getLocale().getLanguageCode()));
           
            SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(Faces.getRequest());
            Faces.redirect(savedRequest != null ? savedRequest.getRequestUrl() : HOME_URL);
        } catch (UnknownAccountException e) {
            Messages.addGlobalError("Invalid username or password");
            e.printStackTrace(); // TODO: logger
        } catch (IncorrectCredentialsException e) {
        	Messages.addGlobalError("Wrong password, please try again");
        	e.printStackTrace();
		} catch (AuthenticationException e) {
            Messages.addGlobalError("Wrong password, please try again");
            e.printStackTrace(); // TODO: logger
		} catch (EJBException e) {
			e.printStackTrace(); // TODO: logger
		} catch (Exception e) {
			e.printStackTrace(); // TODO: logger
		}
    }


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}
