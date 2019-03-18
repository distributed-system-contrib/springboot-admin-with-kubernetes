package io.github.hamelmoon.security;


import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
@Slf4j
@EnableWebSecurity
public class ActiveDirectorySecurityConfig extends WebSecurityConfigurerAdapter {

  private final String adminContextPath;


  public ActiveDirectorySecurityConfig(AdminServerProperties adminServerProperties) {
    this.adminContextPath = adminServerProperties.getContextPath();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
    successHandler.setTargetUrlParameter("redirectTo");

    http.authorizeRequests()
        .antMatchers(adminContextPath + "/assets/**").permitAll()
        .antMatchers(adminContextPath + "/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
        .logout().logoutUrl(adminContextPath + "/logout").and()
        .httpBasic().and()
        .csrf().disable();
  }

  @Bean
  public ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
    ActiveDirectoryLdapAuthenticationProvider activeDirectoryLdapAuthenticationProvider = new
        ActiveDirectoryLdapAuthenticationProvider("localhost.corp", "ldap://localhost.corp:389", "OU=User,ou=localhost,DC=localhost,DC=corp");
    activeDirectoryLdapAuthenticationProvider.setConvertSubErrorCodesToExceptions(true);
    activeDirectoryLdapAuthenticationProvider.setUseAuthenticationRequestCredentials(true);
    activeDirectoryLdapAuthenticationProvider.setSearchFilter("(&(objectClass=user)(objectCategory=person)(userPrincipalName={0}))");
    return activeDirectoryLdapAuthenticationProvider;
  }
}