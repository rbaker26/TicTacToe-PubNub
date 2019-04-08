public class RecorderServer {

    public static void main(String[] args){
        Db_Manager db = Db_Manager.GetInstance();


        try {
            db.WriteMove(2, 22, 222, 2222);
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
    }


}
