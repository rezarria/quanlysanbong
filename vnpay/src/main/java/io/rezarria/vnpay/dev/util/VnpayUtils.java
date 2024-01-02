package io.rezarria.vnpay.dev.util;


import io.rezarria.vnpay.dev.annotation.Nested;
import io.rezarria.vnpay.dev.annotation.StartName;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.util.Pair;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class VnpayUtils {
    public static List<Pair<String, String>> convert(Object data, @Nullable String prefix) {
        List<Pair<String, String>> list = new ArrayList<>();
        var startName = data.getClass().getAnnotation(StartName.class).name();
        if (prefix != null)
            startName = prefix + "_" + startName;
        var f = Arrays.stream(data.getClass().getDeclaredMethods()).filter(i -> i.getName().startsWith("get"));
        String finalStartName = startName;
        f.toList().forEach(i -> {
            var nested = i.getReturnType().getAnnotation(Nested.class);
            if (nested != null) {
                try {
                    var nestedData = i.invoke(data);
                    if (nestedData != null) {
                        list.addAll(convert(nestedData, finalStartName));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return;
            }
            var k = finalStartName + "_" + i.getName().substring("get".length());
            try {
                var o = i.invoke(data);
                if (o == null) return;
                String v;
                if (o instanceof Calendar d) {
                    v = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.US).withZone(ZoneId.systemDefault()).format(d.toInstant());
                } else
                    v = o.toString();
                list.add(Pair.of(k, v));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        Collections.sort(list, Comparator.comparing(Pair::getFirst));
        return list;
    }

    public static String hmacSHA512(final String key, final String data) {
        try {

            if (key == null || data == null) {
                throw new NullPointerException();
            }
            final Mac hmac512 = Mac.getInstance("HmacSHA512");
            byte[] hmacKeyBytes = key.getBytes();
            final SecretKeySpec secretKey = new SecretKeySpec(hmacKeyBytes, "HmacSHA512");
            hmac512.init(secretKey);
            byte[] dataBytes = data.getBytes(StandardCharsets.UTF_8);
            byte[] result = hmac512.doFinal(dataBytes);
            StringBuilder sb = new StringBuilder(2 * result.length);
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();

        } catch (Exception ex) {
            return "";
        }
    }

    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }

}
