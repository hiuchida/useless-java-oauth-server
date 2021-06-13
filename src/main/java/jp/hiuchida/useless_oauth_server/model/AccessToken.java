package jp.hiuchida.useless_oauth_server.model;

import java.util.List;

import jp.hiuchida.useless_oauth_server.Util;

/**
 * Access Token
 */
public class AccessToken {

	public final String value;
	public final String user_id;
	public final String client_id;
	public final List<String> scopes;
	public final long expires_at;

	public AccessToken(String user_id, String client_id, List<String> scopes, long expires_at) {
		this.value = Util.getRandam(6);
		this.user_id = user_id;
		this.client_id = client_id;
		this.scopes = scopes;
		this.expires_at = expires_at;
	}

}
