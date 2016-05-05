
package vanichat.config;

import java.nio.charset.Charset;

public class Configuration {
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-16");
    public static final String SERVER_IP_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 8080;
    public static final byte LOGIN = 0;
    public static final byte GROUP = 1;
    public static final byte MESSAGE = 2;
    public static final byte USERLINK = 3;
    public static final byte CHATLINK = 4;
    public static final String USERNAME_SEPARATOR = ":";
}
