<!DOCTYPE html>
<html>
<head>
    <?php include './head.php'; ?>
</head>
<body>

    <?php include './header.php';?>
    <br/>
    <br/>
    <h1 align="center">Move History by Room ID View</h1>
    <br/>
    <br/>
    <form>
        <input type="text" name="roomid" placeholder="Search Room ID..">
    </form>
    <br/>
    <table id="moves">
        <tr>
            <!-- <th>Primary Key</th> -->
            <th>Room ID</th> 
            <th>Row</th>
            <th>Column</th>
            <th>Player ID</th>
        </tr>
        <?php 
            if(isset($_GET['roomid']) && $_GET['roomid'] != "" ) {
                // $test = $_GET['roomid'];
                include './getMovesTableByRoomID.php';
            } else {
                include './getMovesTable.php';
            }
        ?>
        <!-- <?php //include './getTable.php'; ?> -->
    </table>

</body>
</html>