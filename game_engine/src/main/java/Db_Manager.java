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

            }
            catch (Exception e) {

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
    boolean UserExists(String playerIU) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement(  "    SELECT CASE WHEN EXISTS ( SELECT *  FROM  players  WHERE user_id = (:user_id)  LIMIT  1 )\n" +
                                         "    THEN                " +
                                         "    CAST ( 1 AS BIT)    " +
                                         "    ELSE                " +
                                         "    CAST ( 0 AS BIT)    " +
                                         "    END  );             ");

        preparedStatement.setString(1,"user_id");


        ResultSet rs =  preparedStatement.executeQuery();

        rs.first();

        return rs.getBoolean(0);

    }


    //******************************************************************************************



}