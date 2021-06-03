package sk.stuba.fei.weblab.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.Cookie;

@Named
//@SessionScoped
@RequestScoped
public class LanguageBean implements Serializable {
	
	private static final String LANG_COOKIE_NAME = "jlab-locale";
	private static final int LANG_COOKIE_MAX_AGE = 60*60*24*365;
	
//	private Locale locale;

//	@PostConstruct
//	public void init() {
//		this.locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();
//	}
	
	public void changeLocale(String localeStr) {
		setLocale(parseLocaleFromLanguageAndCountry(localeStr));
	}

	public Locale getLocale() {
		Locale locale;
		Cookie langCookie = (Cookie)FacesContext.getCurrentInstance()
				.getExternalContext().getRequestCookieMap().get(LANG_COOKIE_NAME);
		if(langCookie == null || langCookie.getValue().equals("")) {
			locale = FacesContext.getCurrentInstance().getApplication().getDefaultLocale();	
			Map<String, Object> cookieParams = new HashMap<>();
			cookieParams.put("path", "/");
			cookieParams.put("maxAge", LANG_COOKIE_MAX_AGE);
			FacesContext.getCurrentInstance().getExternalContext().addResponseCookie(LANG_COOKIE_NAME, locale.toString(), cookieParams);	
		} else {
			locale = parseLocaleFromLanguageAndCountry(langCookie.getValue());
			langCookie.setMaxAge(LANG_COOKIE_MAX_AGE);
		}
		
		return locale;
		
//		return locale;
	}

	public void setLocale(Locale locale) {
//		Cookie langCookie = (Cookie)FacesContext.getCurrentInstance()
//				.getExternalContext().getRequestCookieMap().get(LANG_COOKIE_NAME);
//		if(langCookie == null) {
			Map<String, Object> cookieParams = new HashMap<>();
			cookieParams.put("path", "/");
			cookieParams.put("maxAge", LANG_COOKIE_MAX_AGE);
			FacesContext.getCurrentInstance().getExternalContext().addResponseCookie(LANG_COOKIE_NAME, locale.toString(), cookieParams);
//		} else {
//			langCookie.setValue(locale.getLanguage());
//			langCookie.setMaxAge(LANG_COOKIE_MAX_AGE);
//		}
		
//		this.locale = locale;
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}
	
	
	public List<Locale> getAvailableLocales() {
		List<Locale> availableLocales = new ArrayList<Locale>();
		Application application = FacesContext.getCurrentInstance().getApplication();
		
		if(application.getDefaultLocale() != null) {
			availableLocales.add(application.getDefaultLocale());
		}
		
		Iterator<Locale> supportedLocalesIt = application.getSupportedLocales();
		while (supportedLocalesIt.hasNext()) {
			Locale supportedLocale = supportedLocalesIt.next();
			if(!supportedLocale.equals(application.getDefaultLocale())) {
				availableLocales.add(supportedLocale);
			}
			
		}
		
		return availableLocales;
	}
	
	private Locale parseLocaleFromLanguageAndCountry(String languageCountry) {
		String[] languageCountryArray = languageCountry.split("_");
		Locale locale = null;

//		if(languageCountryArray.length < 2) {
//			throw new IllegalArgumentException("string argument must be in format 'language_COUNTRY', for example en_US");
//		}
		
		
		if(languageCountryArray.length < 2) {
			locale = new Locale(languageCountry);
		} else {
			locale = new Locale(languageCountryArray[0], languageCountryArray[1]);
		}
		
		return locale;
	}
}
