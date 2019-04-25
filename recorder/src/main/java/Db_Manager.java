import Messages.MoveInfo;

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
    public void WriteMove( int roomID, int row, int col, String playerID) throws SQLException {
        //    private Statement statement = null;
        //    private PreparedStatement preparedStatement = null;
        //    private ResultSet resultSet = null;

        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.moves (room_id, row_val, col_val, username, datetime) " +
                                      "values (?, ?, ?, ?, ?)");


        preparedStatement.setInt(1,roomID);
        preparedStatement.setInt(2,row);
        preparedStatement.setInt(3,col);
        preparedStatement.setString(4,playerID);
        java.util.Date d = new java.util.Date();
        long ms = d.getTime();
        preparedStatement.setString(5,Long.toString(ms));
        preparedStatement.executeUpdate();
    }
    //******************************************************************************************
    //******************************************************************************************
    public void WriteMove(MoveInfo move) throws SQLException {
        WriteMove( move.getRoomID(), move.getRow(), move.getCol(), move.getPlayerID());
    }
    //******************************************************************************************


//    public void WriteWinner(MoveInfo move) {
//        if(move.getRow() == -1 && move.Get) {
//
//        }
//    }
//


    //******************************************************************************************
    public void WritePlayer(String name, String username)  throws SQLException{

        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.players (player_name, username) values (?, ?)");

        preparedStatement.setString(1,name);
        preparedStatement.setString(2,username);
        preparedStatement.executeUpdate();
    }
    //******************************************************************************************
    //******************************************************************************************
//    public void WritePlayer(Player player)  throws SQLException{
//        WritePlayer(player.name, player.playerID);
//    }
    //******************************************************************************************


    //******************************************************************************************
    public void WriteLobby(int lobbyID, int roomID, int player1ID, int player2ID) { }
    //******************************************************************************************
    //******************************************************************************************
    public void WriteLobby(Lobby lobby) { }
    //******************************************************************************************


    public void UpdateScore(String username,boolean wonGame) throws SQLException {
        PreparedStatement preparedStatement =  connection
                .prepareStatement("insert into  tictactoe.players (win_count, game_count) values (?, ?) where username = ?");



        PreparedStatement innerPreparedStatement =  connection
                .prepareStatement("SELECT win_count, game_count FROM tictactoe.players where username = ?");
        innerPreparedStatement.setString(1,username);

        ResultSet innerRs = innerPreparedStatement.executeQuery();
        innerRs.next();
        int winCount = innerRs.getInt(1);
        int gameCount = innerRs.getInt(2);



        preparedStatement.setInt(1,(wonGame ? winCount + 1 : winCount));
        preparedStatement.setInt(2,gameCount+1);
        preparedStatement.setString(3,username);
        preparedStatement.executeUpdate();
    }
}