package com.avanse.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.avanse.jpa.repository.MstSourceMappingRepository;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    //@Value("${app.jwtSecret}")
    //private String jwtSecret;

    //@Value("${app.jwtExpirationInMs}")
    //private int jwtExpirationInMs;

    @Autowired
	MstSourceMappingRepository mstSourceMappingRepository;
    
	/*
	 * public String generateToken(Authentication authentication) {
	 * 
	 * UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
	 * 
	 * Date now = new Date(); Date expiryDate = new Date(now.getTime() +
	 * jwtExpirationInMs);
	 * 
	 * return Jwts.builder() .setSubject(userPrincipal.getUsername())
	 * .setIssuedAt(new Date()) .setExpiration(expiryDate)
	 * .signWith(SignatureAlgorithm.HS512, jwtSecret) .compact(); }
	 */

    public String getUsernameFromJWT(String authToken) {
        //Claims claims = Jwts.parser()
        //       .setSigningKey(jwtSecret)
        //        .parseClaimsJws(token)
        //        .getBody();

    	byte[] credDecoded = Base64.getDecoder().decode(authToken);
    	String credentials = new String(credDecoded, StandardCharsets.UTF_8);
        //// credentials = username:password
        final String[] values = credentials.split(":", 2);
        ////Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
    	
        return values[0];
    }

    public boolean validateToken(String authToken) {
        try {
        	
        	byte[] credDecoded = 
        			Base64.getDecoder().decode(authToken);
        	String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            //// credentials = username:password
            final String[] values = credentials.split(":", 2);
            
            if(values.length==2) {
            	
            	//List<MstSourceMapping> list=mstSourceMappingRepository.findAll();
            	
            return mstSourceMappingRepository.existsBySourceIdAndSecretKey(UUID.fromString(values[0]),values[1]);
            
            ////Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            //return true;
            }
        } catch (IllegalArgumentException ex) {
            logger.error("Auth token in invalid");
        }
        return false;
    }
}
