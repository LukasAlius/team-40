<?php
$files = scandir('xml/');
foreach($files as $file)
{
  if($file != "." && $file != "..")
  {
    $xml = simplexml_load_string(file_get_contents('xml/'.$file));
    $location = substr($file, 0, -4);

    $xmlOut = "<CheckpointList>";
    foreach ($xml as $b)
    {
      $xmlOut .= "<Checkpoint>".
                  "<name value='$b->name'/>".
                  "<coordinates lat='$b->lat' lon='$b->long'/>".
                  "<id1 value='$b->id1'/>".
                  "<id2 value='$b->id2'/>".
                  "<id3 value='$b->id3'/>".
                  "<localArea value='$b->localArea'/>".
                  "<generalArea value='$b->generalArea'/>".
                  "<wideArea value='$b->wideArea'/>".
                  "<distance value='$b->distance'/>".
                  "</Checkpoint>";
    }
    $xmlOut .= "</$location>";
    file_put_contents("xml/$location.xml", $xmlOut);




  }
}

?>