package com.manoj.project.ppmtool.Security;

import com.manoj.project.ppmtool.domain.User;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Signature;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.manoj.project.ppmtool.Security.SecurityConstant.EXPIRATION_TIME;
import static com.manoj.project.ppmtool.Security.SecurityConstant.SECRET;

@Component
public class JwtTokenProvider {

    public String generateToken(Authentication auth){
        User user = (User)auth.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(now.getTime()+EXPIRATION_TIME);

        String userId = Long.toString(user.getId());

        Map<String,Object>claims = new HashMap<>();
        claims.put("id",(Long.toString(user.getId())));
        claims.put("username",user.getUsername());
        claims.put("fullName",user.getFullName());

        return Jwts.builder().setSubject("").addClaims(claims).setIssuedAt(now).setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512,SECRET).compact();
    }

    public boolean tokenValidator(String token){
        try{
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            System.out.println("Invalid jwt signature");
        }catch (MalformedJwtException e){
            System.out.println("Invalid jwt token");
        }catch (ExpiredJwtException e){
            System.out.println("Invalid jwt token");
        }catch (UnsupportedJwtException e){
            System.out.println("Unsupported jwt token");
        }catch (IllegalArgumentException e){
            System.out.println("JWT claims string is empty");
        }
        return false;
    }


    Long getUserIdFromJwt(String token){
        String id = (String)Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody().get("id");
        return Long.valueOf(id);
    }
}
