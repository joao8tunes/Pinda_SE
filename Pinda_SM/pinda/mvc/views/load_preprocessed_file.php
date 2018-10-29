<?php

//ini_set('display_errors', 1); ini_set('log_errors', 1); ini_set('error_log', dirname(__FILE__) . '/error_log.txt'); error_reporting(E_ALL);
header("Access-Control-Allow-Origin: *");
//header('Content-Type: text/html; charset=utf-8');
header('Content-Type: application/json;charset=ISO-8859-1');
header('Content-Encoding: gzip');
ini_set('max_execution_time', 0);    //Removendo limite de tempo de execução do PHP.
ob_start("ob_gzhandler");

$begin = time();

if (!isset($_POST['database']) || $_POST['database'] == 1) $databaseFile = fopen("../../_import/files/data/json/mapeamento_sistematico_AnaliseSentimentoBaseadaAspecto_[ReadyToView].json", "r");    //File codification: needs to be UTF-8.
else $databaseFile = fopen("../../_import/files/data/json/mapeamento_sistematico_SemanticaMineracaoTextos_[ReadyToView].json", "r");    //File codification: needs to be UTF-8.

$return = "";

while (!feof($databaseFile)) {
    $return .= fgets($databaseFile);
}

fclose($databaseFile);
$end = time();
$time = $end-$begin;
echo $return;
unset($return);
unset($end);
unset($time);
gc_collect_cycles();

?>
