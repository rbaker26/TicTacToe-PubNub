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

$stmt = $conn->prepare("SELECT * FROM moves WHERE room_id = ?");
$stmt->bind_param("s",$roomid);
$roomid = $_GET['roomid'];

$stmt->execute();
$stmt->store_result();

// $result = $conn->query($sql);

if ($stmt->num_rows > 0) {
    $data = [];
    for ($i = 0; $i < $stmt->num_rows; $i++)
    {
        // Add a new array cell to $data, at index $i
        $data[$i] = [];

        // Bind result for row $i
        $stmt->bind_result($data[$i]['pk'],$data[$i]['room_id'],$data[$i]['row_val'],$data[$i]['col_val'],$data[$i]['player_id']);

        // Fetch $i^{th} row
        $stmt->fetch();
        echo "<tr>";
        //echo    "<td>", $row["pk"], "</td>";
        echo    "<td>". $data[$i]["room_id"]. "</td>";
        echo    "<td>". $data[$i]["row_val"]. "</td>";
        echo    "<td>". $data[$i]["col_val"]. "</td>";
        echo    "<td>". $data[$i]["player_id"]. "</td>";
        echo "</tr>";
    }
    /***********************/
    // debug statement      /
    /***********************/
    //var_dump($data);
    /***********************/



    // output data of each row
    // while($row = $stmt->fetch()) {
    //     var_dump($row);
    //     echo "<tr>";
    //     //echo    "<td>", $row["pk"], "</td>";
    //     echo    "<td>". $row["room_id"]. "</td>";
    //     echo    "<td>". $row["row_val"]. "</td>";
    //     echo    "<td>". $row["col_val"]. "</td>";
    //     echo    "<td>". $row["player_id"]. "</td>";
    //     echo "</tr>";
    // }
} else {
    echo "0 results";
}
$conn->close();
?>