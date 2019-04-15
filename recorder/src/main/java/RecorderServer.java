import com.google.gson.JsonObject;

import java.sql.SQLException;
import java.util.ArrayDeque;

public class RecorderServer {

    public static void main(String[] args){

        //*********************************************
        // Get Database Connection
        //*********************************************
        Db_Manager db = Db_Manager.GetInstance();
        //*********************************************



        //*********************************************
        // Connect to PubNub
        //*********************************************


        //*********************************************


       ArrayDeque<JsonObject> bad_transmit = new ArrayDeque<JsonObject>();
       int toggle = 0;
        try {
            while (true) {
                //*********************************************
                // Get Pubnub Message
                //*********************************************
                JsonObject json = new JsonObject();

                //*********************************************


                //*********************************************
                // Call Appropriate DB Function
                //*********************************************
                try {
                    // Send to data base
                    throw new SQLException();
                }
                catch(SQLException sqle) {
                    bad_transmit.add(json);
                }
                finally {
                    toggle = (toggle + 1) %4;
                    if(toggle == 1) {
                        try{
                            // Resend message
                            throw new SQLException();
                        }
                        catch(SQLException sqle) {

                        }

                    }
                }
                //*********************************************

            }
        }
        catch(Exception e) {
            // Exit server
        }
        // Test code
//        Move move = new Move(1,2,3,4);
//        System.out.println(move);
//
//        try {
//            //db.WriteMove(99553, 1, 1, 6587);
//            //db.WritePlayer("Homi", 37159);
//
//            db.WriteMove(10000, 99, 99, 4653);
//
////            for(int i = 0; i < 3; i++) {
////                for(int j = 0; j < 3; j++) {
////                    db.WriteMove(10000, i, j, 4653);
////                    Thread.sleep(1000);
////                }
////            }
//        }
//        catch(Exception e){
//            System.out.println(e.toString());
//        }
    }



    public Move GetMoveFromPubNub() {




        return new Move(0,0,0,0);
    }
}