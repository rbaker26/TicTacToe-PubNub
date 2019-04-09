<?php
$servername = "68.5.123.182";
$username = "recorder";
$password = "recorder0";
$dbname = "tictactoe";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT * FROM moves";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        echo "<tr>";
        //echo    "<td>", $row["pk"], "</td>";
        echo    "<td>". $row["room_id"]. "</td>";
        echo    "<td>". $row["row_val"]. "</td>";
        echo    "<td>". $row["col_val"]. "</td>";
        echo    "<td>". $row["player_id"]. "</td>";
        echo "</tr>";
    }
} else {
    echo "0 results";
}
$conn->close();
?>