
import java.util.Scanner;

import com.pubnub.api.*;

import java.util.Arrays;

public class Client {


    public static void main(String[] args) {


        Scanner consoleInput = new Scanner(System.in);
        System.out.println("Are you A or B?");
        String name = "";

        do {
            name = consoleInput.next();

            if(name.length() > 0) {
                name = name.substring(0, 1);
            }

        } while(!name.equals("A") && !name.equals("B"));
        System.out.println("Alright, you are " + name + "! Connecting...");

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setSubscribeKey("sub-c-89b1d65a-4f40-11e9-bc3e-aabf89950afa");
        pnConfiguration.setPublishKey("pub-c-79140f4c-8b9e-49ed-992f-cf6322c68d04");
        //pnConfiguration.setUuid("User-A::Java");

        String roomRequestChannel = "Rooms::Request";
        String roomUpdateChannel = "Rooms::Update";

        PubNub pubnub = new PubNub(pnConfiguration);
        pubnub.addListener(new RoomRequesterCallback(name, roomRequestChannel, roomRequestChannel));
        pubnub.subscribe().channels(Arrays.asList(roomUpdateChannel)).execute();
    }
}
