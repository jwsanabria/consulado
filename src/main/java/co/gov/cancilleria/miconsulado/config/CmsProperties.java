package co.gov.cancilleria.miconsulado.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;




public class CmsProperties {
	private String user;
	private String password;
	private String host;
	private int port;
	private boolean https;
	
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public boolean isHttps() {
		return https;
	}
	public void setHttps(boolean https) {
		this.https = https;
	}
	@Override
	public String toString() {
		return "CmsProperties [user=" + user + ", password=" + password + ", host=" + host + ", port=" + port
				+ ", https=" + https + "]";
	}
	
	
	
}
