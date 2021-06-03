package sk.stuba.fei.weblab.web.controller;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class CssTestBean {

	private String cssTag = "<style type='text/css'>.blue {color: #0000FF;}</style>";

	public String getCssTag() {
		return cssTag;
	}

	public void setCssTag(String cssTag) {
		this.cssTag = cssTag;
	}
}
