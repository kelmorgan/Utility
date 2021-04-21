package com.fbn.start;

import com.fbn.utils.ConstantsI;
import com.newgen.omni.wf.util.app.NGEjbClient;

import javax.jws.soap.SOAPBinding;

public class CommonsProperties implements ConstantsI {
	public CommonsProperties() {
		setAppServerIp();
		setAppServerPort();
		setAppServerType();
		setUserName();
		setPassword();
		setCabinetName();
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAppServerIp() {
		return appServerIp;
	}

	public void setAppServerIp() {
		this.appServerIp = AppServerIp;
	}

	public String getAppServerPort() {
		return appServerPort;
	}

	public void setAppServerPort() {
		this.appServerPort = AppServerPort;
	}

	public NGEjbClient getNgEjbClient() {
		return ngEjbClient;
	}

	public void setNgEjbClient(NGEjbClient ngEjbClient) {
		this.ngEjbClient = ngEjbClient;
	}

	public String getWrapperPort() {
		return wrapperPort;
	}

	public void setWrapperPort(String wrapperPort) {
		this.wrapperPort = wrapperPort;
	}

	public String getAppServerType() {
		return appServerType;
	}

	public void setAppServerType() {
		this.appServerType = AppServerType;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName() {
		this.userName = UserName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword() {
		this.password = Password;
	}

	private String sessionId;
	private String appServerIp;
	private String appServerPort;
	private NGEjbClient ngEjbClient;
	private String wrapperPort;
	private String appServerType;
	private String userName;
	private String password;

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName() {
		this.cabinetName = CabinetName;
	}

	private String cabinetName;

}
