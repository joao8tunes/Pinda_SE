<?php

//ini_set('display_errors', 1); ini_set('log_errors', 1); ini_set('error_log', dirname(__FILE__) . '/error_log.txt'); error_reporting(E_ALL);
header("Access-Control-Allow-Origin: *");
//header('Content-Type: text/html; charset=utf-8');
header('Content-Type: application/json;charset=ISO-8859-1');
header('Content-Encoding: gzip');
ini_set('max_execution_time', 0);    //Removendo limite de tempo de execução do PHP.
ob_start("ob_gzhandler");

$begin = time();

//1693 x 6353 = 10755629
//ini_set('display_errors', 1); 
//ini_set('log_errors', 1); 
//ini_set('error_log', dirname(__FILE__) . '/error_log.txt'); 
//error_reporting(E_ALL);
//header('Content-type: application/json; charset=UTF-8');
require_once("stemmer.php");
require_once("simple_html_dom.php");

if (!isset($_POST['operation'])) $_POST['operation'] = "search";

$_POST['stemmer'] = "yes";    //yes
$_POST['snippets'] = 'yes';    //yes
$_POST['pex_matrix'] = 'yes';    //yes
$_POST['luhn_cut'] = 'yes';    //yes
$_POST['lower_cut'] = 50;    //40 - 50
$_POST['upper_cut'] = PHP_INT_MAX;    //PHP_INT_MAX
$_POST['frequency_type'] = 'tf-idf';    //'tf' ou 'tf-idf'

if (!isset($_POST['database']) || $_POST['database'] == 1) $_POST['local_file'] = '../../_import/files/data/json/mapeamento_sistematico_AnaliseSentimentoBaseadaAspecto.json';
else $_POST['local_file'] = '../../_import/files/data/json/mapeamento_sistematico_SemanticaMineracaoTextos.json';

$_POST['load_preprocessed_file'] = 'yes';    //no
//$_POST['operation'] = "local";
//$_POST['keywords'] = "democratic";    //firmware => 5 resultados
$_POST['zipf_curve'] = 'no';    //no
$_POST['matrix'] = 'no';    //no
$_POST['pex'] = 'no';    //no
$_POST['startwords'] = 'no';    //no
$_POST['detail'] = 'no';    //no
$_POST['teste'] = 'no';    //no
$_POST['normalize'] = 'no';    //no

if ($_POST['operation'] == "search") {
    $_POST['luhn_cut'] = 'no';    //yes
    $_POST['load_preprocessed_file'] = 'no';    
}

if (!isset($_POST['keywords'])) $_POST['keywords'] = "";
if (!isset($_POST['api'])) $_POST['api'] = "ddg";

//Extracted from: https://core.trac.wordpress.org/browser/tags/3.9/src/wp-includes/formatting.php#L682
function removeAccents($string) 
{
    if ( !preg_match('/[\x80-\xff]/', $string) ) return $string;

    $chars = array(
    // Decompositions for Latin-1 Supplement
    chr(195).chr(128) => 'A', chr(195).chr(129) => 'A',
    chr(195).chr(130) => 'A', chr(195).chr(131) => 'A',
    chr(195).chr(132) => 'A', chr(195).chr(133) => 'A',
    chr(195).chr(135) => 'C', chr(195).chr(136) => 'E',
    chr(195).chr(137) => 'E', chr(195).chr(138) => 'E',
    chr(195).chr(139) => 'E', chr(195).chr(140) => 'I',
    chr(195).chr(141) => 'I', chr(195).chr(142) => 'I',
    chr(195).chr(143) => 'I', chr(195).chr(145) => 'N',
    chr(195).chr(146) => 'O', chr(195).chr(147) => 'O',
    chr(195).chr(148) => 'O', chr(195).chr(149) => 'O',
    chr(195).chr(150) => 'O', chr(195).chr(153) => 'U',
    chr(195).chr(154) => 'U', chr(195).chr(155) => 'U',
    chr(195).chr(156) => 'U', chr(195).chr(157) => 'Y',
    chr(195).chr(159) => 's', chr(195).chr(160) => 'a',
    chr(195).chr(161) => 'a', chr(195).chr(162) => 'a',
    chr(195).chr(163) => 'a', chr(195).chr(164) => 'a',
    chr(195).chr(165) => 'a', chr(195).chr(167) => 'c',
    chr(195).chr(168) => 'e', chr(195).chr(169) => 'e',
    chr(195).chr(170) => 'e', chr(195).chr(171) => 'e',
    chr(195).chr(172) => 'i', chr(195).chr(173) => 'i',
    chr(195).chr(174) => 'i', chr(195).chr(175) => 'i',
    chr(195).chr(177) => 'n', chr(195).chr(178) => 'o',
    chr(195).chr(179) => 'o', chr(195).chr(180) => 'o',
    chr(195).chr(181) => 'o', chr(195).chr(182) => 'o',
    chr(195).chr(182) => 'o', chr(195).chr(185) => 'u',
    chr(195).chr(186) => 'u', chr(195).chr(187) => 'u',
    chr(195).chr(188) => 'u', chr(195).chr(189) => 'y',
    chr(195).chr(191) => 'y',
    // Decompositions for Latin Extended-A
    chr(196).chr(128) => 'A', chr(196).chr(129) => 'a',
    chr(196).chr(130) => 'A', chr(196).chr(131) => 'a',
    chr(196).chr(132) => 'A', chr(196).chr(133) => 'a',
    chr(196).chr(134) => 'C', chr(196).chr(135) => 'c',
    chr(196).chr(136) => 'C', chr(196).chr(137) => 'c',
    chr(196).chr(138) => 'C', chr(196).chr(139) => 'c',
    chr(196).chr(140) => 'C', chr(196).chr(141) => 'c',
    chr(196).chr(142) => 'D', chr(196).chr(143) => 'd',
    chr(196).chr(144) => 'D', chr(196).chr(145) => 'd',
    chr(196).chr(146) => 'E', chr(196).chr(147) => 'e',
    chr(196).chr(148) => 'E', chr(196).chr(149) => 'e',
    chr(196).chr(150) => 'E', chr(196).chr(151) => 'e',
    chr(196).chr(152) => 'E', chr(196).chr(153) => 'e',
    chr(196).chr(154) => 'E', chr(196).chr(155) => 'e',
    chr(196).chr(156) => 'G', chr(196).chr(157) => 'g',
    chr(196).chr(158) => 'G', chr(196).chr(159) => 'g',
    chr(196).chr(160) => 'G', chr(196).chr(161) => 'g',
    chr(196).chr(162) => 'G', chr(196).chr(163) => 'g',
    chr(196).chr(164) => 'H', chr(196).chr(165) => 'h',
    chr(196).chr(166) => 'H', chr(196).chr(167) => 'h',
    chr(196).chr(168) => 'I', chr(196).chr(169) => 'i',
    chr(196).chr(170) => 'I', chr(196).chr(171) => 'i',
    chr(196).chr(172) => 'I', chr(196).chr(173) => 'i',
    chr(196).chr(174) => 'I', chr(196).chr(175) => 'i',
    chr(196).chr(176) => 'I', chr(196).chr(177) => 'i',
    chr(196).chr(178) => 'IJ',chr(196).chr(179) => 'ij',
    chr(196).chr(180) => 'J', chr(196).chr(181) => 'j',
    chr(196).chr(182) => 'K', chr(196).chr(183) => 'k',
    chr(196).chr(184) => 'k', chr(196).chr(185) => 'L',
    chr(196).chr(186) => 'l', chr(196).chr(187) => 'L',
    chr(196).chr(188) => 'l', chr(196).chr(189) => 'L',
    chr(196).chr(190) => 'l', chr(196).chr(191) => 'L',
    chr(197).chr(128) => 'l', chr(197).chr(129) => 'L',
    chr(197).chr(130) => 'l', chr(197).chr(131) => 'N',
    chr(197).chr(132) => 'n', chr(197).chr(133) => 'N',
    chr(197).chr(134) => 'n', chr(197).chr(135) => 'N',
    chr(197).chr(136) => 'n', chr(197).chr(137) => 'N',
    chr(197).chr(138) => 'n', chr(197).chr(139) => 'N',
    chr(197).chr(140) => 'O', chr(197).chr(141) => 'o',
    chr(197).chr(142) => 'O', chr(197).chr(143) => 'o',
    chr(197).chr(144) => 'O', chr(197).chr(145) => 'o',
    chr(197).chr(146) => 'OE',chr(197).chr(147) => 'oe',
    chr(197).chr(148) => 'R',chr(197).chr(149) => 'r',
    chr(197).chr(150) => 'R',chr(197).chr(151) => 'r',
    chr(197).chr(152) => 'R',chr(197).chr(153) => 'r',
    chr(197).chr(154) => 'S',chr(197).chr(155) => 's',
    chr(197).chr(156) => 'S',chr(197).chr(157) => 's',
    chr(197).chr(158) => 'S',chr(197).chr(159) => 's',
    chr(197).chr(160) => 'S', chr(197).chr(161) => 's',
    chr(197).chr(162) => 'T', chr(197).chr(163) => 't',
    chr(197).chr(164) => 'T', chr(197).chr(165) => 't',
    chr(197).chr(166) => 'T', chr(197).chr(167) => 't',
    chr(197).chr(168) => 'U', chr(197).chr(169) => 'u',
    chr(197).chr(170) => 'U', chr(197).chr(171) => 'u',
    chr(197).chr(172) => 'U', chr(197).chr(173) => 'u',
    chr(197).chr(174) => 'U', chr(197).chr(175) => 'u',
    chr(197).chr(176) => 'U', chr(197).chr(177) => 'u',
    chr(197).chr(178) => 'U', chr(197).chr(179) => 'u',
    chr(197).chr(180) => 'W', chr(197).chr(181) => 'w',
    chr(197).chr(182) => 'Y', chr(197).chr(183) => 'y',
    chr(197).chr(184) => 'Y', chr(197).chr(185) => 'Z',
    chr(197).chr(186) => 'z', chr(197).chr(187) => 'Z',
    chr(197).chr(188) => 'z', chr(197).chr(189) => 'Z',
    chr(197).chr(190) => 'z', chr(197).chr(191) => 's'
    );

    $string = strtr($string, $chars);
    unset($chars);

    return $string;
}

function removeSpecialChars($string)
{
	$regexJustLettersAndSpaces = "/[^ \w]+/";    //Remove all chars, except letters, numbers, spaces and _.
	$string = str_replace(array('_', '<br>', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'), " ", $string);
	$string = preg_replace($regexJustLettersAndSpaces, " ", $string);
    unset($regexJustLettersAndSpaces);

	return $string;
}

//$teste = "Àrtígôs cõm títúlòs grândé$#@%%$@&*(@*$_$$*$@@$s ê chéíós dé ácéntós, ápênàs1990 200w0s párâ fâzér á URL fícâr ênórmê!!!!????....";
//echo removeSpecialChars(removeAccents($teste)) . "<br>";

//Extracted from: http://php.net/manual/pt_BR/function.rmdir.php
function delTree($dir) 
{
    $files = glob( $dir . '*', GLOB_MARK );

    foreach( $files as $file ){
        if( substr( $file, -1 ) == '/' )
            delTree( $file );
        else
            unlink( $file );
    }
   
    if (is_dir($dir)) rmdir( $dir );
    unset($files);
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

function httpRequest($url) 
{
	$cURL = curl_init($url);
	curl_setopt($cURL, CURLOPT_RETURNTRANSFER, true);
	//curl_setopt($cURL, CURLOPT_FOLLOWLOCATION, true);
	$resultado = curl_exec($cURL);
	//$resposta = curl_getinfo($cURL, CURLINFO_HTTP_CODE);
	curl_close($cURL);
    unset($cURL);

	return $resultado;
}

$_POST['keywords'] = trim($_POST['keywords']);
$_POST['keywords'] = str_replace(" ", "+", $_POST['keywords']);

$originalSnippets = array();
$originalTotalWords = 0;

//Verificando qual a operação que a ferramenta deve realizar: agrupar resultados de mecanismos de buscas ou resultados de um arquivo json.
if ($_POST['operation'] == "local") {    //Grouping own database.
    //**************************************************
    //Extracting the results content from database file:
    //**************************************************
    $databaseFile = fopen($_POST['local_file'], "r");    //File codification: needs to be UTF-8.
    //$databaseFile = fopen("../../_import/files/data/json/noticias_esportivas_BestSports.json", "r");    //File codification: needs to be UTF-8.
    $content = "";

    if ($databaseFile) {
        while (!feof($databaseFile)) {
        	//$content .= strtolower( mb_convert_encoding(fgets($databaseFile), "UTF-8", mb_detect_encoding(fgets($databaseFile), "UTF-8, ISO-8859-1, ISO-8859-15", true)) );    //Para UTF-8.
        	$content .= fgets($databaseFile);
        }
    }

    fclose($databaseFile);
    unset($databaseFile);
    $resultPage = json_decode($content, true);

    for ($i = 0; $i < count($resultPage["data"]); $i++) {
        if (!isset($resultPage["data"][$i]['title'])) break;

        $originalSnippets[] = array('title' => $resultPage["data"][$i]['title'], 
            'link' => $resultPage["data"][$i]['url'], 
            'description' => $resultPage["data"][$i]['abstract'],
            'category' => $resultPage["data"][$i]['category'],
            'num_words' => count(explode(" ", $resultPage["data"][$i]['title'])) + count(explode(" ", $resultPage["data"][$i]['abstract'])));

        //$originalTotalWords += count( explode(" ", $resultPage["data"][$i]['abstract']) );
    }
}
else {    //search
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
	                         'description' => $resultPage['results'][$i]['kwic'],
                             'num_words' => count(explode(" ", $resultPage['results'][$i]['title'])) + count(explode(" ", $resultPage['results'][$i]['kwic'])));

	                     //$originalTotalWords += count( explode(" ", $resultPage['results'][$i]['kwic']) );
	                 }

	                 $page += 10;
	                 //echo "<br><br>" . "http://www.faroo.com/api?q=" . $_POST['keywords'] . "&start=" . $page . "&length=10&l=en&src=web&f=json&key=4jRpMYuh2FxzDW03lYlimvAeGEY_" . "<br><br><br><br>";
	                 sleep(1);
                     ob_flush();
                     flush();
	             }
	             else break;

                 unset($resultPage);
                 unset($url);
                 unset($link);
	         }
	        //**************************************
	    } break;
	    default : {
	        //****************************************************
	        //Extracting the snippets content from DuckDuckGo API:
	        //****************************************************
	        $link = "http://api.duckduckgo.com/?q=" . trim($_POST['keywords']) . "&format=json&pretty=1";
	        $url = httpRequest($link);
	        $url = str_replace('&quot;', '"', $url);
	        $resultPage = json_decode($url, true);

	        for ($i = 0; $i < count($resultPage['RelatedTopics']); $i++) {
	            if (!isset($resultPage['RelatedTopics'][$i]['Result'])) break;

	            $dom = new simple_html_dom($resultPage['RelatedTopics'][$i]['Result']);
	            $title_res = $dom->find('a', 0)->plaintext;
	            $text_res = substr_replace($resultPage['RelatedTopics'][$i]['Text'], "", 0, strlen($dom->find('a', 0)->plaintext));

	            $originalSnippets[] = array('title' => $title_res, 
	                'link' => $resultPage['RelatedTopics'][$i]['FirstURL'], 
	                'description' => $text_res,
                    'num_words' => count(explode(" ", $title_res)) + count(explode(" ", $text_res)));

	            //$originalTotalWords += count( explode(" ", $text_res) );
	        }
	        //**************************************

            unset($dom);
            unset($title_res);
            unset($text_res);
            unset($resultPage);
            unset($url);
            unset($link);
	    }
	}
}


//**************************************
//Knowledge Discovery in Databases:
//**************************************
$stopwordsList = array();
$startwords = array();
$processedSnippets = $originalSnippets;
          
//if (strcasecmp("search", $_POST['operation']) == 0) $stopwordsFilePath = "../../_import/files/stopwords_en-us";
//else {
//	$stopwordsFilePath = "../../_import/files/stopwords-RSparc.txt";
//	//$stopwordsFilePath = "../../_import/files/esportes.txt";
//}
$stopwordsFilePath = "../../_import/files/stopwords/stopwords-RSparc.txt";

if ($_POST['load_preprocessed_file'] == 'yes') {
    $return = array('total' => count($originalSnippets));
    $return['categories'] = $resultPage["categories"];
    $return['snippets'] = $originalSnippets;
    $end = time();
    $time = $end-$begin;
    $return['time'] = $time;

    echo json_encode($return, JSON_PRETTY_PRINT);
    unset($return);
    unset($end);
    unset($time);
    gc_collect_cycles();

    exit;
}

//Jogando as stopwords em um array (pq vai ser lido muitas vezes):
if (($stopwordsListFile = fopen($stopwordsFilePath, "r"))) {
    while (($line = fgets($stopwordsListFile)) !== false) {
    	//$w = trim(strtolower($line));
		$w = removeSpecialChars(removeAccents(trim(strtolower( mb_convert_encoding($line, "UTF-8", mb_detect_encoding($line, "UTF-8, ISO-8859-1, ISO-8859-15", true)) ))));    //Para UTF-8.
		//$w = trim(strtolower( mb_convert_encoding($line, "ISO-8859-1", mb_detect_encoding($line, "UTF-8, ISO-8859-1, ISO-8859-15", true)) ));    //Para ISO-8859-1.
		$stopwordsList[] = $w;
    }
}
        
fclose($stopwordsListFile);
unset($stopwordsFilePath);
unset($stopwordsListFile);
sort($stopwordsList);
$processedTotalWords = 0;
        
foreach ($processedSnippets as $indexSnippet => $snippet) {
  	//Remoção das pontuações:
  	$snippet['title'] = removeSpecialChars(removeAccents($snippet['title']));
 	$splitedWords = explode(" ", $snippet['title']);
  	
    //Remoção de stopwords:
  	for ($i = 0; $i < count($splitedWords); ++$i) {
  		$splitedWords[$i] = strtolower(trim($splitedWords[$i]));

  		for ($j = 0; $j < count($stopwordsList); ++$j) {
  			if (strcasecmp($splitedWords[$i], $stopwordsList[$j]) == 0) {
  				$splitedWords[$i] = "";
  				break;
  			}
  		}
  	}
                     
 	//Contagem de frequência dos termos:
 	//$words = explode(" ", $snippet['title']);
 	$words = $splitedWords;
 	$processedTotalWords += count($words);
           
 	foreach ($words as $word) {
 		$word = trim($word);

 		if (strlen($word) <= 3) continue;

 		if ($_POST['stemmer'] == "yes") $word = PorterStemmer::Stem($word);
 		$newWord = true;
                
	 	for ($i = 0; $i < count($startwords); $i++) {
	 		if (strcasecmp($startwords[$i]['word'], $word) == 0) {
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
 	
	//Remoção das pontuações:
	$snippet['description'] = removeSpecialChars(removeAccents($snippet['description']));
	$splitedWords = explode(" ", $snippet['description']);
  	
    //Remoção de stopwords:
  	for ($i = 0; $i < count($splitedWords); ++$i) {
  		$splitedWords[$i] = strtolower(trim($splitedWords[$i]));

  		for ($j = 0; $j < count($stopwordsList); ++$j) {
  			if (strcasecmp($splitedWords[$i], $stopwordsList[$j]) == 0) {
  				$splitedWords[$i] = "";
  				break;
  			}
  		}
  	}

 	//Contagem de frequência dos termos:
 	//$words = explode(" ", $snippet['description']);
 	$words = $splitedWords;
 	$processedTotalWords += count($words);
           
 	foreach ($words as $word) {
 		$word = trim($word);
 		
 		if (strlen($word) <= 3) continue;
 		
 		if ($_POST['stemmer'] == "yes") $word = PorterStemmer::Stem($word);
 		$newWord = true;
                
	 	for ($i = 0; $i < count($startwords); $i++) {
	 		if (strcasecmp($startwords[$i]['word'], $word) == 0) {
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


//for ($i = 0; $i < count($startwords); ++$i) {    //TF
//    $numTotalWords = 0;
//
//    foreach ($startwords[$i]['snippets'] as $indexSnippet) {
//        $numTotalWords += $processedSnippets[$indexSnippet['indexSnippet']]['num_words'];
//    }
//
//    $startwords[$i]['freq'] = $startwords[$i]['freq'] / $numTotalWords;
//}

//**************************************************
//CONVERSÃO DA MÉTRICA DE FREQUÊNCIA UTILIZADA:
if ($_POST['frequency_type'] == 'tf-idf') {    //TF-IDF
    for ($i = 0; $i < count($startwords); ++$i) {    //TF
        $startwords[$i]['freq'] = $startwords[$i]['freq'] * log10(count($processedSnippets) / count($startwords[$i]['snippets']));
    }
}
//**************************************************

//**************************************************
//NORMALIZAÇÃO DE FREQUEÊNCIAS (ENTRE 0 E 1):
if ($_POST['normalize'] == 'yes') {
    $maxFreq = 0;

    for ($i = 0; $i < count($startwords); ++$i) {
        if ($startwords[$i]['freq'] > $maxFreq) $maxFreq = $startwords[$i]['freq'];
    }

    for ($i = 0; $i < count($startwords); ++$i) {    //Normalização.
        $startwords[$i]['freq'] = $startwords[$i]['freq'] / $maxFreq;
    }
}
//**************************************************

//**************************************************
//CORTE DE FREQUÊNCIAS:
if ($_POST['luhn_cut'] == "yes") {
    $newArray = array();
    //echo "ANTES: " . count($startwords) . "\n";

    foreach ($startwords as $key => $startword) {
        //if ( ($startword['freq'] <= $_POST['upper_cut']) ) array_push($newArray, $startword);
        if ( ($startword['freq'] >= $_POST['lower_cut']) && ($startword['freq'] <= $_POST['upper_cut']) ) array_push($newArray, $startword);
        //if ($startword['freq'] > $_POST['lower_cut'] && $startword['freq'] < $_POST['upper_cut']) array_push($newArray, $startwords[$key]);
        unset($startwords[$key]);
    }

    unset($startwords);
    $startwords = $newArray;
    unset($newArray);
    //echo "DEPOIS: " . count($startwords) . "\n";
}
//**************************************************

//**************************************************
//ORDENAÇÃO ALFABÉTICA DO ARRAY DE PALAVRAS:
if ($_POST['zipf_curve'] == "yes") {
    function cmp_by_freq($b, $a) {
        if ($a['freq'] > $b['freq']) return 1;
        else if ($a['freq'] < $b['freq']) return -1;
        else return 0;
    }

    usort($startwords, "cmp_by_freq");
    echo "ZIPF CURVE:\n\n";

    foreach ($startwords as $key => $startword) {
        echo ($key+1) . ";" . $startword['word'] . ";" . $startword['freq'] . ";" . count($startword['snippets']) . "\n";
    }

    echo "\n\n";
}
else sort($startwords);
//**************************************************

//**************************************************
//TESTE (VALORES ESTATÍSTICOS):
if ($_POST['teste'] == "yes") {
    $totalFreq = 0;
    $minFreq = 2147483647;    //Maior valor inteiro do PHP.
    $maxFreq = 0;
    $medFreq = 0;
    $var = 0;

    echo "ESTATISTICAS (" . $_POST['lower_cut'] . " - " . $_POST['upper_cut'] . "):\nnº termos: " . count($startwords) . "\n";

    //MÉDIA DE FREQUÊNCIAS:
    foreach ($startwords as $key => $startword) {
        $totalFreq += $startword['freq'];
        if ($startword['freq'] < $minFreq) $minFreq = $startword['freq'];
        if ($startword['freq'] > $maxFreq) $maxFreq = $startword['freq'];
    }

    $medFreq = $totalFreq/count($startwords);

    echo ">>> FREQUENCIA:\nmin: " . $minFreq . "\n" . "med: " . $medFreq . "\n" . "max: " . $maxFreq . "\n";

    //VARIÂNCIA E DESVIO PADRÃO:
    foreach ($startwords as $key => $startword) {
        $var += pow($startword['freq']-$medFreq, 2);
    }

    $var = $var/( count($startwords)-1 );
    $dp = sqrt($var);

    echo "VAR: " . $var . "\n" . "DP: " . $dp . "\n\n";
}
//**************************************************

$return = array('total' => count($originalSnippets));
$return['categories'] = $resultPage["categories"];
//gzcompress($return, 9);

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

    //$return['pex_matrix'] = nl2br($return['pex_matrix']);
}
else $return['pex_matrix'] = null;
//**************************************



$end = time();
$time = $end-$begin;
$return['time'] = $time;

//echo gzencode(json_encode($return));
echo json_encode($return, JSON_PRETTY_PRINT);
unset($return);
unset($end);
unset($time);
gc_collect_cycles();

?>
