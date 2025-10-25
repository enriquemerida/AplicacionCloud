package AplicacionSegura.appsegura;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Base64;

@Converter
public class AesStringEncryptor implements AttributeConverter<String, String> {

    private static final byte[] KEY = "0123456789abcdef0123456789abcdef".getBytes(); // DEMO 256-bit
    private static final String ALGO = "AES";
    private static final String TRANS = "AES/GCM/NoPadding";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);
            Cipher cipher = Cipher.getInstance(TRANS);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALGO), new GCMParameterSpec(128, iv));
            byte[] ct = cipher.doFinal(attribute.getBytes());
            ByteBuffer bb = ByteBuffer.allocate(12 + ct.length).put(iv).put(ct);
            return Base64.getEncoder().encodeToString(bb.array());
        } catch (Exception e) {
            throw new IllegalStateException("Error cifrando", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            byte[] all = Base64.getDecoder().decode(dbData);
            byte[] iv = new byte[12];
            byte[] ct = new byte[all.length - 12];
            System.arraycopy(all, 0, iv, 0, 12);
            System.arraycopy(all, 12, ct, 0, ct.length);
            Cipher cipher = Cipher.getInstance(TRANS);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALGO), new GCMParameterSpec(128, iv));
            return new String(cipher.doFinal(ct));
        } catch (Exception e) {
            throw new IllegalStateException("Error descifrando", e);
        }
    }
}
