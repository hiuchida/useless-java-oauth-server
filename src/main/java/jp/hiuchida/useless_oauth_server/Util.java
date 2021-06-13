package jp.hiuchida.useless_oauth_server;

import java.security.SecureRandom;
import java.util.Base64;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

	public static String getRandam(int len) {
		SecureRandom random = new SecureRandom();
		byte buf[] = new byte[len];
		random.nextBytes(buf);
		return Base64.getUrlEncoder().encodeToString(buf);
	}

	public static String toJsonString(Object o) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(o);
			return json;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
