import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class NetworkManager {

    private static final String subKey = "sub-c-89b1d65a-4f40-11e9-bc3e-aabf89950afa";
    private static final String pubKey = "pub-c-79140f4c-8b9e-49ed-992f-cf6322c68d04";
    private static String uuidModifier = "";

    private static NetworkManager _instance;
    private static NetworkManager getInstance() {
        if(_instance == null) {
            _instance = new NetworkManager();
        }

        return _instance;
    }

    public static PubNub getPubNub() {
        return getInstance().pn;
    }

    public static void setUuidModifier(String uuidModifier) {
        NetworkManager.uuidModifier = uuidModifier;
    }

    private PubNub pn;

    private NetworkManager() {
        this(getDefaultUUID());
    }
    private NetworkManager(String uuid) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(subKey);
        pnConfiguration.setPublishKey(pubKey);
        pnConfiguration.setUuid(uuid + "Client" + uuidModifier);

        pn = new PubNub(pnConfiguration);
    }

    private static String getDefaultUUID() {
        String hostname = "Unknown";

        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();

        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        return hostname;
    }



}
