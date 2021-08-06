package com.prime.rush_hour.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    public JwtTokenVerifier(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader  = request.getHeader(jwtConfig.getAuthorizationHeader());

        //TODO: Zasto je ovako uradio?
        if(authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){
            filterChain.doFilter(request, response);
            return;
        }

        String token= authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

        try {

            //String key = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure";
//            SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//            String secretString = Encoders.BASE64.encode(key.getEncoded());

            //TODO: Izmenio sam jer je onda metoda zastarela, vidi dal ce to da ga jebe
            //String secretKey = "ovogovnomoradabudebasbasbasdugackodabileporadiloidanebipraviloproblemnadamsedaimjeovodovoljnodugackojebemimmajkuustanapisacujossamodanebislucajnobiloprekratkoovogovnomoradabudebasbasbasdugackodabileporadiloidanebipraviloproblemnadamsedaimjeovodovoljnodugackojebemimmajkuustanapisacujossamodanebislucajnobiloprekratko";
            Jws<Claims> claimsJws = Jwts
                                    .parser()
                                    .setSigningKey(secretKey)
                                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();
            String userName = body.getSubject();

            var authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());


            //TODO:Preskocicu authorities deo jer ja jos uvek nemam to

            //TODO: Vidi zasto je null za credentials?
            Authentication authentication =  new UsernamePasswordAuthenticationToken(userName, null, simpleGrantedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            //TODO: Vidi kako ces ovo da handle-as
        } catch (JwtException e) {
          //  throw new IllegalStateException(String.format("Token %s cannot be trusted", token));
//            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }
}
