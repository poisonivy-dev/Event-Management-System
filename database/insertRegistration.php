<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
 $Name=$_POST['rName'];
 $Roll=$_POST['rRoll'];
 $Dept=$_POST['rDept'];
 $Contact=$_POST['rContact'];
 $Status=$_POST['rStatus'];
 $eID=(int) $_POST['rEventID'];

 $query="insert into registration_tbl(registration_name,registration_roll,registration_department,registration_contact,registration_status,id)  values('$Name','$Roll','$Dept','$Contact','$Status',$eID)";

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