import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.Arrays;

public class Engine {

    public static GameInfo game;

    public static void main(String[] args) {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-89b1d65a-4f40-11e9-bc3e-aabf89950afa");
        pnConfiguration.setPublishKey("pub-c-79140f4c-8b9e-49ed-992f-cf6322c68d04");


        String roomRequestChannelName = "Rooms::Request";
        String roomUpdateChannelName = "Rooms::Update";
        //String moveChannelName = "Move";

        // create message payload using Gson
        /*
        MoveData data = new MoveData();
        data.column = 1;
        data.row = 30;
        data.sender = "Rad dude";
        data.eVal = MoveData.TestEnum.Value2;
        */


        //System.out.println("Message to send: " + data.toString());
        //System.out.println("Message to send: " + data.toString());

        GameInfo game = new GameInfo();

        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new RoomCreatorCallback(game, roomRequestChannelName, roomUpdateChannelName));
        pubnub.subscribe().channels(Arrays.asList(roomRequestChannelName)).execute();
    }

    public static void SendGameInfo(PubNub pubnub, GameInfo game, Integer previousRow, Integer previousCol) {
        JsonObject data = new JsonObject();
        data.addProperty("PreviousRow", previousRow);
        data.addProperty("PreviousColumn", previousCol);
        // IsGameOver
        // Winner
        // WinReason
        //data.addProperty("PreviousPlayer", game.get)
    }
}
