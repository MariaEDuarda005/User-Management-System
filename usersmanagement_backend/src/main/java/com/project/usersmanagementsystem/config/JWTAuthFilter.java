package com.project.usersmanagementsystem.config;

import com.project.usersmanagementsystem.service.JWTUtils;
import com.project.usersmanagementsystem.service.OurUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// A classe JWTAuthFilter é um filtro de autenticação do Spring Security. Ele é responsável por interceptar as requisições HTTP para verificar se o usuário está autenticado por meio de um token JWT
@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    // Este é o método que vai verificar cada requisição e decidir se ela deve ser autenticada.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // Verifica se o cabeçalho Authorization está presente
        if (authHeader == null || authHeader.isBlank()){
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7); // Retira o prefixo "Bearer " do token
        userEmail = jwtUtils.extractUsername(jwtToken); // Extrai o nome de usuário do token

        // A verificação SecurityContextHolder.getContext().getAuthentication() == null é necessária para garantir que o sistema só tente autenticar o usuário se ele ainda não estiver autenticado.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail); // Carrega os detalhes do usuário

            if (jwtUtils.isTokenValid(jwtToken, userDetails)){ // Verifica se o token é válido
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // Cria um contexto de segurança vazio
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // Adiciona detalhes à autenticação
                securityContext.setAuthentication(token); // Adiciona o token de autenticação ao contexto de segurança
                SecurityContextHolder.setContext(securityContext); // Define o contexto de segurança para a requisição
            }
        }
        // passando a requisição adiante
        filterChain.doFilter(request, response);
    }
}
