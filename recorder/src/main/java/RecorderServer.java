public class RecorderServer {

    public static void main(String[] args){
        Db_Manager db = Db_Manager.GetInstance();



        Move move = new Move(1,2,3,4);
        System.out.println(move);

        try {
            //db.WriteMove(99553, 1, 1, 6587);
            //db.WritePlayer("Naomi", 88888);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }


}