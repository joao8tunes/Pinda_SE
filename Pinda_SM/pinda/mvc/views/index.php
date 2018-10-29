<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=0.6">
    <link rel="icon" href=<?php echo $_SESSION['importDir'] . "img/icone_superior_pinda.png" ?> type="image/x-icon">
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "css/bootstrap.css" ?>>
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "css/bootswatch.min.css" ?>>
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "css/styles.css" ?>>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/jquery-2.2.4.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/bootstrap.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/bootswatch.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/cookies.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/search.js" ?>></script>
    <title>Pinda</title>
  </head>
  <body>
    <?php if (session_id() == '' || session_status() == PHP_SESSION_NONE) require("_config/session.php"); ?>
  
    <div class="col-md-12">  

      <div style="margin-top: 140px; margin-bottom: 150px; width: 500px; display: block; margin-left: auto; margin-right: auto;">
        <div style="width: 280; display: block; margin-left: 140px; margin-right: auto;">
          <img src="<?php echo $_SESSION['importDir'] . "img/logo_pinda.png" ?>" width="200px;" title="Pinda">
        </div>

        <div class="row" style="margin-top: 20px;">
          <div>
            <div class="input-group">
              <input id="keywords" name="q" type="text" class="form-control" placeholder="Procurar por..." autofocus>
              <span class="input-group-btn">
                <button id="btn_search" class="btn btn-default" type="button">Buscar</button>
              </span>
            </div><!-- /input-group -->
          </div><!-- /.col-lg-6 -->
        </div><!-- /.row -->
      </div>
    </div>

    <footer class="footer">
      <div class="container">
              <div class="row">
          <div class="col-lg-12">

            <ul class="list-unstyled">
              <li><a href="http://fct.unesp.br/">FCT/UNESP</a></li>
              <li><a href="http://www.faroo.com/hp/api/api.html">Faroo - Free API</a></li>
              <li><a href="https://duckduckgo.com/api">DuckDuckGo - Instant Answer API</a></li>
              <li><a href="http://vicg.icmc.usp.br/infovis2/PEx">Projection Explorer (PEx)</a></li>
              <li><a href="https://github.com/thomaspark/bootswatch/">GitHub</a></li>
            </ul>
            <p>Desenvolvido por <a href="http://lattes.cnpq.br/3035821945210401" rel="nofollow">João Antunes</a>. Entre em contato em <a href="mailto:j.antunes.cc@gmail.com">j.antunes.cc@gmail.com</a>.</p>
            <!--<p>Code released under the <a href="https://github.com/thomaspark/bootswatch/blob/gh-pages/LICENSE">MIT License</a>.</p>-->
            <p>Design baseado no <a href="http://getbootstrap.com" rel="nofollow">Bootstrap</a>, com ícones do <a href="http://fortawesome.github.io/Font-Awesome/" rel="nofollow">Font Awesome</a> e fontes do <a href="http://www.google.com/webfonts" rel="nofollow">Google</a>.</p>

          </div>
        </div>
      </div>
    </footer>    

    <script type="text/javascript">
      if (readCookie("api") == "") createCookie("api", "ddg", 1);

      //EVENTOS:   
      $("#keywords").keyup(function(event){
          if(event.keyCode == 13){
              $("#btn_search").click();
          }
      });

      $("#btn_search").click(function () {
        if ($('#keywords').val().trim() != "") requisitar('', {c: 'sch', o: 'sch', q: $('#keywords').val().trim(), a: readCookie("api")}, 'get');
      });
    </script>
  </body>
</html>
