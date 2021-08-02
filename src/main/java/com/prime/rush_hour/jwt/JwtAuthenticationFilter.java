package com.prime.rush_hour.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    //TODO: Move this to some config class
//    public static final String secretKey = Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthenticationRequest authenticationRequest= new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequest.class);
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;

        } catch (IOException e) {//TODO: Vidi sta ces sa ovim, nudi mi i neki @SneakyThrows
            throw new RuntimeException();
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//        String secretString = Encoders.BASE64.encode(key.getEncoded());
        String secretKey = "ovogovnomoradabudebasbasbasdugackodabileporadiloidanebipraviloproblemnadamsedaimjeovodovoljnodugackojebemimmajkuustanapisacujossamodanebislucajnobiloprekratkoovogovnomoradabudebasbasbasdugackodabileporadiloidanebipraviloproblemnadamsedaimjeovodovoljnodugackojebemimmajkuustanapisacujossamodanebislucajnobiloprekratko";

        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(10)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        //TODO: Vidi za ovaj Bearer?
        response.addHeader("Authorization", "Bearer " + token);

    }
}
