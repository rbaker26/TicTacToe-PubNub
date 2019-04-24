import Messages.*;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

public class AuthorizationListener extends SubscribeCallback {
    private String authChannel = Channels.authCheckChannel;
    private String createChannel = Channels.authCreateChannel;

    @Override
    public void status(PubNub pb, PNStatus status) {
    }

    @Override
    public void message(PubNub pb, PNMessageResult message) {
        if(message.getChannel().equals(authChannel) || message.getChannel().equals(createChannel)) {
            LoginInfo loginRequest = Converter.fromJson(message.getMessage(), LoginInfo.class);
            System.out.println("Message received: " + message.getMessage());
            try {
                if(message.getChannel().equals(authChannel)) {
                    if (Db_Manager.GetInstance().ValidateUser(loginRequest.getUsername(), loginRequest.getPassword())) {
                        System.out.println("Login Valid");
                        pb.publish()
                                .message(new LoginResponse(loginRequest, true, null))
                                .channel(Channels.privateChannelSet + message.getPublisher())
                                .async(new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                        // handle publish result, status always present, result if successful
                                        // status.isError() to see if error happened
                                        if (!status.isError()) {
                                        }
                                    }
                                });
                    }
                    else {
                        System.out.println("Invalid Login");
                        pb.publish()
                                .message(new LoginResponse(loginRequest, false, "Invalid username/password"))
                                .channel(Channels.privateChannelSet + message.getPublisher())
                                .async(new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                        // handle publish result, status always present, result if successful
                                        // status.isError() to see if error happened
                                        if (!status.isError()) {
                                        }
                                    }
                                });
                    }
                }
                if(message.getChannel().equals(createChannel)) {
                    if(Db_Manager.GetInstance().UserExistsByName(loginRequest.getUsername())) {
                        System.out.println("Username already exists");
                        pb.publish()
                                .message(new LoginResponse(loginRequest, false, "Username already exists"))
                                .channel(Channels.privateChannelSet + message.getPublisher())
                                .async(new PNCallback<PNPublishResult>() {
                                    @Override
                                    public void onResponse(PNPublishResult result, PNStatus status) {
                                        // handle publish result, status always present, result if successful
                                        // status.isError() to see if error happened
                                        if (!status.isError()) {
                                        }
                                    }
                                });
                    }
                    else {

                    }
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
                pb.publish()
                        .message(new LoginResponse(loginRequest, false, "Can't connect to SQL Database"))
                        .channel(Channels.privateChannelSet + message.getPublisher())
                        .async(new PNCallback<PNPublishResult>() {
                            @Override
                            public void onResponse(PNPublishResult result, PNStatus status) {
                                // handle publish result, status always present, result if successful
                                // status.isError() to see if error happened
                                if (!status.isError()) {
                                }
                            }
                        });
            }
        }
    }

    @Override
    public void presence(PubNub pb, PNPresenceEventResult presence) {

    }
}
