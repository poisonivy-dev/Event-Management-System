<?php
$user_name="id17061348_eventmanagement";
$password="Contacts1234;";
$server="localhost";
$db_name="id17061348_eventlist";


$con=mysqli_connect($server,$user_name,$password,$db_name);

if($con){
 $title=$_POST['eTitle'];
 $type=$_POST['eType'];
 $venue=$_POST['eVenue'];
 $date=$_POST['eDate'];
 $stime=$_POST['eStime'];
 $etime=$_POST['eEtime'];
 $desc=$_POST['eDesc'];

 $query="insert into event_tbl(event_title,event_type,event_venue,event_date,event_stime,event_etime,event_description)  values('$title','$type','$venue','$date','$stime','$etime','$desc')";

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