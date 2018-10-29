<?php

//ini_set('display_errors', 1); ini_set('log_errors', 1); ini_set('error_log', dirname(__FILE__) . '/error_log.txt'); error_reporting(E_ALL);
if (session_id() == '' || session_status() == PHP_SESSION_NONE) require("_config/session.php");

if (!isset($_GET['c'])) $_GET['c'] = "";

switch ($_GET['c']) {
	case 'sch' : {
        $C_Search = new C_Search();
    	$C_Search->loadSearchPage();		
	} break;
    default : {
    	$C_Search = new C_Search();
    	$C_Search->loadIndexPage();
    }
}

?>
