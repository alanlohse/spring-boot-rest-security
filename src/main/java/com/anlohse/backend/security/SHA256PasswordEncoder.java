package com.anlohse.backend.security;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This is a customized password encoder. It's necessary because the password encoders already provided by spring don't generate a constant hash.
 * @author alanlohse
 *
 */
public class SHA256PasswordEncoder implements PasswordEncoder {

	@Override
	public String encode(CharSequence rawPassword) {
		return Base64.encodeBase64String(DigestUtils.sha256(rawPassword.toString()));
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}

	public static String encode(String rawPassword) {
		return Base64.encodeBase64String(DigestUtils.sha256(rawPassword.toString()));
	}

	public static void main(String[] args) {
		// generate the hash for a test password
		System.out.println(encode("123"));
	}

}
