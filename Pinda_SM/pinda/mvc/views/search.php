<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Pinda</title>
    <!-- Include meta tag to ensure proper rendering and touch zooming -->
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href=<?php echo $_SESSION['importDir'] . "img/icone_superior_pinda.png" ?> type="image/x-icon">
    <!-- Bootstrap -->
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "css/bootstrap.css" ?>>
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "css/bootswatch.min.css" ?>>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "api/bootstrap-select-master/dist/css/bootstrap-select.min.css" ?>>
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "api/vakata-jstree-5bece58/dist/themes/default/style.min.css" ?>>
    <link rel="stylesheet" type="text/css" href=<?php echo $_SESSION['importDir'] . "css/styles.css" ?>>
    <style type="text/css">
      @-moz-document url-prefix() { 
        #visualizations_space { height: 88vh; }
        #sunburst { top: calc(45vh - 29px); }
        #vis_sunburst { height: calc(43vh - 4px); }
        #treemap { top: calc(45vh - 29px); }
        #vis_treemap { height: calc(43vh - 4px); }
        #vis_scatterplot { height: calc(42vh - 33px); }
        #vis_snippets { height: calc(88vh - 33px); }
        #directories { top: calc(40vh); }
        #vis_directories { height: calc(48vh - 33px); }
        .vis_thumbnail_LT5 { height: calc(90vh - 37px) !important; }
      }
    </style>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/jquery-2.2.4.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/jquery.blockUI.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/bootstrap.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/bootswatch.js" ?>></script>
    <!--Wijmo Widgets JavaScript-->
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/d3.v4.0.0-alpha.44.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/d3.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "api/vakata-jstree-5bece58/dist/jstree.min.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/visualizations.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "api/jquery-simulate-master/jquery.simulate.js" ?>></script>
    <script type="text/javascript" src=<?php echo $_SESSION['importDir'] . "js/cookies.js" ?>></script>
  </head>
  <body>

  <?php if (session_id() == '' || session_status() == PHP_SESSION_NONE) require("_config/session.php"); ?>

    <div class="navbar navbar-default navbar-fixed-top">
      <div class="">
        <div class="navbar-header">
          <a href="<?php echo $_SESSION['siteURL'] ?>" class="navbar-brand" style="margin-top: -10px; margin-right: -15px;"><img src="<?php echo $_SESSION['importDir'] . "img/logo_pinda.png" ?>" height="40px;" title="Ir para a página inicial do Pinda"></a>
          <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar-main">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
        </div>
        <div class="navbar-collapse collapse" id="navbar-main">
          <ul class="nav navbar-nav">
            <div class="navbar-form navbar-left">
              <input id="keywords" type="text" class="form-control" style="width: 650px;" placeholder="Procurar por..." value="" autofocus>
              <input id='btn_search' type="button" value="Buscar" class="btn btn-primary" ><br>
            </div>
          </ul>

          <ul class="nav navbar-nav navbar-right" style="margin-right: 5px;">
            <li class="dropdown">
              <a class="dropdown-toggle" data-toggle="dropdown" href="#" id="buscador">Buscador <span class="caret"></span></a>
              <ul class="dropdown-menu" aria-labelledby="buscador">
                <li><a href="" class="api" name="duckduckgo">DuckDuckGo</a></li>
                <li><a href="" class="api" name="faroo">Faroo</a></li>
              </ul>
            </li>
            <li><a id="img_api_selected" href="" target="_blank"></a></li>
            <!--<li><a href="" target="_blank">Sobre</a></li>-->
          </ul>

        </div>
      </div>
    </div>


    <div class="col-md-12">

      <div id="visualizations_space">
        <div id="snippets" style="display: none;">
          <div class="windowTitle" title="Snippets">Snippets</div>
          <div id="vis_snippets"></div>
        </div>
        <div id="sunburst" style="display: none;">
          <div class="windowTitle" title="Sunburst">Sunburst</div>
          <div id="vis_sunburst"></div>
        </div>
        <div id="treemap" style="display: none;">
            <div class="windowTitle" title="Treemap">Treemap</div>
          <div id="vis_treemap"></div>
        </div>
        <div id="scatterplot" style="display: none;">
          <div class="windowTitle" title="Scatterplot (Projeção Multidimensional)">Scatterplot (Projeção Multidimensional)</div>
          <div id="vis_scatterplot"></div>
        </div>
        <div id="directories" style="display: none;">
          <div class="windowTitle" title="Diretórios">Diretórios</div>
          <div id="vis_directories" class="demo"></div>
        </div>
        <div id="thumbnail" style="display: none;">
          <div class="windowTitle" title="Miniatura">Miniatura</div>
            <div id="vis_thumbnail"></div>
        </div>
      </div>

      <footer style="margin-top: 2px;">
        <div class="row">
          <div class="col-lg-12">

            <ul class="list-unstyled">
              <!--<li class="pull-right"><a href="#top">Voltar ao topo</a></li>-->
              <li class="pull-right"><a id="btn_local_2" style="cursor: pointer;">* Base 2 (Semântica)</a></li>
              <li class="pull-right"><a id="btn_local_1" style="cursor: pointer;">* Base 1 (Análise de Sentimentos)</a></li>
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

      </footer>
    </div>

    <script type="text/javascript">
      if ("<?php echo $_GET['a'] ?>" != null && "<?php echo $_GET['a'] ?>" != "") {
        eraseCookie("api");
        if ("<?php echo $_GET['a'] ?>" == 'foo') createCookie("api", "foo", 1);
        else createCookie("api", "ddg", 1);
      }
      else if (readCookie("api") == "") createCookie("api", "ddg", 1);
        
      $('#keywords').val("<?php echo $_GET['q'] ?>".trim());

      $('#btn_local_1').click(function ()
      {
        process_local_file(1);
      });

      $('#btn_local_2').click(function ()
      {
        process_local_file(2);
      });      

      //AGRUPAMENTO LOCAL:
      function process_local_file(database_number) {
        var requestPinda = '';
        var requestTomcat = '';
        var notfound = false, error = false;
        var servletURL = "<?php echo $_SESSION['servletURL'] ?>";
        var searchEngineURL = "<?php echo $_SESSION['searchEngineURL'] ?>";
        var query = $('#keywords').val().trim().replace(/\s/g,"+");
          
        if (query != '') {
          window.history.pushState("string", "Title", "?c=sch&q=" + $('#keywords').val().trim() + "&a=" + readCookie("api"));
          $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Processando...</h4>'});
           
          $.ajax({    //LENDO O ARQUIVO JSON LOCAL E MONTANDO A MATRIX DE FREQUÊNCIAS:
            type: "POST",
            dataType: "json",
            cache: false,
            url: searchEngineURL,
            data: {
              'operation' : "local",
              'keywords' : query,
              'api' : readCookie("api"),
              'database' : database_number
            },
            success: function(data) {
              var index = 0;

              if (data == null) error = true;
              else {
                requestPinda = '<div style="font-size: 15px; text-align: right;"><b>' + data['total'] + " resultados (" + data['time'] + " segundos)</b><br></div>";

                /************************/
                //SNIPPETS:
                /************************/
                if (data['snippets'] != null && data['snippets'].length > 0) {
                  $('#visualizations_space').contents().show();
                  setNumOfSnippets(data['snippets'].length);
                    
                  for (var i = 0; i < data['snippets'].length; i++) {
                    ++index;
                    //$("#iframeVisualizations").contents().find("#vis_snippets").append('<div class="snippet"><b> Posição: </b>' + index + '<br><b>Título: </b>' + data['snippets'][i]['title'] + '<br><b> Link: </b>' + data['snippets'][i]['link'] + '<br><b> Resumo: </b>' + data['snippets'][i]['description'] + '</div><br>');
                    requestPinda += '<div class="snippet" id="snp_' + index + '"><b> Posição: </b>' + index + '<br><b>Título: </b>' + data['snippets'][i]['title'] + '<br><b> Link: </b><span class="snippeturl">' + data['snippets'][i]['link'] + '</span><br><b> Resumo: </b>' + data['snippets'][i]['description'] + '</div>';
                  }

                  //console.log(data['pex_matrix']);

                  categories = "";

                  for (i = 0; i < data['snippets'].length; ++i) {
                    categories += $.trim(data['snippets'][i]['category']);
                    if (i+1 < data['snippets'].length) categories += ";";
                  }

                  $.ajax({    //ENVIANDO A MATRIZ DE FREQUÊNCIAS AO SERVIDOR TOMCAT PARA REALIZAR A CLUSTERIZAÇÃO:
                    type: "POST",
                    dataType: "json",
                    cache: false,
                    //url: servletURL,
                    url: "mvc/views/load_preprocessed_file.php",
                    data: {
                      'matrix' : data['pex_matrix'],
                      'categories' : categories,
                      'database' : database_number
                    },
                    success: function(data2) {
                      //console.log(categories);

                      if (data2 == null || data2 == 'null') error = true;    //$("#vis_snippets").html("<h2>Erro:</h2>Não foi possível realizar a pesquisa.");
                      else {
                        //$("#iframeVisualizations").contents().find("#vis_snippets").append('<b>Request: </b>' + data2 + '<br>');
                        requestTomcat = data2;
                        //console.log(data['pex_matrix']);
                        var temp = { arr : requestTomcat };
                        var obj = $.extend(true, {}, temp);
                        var r1 = obj.arr;

                        loadThumbnail(data['snippets'][0]['link']);
                        loadSnippets(requestPinda);
                        loadSunburst(r1);    //Utiliza o json original, mas o 'loadTreemap' modifica o json; por isso precisa clonar a variavel, e não apenas passar referência.
                        loadTreemap(requestTomcat);
                        loadScatterplot(requestTomcat, data['categories']);
                        loadDirectories(requestTomcat);
                      }

                      $('#visualizations_space').unblock();
                    }
                  });
                }
                else notfound = true;
              }

              if (error) {
                  $('#visualizations_space').contents().hide();
                  $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Erro ao buscar por "' + $('#keywords').val().trim() + '"!</h4><h4>Tente outro termo.</h4>'});
              }
                
              if (notfound) {
                  $('#visualizations_space').contents().hide();
                  $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Nenhum resultado encontrado para "' + $('#keywords').val().trim() + '"!</h4><h4>Tente outro termo.</h4>'});
              }
            }
          });
        }
        else {
          window.history.pushState("string", "Title", "?c=sch&q=" + $('#keywords').val().trim() + "&a=" + readCookie("api"));
          $('#visualizations_space').contents().hide();
          $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Vamos lá, me pergunte algo! :-(</h4>'});
        }        
      }

      $('#btn_search').click(function ()
      {
        var requestPinda = '';
        var requestTomcat = '';
        var notfound = false, error = false;
        var servletURL = "<?php echo $_SESSION['servletURL'] ?>";
        var searchEngineURL = "<?php echo $_SESSION['searchEngineURL'] ?>";
        var query = $('#keywords').val().trim().replace(/\s/g,"+");
          
        if (query != '') {
          window.history.pushState("string", "Title", "?c=sch&q=" + $('#keywords').val().trim() + "&a=" + readCookie("api"));
          $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Processando...</h4>'});
           
          $.ajax({    //REALIZANDO A BUSCA PELA API E MONTANDO MATRIX DE FREQUÊNCIAS:
            type: "POST",
            dataType: "json",
            cache: false,
            url: searchEngineURL,
            data: {
              'operation' : 'search',
              'keywords' : query,
              'api' : readCookie("api")
            },
            success: function(data) {
              var index = 0;

              if (data == null) error = true;
              else {
                requestPinda = '<div style="font-size: 15px; text-align: right;"><b>' + data['total'] + " resultados (" + data['time'] + " segundos)</b><br></div>";

                /************************/
                //SNIPPETS:
                /************************/
                if (data['snippets'] != null && data['snippets'].length > 0) {
                  $('#visualizations_space').contents().show();
                  setNumOfSnippets(data['snippets'].length);
                    
                  for (var i = 0; i < data['snippets'].length; i++) {
                    ++index;
                    //$("#iframeVisualizations").contents().find("#vis_snippets").append('<div class="snippet"><b> Posição: </b>' + index + '<br><b>Título: </b>' + data['snippets'][i]['title'] + '<br><b> Link: </b>' + data['snippets'][i]['link'] + '<br><b> Resumo: </b>' + data['snippets'][i]['description'] + '</div><br>');
                    requestPinda += '<div class="snippet" id="snp_' + index + '"><b> Posição: </b>' + index + '<br><b>Título: </b>' + data['snippets'][i]['title'] + '<br><b> Link: </b><span class="snippeturl">' + data['snippets'][i]['link'] + '</span><br><b> Resumo: </b>' + data['snippets'][i]['description'] + '</div>';
                  }

                  $.ajax({    //ENVIANDO MATRIZ DE FREQUÊNCIAS AO SERVIDOR TOMCAT PARA REALIZAR A CLUSTERIZAÇÃO:
                    type: "POST",
                    dataType: "json",
                    cache: false,
                    url: servletURL,
                    data: {
                      'matrix' : data['pex_matrix']
                    },
                    success: function(data2) {
                      if (data2 == null) error = true;    //$("#vis_snippets").html("<h2>Erro:</h2>Não foi possível realizar a pesquisa.");
                      else {
                        //$("#iframeVisualizations").contents().find("#vis_snippets").append('<b>Request: </b>' + data2 + '<br>');
                        requestTomcat = data2;
                        //console.log(data['pex_matrix']);
                        var temp = { arr : requestTomcat };
                        var obj = $.extend(true, {}, temp);
                        var r1 = obj.arr;

                        loadThumbnail(data['snippets'][0]['link']);
                        loadSnippets(requestPinda);
                        loadSunburst(r1);    //Utiliza o json original, mas o 'loadTreemap' modifica o json; por isso precisa clonar a variavel, e não apenas passar referência.
                        loadTreemap(requestTomcat);
                        loadScatterplot(requestTomcat);
                        loadDirectories(requestTomcat);
                        $('#visualizations_space').unblock();
                      }
                    }
                  });
                }
                else notfound = true;
              }

              if (error) {
                  $('#visualizations_space').contents().hide();
                  $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Erro ao buscar por "' + $('#keywords').val().trim() + '"!</h4><h4>Tente outro termo.</h4>'});
              }
                
              if (notfound) {
                  $('#visualizations_space').contents().hide();
                  $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Nenhum resultado encontrado para "' + $('#keywords').val().trim() + '"!</h4><h4>Tente outro termo.</h4>'});
              }
            }
          });
        }
        else {
          window.history.pushState("string", "Title", "?c=sch&q=" + $('#keywords').val().trim() + "&a=" + readCookie("api"));
          $('#visualizations_space').contents().hide();
          $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Vamos lá, me pergunte algo! :-(</h4>'});
        }
      });

      $("#keywords").keyup(function(event){
          if(event.keyCode == 13){
              $("#btn_search").click();
          }
      });
        
        //EVENTOS:
        $("#btn_search").click();
        
        $('.api').click(function () {
            var path = "<?php echo $_SESSION['importDir'] ?>";
            
            $('.api').removeClass('api_selected');
            $(this).addClass('api_selected');
            eraseCookie("api");

            switch ($(this).attr("name")) {
                case 'faroo' : {
                    createCookie("api", "foo", 1);
                    $('#img_api_selected').attr("href", "http://www.faroo.com/hp/api/api.html");
                    $('#img_api_selected').html('<img src="' + path + 'img/faroo_icon.png" height="30" title="Faroo API">');
                } break;
                default : {
                    createCookie("api", "ddg", 1);
                    $('#img_api_selected').attr("href", "https://duckduckgo.com/api");
                    $('#img_api_selected').html('<img src="' + path + 'img/duckduckgo_icon.png" height="30" title="DuckDuckGo API">');
                }
            }

            window.history.pushState("string", "Title", "?c=sch&q=" + $('#keywords').val().trim() + "&a=" + readCookie("api"));
            $('#buscador').click();
            
            return false;
        });
        
        $('#img_api_selected').click(function () {
            window.open($('#img_api_selected').attr("href"), '_blank');
        });

        $('#buscador').click();
        if (readCookie("api") == 'ddg') $('a[name=duckduckgo]').click();
        else $('a[name=faroo]').click();
    </script>
  </body>
</html>
