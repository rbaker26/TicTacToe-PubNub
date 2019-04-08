public class RecorderServer {

    public static void main(String[] args){
        Db_Manager db = Db_Manager.GetInstance();



        Move move = new Move(1,2,3,4);
        System.out.println(move);
//
//        try {
//            db.WriteMove(2, 22, 222, 2222);
//        }
//        catch(Exception e){
//            System.out.println(e.toString());
//        }
    }


}
