


// NOTE: To change the uuid, go to the launch configurations and add this to the "Arguments" field
//   -Pargs=Something
// This will tack on "Something" to the UUID. Make several launch configs if you want.

import AI.NPCBehaviour;
import AI.NPCEasy;
import Messages.Channels;
import Messages.PlayerInfo;
import Network.AICallback;
import Network.SingletonCallback;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class AI_Client {

    private static PubNub pn;

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

        pn = new PubNub(pnConfiguration);

        System.out.println("Checking for another AI client");

        SingletonCallback singleton = new SingletonCallback(Channels.AI.singletonChannel);
        singleton.setIsMasterCallback(AI_Client::onSucceededToBecomeMaster);
        singleton.setNotMasterCallback(AI_Client::onFailedToBecomeMaster);
        singleton.setInterruptedCallback(AI_Client::onInterrupt);

        pn.addListener(singleton);
        pn.subscribe().channels(Arrays.asList(Channels.AI.singletonChannel)).execute();
    }

    private static void onSucceededToBecomeMaster() {
        System.out.println("Response timeout; elevating self to master.");
        System.out.print("Spinning up AI callbacks...");

        String outgoingChannel = Channels.roomMoveChannel;
        String incomingChannel = Channels.moveRequestChannelSet;

        PlayerInfo easyAIPlayer = new PlayerInfo(pn.getConfiguration().getUuid(), "EasyAI", "");
        NPCBehaviour easyAIBehaviour = new NPCEasy();

        pn.addListener(new AICallback(easyAIPlayer, easyAIBehaviour, incomingChannel, outgoingChannel));
        pn.subscribe().channels(Arrays.asList(incomingChannel + "*")).execute();

        System.out.println("done. Hit CTRL-C to close.");
    }

    private static void onFailedToBecomeMaster() {
        System.out.println("There seems to already be another AI Client running.");
        System.out.println("Aborting...");
        System.exit(0);
    }

    private static void onInterrupt() {
        System.out.println("Interrupted.");
        System.exit(1);
    }
}

