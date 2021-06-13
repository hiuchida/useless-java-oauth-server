package jp.hiuchida.useless_oauth_server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.hiuchida.useless_oauth_server.DataStore;
import jp.hiuchida.useless_oauth_server.model.Client;

/**
 * Authorization Endpoint
 */
public class AuthorizationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DataStore dataStore = DataStore.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthorizationServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// --- client_id ---
		// Look up a client by the 'client_id' request parameter.
		String client_id = request.getParameter("client_id");
		Client client = dataStore.look_up_client(client_id);
		if (client == null) {
			response.sendError(400, "client_id is wrong.");
			return;
		}

		// --- redirect_uri ---
		// This implementation always requires the 'redirect_uri' request parameter.
		String redirect_uri = request.getParameter("redirect_uri");
		if (!client.redirect_uris.contains(redirect_uri)) {
			response.sendError(400, "redirect_uri is wrong.");
			return;
		}

		// --- response_type ---
		// Supports the authorization code flow only.
		String response_type = request.getParameter("response_type");
		if (!"code".equals(response_type)) {
			response.sendError(400, "response_type is wrong.");
			return;
		}

		// --- state ---
		String state = request.getParameter("state");
		if (state == null) {
			state = "";
		}

		// --- scope ---
		String scope = request.getParameter("scope");
		List<String> scopes = dataStore.filter_scopes(scope);

		// Put some parameters into the session for later use.
		HttpSession session = request.getSession();
		session.setAttribute("client", client);
		session.setAttribute("state", state);
		session.setAttribute("scopes", scopes);
		session.setAttribute("redirect_uri", redirect_uri);

		// Render the authorization page.
		request.setAttribute("client_name", client.client_name);
		request.setAttribute("scopes", scopes);
		RequestDispatcher dispatch = request.getRequestDispatcher("authorization_page.jsp");
		dispatch.forward(request, response);
	}

}
