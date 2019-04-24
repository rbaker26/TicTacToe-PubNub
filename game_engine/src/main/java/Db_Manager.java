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
                connection =  DriverManager.getConnection("jdbc:mysql://"+IP+":"+PORT +"?" + "user="+USERNAME+"&password="+PASSWORD);
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
                connection =  DriverManager.getConnection("jdbc:mysql://"+IP+":"+PORT +"?" + "user="+USERNAME+"&password="+PASSWORD);
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
                .prepareStatement(  "    SELECT CASE WHEN EXISTS ( SELECT *  FROM  players  WHERE user_id = ?  LIMIT  1 )" +
                                         "    THEN                " +
                                         "    CAST ( 1 AS BIT)    " +
                                         "    ELSE                " +
                                         "    CAST ( 0 AS BIT)    " +
                                         "    END  );             ");

        preparedStatement.setString(1,playerName);


        ResultSet rs =  preparedStatement.executeQuery();

        rs.first();

        return rs.getBoolean(0);

    }
    //******************************************************************************************


    //******************************************************************************************
    boolean UserExistsByID(int playerID) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement(  "    SELECT CASE WHEN EXISTS ( SELECT *  FROM  players  WHERE user_id = ?  LIMIT  1 )" +
                        "    THEN                " +
                        "    CAST ( 1 AS BIT)    " +
                        "    ELSE                " +
                        "    CAST ( 0 AS BIT)    " +
                        "    END  );             ");

        preparedStatement.setInt(1,playerID);


        ResultSet rs =  preparedStatement.executeQuery();

        rs.first();

        return rs.getBoolean(0);

    }
    //******************************************************************************************


    //******************************************************************************************
    boolean ValidateUser(String playerID, String password ) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement(  "    SELECT CASE WHEN EXISTS ( SELECT *  FROM  players  WHERE user_id = ? AND password = ? LIMIT  1 )" +
                                         "    THEN                " +
                                         "    CAST ( 1 AS BIT)    " +
                                         "    ELSE                " +
                                         "    CAST ( 0 AS BIT)    " +
                                         "    END  );             ");

        preparedStatement.setString(1,playerID);
        preparedStatement.setString(2,password);


        ResultSet rs =  preparedStatement.executeQuery();

        rs.first();

        return rs.getBoolean(0);
    }
    //******************************************************************************************




    //******************************************************************************************
    void AddUser(String playerID, String username, String password) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.players (player_id, player_name, password) values (?, ?, ?)");

        preparedStatement.setString(1,playerID);
        preparedStatement.setString(2,username);
        preparedStatement.setString(3,password);
        preparedStatement.executeUpdate();
    }
    //******************************************************************************************
}