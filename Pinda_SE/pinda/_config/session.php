<?php

session_start();

$_SESSION['host'] = "http://" . gethostbyname(gethostname());
$_SESSION['siteURL'] = $_SESSION['host'] . "/pinda";
$_SESSION['searchEngineURL'] = $_SESSION['siteURL'] . "/mvc/views/search_engine.php";
$_SESSION['servletURL'] = $_SESSION['host'] . ":8080/Pinda/Servlet";
// $_SESSION['host'] = "http://175d.fct.unesp.br/~cc1305093";
// $_SESSION['siteURL'] = $_SESSION['host'] . "/pinda";
// $_SESSION['searchEngineURL'] = $_SESSION['siteURL'] . "/mvc/views/search_engine.php";
// $_SESSION['servletURL'] = "http://175d.fct.unesp.br:8080/cc1305093/Pinda/Servlet";
$_SESSION['rootDir'] = str_replace(DIRECTORY_SEPARATOR . "_config", "", dirname(__FILE__));
$_SESSION['importDir'] = "_import/";
$_SESSION['configDir'] = $_SESSION['rootDir'] . "/_config/";

$_SESSION['domainDir'] = $_SESSION['rootDir'] . "/_domain/";
$_SESSION['mDir'] = $_SESSION['rootDir'] . "/mvc/models/";
$_SESSION['vDir'] = $_SESSION['rootDir'] . "/mvc/views/";
$_SESSION['cDir'] = $_SESSION['rootDir'] . "/mvc/controllers/";

//Importing controllers:
require($_SESSION['cDir'] . "/C_Search.class.php");

//Importing models:
//require_once($_SESSION['mDir'] . "/M_User.class.php");

//Importing domain:
//require_once($_SESSION['domainDir'] . "/User.class.php");


?>
