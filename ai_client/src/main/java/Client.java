


// NOTE: To change the uuid, go to the launch configurations and add this to the "Arguments" field
//   -Pargs=Something
// This will tack on "Something" to the UUID. Make several launch configs if you want.

import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) {

        String uuidModifier = "";
        if(args.length > 0) {
            uuidModifier = args[0];
        }

        String hostname = "Unknown";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();

        } catch (UnknownHostException ex) {
            System.out.println("Hostname can not be resolved");
        }

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey(Messages.Keys.subKey);
        pnConfiguration.setPublishKey(Messages.Keys.pubKey);
        pnConfiguration.setUuid(hostname + "AiClient" + uuidModifier);

        PubNub pn = new PubNub(pnConfiguration);

        String testChannel = "AI_Test";


    }

    public static boolean isAlreadyRunning() {
        return false;
    }
}

