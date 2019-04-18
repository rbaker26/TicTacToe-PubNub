<!DOCTYPE html>
<html>
<head>
    <?php include './head.php'; ?>
</head>
<body>

    <?php include './header.php';?>
    <br/>
    <br/>
    <h1 align="center">Player History View</h1>
    <br/>
    <br/>
    <table id="moves">
        <tr>
            <!-- <th>Primary Key</th> -->
            <th>Player ID</th> 
            <th>Name</th>
            <th>Games Won</th>
            <th>Total Games Played</th>
        </tr>
        <?php include './getPlayersTable.php'; ?>
    </table>

</body>
</html>