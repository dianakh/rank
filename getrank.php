<?php 
require "conn.php";
$sql = "select rank from recipe where recipe='apple'";
$res = mysqli_query($conn,$sql);
$result = array();
while($row = mysqli_fetch_array($res)){
array_push($result,
array('rank'=>$row[0]
));
}
echo json_encode(array("result"=>$result));
mysqli_close($conn);
?>
