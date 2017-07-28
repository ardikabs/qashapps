package gravicodev.qash.Helper;

import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by mfatihas on 7/28/2017.
 */

public class ApiHelper {
    public static String access_token = null;

    public String generateSignature(String method, String url, String timestamp, String body){
        String apiSecret = "8fb76d7b-e4c0-44d2-bae3-5bad7167a629";

        method = method.toUpperCase();

        body = body.replaceAll("\r","");
        body = body.replaceAll("\n","");
        body = body.replaceAll("\t","");
        body = body.replaceAll(" ","");
        try {
            body = SHA256(body);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        body = body.toLowerCase();

        String toSign = method+":"+url+":"+this.access_token+":"+body+":"+timestamp;
        try {
            toSign = hmacSHA256(apiSecret,toSign);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return toSign;
    }

    public static String hmacSHA256(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        return String.format("%064x", new java.math.BigInteger(1, sha256_HMAC.doFinal(data.getBytes("UTF-8"))));
    }

    public static String SHA256 (String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return String.format("%064x", new java.math.BigInteger(1, digest));
    }
}
