import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;

public class ClientPresenceTracker extends PNCallback<PNHereNowResult> {
    @Override
    public void onResponse(PNHereNowResult result, PNStatus status) {
        if (status.isError()) {
            // handle error
            return;
        }

        for (PNHereNowChannelData channelData : result.getChannels().values()) {
            System.out.println("---");
            System.out.println("channel:" + channelData.getChannelName());
            System.out.println("occupancy: " + channelData.getOccupancy());
            System.out.println("occupants:");
            for (PNHereNowOccupantData occupant : channelData.getOccupants()) {
                System.out.println("uuid: " + occupant.getUuid() + " state: " + occupant.getState());
            }
        }
    }
}
