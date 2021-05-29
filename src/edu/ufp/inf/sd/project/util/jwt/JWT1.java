package edu.ufp.inf.sd.project.util.jwt;

public class JWT1 {

    private String SECRET_KEY= "advovmoigungmjgnvmnhggposbj";
    /*
    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary("advovmoigungmjgnvmnhggposbj"))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }
*/
    public static void createJWT(String id, String issuer, String subject, long ttlMillis) {
        System.out.println("sadfsfasr");
    }
}