package by.prus.LabProject.service;

import by.prus.LabProject.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();
    private final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public String generateUserId(int length){
        return generatedString(length);
    }

    private String generatedString (int length){
        StringBuilder returnValue = new StringBuilder(length);

        for (int i=0; i<length; i++){
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

    public String generateEmailVerificationToken(String userId){
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+ SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
    }

    public String generatePasswordResetToken(String userId){
        String token = Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis()+SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
        return token;
    }

    // Важная формулировка. Обратить внимание на тестирование этого метода
    public static boolean hasTokenExpired(String token){
        boolean returnValue = false;

        try {
            Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
                    .getBody();
            Date tokenExpirationDate = claims.getExpiration();
            Date todayDate = new Date();
            returnValue = tokenExpirationDate.before(todayDate);
        } catch (ExpiredJwtException ex) { returnValue = true; }
        return returnValue;
    }

}
