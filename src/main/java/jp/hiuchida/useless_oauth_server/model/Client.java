package jp.hiuchida.useless_oauth_server.model;

import java.util.List;

/**
 * Client Application
 */
public class Client {

	public final String client_id;
	public final String client_name;
	public final List<String> redirect_uris;

	public Client(String client_id, String client_name, List<String> redirect_uris) {
		this.client_id = client_id;
		this.client_name = client_name;
		this.redirect_uris = redirect_uris;
	}

}
