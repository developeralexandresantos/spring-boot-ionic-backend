package br.com.developeralexandresantos.cursomc.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret.getBytes())
                .compact();
    }

//    Caso seja feita a atualização das dependencias, utilizar esse método
//    public String generateToken(String username){
//        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
//        return Jwts.builder()
//                .setSubject(username)
//                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                .signWith(secretKey)
//                .compact();
//    }

}
