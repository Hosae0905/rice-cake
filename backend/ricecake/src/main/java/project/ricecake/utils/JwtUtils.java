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

/**
 * JwtUtils
 * JWT 토큰과 관련된 기능들을 모아둔 클래스
 * 기능 목록: 토큰 생성, 토큰 검증, 정보 추출, 정보 생성, 키 생성
 *
 */
@Component
public class JwtUtils {

    @Value("${jwt.secretKey}")
    private String secretKey;       // 서버에서 관리하는 비밀 키
    @Value("${jwt.expiredTimeMs}")
    private Long expiredTimeMs;     // JWT 토큰 만료 시간

    /**
     * JWT 토큰을 생성하는 기능
     * @param memberIdx (회원 인덱스)
     * @param memberId (회원 아이디)
     * @return JWT 토큰
     */
    public String generateToken(Long memberIdx, String memberId) {
        return Jwts.builder()
                .claims(createClaim(memberIdx, memberId))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSignKey(), HS256)
                .compact();
    }

    /**
     * JWT 토큰 검증 기능
     * @param token (JWT 토큰)
     * @param memberId (회원 아이디)
     * @return 검증에 성공할 경우 true | 실패할 경우 false
     */
    public Boolean validateToken(String token, String memberId) {
        try {
            // 토큰에서 정보를 추출
            Claims payload = Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String getMemberId = payload.get("memberId", String.class);

            /**
             * 만약 토큰에 있는 회원 아이디와 파라미터로 받은 회원 아이디가 같을 경우 true 반환
             * 그렇지 않을 경우 false 반환
             */
            if (getMemberId.equals(memberId)) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰에서 정보를 추출하는 기능
     * @param token (JWT 토큰)
     * @return 토큰에 담긴 회원 아이디
     */
    public String extractClaim(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("memberId", String.class);
    }

    /**
     * JWT 토큰에 담길 정보를 생성하는 기능
     * @param memberIdx (회원의 인덱스)
     * @param memberId (회원의 아이디)
     * @return 회원의 정보가 담긴 Map 객체
     */
    private Map<String, Object> createClaim(Long memberIdx, String memberId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("idx", memberIdx);
        claims.put("memberId", memberId);
        return claims;
    }

    /**
     * 서버에서 관리하는 비밀 키로 서명 키를 암호화된 키로 생성하는 기능
     * @return 암호화된 비밀 키
     */
    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
