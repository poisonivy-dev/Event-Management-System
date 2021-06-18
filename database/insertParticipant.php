<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
 $Name=$_POST['name'];
 $Roll=$_POST['roll'];
 $Dept=$_POST['dept'];
 $Contact=$_POST['contact'];
 $Status=$_POST['role'];
 $eID=(int) $_POST['eID'];

 $query="insert into participant_tbl(participant_name,participant_roll,participant_department,participant_contact,participant_role,p_event_ID)  values('$Name','$Roll','$Dept','$Contact','$Status',$eID)";

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