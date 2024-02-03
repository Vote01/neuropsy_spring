package pin0.al;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
public class WebSecurityConfigAuthentication{


    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http,
                                                      HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.csrf(csrfConfigurer ->
                csrfConfigurer.ignoringRequestMatchers(mvcMatcherBuilder.pattern("/client"),
                        PathRequest.toH2Console()));

        http.headers(headersConfigurer ->
                headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.authorizeHttpRequests(auth ->
                auth
                        .requestMatchers(mvcMatcherBuilder.pattern("/home")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/register")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/registerpsy")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/registpsythank")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/psychologist")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/client"),
                                mvcMatcherBuilder.pattern("/method"),
                                mvcMatcherBuilder.pattern("/specialization"),
                                mvcMatcherBuilder.pattern("/session")).hasAnyRole("ADMIN")
                        // .requestMatchers(mvcMatcherBuilder.pattern("/psychologist")).hasAnyRole("PSYCHOLOGIST", "CLIENT", "ADMIN")
                        .requestMatchers(PathRequest.toH2Console()).hasAnyRole("ADMIN")
                        // .antMatchers("/").permitAll()
                        .anyRequest().authenticated()
        );
        http.logout(logout -> logout.logoutSuccessUrl("/home"));
        http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        UserDetails user1 =
                User
                        .withUsername("user")
                        .password(passwordEncoder().encode("password"))
                        .roles("CLIENT")
                        .build();
        UserDetails user2 =
                User
                        .withUsername("admin")
                        .password(passwordEncoder().encode("password"))
                        .roles("ADMIN")
                        .build();
        userDetailsList.add(user1); userDetailsList.add(user2);
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }
}
