package org.sorincos.newtumble;

import java.io.IOException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sorincos.newtumble.api.Blog;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth.common.signature.SharedConsumerSecretImpl;
import org.springframework.security.oauth.consumer.BaseProtectedResourceDetails;
import org.springframework.security.oauth.consumer.OAuthConsumerToken;
import org.springframework.security.oauth.consumer.OAuthSecurityContextHolder;
import org.springframework.security.oauth.consumer.OAuthSecurityContextImpl;
import org.springframework.security.oauth.consumer.client.OAuthRestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ConfigurationProperties("tumblr")
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private String root;
    private String username;
    private String blogname;
    private String key;
    private String secret;
    private String oauthtoken;
    private String oauthpass;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBlogname() {
        return blogname;
    }

    public void setBlogname(String blogname) {
        this.blogname = blogname;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getOauthtoken() {
        return oauthtoken;
    }

    public void setOauthtoken(String oauthtoken) {
        this.oauthtoken = oauthtoken;
    }

    public String getOauthpass() {
        return oauthpass;
    }

    public void setOauthpass(String oauthpass) {
        this.oauthpass = oauthpass;
    }

    public String connect() throws IOException {

        BaseProtectedResourceDetails resourceDetails = new BaseProtectedResourceDetails();
        resourceDetails.setId("newtumble");
        resourceDetails.setConsumerKey(key);
        resourceDetails.setSharedSecret(new SharedConsumerSecretImpl(secret));
        OAuthSecurityContextImpl context = new OAuthSecurityContextImpl();
        OAuthConsumerToken token = new OAuthConsumerToken();
        token.setValue(oauthtoken);
        token.setSecret(oauthpass);
        context.setAccessTokens(Collections.singletonMap("newtumble", token));
        OAuthSecurityContextHolder.setContext(context);
        OAuthRestTemplate restTemplate = new OAuthRestTemplate(resourceDetails);

        // ResponseEntity<String> json = restTemplate
        // .getForEntity(root + "/blog/" + blogname + ".tumblr.com/info?api_key=" + key, String.class);
        ResponseEntity<String> json = restTemplate.getForEntity(root + "/blog/" + blogname + ".tumblr.com/followers",
                String.class);
        ObjectMapper mapper = new ObjectMapper();
        Object mapped = mapper.readValue(json.getBody(), Object.class);
        log.info("Json: \n" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapped));

        Blog blog = restTemplate.getForObject(root + "/blog/" + blogname + ".tumblr.com/info?api_key=" + key,
                Blog.class);
        return blog.toString();
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("Let's connect: " + connect());
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}