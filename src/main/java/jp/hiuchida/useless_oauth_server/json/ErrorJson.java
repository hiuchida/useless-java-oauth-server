package jp.hiuchida.useless_oauth_server.json;

import com.fasterxml.jackson.annotation.JsonInclude;

import jp.hiuchida.useless_oauth_server.Util;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorJson {

	public final String error;
	public final String error_description;

	public ErrorJson(String error) {
		this.error = error;
		this.error_description = null;
	}

	public ErrorJson(String error, String error_description) {
		this.error = error;
		this.error_description = error_description;
	}

	public String toJsonString() {
		return Util.toJsonString(this);
	}

}
