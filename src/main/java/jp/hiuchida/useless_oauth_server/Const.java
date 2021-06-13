package jp.hiuchida.useless_oauth_server;

import java.util.ArrayList;
import java.util.List;

/**
 * Constants
 */
public class Const {

	public static final List<String> SUPPORTED_SCOPES = new ArrayList<>();
	public static final int AUTHORIZATION_CODE_DURATION = 600;
	public static final int ACCESS_TOKEN_DURATION = 86400;

	static {
		SUPPORTED_SCOPES.add("read");
		SUPPORTED_SCOPES.add("write");
	}

}
