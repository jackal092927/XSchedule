package com.tongji.share.view.schedule;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class ContextEditorBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8852389272583482228L;
	
	private String content;

	public ContextEditorBean() {
		content = "Hi Showcase User";
	}

	public void saveListener() {
		content = content.replaceAll("\\r|\\n", "");

		final FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Content", content.length() > 150 ? content.substring(0, 100)
						: content);

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}



	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

}
