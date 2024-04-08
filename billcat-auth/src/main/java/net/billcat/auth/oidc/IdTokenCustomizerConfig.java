package net.billcat.auth.oidc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

@Configuration
public class IdTokenCustomizerConfig {

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(net.billcat.auth.oidc.OidcUserInfoService userInfoService) {
		return (context) -> {
			if (OidcParameterNames.ID_TOKEN.equals(context.getTokenType().getValue())) {
				OidcUserInfo userInfo = userInfoService.loadUser(context.getPrincipal().getName());
				context.getClaims().claims(claims -> claims.putAll(userInfo.getClaims()));
			}
		};
	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("password"));
	}

}
