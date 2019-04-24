<!DOCTYPE html>
<html>
<head>
    <?php include 'head.php'; ?>
    <script src="moveUpdate.js"></script>

</head>
<body>
<div onmouseover="reload_table()" onmouseout="reload_table()" onclick="reload_table()">

    <?php include './header.php';?>
    <br/>
    <br/>
    <h1 align="center">Move History View</h1>
    <br/>
    <br/>
    <h1 id="test"> </h1>
    <table id="moves" onmouseover="reload_table()" onmouseout="reload_table()" onclick="reload_table()">
        <tr>
            <!-- <th>Primary Key</th> -->
            <th>Room ID</th> 
            <th>Row</th>
            <th>Column</th>
            <th>Player ID</th>
        </tr>
        <?php include './getMovesTable.php'; ?>
          
    </table>
<div>
</body>
</html>