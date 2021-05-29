package edu.ufp.inf.sd.project.util.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

public class JWT {
    private String SECRET_KEY= "advovmoigungmjgnvmnhggposbj";
    /*public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("advovmoigungmjgnvmnhggposbj"))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }*/

    public static void createJWT(String id, String issuer, String subject, long ttlMillis) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        System.out.println(key);

        String jws = Jwts.builder().setSubject(subject).signWith(key).compact();
        System.out.println(jws);
    }
}
