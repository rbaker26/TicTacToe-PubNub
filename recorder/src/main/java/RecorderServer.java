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


        while (true) {
            //*********************************************
            // Get Pubnub Message
            //*********************************************


            //*********************************************


            //*********************************************
            // Call Appropriate DB Function
            //*********************************************


            //*********************************************
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


}