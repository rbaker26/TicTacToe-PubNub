public class RecorderServer {

    public static void main(String[] args){
        Db_Manager db = Db_Manager.GetInstance();



        Move move = new Move(1,2,3,4);
        System.out.println(move);

        try {
            //db.WriteMove(99553, 1, 1, 6587);
            //db.WritePlayer("Naomi", 88888);



            for(int i = 0; i < 3; i++) {
                for(int j = 0; j < 3; j++) {
                    db.WriteMove(10000, i, j, 4653);
                 //   Thread.sleep(10);
                }
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }


}