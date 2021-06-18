<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
 $Id= (int)$_POST['id'];
 $title=$_POST['eTitle'];
 $type=$_POST['eType'];
 $venue=$_POST['eVenue'];
 $date=$_POST['eDate'];
 $stime=$_POST['eStime'];
 $etime=$_POST['eEtime'];
 $desc=$_POST['eDesc'];

 $query="Update event_tbl set event_title = '$title' , event_type = '$type' , event_venue = '$venue' , 
        event_date = '$date' , event_stime = '$stime' , event_etime = '$etime' , event_description = '$desc'
        where id = $Id ";

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