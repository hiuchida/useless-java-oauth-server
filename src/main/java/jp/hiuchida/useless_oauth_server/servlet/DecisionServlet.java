package jp.hiuchida.useless_oauth_server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.hiuchida.useless_oauth_server.Const;
import jp.hiuchida.useless_oauth_server.DataStore;
import jp.hiuchida.useless_oauth_server.model.AuthorizationCode;
import jp.hiuchida.useless_oauth_server.model.Client;
import jp.hiuchida.useless_oauth_server.model.User;

/**
 * Decision Endpoint
 */
public class DecisionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DataStore dataStore = DataStore.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DecisionServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		Client client = (Client) session.getAttribute("client");
		String state = (String) session.getAttribute("state");
		@SuppressWarnings("unchecked")
		List<String> scopes = (List<String>) session.getAttribute("scopes");
		String redirect_uri = (String) session.getAttribute("redirect_uri");
		session.invalidate();

		// The response will be returned to the location pointed to by
		// the redirect URI with the 'state'.
		String location = redirect_uri + "?state=" + state;

		// If the 'Approve' button was not pressed.
		String approved = request.getParameter("approved");
		if (!"true".equals(approved)) {
			String redirect = location + "&error=access_denied" + "&error_description=The+request+was+not+approved.";
			response.sendRedirect(redirect);
			return;
		}

		// Look up a user by the login ID and the password.
		String login_id = request.getParameter("login_id");
		String password = request.getParameter("password");
		User user = dataStore.find_user(login_id, password);
		if (user == null) {
			String redirect = location + "&error=access_denied" + "&error_description=End-user+authentication-failed.";
			response.sendRedirect(redirect);
			return;
		}

		// Generate an authorization code and save it to the store.
		long expires_at = System.currentTimeMillis() + Const.AUTHORIZATION_CODE_DURATION * 1000L;
		AuthorizationCode code = new AuthorizationCode(user.user_id, client.client_id, scopes, redirect_uri,
				expires_at);
		dataStore.add_authorization_code(code.value, code);

		// Successful response with the authorization code.
		String redirect = location + "&code=" + code.value;
		response.sendRedirect(redirect);
	}

}
