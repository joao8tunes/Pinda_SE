<?php 
$acctKey = 'r+TWpvMAYaRIM/Ao4e8yDp4d+TvMWDBH4rTBNNKUoBc';
$rootUri = 'https://api.datamarket.azure.com/Bing/Search';
$query = 'pindamonhangaba';
$serviceOp ='Web';
$market ='en-us';
$query = urlencode("'$query'");
$market = urlencode("'$market'");
$requestUri = "$rootUri/$serviceOp?\$format=json&Query=$query";//&Market=$market
$auth = base64_encode("$acctKey:$acctKey");
$data = array(  
            'http' => array(
                        'request_fulluri' => true,
                        'ignore_errors' => true,
                        'header' => "Authorization: Basic $auth"
                        )
            );
$context = stream_context_create($data);
$response = file_get_contents($requestUri, 0, $context);
$response=json_decode($response);
echo "<pre>";
print_r($response);
echo "</pre>";
 ?>





<!--000027601836898703234:qvy6jx8oubis-->
<!--000027601836898703234:qvy6jx8oubi-->

<!--
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
    "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Hello World - Google  Web Search API Sample</title>
    <script src="https://www.google.com/jsapi" type="text/javascript"></script>
  </head>
  <body>
    <div id="results"></div>

    <script type="text/javascript">
      google.load('search-form', '1');

      function onLoad() {
          var customSearchControl = new google.search.CustomSearchControl("000027601836898703234:qvy6jx8oubi");
          var drawOptions = new google.search.DrawOptions();
          //drawOptions.setSearchFormRoot('search-form');
          customSearchControl.draw('results', drawOptions);
      }
      google.setOnLoadCallback(onLoad);
    </script>
  </body>
</html>
-->