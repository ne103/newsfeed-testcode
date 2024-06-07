package org.example.newsfeed.jwt;

import org.example.newsfeed.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


//Util = 특정한 매개변수, 혹은 parameter에 대한 어떠한 작업을 수행하는 메서드들이 존재하는 Class
//다른 객체에 의존하지 않고 하나의 모듈로서 동작하는 Class
//JwtUtil = Jwt관련된  Class를 만들고 그 내부에 관련된 데이터들을 선언 or 가져다 사용! claim기반 웹토큰
@Component
public class JwtUtil {

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자 ( Bearer 뒤에 한 칸 띄우기 "Bearer "; )
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey, "beans.factory." 에 annotation 해야 함!
    private String secretKey;
    private Key key; //여기에 secret키 담아서 JWT를 암호화하거나, 복호화 해서 검증할 때 사용
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 256이 SignatureAlgorithm signatureAlgorithm에 담김

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @PostConstruct //딱 한 번만 받아 오면 되는 값을 사용할 때마다 요청을 새로 호출하는 실수를 방지하기 위해 사용이 됨
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey); //incoding해놓은 것 decoding하는 함수
        key = Keys.hmacShaKeyFor(bytes);
    }


    // JMT 토큰 생성
    public String createToken(String userId, UserRoleEnum role) {
        Date date = new Date();

        return BEARER_PREFIX +
            Jwts.builder()
                .setSubject(userId) // 사용자 식별자값(ID)
                .claim(AUTHORIZATION_KEY, role) // 사용자 권한 (claim에 key, value를 넣을 수 있다)
                .setExpiration(new Date(
                    date.getTime() + TOKEN_TIME)) // (date.getTIme() = 현재시간, TOKEN_TIME = 만료시간
                .setIssuedAt(date) // 발급일
                .signWith(key, signatureAlgorithm) // 암호화 알고리즘(시크릿키, HS256)
                .compact();
    }


    // 생성한 JMT를 Console에 저장
    // JWT Cookie 에 저장
    public void addJwtToCookie(String token, HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8")
                .replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
    }


    // Console에 들어있던 JMT 토큰을 Substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7); //"Bearer " = Bearer과 공백까지 총 7글자 빼면 순수한 토큰 값 남음
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }


    //JMT 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
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


    //JMT 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            .getBody(); //body부분에 있는 claims 가지고 올 수 있음.
    }

    // HttpServletRequest 에서 Cookie Value : JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(),
                            "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }


}

