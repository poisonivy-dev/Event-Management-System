<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
 $Id= (int)$_POST['id'];
 $Title=$_POST['name'];
 $Roll=$_POST['roll'];
 $Dept=$_POST['dept'];
 $Contact=$_POST['contact'];
 $Role=$_POST['role'];
 $EID=(int)$_POST['eID'];
 

 $query="Update participant_tbl set participant_name = '$Title' , participant_roll = '$Roll' , participant_department = '$Dept' ,  participant_contact = '$Contact' , participant_role = '$Role' , p_event_ID = $EID 
        where participant_ID = $Id ";

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