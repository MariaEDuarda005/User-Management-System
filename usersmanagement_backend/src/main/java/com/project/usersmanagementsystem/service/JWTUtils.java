package com.project.usersmanagementsystem.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JWTUtils {

    private final SecretKey Key;
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public JWTUtils(){
        // uma chave secreta em formato base64
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        // Primeiro, a string secreteString é convertida em um array de bytes usando a codificação UTF-8. Em seguida, ela é decodificada de Base64 para obter os bytes da chave secreta.
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        // A chave decodificada (em keyBytes) é então utilizada para criar um objeto SecretKey com o algoritmo HmacSHA256. O SecretKeySpec é uma implementação de SecretKey que aceita os bytes da chave e o nome do algoritmo (HMAC com SHA-256).
        this.Key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }

    // Este método cria um token JWT para um usuário.
    public String generateToken(UserDetails userDetails){
        return Jwts.builder()
                .subject(userDetails.getUsername()) // O nome de usuário do usuário é adicionado como "subject" (assunto) do token.
                .issuedAt(new Date(System.currentTimeMillis())) // emissão do token (data atual
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiração do token, que é 24 horas depois da sua criação
                .signWith(Key) // O token é assinado usando a chave secreta Key  (que foi definida no construtor, utilizando o algoritmo HMAC SHA-256)
                .compact(); // Cria o token JWT final, pronto para ser enviado
    }

    // Similar ao método anterior, mas aqui você pode adicionar informações extras (chamadas de "claims") no token.
    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails){
        return Jwts.builder()
                .claims(claims) // Um mapa (HashMap<String, Object>) que permite adicionar dados personalizados no token (por exemplo, roles ou permissões do usuário).
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Key)
                .compact();
    }

    // Este método extrai o nome de usuário
    public String extractUsername(String token){
        // Ele chama o método extractClaims(), que é responsável por extrair qualquer informação do token, e no caso específico, passa a função Claims::getSubject para pegar o "subject"
        return extractClaims(token, Claims::getSubject);
    }

    // método genérico é usado para extrair qualquer informação do token.
    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction){
        // usa a função que é passada para o método, por exemplo, Claims::getExpiration para obter a data de expiração do token ou Claims::getSubject para o nome do usuário.
        return claimsTFunction.apply(Jwts.parser().verifyWith(Key)
                .build().parseSignedClaims(token).getPayload());

        // Jwts.parser().verifyWith(Key): Verifica a assinatura do token usando a chave secreta para garantir que o token não foi alterado.
        // parseSignedClaims(token): Analisa e decodifica o token JWT.
    }

    // verifica se o token é valido, e verifica se o token não expirou
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // verifca se o token expirou. Ele extrai a data de expiração do token e a compara com a data atual. Se a data de expiração for anterior à data atual, significa que o token expirou.
    public boolean isTokenExpired(String token){
        return extractClaims(token, Claims::getExpiration)
                .before(new Date());
    }
}
