import java.sql.*;


public class Db_Manager {

    //******************************************************************************************
    // Server Details
    //******************************************************************************************
    private final static String IP       = "68.5.123.182";
    private final static String PORT     = "3306";
    private final static String USERNAME = "engine";
    private final static String PASSWORD = "engine0";
    //******************************************************************************************


    //******************************************************************************************
    // Singleton Pattern
    //******************************************************************************************
    private static Db_Manager instance = null;
    private static Connection connection = null;
    //******************************************************************************************
    public static Db_Manager GetInstance() {
        if( instance == null) {
            instance = new Db_Manager();
        }
        if(connection == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection =  DriverManager.getConnection("jdbc:mysql://"+IP+":"+PORT +"/tictactoe?" + "user="+USERNAME+"&password="+PASSWORD);
            }
            catch (SQLException sqle) {
                System.out.println("sqle exception");
            }
            catch (Exception e) {
                e.printStackTrace();
                System.out.println("other exception");
            }
        }
        return instance;
    }
    //******************************************************************************************
    private Db_Manager() {
        if(connection == null){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection =  DriverManager.getConnection("jdbc:mysql://"+IP+":"+PORT +"/tictactoe?" + "user="+USERNAME+"&password="+PASSWORD);
            }
            catch (SQLException sqle) {

            }
            catch (Exception e) {

            }
        }
    }
    //******************************************************************************************




    //******************************************************************************************
    boolean UserExistsByName(String playerName) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement(  "    SELECT CASE WHEN EXISTS ( SELECT *  FROM  players  WHERE username = ?  LIMIT  1 )" +
                                         "    THEN                " +
                                         "    true    " +
                                         "    ELSE                " +
                                         "    false    " +
                                         "    END  ;             ");

        preparedStatement.setString(1,playerName);


        ResultSet rs =  preparedStatement.executeQuery();

        rs.first();

        return rs.getBoolean(1);

    }
    //******************************************************************************************

    String GetPlayerName(String userName) throws SQLException {
        PreparedStatement prepareStatement = connection
                .prepareStatement("SELECT screen_name FROM tictactoe.players WHERE username = ?;");
        prepareStatement.setString(1, userName);

        ResultSet rs = prepareStatement.executeQuery();

        rs.next();

        return rs.getString(1);
    }

    int GetLastGameID() throws SQLException {
        PreparedStatement prepareStatement = connection
                .prepareStatement("SELECT max(room_id) FROM tictactoe.moves;");

        ResultSet rs = prepareStatement.executeQuery();

        rs.next();

        return rs.getInt(1);
    }
    //******************************************************************************************
    boolean ValidateUser(String playerID, String password ) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement(  "SELECT CASE WHEN EXISTS ( SELECT *  FROM  tictactoe.players  WHERE username = ? AND password = ? LIMIT  1 ) " +
                        "THEN               " +
                        "true " +
                        "ELSE               " +
                        "false " +
                        "END  ;           ");

        preparedStatement.setString(1,playerID);
        preparedStatement.setString(2,password);

        ResultSet rs =  preparedStatement.executeQuery();

        rs.next();

        return rs.getBoolean(1);
    }
    //******************************************************************************************




    //******************************************************************************************
    void AddUser(String playerID, String username, String password) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.players (username, screen_name, password) values (?, ?, ?)");

        preparedStatement.setString(1,playerID);
        preparedStatement.setString(2,username);
        preparedStatement.setString(3,password);
        preparedStatement.execute();
    }
    //******************************************************************************************
}