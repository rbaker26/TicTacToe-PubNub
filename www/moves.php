<!DOCTYPE html>
<html>
<head>
    <?php include 'head.php'; ?>
</head>
<body>

    <?php include './header.php';?>
    <br/>
    <br/>
    <h1 align="center">Move History View</h1>
    <br/>
    <br/>
    <table id="moves">
        <tr>
            <!-- <th>Primary Key</th> -->
            <th>Room ID</th> 
            <th>Row</th>
            <th>Column</th>
            <th>Player ID</th>
        </tr>
        <?php include './getMovesTable.php'; ?>
    </table>

</body>
</html>