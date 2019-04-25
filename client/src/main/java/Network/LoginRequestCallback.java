package Network;

import Messages.*;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.function.Consumer;

public class LoginRequestCallback extends SubscribeCallback {

    LoginInfo loginInfo;

    private LoginInfo info;
    private String outgoingChannel;
    private String incomingChannel;
    private Consumer<String> successResponse;
    private Consumer<String> failResponse;

    public LoginRequestCallback(
            LoginInfo info,
            String outgoingChannel, String incomingChannel,
            Consumer<String> success, Consumer<String> fail) {
        this.info = info;
        this.incomingChannel = incomingChannel;
        this.outgoingChannel = outgoingChannel;
        this.successResponse = success;
        this.failResponse = fail;


    }


    @Override
    public void status(PubNub pubnub, PNStatus status) {

        if(status.getCategory() == PNStatusCategory.PNAccessDeniedCategory.PNUnexpectedDisconnectCategory) {
            //Event for when the radio/connectivity is lost
        }

        else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
            System.out.println("Publishing message on " + outgoingChannel);
            // Runs when we are ready to receive responses
            pubnub.publish().message(info).channel(outgoingChannel).async(new PNCallback<PNPublishResult>() {
                @Override
                public void onResponse(PNPublishResult result, PNStatus status) {
                    // handle publish result, status always present, result if successful
                    // status.isError() to see if error happened
                    if (!status.isError()) {
                        // Lele is fine
                    }
                    else {
                        System.out.println("Failure");
                    }
                }
            });

        }

    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        String sourceChannel = ( message.getChannel() != null
                ? message.getChannel()
                : message.getSubscription()
        );

        System.out.println();
        System.out.println("msg content: " + message.getMessage());
        System.out.println("source channel: " + sourceChannel);

        //if(message.getPublisher().contains("engine")) {
            if (sourceChannel.equals(incomingChannel)) {
                // code to handle login response
                // code that converts our message to the appropriate object

                //loginrequest
                LoginResponse response = Converter.fromJson(message.getMessage(), LoginResponse.class);

                //TODO need to be fixed

                if(response.isLoginSuccess()) {
                    successResponse.accept(response.getLoginRequest().getScreenName());
                }
                else {
                    failResponse.accept(response.getInformation());
                }
//                if(failResponse != null) {
//                    failResponse.run();
//                }
//
//                else if(successResponse != null) {
//                    successResponse.run();
//                }

            }
        //}

    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {

    }
}
