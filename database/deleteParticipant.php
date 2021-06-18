<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
    
 $id=$_POST['id'];

 $query="DELETE FROM participant_tbl WHERE participant_ID='$id'";

    $result=mysqli_query($con,$query);    
 

if($result){
    echo "OK";

}
else{
    echo " Result FAILED";
}

}

else
{
     echo " Connection FAILED";
}

mysqli_close($con);

?>