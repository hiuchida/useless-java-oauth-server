package jp.hiuchida.useless_oauth_server.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import jp.hiuchida.useless_oauth_server.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenJson {

	public final String access_token;
	public final String token_type;
	public final Long expires_in;
	public final String scope;

	public TokenJson(String access_token, String token_type, long expires_in, List<String> scopes) {
		this.access_token = access_token;
		this.token_type = token_type;
		this.expires_in = expires_in;
		this.scope = String.join(" ", scopes);
	}

	public String toJsonString() {
		return Util.toJsonString(this);
	}

}
