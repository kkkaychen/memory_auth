package com.example.memory_auth_microservice.util;

import com.example.memory_auth_microservice.dao.EmpDao;
import com.example.memory_auth_microservice.dao.MemberDao;
import com.example.memory_auth_microservice.model.EmpEntity;
import com.example.memory_auth_microservice.model.MemEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
@Slf4j
public class JwtTokenUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final MemberDao memberDao;
    private final EmpDao empDao;

    // 構造函數中加載公鑰和私鑰
//    public JwtTokenUtil() throws Exception {
//        String privateKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/key/private_key.pem")));
//        this.privateKey = loadPrivateKey(privateKeyPem);
//
//        String publicKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/key/public_key.pem")));
//        this.publicKey = loadPublicKey(publicKeyPem);
//    }

    public JwtTokenUtil(MemberDao memberDao, EmpDao empDao) throws Exception {
        this.memberDao = memberDao;
        this.empDao = empDao;
        // Check if the environment variables are set
        String privateKeyPath = System.getenv("JWT_PRIVATE_KEY_PATH");
        String publicKeyPath = System.getenv("JWT_PUBLIC_KEY_PATH");



        // Use the provided paths or fallback to the local file paths for development
        if (privateKeyPath == null) {
            privateKeyPath = "src/main/resources/key/private_key.pem";
        }
        if (publicKeyPath == null) {
            publicKeyPath = "src/main/resources/key/public_key.pem";
        }

//        log.info("------ privateKeyPath: " + privateKeyPath);
//        log.info("------ publicKeyPath: " + publicKeyPath);

        // Load the private key
        String privateKeyPem = new String(Files.readAllBytes(Paths.get(privateKeyPath)));
        this.privateKey = loadPrivateKey(privateKeyPem);
//        log.info("------ privateKeyPem: " + privateKeyPem);

        // Load the public key
        String publicKeyPem = new String(Files.readAllBytes(Paths.get(publicKeyPath)));
        this.publicKey = loadPublicKey(publicKeyPem);
//        log.info("------ publicKeyPem: " + publicKeyPem);
    }


    // 加載私鑰使用來簽名
    private PrivateKey loadPrivateKey(String key) throws Exception {
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // 加載公鑰用來驗證簽名
    private PublicKey loadPublicKey(String key) throws Exception {
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 提取 JWT 中的使用者名稱
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 提取 JWT 中的過期時間
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 生成 access Token
    public String generateMemberAccessToken(String username, String role) {
        MemEntity byMemEmail = memberDao.findByMemEmail(username);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("memNo",byMemEmail.getMemNo())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

    }

    public String generateEmpAccessToken(String username, String role) {
        EmpEntity emp = empDao.findByEmpAcc(username);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("empNo",emp.getEmpNo())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();

    }

    // 生成 refresh Token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }


    private String createToken(String subject, long expirationTime) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    // 驗證 Token 是否有效
    public Boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

}
