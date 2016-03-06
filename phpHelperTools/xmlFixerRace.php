<?php
$files = scandir('raceXML/');
foreach($files as $file)
{
  if($file != "." && $file != "..")
  {
    $xml = simplexml_load_string(file_get_contents('raceXML/'.$file));
    $raceTitle = substr($file, 0, -4);

    $xmlOut = "<race>";
    foreach ($xml as $b)
    {
      $xmlOut .= "<Checkpoint>".
                  "<name value='$b->name'/>".
                  "<short value='$b->short'/>".
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
    $xmlOut .= "</race>";
    file_put_contents("raceXMLFixed/$raceTitle.xml", $xmlOut);




  }
}

?>