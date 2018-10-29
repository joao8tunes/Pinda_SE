<?php

header("Access-Control-Allow-Origin: *");
header('Content-Type: text/html; charset=utf-8');

$begin = time();

ini_set('display_errors', 1); ini_set('log_errors', 1); ini_set('error_log', dirname(__FILE__) . '/error_log.txt'); error_reporting(E_ALL);
//header('Content-type: application/json; charset=UTF-8');
require_once("stemmer.php");
require_once("simple_html_dom.php");

//$_POST['keywords'] = "apple";
$_POST['startwords'] = 'no';
$_POST['detail'] = 'no';
$_POST['snippets'] = 'yes';
$_POST['matrix'] = 'yes';
$_POST['pex'] = 'no';
$_POST['pex_matrix'] = 'yes';

if (!isset($_POST['keywords'])) $_POST['keywords'] = "";
if (!isset($_POST['api'])) $_POST['api'] = "ddg";

//Extracted from: http://php.net/manual/pt_BR/function.rmdir.php
function delTree($dir) {
    $files = glob( $dir . '*', GLOB_MARK );
    foreach( $files as $file ){
        if( substr( $file, -1 ) == '/' )
            delTree( $file );
        else
            unlink( $file );
    }
   
    if (is_dir($dir)) rmdir( $dir );
}

//Extracted from: http://stackoverflow.com/questions/1334613/how-to-recursively-zip-a-directory-in-php
function Zip($source, $destination)
{
    if (!extension_loaded('zip') || !file_exists($source)) {
        return false;
    }

    $zip = new ZipArchive();
    if (!$zip->open($destination, ZIPARCHIVE::CREATE)) {
        return false;
    }

    $source = str_replace('\\', '/', realpath($source));

    if (is_dir($source) === true)
    {
        $files = new RecursiveIteratorIterator(new RecursiveDirectoryIterator($source), RecursiveIteratorIterator::SELF_FIRST);

        foreach ($files as $file)
        {
            $file = str_replace('\\', '/', $file);

            // Ignore "." and ".." folders
            if( in_array(substr($file, strrpos($file, '/')+1), array('.', '..')) )
                continue;

            $file = realpath($file);

            if (is_dir($file) === true)
            {
                $zip->addEmptyDir(str_replace($source . '/', '', $file . '/'));
            }
            else if (is_file($file) === true)
            {
                $zip->addFromString(str_replace($source . '/', '', $file), file_get_contents($file));
            }
        }
    }
    else if (is_file($source) === true)
    {
        $zip->addFromString(basename($source), file_get_contents($source));
    }

    return $zip->close();
}

function httpRequest($url) {
	$cURL = curl_init($url);
	curl_setopt($cURL, CURLOPT_RETURNTRANSFER, true);
	curl_setopt($cURL, CURLOPT_FOLLOWLOCATION, true);
	$resultado = curl_exec($cURL);
	$resposta = curl_getinfo($cURL, CURLINFO_HTTP_CODE);
	curl_close($cURL);
	return $resultado;
}




$_POST['keywords'] = trim($_POST['keywords']);
$_POST['keywords'] = str_replace(" ", "+", $_POST['keywords']);

$originalSnippets = array();
$originalTotalWords = 0;



switch ($_POST['api']) {
    case 'foo' : {
        //***********************************************
        //Extracting the snippets content from FAROO API:
        //***********************************************
        $page = 1;

         while (true) {
             $link = "http://www.faroo.com/api?q=" . $_POST['keywords'] . "&start=" . $page . "&length=10&l=en&src=web&f=json&key=4jRpMYuh2FxzDW03lYlimvAeGEY_";
             $url = httpRequest($link);
             $resultPage = json_decode($url, true);

             //echo $page . "<br>" . $url . "<br><br><br>" . $resultPage;

             if (count($resultPage['results']) > 0) {    //Pelo menos um resultado retornado.
                 //echo "TAMANHO: " . $resultPage['results'] . "<br><br>";

                 for ($i = 0; $i < count($resultPage['results']); $i++) {
                     $originalSnippets[] = array('title' => $resultPage['results'][$i]['title'], 
                         'link' => $resultPage['results'][$i]['url'], 
                         'description' => $resultPage['results'][$i]['kwic']);

                     $originalTotalWords += count( explode(" ", $resultPage['results'][$i]['kwic']) );
                 }

                 $page += 10;
                 //echo "<br><br>" . "http://www.faroo.com/api?q=" . $_POST['keywords'] . "&start=" . $page . "&length=10&l=en&src=web&f=json&key=4jRpMYuh2FxzDW03lYlimvAeGEY_" . "<br><br><br><br>";
                 sleep(1);
             }
             else break;
         }
        //**************************************
    } break;
    default : {
        //****************************************************
        //Extracting the snippets content from DuckDuckGo API:
        //****************************************************
        $link = "http://api.duckduckgo.com/?q=" . trim($_POST['keywords']) . "&format=json&pretty=1";
        $url = httpRequest($link);
        $url= str_replace('&quot;', '"', $url);
        $resultPage = json_decode($url, true);

        for ($i = 0; $i < count($resultPage['RelatedTopics']); $i++) {
            if (!isset($resultPage['RelatedTopics'][$i]['Result'])) break;

            $dom = new simple_html_dom($resultPage['RelatedTopics'][$i]['Result']);
            $title_res = $dom->find('a', 0)->plaintext;
            $text_res = substr_replace($resultPage['RelatedTopics'][$i]['Text'], "", 0, strlen($dom->find('a', 0)->plaintext));

            $originalSnippets[] = array('title' => $title_res, 
                'link' => $resultPage['RelatedTopics'][$i]['FirstURL'], 
                'description' => $text_res);

            $originalTotalWords += count( explode(" ", $text_res) );
        }
        //**************************************
    }
}


//**************************************
//Knowledge Discovery in Databases:
//**************************************
$stopwords_EnUS = array();
$startwords = array();
$processedSnippets = $originalSnippets;
          
//Jogando as stopwords em um array (pq vai ser lido muitas vezes):
if (($stopwords_EnUSFile = fopen("../../_import/files/stopwords_en-us", "r"))) {
    while (($line = fgets($stopwords_EnUSFile)) !== false) {
    	$w = " ".trim($line)." ";
    	$w = strtolower($w);
        $stopwords_EnUS[] = $w;
    }
}
        
fclose($stopwords_EnUSFile);
sort($stopwords_EnUS);
$processedTotalWords = 0;
        
foreach ($processedSnippets as $indexSnippet => $snippet) {
 	//Remoção dos stopwords:
  	$snippet['title'] = strtolower($snippet['title']);
    $snippet['title'] = str_replace($stopwords_EnUS, " ", $snippet['title']);

  	//Remoção das pontuações:
	$snippet['title'] = str_replace(array(".", ",", "\"", "/", "(", ")", "!", "?", ":", ";", "'", "–", "-", "*", "#", "#", "§", "º", "]", "[", "{", "}", "ª", "~", "^", "°", "¢", "¹", "²", "³", "£", "¬", "<", ">"), "", $snippet['title']);
   	$snippet['title'] = str_replace(range( 0, 9 ), null, $snippet['title']);
                     
 	//Contagem de frequência dos termos:
 	$words = explode(" ", $snippet['title']);
 	$processedTotalWords += count($words);
           
 	foreach ($words as $word) {
 		$word = trim($word);

 		if (strlen($word) <= 3) continue;

 		//$word = PorterStemmer::Stem($word);
 		$newWord = true;
                
	 	for ($i = 0; $i < count($startwords); $i++) {
	 		if ($startwords[$i]['word'] == $word) {
	 			$newWord = false;
	 			$newSnippet = true;
	 			++$startwords[$i]['freq'];
                    
	 			for ($j = 0; $j < count($startwords[$i]['snippets']); $j++) {
	 				if ($startwords[$i]['snippets'][$j]['indexSnippet'] == $indexSnippet) {    //Uma referência desse snippet para esse termo já existe.
	 					$newSnippet = false;
	 					++$startwords[$i]['snippets'][$j]['freq'];
	 					break;
	 				}
	 			}
                     
	 			if ($newSnippet) $startwords[$i]['snippets'][count($startwords[$i]['snippets'])] = array('indexSnippet' => $indexSnippet, 'freq' => 1);
                
	 			break;
	 		}
	 	}
                     
	 	if ($newWord) $startwords[] = array('word' => $word, 'freq' => 1, 'snippets' => array(array('indexSnippet' => $indexSnippet, 'freq' => 1)));
 	}

 	//-------------

 	//Remoção dos stopwords:
  	$snippet['description'] = strtolower($snippet['description']);
    $snippet['description'] = str_replace($stopwords_EnUS, " ", $snippet['description']);

  	//Remoção das pontuações:
	$snippet['description'] = str_replace(array(".", ",", "\"", "/", "(", ")", "!", "?", ":", ";", "'", "–", "-", "*", "#", "#", "§", "º", "]", "[", "{", "}", "ª", "~", "^", "°", "¢", "¹", "²", "³", "£", "¬", "<", ">"), "", $snippet['description']);
   	$snippet['description'] = str_replace(range( 0, 9 ), null, $snippet['description']);
                     
 	//Contagem de frequência dos termos:
 	$words = explode(" ", $snippet['description']);
 	$processedTotalWords += count($words);
           
 	foreach ($words as $word) {
 		$word = trim($word);
 		
 		if (strlen($word) <= 3) continue;
 		
 		//$word = PorterStemmer::Stem($word);
 		$newWord = true;
                
	 	for ($i = 0; $i < count($startwords); $i++) {
	 		if ($startwords[$i]['word'] == $word) {
	 			$newWord = false;
	 			$newSnippet = true;
	 			++$startwords[$i]['freq'];
                    
	 			for ($j = 0; $j < count($startwords[$i]['snippets']); $j++) {
	 				if ($startwords[$i]['snippets'][$j]['indexSnippet'] == $indexSnippet) {    //Uma referência desse snippet para esse termo já existe.
	 					$newSnippet = false;
	 					++$startwords[$i]['snippets'][$j]['freq'];
	 					break;
	 				}
	 			}
                     
	 			if ($newSnippet) $startwords[$i]['snippets'][count($startwords[$i]['snippets'])] = array('indexSnippet' => $indexSnippet, 'freq' => 1);
                
	 			break;
	 		}
	 	}
                     
	 	if ($newWord) $startwords[] = array('word' => $word, 'freq' => 1, 'snippets' => array(array('indexSnippet' => $indexSnippet, 'freq' => 1)));
 	}
}

$return = array('total' => count($originalSnippets));

if ($_POST['detail'] == 'yes') $return['detail'] = array('originalTotalWords' => $originalTotalWords, 'processedTotalWords' => $processedTotalWords, 'finalTotalWords' => count($startwords));
else $return['detail'] = null;

if ($_POST['snippets'] == 'yes') $return['snippets'] = $originalSnippets;
else $return['snippets'] = null;

if ($_POST['matrix'] == 'no') $return['matrix'] = null;
else {
	//Ordenando startwords:
	switch ($_POST['matrix']) {
		case 'sort_a-c': {
			sort($startwords);    //Ordem alfabética crescente.
		} break;
		case 'sort_d-f-t': {
		    usort($startwords, function($a, $b) {    //Ordem decrescente de frequência total.
			    return $b['freq'] - $a['freq'];
			});
		} break;
		case 'sort_c-f-t': {
		    usort($startwords, function($a, $b) {    //Ordem crescente de frequência total.
			    return $a['freq'] - $b['freq'];
			});
		} break;
		case 'sort_c-i': {
		    usort($startwords, function($a, $b) {    //Ordem crescente de incidência.
			    return count($a['snippets']) - count($b['snippets']);
			});
		} break;
		case 'sort_d-i': {
		    usort($startwords, function($a, $b) {    //Ordem decrescente de incidência.
			   return count($b['snippets']) - count($a['snippets']);
			});
		} break;
		case 'sort_c-i-f-t': {
		    usort($startwords, function($a, $b) {    //Ordem crescente de incidência e frequência total.
			   return (count($a['snippets'])+$a['freq']) - (count($b['snippets'])+$b['freq']);
			});
		} break;
		case 'sort_d-i-f-t': {
		    usort($startwords, function($a, $b) {    //Ordem decrescente de incidência e frequência total.
			  return (count($b['snippets'])+$b['freq']) - (count($a['snippets'])+$a['freq']);
			});
		} break;		
	}	

	$return['matrix'] = $startwords;
}

if ($_POST['startwords'] == 'yes') {
	$return['startwords'] = array();
	 
	for ($i = 0; $i < count($startwords); $i++) {
		$return['startwords'][] = $startwords[$i]['word'];
	}	
}
else $return['startwords'] = null;

if ($_POST['pex'] == 'yes') {
	$pathToDir = "pex";
	if (file_exists($pathToDir)) delTree($pathToDir);

	mkdir($pathToDir, 0777);
	$file = fopen($pathToDir . "/matrix.data", "w");
	fwrite($file, "DN\n" . $return['total'] . "\n" . count($startwords) . "\n");

	for ($i = 0; $i < count($startwords); $i++) {
		fwrite($file, $startwords[$i]['word']);

		if ($i+1 < count($startwords)) fwrite($file, ";");
	}

	fwrite($file, "\n");

	for ($i = 0; $i < $return['total']; $i++) {
		fwrite($file, "snippet_" . ($i+1));

		for ($j = 0; $j < count($startwords); $j++) {
			$nullFreq = true;

			for ($k = 0; $k < count($startwords[$j]['snippets']); $k++) {
				if ($startwords[$j]['snippets'][$k]['indexSnippet'] == $i) {
					fwrite($file, ";" . $startwords[$j]['snippets'][$k]['freq']);
					$nullFreq = false;
					break;
				}
			}

			if ($nullFreq) fwrite($file, ";0");
		}

		fwrite($file, "\n");
	}

	fclose($file);
	mkdir($pathToDir . "/snippets");

	for ($i = 0; $i < $return['total']; $i++) {
		$file = fopen($pathToDir . "/snippets/snippet_" . ($i+1), "w");
		fwrite($file, $originalSnippets[$i]['title'] . "\n\n" . $originalSnippets[$i]['description']);
		//fwrite($file, $originalSnippets[$i]['title'] . "\n\n" . $originalSnippets[$i]['link'] . "\n\n" . $originalSnippets[$i]['description']);
		fclose($file);
	}

	Zip($pathToDir . "/snippets", $pathToDir . '/snippets.zip');

}

if ($_POST['pex_matrix'] == 'yes') {
	$return['pex_matrix'] = "DN\n" . $return['total'] . "\n" . count($startwords) . "\n";

	for ($i = 0; $i < count($startwords); $i++) {
		$return['pex_matrix'] .= $startwords[$i]['word'];

		if ($i+1 < count($startwords)) $return['pex_matrix'] .= ";";
	}

	$return['pex_matrix'] .= "\n";

	for ($i = 0; $i < $return['total']; $i++) {
		$return['pex_matrix'] .= "snippet_" . ($i+1);

		for ($j = 0; $j < count($startwords); $j++) {
			$nullFreq = true;

			for ($k = 0; $k < count($startwords[$j]['snippets']); $k++) {
				if ($startwords[$j]['snippets'][$k]['indexSnippet'] == $i) {
					$return['pex_matrix'] .= ";" . $startwords[$j]['snippets'][$k]['freq'];
					$nullFreq = false;
					break;
				}
			}

			if ($nullFreq) $return['pex_matrix'] .= ";0";
		}

		$return['pex_matrix'] .= "\n";
	}
}
else $return['pex_matrix'] = null;
//**************************************



$end = time();
$time = $end-$begin;
$return['time'] = $time;

echo json_encode($return);

?>
