package com.project.usersmanagementsystem.config;

import com.project.usersmanagementsystem.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()) // Habilita CORS (Cross-Origin Resource Sharing), que permite que o frontend em um domínio diferente possa acessar recursos da API.
                .authorizeHttpRequests(request -> request.requestMatchers("/auth/**", "/public/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN") // requisição apenas para admin
                        .requestMatchers("/users/**").hasAnyAuthority("USER") // requisição apenas para user
                        .requestMatchers("/adminuser/**").hasAnyAuthority("ADMIN", "USER") // requisição apenas para admin e user
                        .anyRequest().authenticated()) // Qualquer outra requisição precisa ser autenticada.
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // a aplicação não usará sessões de servidor, pois ela vai autenticar o usuário com base no token JWT. Cada requisição será independente.
                .authenticationProvider(authenticationProvider()).addFilterBefore( // Configura o provedor de autenticação para que o Spring Security saiba como autenticar o usuário. O provedor é configurado abaixo.
                        // Adiciona o filtro JWT antes do filtro de autenticação padrão UsernamePasswordAuthenticationFilter isso garante que a autenticação JWT seja feita antes de qualquer outra verificação de login
                        jwtAuthFilter, UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }

    @Bean
    // Este método cria um provedor de autenticação (DaoAuthenticationProvider), que é responsável por autenticar o usuário com o nome de usuário e senha.
    public AuthenticationProvider authenticationProvider(){
        // O DaoAuthenticationProvider utiliza o banco de dados para fazer a autenticação
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(ourUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    // O PasswordEncoder é responsável por codificar e decodificar as senhas dos usuários.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    // Este método cria um AuthenticationManager, que é usado para autenticar usuários com base em nome de usuário e senha.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws  Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
