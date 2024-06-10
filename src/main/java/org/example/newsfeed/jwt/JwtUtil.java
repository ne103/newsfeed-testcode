package org.example.newsfeed.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_HEADER_REFRESH = "Authorization-Refresh";
    // Token 식별자 ( Bearer 뒤에 한 칸 띄우기 "Bearer "; )
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey, "beans.factory." 에 annotation 해야 함!
    private String secretKey;
    private Key key; //여기에 secret키 담아서 JWT를 암호화하거나, 복호화 해서 검증할 때 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 256이 SignatureAlgorithm signatureAlgorithm에 담김

    private final UserRepository userRepository; // UserRepository 주입

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    public JwtUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct //딱 한 번만 받아 오면 되는 값을 사용할 때마다 요청을 새로 호출하는 실수를 방지하기 위해 사용이 됨
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //incoding해놓은 것 decoding하는 함수
        key = Keys.hmacShaKeyFor(bytes);
    }

    // JMT 토큰 생성
    public String createToken(String userId, Token tokenType) {
        Date date = new Date();

        // 토큰 만료 시간
        Long tokenTime;
        if (Token.TOKEN_TYPE_ACCESS.getValue().equals(tokenType.getValue())) {
            tokenTime = 1000L * 60 * 30;            // 30분
        } else if (Token.TOKEN_TYPE_REFRESH.getValue().equals(tokenType.getValue())) {
            tokenTime = 1000L * 60 * 60 * 24 * 14;  // 2주
        } else {
            throw new IllegalArgumentException("Illegal Token Type Error");
        }

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(userId) // 사용자 식별자값(ID)
//                .claim(AUTHORIZATION_KEY, role) // 사용자 권한 (claim에 key, value를 넣을 수 있다)
                .setExpiration(new Date(date.getTime() + tokenTime)) // (date.getTIme() = 현재시간, TOKEN_TIME = 만료시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘(시크릿키, HS256)
                .compact();
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request, Token tokenType) {
        String bearerToken = request.getHeader(tokenType.getValue());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JMT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // JMT 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); //body부분에 있는 claims 가지고 올 수 있음.
    }

    @Transactional
    // 토큰 발급
    public void generateTokenAndResponse(HttpServletResponse httpServletResponse, String UserId) throws IOException {
        String refreshToken = createToken(UserId, Token.TOKEN_TYPE_REFRESH);
        httpServletResponse.addHeader(
            Token.AUTHORIZATION_HEADER.getValue(),
            createToken(UserId, Token.TOKEN_TYPE_ACCESS));
        httpServletResponse.addHeader(
            Token.AUTHORIZATION_HEADER_REFRESH.getValue(),
            refreshToken);
        User user = userRepository.findByUserId(UserId).orElseThrow();
        user.setRefreshToken(refreshToken.substring(7));
        userRepository.save(user); // 저장 누락 방지
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}
