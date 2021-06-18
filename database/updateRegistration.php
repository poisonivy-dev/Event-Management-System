<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
 $Id= (int)$_POST['id'];
 $title=$_POST['rName'];
 $roll=$_POST['rRoll'];
 $dept=$_POST['rDept'];
 $contact=$_POST['rContact'];
 $status=$_POST['rStatus'];
 $eID=(int)$_POST['rEventID'];
 

 $query="Update registration_tbl set registration_name = '$title' , registration_roll = '$roll' , registration_department = '$dept' , 
        registration_contact = '$contact' , registration_status = '$status' , id = $eID 
        where registration_ID = $Id ";

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