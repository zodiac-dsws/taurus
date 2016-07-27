package br.cefetrj.sagitarii.action;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;

import br.cefetrj.sagitarii.persistence.entity.User;

public class BasicActionClass {
	private User loggedUser;
	private String serverBaseUrl;
	
	public String getServerBaseUrl() {
		return serverBaseUrl;
	}
	
	public User getLoggedUser() {
		loggedUser = (User)ActionContext.getContext().getSession().get("loggedUser");
		return loggedUser;
	}
	
	public void setMessageText(String messageText) {
		messageText = messageText.replaceAll("[\n\r]", "");
		ActionContext.getContext().getSession().put("messageText", messageText );
	}

	public String getMessageText() {
		String messageText = (String)ActionContext.getContext().getSession().get("messageText");
		setMessageText("");
		return messageText;
	}

	public BasicActionClass() {
		HttpServletRequest request = ServletActionContext.getRequest();
		serverBaseUrl = request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
	}

	
}
