package jp.hiuchida.useless_oauth_server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import jp.hiuchida.useless_oauth_server.model.AccessToken;
import jp.hiuchida.useless_oauth_server.model.AuthorizationCode;
import jp.hiuchida.useless_oauth_server.model.Client;
import jp.hiuchida.useless_oauth_server.model.User;

/**
 * Data Stores
 */
public class DataStore {

	private static DataStore singleton = new DataStore();

	public static DataStore getInstance() {
		return singleton;
	}

	/**
	 * Registered Client Applications
	 */
	private Map<String, Client> clientStore = new TreeMap<>();

	/**
	 * Registered Users
	 */
	private Map<String, User> userStore = new TreeMap<>();

	/**
	 * Issued Authorization Codes Keys are AuthorizationCode.value, values are
	 * AuthorizationCode instances.
	 */
	private Map<String, AuthorizationCode> authorizationCodeStore = new TreeMap<>();

	/**
	 * Issued Access Tokens Keys are AccessToken.value, values are AccessToken
	 * instances.
	 */
	private Map<String, AccessToken> accessTokenStore = new TreeMap<>();

	public DataStore() {
		List<String> l1 = new ArrayList<>();
		l1.add("http://example.com/");
		Client c1 = new Client("1", "My Client", l1);
		clientStore.put(c1.client_id, c1);

		User u1 = new User("1", "john", "john");
		userStore.put(u1.login_id, u1);
	}

	public Client look_up_client(String value) {
		if (value == null) {
			return null;
		}
		return clientStore.get(value);
	}

	public User find_user(String login_id, String password) {
		if (login_id == null) {
			return null;
		}
		User user = userStore.get(login_id);
		if (user == null || !user.password.equals(password)) {
			return null;
		}
		return user;
	}

	public List<String> filter_scopes(String value) {
		List<String> list = new ArrayList<>();
		if (value == null) {
			return list;
		}
		String[] scopes = value.split(" ");
		for (String scope : scopes) {
			if (Const.SUPPORTED_SCOPES.contains(scope)) {
				list.add(scope);
			}
		}
		return list;
	}

	public void add_authorization_code(String code_value, AuthorizationCode code) {
		authorizationCodeStore.put(code_value, code);
	}

	public AuthorizationCode find_authorization_code(String code_value) {
		return authorizationCodeStore.get(code_value);
	}

	public void delete_authorization_code(String code_value) {
		authorizationCodeStore.remove(code_value);
	}

	public void add_access_token(String token_value, AccessToken token) {
		accessTokenStore.put(token_value, token);
	}

}
