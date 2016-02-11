package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value( "${tobias.clients.ldap.url}" )
  private String clientsLdapURL;
  @Value ( "${tobias.clients.ldap.base}" )
  private String clientsLdapDefaultBase;
  @Value ( "${tobias.clients.ldap.user}" )
  private String clientsLdapUserDn;
  @Value ( "${tobias.clients.ldap.pwd}" )
  private String clientsLdapPassword;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
            .authorizeRequests()
            .antMatchers("/css/**").permitAll()
            .anyRequest().fullyAuthenticated()
            .and()
            .formLogin();
  }

  //The following is the code used to access ldap

  @Autowired
  @Override
  protected void configure(AuthenticationManagerBuilder authManagerBuilder)
          throws Exception {
    authManagerBuilder
      .ldapAuthentication()

      .userDnPatterns("uid={0}," + clientsLdapDefaultBase)
      .groupSearchBase(clientsLdapDefaultBase)
      .contextSource()
      .url(clientsLdapURL)
      .managerDn(clientsLdapUserDn)
      .managerPassword(clientsLdapPassword);
  }

}
