package sk.stuba.fei.weblab.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.omnifaces.util.Messages;

import sk.stuba.fei.weblab.web.model.Locale;
import sk.stuba.fei.weblab.web.model.User;
import sk.stuba.fei.weblab.web.service.LocaleService;
import sk.stuba.fei.weblab.web.service.UserService;

@Named
@RequestScoped
public class UserProfileBean {
	
	private User user;
	
	private String newPassword;	
	private String checkOldPassword;
	private String email;
	private Map<String, Integer> languages;
	private Integer selectedLanguage;
	
	
	
	@EJB
	private UserService userService;
	
	@EJB
	private LocaleService localeService;
	
	@PostConstruct
	public void init() {
		try {
			user = userService.findUserByUsername((String)SecurityUtils.getSubject().getPrincipal());
			email = user.getEmail();
			languages = new HashMap<>();
			List<Locale> locales = localeService.findAll();
			selectedLanguage = user.getLocale().getId();
			for (Locale locale : locales) {
				languages.put(locale.getLanguageName(), locale.getId());
			}
		} catch (Exception e) {
            Messages.addGlobalError("Init profile failed: {0}", e.getMessage());
            e.printStackTrace(); // TODO: logger.
		}
		
	}

	
	public void update() {
		try {
			user.setEmail(email);
			Locale locale = localeService.find(selectedLanguage);
			user.setLocale(locale);
			userService.update(user);
			System.out.println(user.getEmail());
			Messages.addGlobalInfo("Succefully saved");
		} catch (RuntimeException e) {
            Messages.addGlobalError("Updating failed: {0}", e.getMessage());
            e.printStackTrace(); // TODO: logger.
		}
	}
	
	public void updatePassword() {
		try {
			String hashedCheckPassword = new Sha256Hash(checkOldPassword).toHex();
			String hashedNewPassword = new Sha256Hash(newPassword).toHex();
			if(user.getPassword().equals(hashedCheckPassword)) {
				user.setPassword(hashedNewPassword);
				userService.update(user);
				Messages.addGlobalInfo("Succefully saved");
			} else {
				Messages.addGlobalError("Wrong old password, password wasn't changed");
			}
			
		} catch (RuntimeException e) {
            Messages.addGlobalError("Updating failed: {0}", e.getMessage());
            e.printStackTrace(); // TODO: logger.
		}
	}
	
	public User getUser() {
		return user;
	}


//	public void setUser(User user) {
//		this.user = user;
//	}
	
	
	public String getNewPassword() {
		return newPassword;
	}


	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}


	public String getCheckOldPassword() {
		return checkOldPassword;
	}


	public void setCheckOldPassword(String checkPassword) {
		this.checkOldPassword = checkPassword;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Map<String, Integer> getLanguages() {
		return languages;
	}


	public void setLanguages(Map<String, Integer> languages) {
		this.languages = languages;
	}


	public Integer getSelectedLanguage() {
		return selectedLanguage;
	}


	public void setSelectedLanguage(Integer selectedLanguage) {
		this.selectedLanguage = selectedLanguage;
	}
	
}
