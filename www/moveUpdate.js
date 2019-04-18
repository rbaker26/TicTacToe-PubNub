
function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
};

var table_header = "<tr>"                    + 
                        "<th>Room ID</th>"   + 
                        "<th>Row</th>"       +
                        "<th>Column</th>"    +
                        "<th>Player ID</th>" +
                    "</tr>";


var reload_table = function() {
    sleep(500);
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            document.getElementById("moves").innerHTML = table_header + this.responseText;
        }
    };
    xmlhttp.open("GET", "getMovesTable.php");
    xmlhttp.send();
};