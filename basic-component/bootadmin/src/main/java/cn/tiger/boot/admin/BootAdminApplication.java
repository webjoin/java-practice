package cn.tiger.boot.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAdminServer
public class BootAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootAdminApplication.class, args);

    }


//    @EnableWebSecurity
//    static class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
//            http.csrf().disable();
//            http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
//        }
//    }

//
//    @Configuration
//    public static class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//        private final String adminContextPath;
//
//        public SecurityConfig(AdminServerProperties adminServerProperties) {
//            this.adminContextPath = adminServerProperties.getContextPath();
//        }
//
////        @Override
////        protected void configure(HttpSecurity http) throws Exception {
////            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
////            successHandler.setTargetUrlParameter("redirectTo");
////            successHandler.setDefaultTargetUrl(adminContextPath + "/");
////
////            http.authorizeRequests().antMatchers(adminContextPath + "/assets/**").permitAll()
////                    .antMatchers(adminContextPath + "/login").permitAll().anyRequest().authenticated()
////                    .and().formLogin().loginPage(adminContextPath + "/login")
////                    .successHandler(successHandler).and().logout()
////                    .logoutUrl(adminContextPath + "/logout").and().httpBasic().and().csrf()
////                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
////                    .ignoringAntMatchers(adminContextPath + "/instances",
////                            adminContextPath + "/actuator/**");
////        }
//    }
}