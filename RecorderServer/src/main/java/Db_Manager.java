import java.sql.*;


public class Db_Manager {
    private final static String IP       = "68.5.123.182";
    private final static String PORT     = "3306";
    private final static String USERNAME = "recorder";
    private final static String PASSWORD = "recorder0";
    private Db_Manager() {
        if(connection == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection =  DriverManager.getConnection("jdbc:mysql://"+IP+":"+PORT +"?" + "user="+USERNAME+"&password="+PASSWORD);
            }
            catch (SQLException sqle) {

            }
            catch (Exception e) {

            }
        }
    }
    private static Db_Manager instance = null;
    private static Connection connection = null;
    public static Db_Manager GetInstance() {
        if( instance == null) {
            instance = new Db_Manager();
        }
        if(connection == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection =  DriverManager.getConnection("jdbc:mysql://68.5.123.182:3306?" + "user=recorder&password=recorder0");
            }
            catch (SQLException sqle) {

            }
            catch (Exception e) {

            }
        }
        return instance;
    }




    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;


    public void WriteMove( int roomID, int row, int col, int playerID) throws Exception {
      //  Class.forName("com.mysql.jdbc.Driver");

        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.moves (room_id, row_val, col_val, player_id) values ( ?, ?, ?,?)");

        preparedStatement.setInt(1,roomID);
        preparedStatement.setInt(2,row);
        preparedStatement.setInt(3,col);
        preparedStatement.setInt(4,playerID);
        preparedStatement.executeUpdate();

    }

}
