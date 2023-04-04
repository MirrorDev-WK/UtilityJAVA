package th.co.ncr.connector.utils;

import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import th.co.ncr.connector.vo.UserTokenVo;

@Component
@Slf4j
public class JwtUtil implements InitializingBean {

	public static final String JWT_AUTHORIZATION_HEADER = "Authorization";
	public static final String JWT_SECRET_KEY_HEADER = "SecretKey";
	public static final String JWT_HEADER_PREFIX = "Bearer ";
	public static final String JWT_QUERY_STRING_PARAM = "T";
	
	@Getter private String secret;
	@Getter private Integer expiredSec;
	@Getter private Boolean encodedToken = Boolean.TRUE;
	
	@Autowired private Environment env;

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info(">> new "+ JwtUtil.class.getSimpleName());
		secret = env.getProperty("helpdesk.web-jwt.secret", generateSecretKey());
		expiredSec = Integer.parseInt(env.getProperty("helpdesk.web-jwt.expired-sec", "900"));
    	log.info(">>     helpdesk.web-jwt.expired-sec: "+expiredSec);
    	encodedToken = Boolean.parseBoolean(env.getProperty("helpdesk.web-jwt.encoded-token", "true"));
		log.info(">>     helpdesk.web-jwt.encoded-token: "+encodedToken);

	}

	
	public String generateSecretKey() {
		SecretKey newKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
		return Base64.getEncoder().encodeToString(newKey.getEncoded());
	}
	
	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secret.getBytes());
	}
	
	public String generateToken(UserTokenVo user) {
		Claims claims = Jwts.claims().setSubject(user.getLogin());
//		claims.put("userID", user.getUserID() + "");
		claims.put("tokenID", user.getTokenID());
		
		if(user.getExpiredTime() != null) {
			claims.put("expired", user.getExpiredTime().getTime() + "");
		} else {
			claims.put("expired", DateUtil.currDatePlusSec(expiredSec).getTime() + "");
		}
		
		SecretKey key = getSecretKey();
		return Jwts.builder().setClaims(claims).signWith(key, SignatureAlgorithm.HS512).compact();
	}
	
	public UserTokenVo parseToken(String token) throws JwtException, ClassCastException{
        try {
        	
        	SecretKey key = getSecretKey();
            
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Claims body = jws.getBody();

            UserTokenVo u = new UserTokenVo();
            u.setLogin(body.getSubject());
//            u.setUserID(Integer.parseInt((String) body.get("userID")));
            u.setTokenID((String) body.get("tokenID"));
            Long exprTime = Long.parseLong((String) body.get("expired"));
            u.setExpiredTime(new Date(exprTime));
            return u;

        } catch (JwtException | ClassCastException e) {
            throw e;
        }
    }
}
