<?php 

/**
* 
*/
class C_Search
{
	
	public function loadIndexPage()
	{
		require_once($_SESSION['vDir'] . "index.php");
	}

	public function loadSearchPage()
	{
		require_once($_SESSION['vDir'] . "search.php");
	}

}

?>