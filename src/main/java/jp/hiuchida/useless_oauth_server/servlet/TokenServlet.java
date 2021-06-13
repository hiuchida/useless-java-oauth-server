package jp.hiuchida.useless_oauth_server.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.hiuchida.useless_oauth_server.Const;
import jp.hiuchida.useless_oauth_server.DataStore;
import jp.hiuchida.useless_oauth_server.json.ErrorJson;
import jp.hiuchida.useless_oauth_server.json.TokenJson;
import jp.hiuchida.useless_oauth_server.model.AccessToken;
import jp.hiuchida.useless_oauth_server.model.AuthorizationCode;

/**
 * Token Endpoint
 */
public class TokenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private DataStore dataStore = DataStore.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TokenServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json");

		try {
			// --- grant_type ---
			// Supports the authorization code flow only.
			String grant_type_value = extract_mandatory_parameter(request, "grant_type");
			if (!"authorization_code".equals(grant_type_value)) {
				ErrorJson json = new ErrorJson("unsupported_grant_type");
				throw new IllegalArgumentException(json.toJsonString());
			}

			// --- code ---
			// In the authorization code flow, the 'code' parameter is mandatory.
			String code_value = extract_mandatory_parameter(request, "code");
			AuthorizationCode code = dataStore.find_authorization_code(code_value);
			if (code == null) {
				ErrorJson json = new ErrorJson("invalid_grant", "The authorization code is not found.");
				throw new IllegalArgumentException(json.toJsonString());
			}
			if (code.expires_at < System.currentTimeMillis()) {
				dataStore.delete_authorization_code(code_value);
				ErrorJson json = new ErrorJson("invalid_grant", "The authorization code has expired.");
				throw new IllegalArgumentException(json.toJsonString());
			}

			// --- redirect_uri --
			// If the corresponding authorization request included the 'redirect_uri'
			// parameter, the token request also must include the 'redirect_uri'
			// parameter. This implementation rejects all authorization requests that
			// don't include the 'redirect_uri' parameter.
			String redirect_uri_value = extract_mandatory_parameter(request, "redirect_uri");
			if (!redirect_uri_value.equals(code.redirect_uri)) {
				ErrorJson json = new ErrorJson("invalid_grant", "redirect_uri is wrong.");
				throw new IllegalArgumentException(json.toJsonString());
			}

			// --- client_id ---
			// The 'client_id' parameter is mandatory unless the client type is
			// confidential and the client authentication method does not need the
			// 'client_id' parameter.
			String client_id_value = extract_mandatory_parameter(request, "client_id");
			if (!client_id_value.equals(code.client_id)) {
				ErrorJson json = new ErrorJson("invalid_grant", "client_id is wrong.");
				throw new IllegalArgumentException(json.toJsonString());
			}

			// Generate an access token and save it to the store.
			long expires_at = System.currentTimeMillis() + Const.ACCESS_TOKEN_DURATION * 1000L;
			AccessToken token = new AccessToken(code.user_id, code.client_id, code.scopes, expires_at);
			dataStore.add_access_token(token.value, token);

			// Remove the used authorization code.
			dataStore.delete_authorization_code(code_value);

			// Successful response with the access token.
			TokenJson json = new TokenJson(token.value, "Bearer", Const.ACCESS_TOKEN_DURATION, token.scopes);
			response.getWriter().print(json.toJsonString());
		} catch (IllegalArgumentException e) {
			response.setStatus(400);
			response.getWriter().print(e.getMessage());
		}
	}

	private String extract_mandatory_parameter(HttpServletRequest request, String key) throws IOException {
		String value = request.getParameter(key);

		if (value == null || "".equals(value)) {
			ErrorJson json = new ErrorJson("invalid_request", key + " is missing.");
			throw new IllegalArgumentException(json.toJsonString());
		}

		return value;
	}

}
