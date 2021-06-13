package jp.hiuchida.useless_oauth_server.model;

/**
 * User
 */
public class User {

	public final String user_id;
	public final String login_id;
	public final String password;

	public User(String user_id, String login_id, String password) {
		this.user_id = user_id;
		this.login_id = login_id;
		this.password = password;
	}

}
