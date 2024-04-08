package net.billcat.auth.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import net.billcat.auth.jose.Jwks;
import net.billcat.auth.mixin.SingletonMapMixin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class AuthorizationServerConfig {

	private static final String CUSTOM_CONSENT_PAGE_URI = "/oauth2/consent";

	@Value("${app.auth-server}")
	private String appAuthServer;

	/**
	 * Here we expose a SecurityFilterChain which is a new way of configuring spring
	 * security instead of the old where we would extend the WebSecurityConfigurerAdapter.
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
			RegisteredClientRepository registeredClientRepository,
			AuthorizationServerSettings authorizationServerSettings) throws Exception {

		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

		/*
		 * This sample demonstrates the use of a public client that does not store
		 * credentials or authenticate with the authorization server.
		 *
		 * The following components show how to customize the authorization server to
		 * allow for device clients to perform requests to the OAuth 2.0 Device
		 * Authorization Endpoint and Token Endpoint without a clientId/clientSecret.
		 *
		 * CAUTION: These endpoints will not require any authentication, and can be
		 * accessed by any client that has a valid clientId.
		 *
		 * It is therefore RECOMMENDED to carefully monitor the use of these endpoints and
		 * employ any additional protections as needed, which is outside the scope of this
		 * sample.
		 */

		// @formatter:off
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
				.authorizationEndpoint(authorizationEndpoint ->
						authorizationEndpoint.consentPage(CUSTOM_CONSENT_PAGE_URI))
				.oidc(Customizer.withDefaults());	// Enable OpenID Connect 1.0
		// @formatter:on

		// @formatter:off
		http
				.exceptionHandling((exceptions) -> exceptions
						.defaultAuthenticationEntryPointFor(
								new LoginUrlAuthenticationEntryPoint("/login"),
								new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
						)
				)
				// 官方没加 cors 设置,  和前端使用 oidc client 的时候就会报下面的错误
				// 另见: https://github.com/spring-projects/spring-authorization-server/issues/809
				// Access to fetch at 'http://billcat-auth:9000/.well-known/openid-configuration'
				// from origin 'http://127.0.0.1:3000' has been blocked by CORS policy:
				// No 'Access-Control-Allow-Origin' header is present on the requested resource.
				// If an opaque response serves your needs, set the request's mode to 'no-cors' to fetch the resource with CORS disabled.
				.cors(cors -> cors.configurationSource(request -> CorsConfig.create()))

				.oauth2ResourceServer(oauth2ResourceServer ->
						oauth2ResourceServer.jwt(Customizer.withDefaults()));
		// @formatter:on
		return http.build();
	}

	/**
	 * Next we expose a RegisteredClientRepository bean, which you can think of as a
	 * UserDetailsService but only for clients. If you check the interface you will see it
	 * has the same methods as the UserDetailsService.
	 * @param jdbcTemplate
	 * @return
	 */
	@Bean
	public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {

		// 改了 RegisteredClientRepository 记得清一下数据库.
		String clientId = "client";
		RegisteredClient webClient = RegisteredClient.withId(UUID.randomUUID().toString())
			.clientId(clientId)
			.clientSecret("{noop}secret")

			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			// 官方没加 ClientAuthenticationMethod.NONE 设置,  和前端使用 oidc client 的时候就会报下面的错误
				// 调用 oauth/token 时报 invalid_client 的错误
			.clientAuthenticationMethod(ClientAuthenticationMethod.NONE)

			// Note that we defined the client with ClientAuthenticationMethod.NONE
			// and that is because I will use postman to get the token, postman is a
			// public client
			// hence we cannot safely send client credentials and for that reason we use
			// PKCE.
			// It seems like spring security authorization server will require PKCE by
			// default
			// if the client authentication is set to none, which is a good thing.
			// .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)

			// hash fragment is not allowed in redirect URI
			// https://stackoverflow.com/questions/60257717/why-oauth2-is-not-allowing-to-contain-fragment-identifier-in-redirect-uri
			.redirectUri("http://127.0.0.1:3000/user/signin-callback")
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
			.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build())
			.build();

		// Save registered client in db as if in-memory
		JdbcRegisteredClientRepository registeredClientRepository = new JdbcRegisteredClientRepository(jdbcTemplate);

		if (registeredClientRepository.findByClientId(clientId) == null) {
			log.info("Register new client: {}", clientId);
			registeredClientRepository.save(webClient);
			registeredClientRepository.save(passportClient());
		}
		else {
			log.info("Client already exist, skip ...");
		}

		return registeredClientRepository;
	}

	public RegisteredClient passportClient() {
		return RegisteredClient.withId(UUID.randomUUID().toString())
			.clientId("passport")
			.clientSecret("{noop}passport")

			.authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
			.clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)

			// hash fragment is not allowed in redirect URI
			// https://stackoverflow.com/questions/60257717/why-oauth2-is-not-allowing-to-contain-fragment-identifier-in-redirect-uri
			.redirectUri("http://billcat-passport:8080/login/oauth2/code/spring")
			.scope(OidcScopes.OPENID)
			.scope(OidcScopes.PROFILE)
			.clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
			.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofHours(2)).build())
			.build();
	}

	@Bean
	public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		// return new JdbcOAuth2AuthorizationService(jdbcTemplate,
		// registeredClientRepository);
		// 报错, 见
		// https://github.com/spring-projects/spring-authorization-server/issues/1009
		// 另见我的 obs 笔记: 01 密码模式
		JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate,
				registeredClientRepository);
		JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(
				registeredClientRepository);

		ObjectMapper objectMapper = new ObjectMapper();
		ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
		List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
		objectMapper.registerModules(securityModules);
		objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
		// You will need to write the Mixin for your class so Jackson can marshall it.
		// objectMapper.addMixIn(XyzUserDetails.class, XyzUserMixin.class);
		objectMapper.addMixIn(Collections.singletonMap(String.class, Object.class).getClass(), SingletonMapMixin.class);

		rowMapper.setObjectMapper(objectMapper);
		service.setAuthorizationRowMapper(rowMapper);
		return service;
	}

	@Bean
	public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate,
			RegisteredClientRepository registeredClientRepository) {
		return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
	}

	/**
	 * We also need to define a key source which the library needs to sign and verify
	 * tokens. We will add one key pair to the source and expose the bean.
	 */
	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		RSAKey rsaKey = Jwks.generateRsa();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	/**
	 * An instance of JwtDecoder used to validate access tokens. Used for /userinfo
	 * endpoint.
	 */
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	/**
	 * And finally we need to expose the AuthorizationServerSettings bean which defines
	 * the endpoints settings for the provider, defaults are used if not provided. You can
	 * see the defaults here, or later in the .well-known endpoint.
	 * @return
	 */
	@Bean
	public AuthorizationServerSettings authorizationServerSettings() {
		return AuthorizationServerSettings.builder().issuer(appAuthServer).build();
	}

}