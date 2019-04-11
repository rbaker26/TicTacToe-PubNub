import java.sql.*;


class Db_Manager {

    //******************************************************************************************
    // Server Details
    //******************************************************************************************
    private final static String IP       = "68.5.123.182";
    private final static String PORT     = "3306";
    private final static String USERNAME = "recorder";
    private final static String PASSWORD = "recorder0";
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
    public void WriteMove( int roomID, int row, int col, int playerID) throws SQLException {
        //    private Statement statement = null;
        //    private PreparedStatement preparedStatement = null;
        //    private ResultSet resultSet = null;

        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.moves (room_id, row_val, col_val, player_id) values (?, ?, ?, ?)");


        preparedStatement.setInt(1,roomID);
        preparedStatement.setInt(2,row);
        preparedStatement.setInt(3,col);
        preparedStatement.setInt(4,playerID);
        preparedStatement.executeUpdate();
    }
    //******************************************************************************************
    //******************************************************************************************
    public void WriteMove(Move move) throws SQLException {
        WriteMove( move.roomID, move.row, move.col, move.playerID);
    }
    //******************************************************************************************



    //******************************************************************************************
    public void WritePlayer(String name, int playerID)  throws SQLException{

        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.players (player_name, player_id) values (?, ?)");

        preparedStatement.setString(1,name);
        preparedStatement.setInt(2,playerID);
        preparedStatement.executeUpdate();
    }
    //******************************************************************************************
    //******************************************************************************************
    public void WritePlayer(Player player)  throws SQLException{
        WritePlayer(player.name, player.playerID);
    }
    //******************************************************************************************


    //******************************************************************************************
    public void WriteLobby(int lobbyID, int roomID, int player1ID, int player2ID) { }
    //******************************************************************************************
    //******************************************************************************************
    public void WriteLobby(Lobby lobby) { }
    //******************************************************************************************
}