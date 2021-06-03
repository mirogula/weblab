package sk.stuba.fei.weblab.web.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.omnifaces.util.Faces;

@Named
@RequestScoped
public class LogoutBean {

	    public void submit() {
	    	try {
		        SecurityUtils.getSubject().logout();
		        Faces.invalidateSession();
		        Faces.redirect(LoginBean.HOME_URL);
			} catch (Exception e) {
				e.printStackTrace();
			}
	    }

}
