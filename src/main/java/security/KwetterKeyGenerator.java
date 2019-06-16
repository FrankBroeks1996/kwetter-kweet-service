package security;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class KwetterKeyGenerator implements IKeyGenerator {
    @Override
    public Key generateKey() {
        String keyString = "kwetterkey";
        Key key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
        return key;
    }
}
