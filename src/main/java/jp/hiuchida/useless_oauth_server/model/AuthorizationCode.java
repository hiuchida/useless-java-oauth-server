package jp.hiuchida.useless_oauth_server.model;

import java.util.List;

import jp.hiuchida.useless_oauth_server.Util;

/**
 * Authorization Code
 */
public class AuthorizationCode {

	public final String value;
	public final String user_id;
	public final String client_id;
	public final List<String> scopes;
	public final String redirect_uri;
	public final long expires_at;

	public AuthorizationCode(String user_id, String client_id, List<String> scopes, String redirect_uri,
			long expires_at) {
		this.value = Util.getRandam(6);
		this.user_id = user_id;
		this.client_id = client_id;
		this.scopes = scopes;
		this.redirect_uri = redirect_uri;
		this.expires_at = expires_at;
	}

}
