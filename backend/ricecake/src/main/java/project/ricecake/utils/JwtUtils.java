package project.ricecake.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.SIG.*;

@Component
public class JwtUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${jwt.expiredTimeMs}")
    private Long expiredTimeMs;

    public String generateToken(Long memberIdx, String memberId) {
        return Jwts.builder()
                .claims(createClaim(memberIdx, memberId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSignKey(), HS256)
                .compact();
    }

    public Boolean validateToken(String token, String memberId) {
        try {
            Claims payload = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String getMemberId = payload.get("memberId", String.class);

            if (getMemberId.equals(memberId)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    public String extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", String.class);
    }

    private Map<String, Object> createClaim(Long memberIdx, String memberId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idx", memberIdx);
        claims.put("memberId", memberId);
        return claims;
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
