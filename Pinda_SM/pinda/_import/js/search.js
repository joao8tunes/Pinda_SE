      $('#btn_search').click(function ()
      {
        var requestPinda = '';
        var requestTomcat = '';
        var notfound = false, error = false;
        var servletURL = "<?php echo $_SESSION['servletURL'] ?>";
        var searchEngineURL = "<?php echo $_SESSION['searchEngineURL'] ?>";
        var api = "<?php echo $_SESSION['api'] ?>";
          
        if ($('#keywords').val().trim() != '') {
          window.history.pushState("string", "Title", "?c=search&m=search&q=" + $('#keywords').val().trim() + "&api=" + $('.api_selected').attr('name'));
          $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Processando...</h4>'});
       
          //console.log("1 - " + $('#keywords').val());
           
          $.ajax({    //REALIZANDO A BUSCA PELO FAROO E MONTANDO MATRIX DE FREQUÊNCIAS:
            type: "POST",
            dataType: "json",
            url: searchEngineURL,
            data: {
              'keywords' : $('#keywords').val(),
              'api' : $('.api_selected').attr('name')
            },
            success: function(data) {
              var index = 0;

              if (data == null) error = true;
              else {
                requestPinda = '<div style="font-size: 12px;">' + data['total'] + " resultado(s) (" + data['time'] + " segundo(s))<br></div>";

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

                        setSourceClick('Initializing...');
						loadThumbnail(data['snippets'][0]['link']);
                        loadSnippets(requestPinda);
                        loadSunburst(r1);    //Utiliza o json original, mas o 'loadTreemap' modifica o json; por isso precisa clonar a variavel, e não apenas passar referência.
                        loadTreemap(requestTomcat);
                        loadScatterplot(requestTomcat);
                        loadDirectories(requestTomcat);
                        setSourceClick(null);
                        $('#visualizations_space').unblock();
                      }
                    }
                  });
                }
                else notfound = true;
              }

              if (error) {
                  $('#visualizations_space').contents().hide();
                  $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Erro ao buscar por "' + $('#keywords').val() + '"!</h4><h4>Tente outro termo.</h4>'});
              }
                
              if (notfound) {
                  $('#visualizations_space').contents().hide();
                  $('#visualizations_space').block({message: '<img src="_import/img/icone_pinda.png" width="60px;" style="margin-top: 10px;"> <h4>Nenhum resultado encontrado para "' + $('#keywords').val() + '"!</h4><h4>Tente outro termo.</h4>'});
              }
            }
          });
        }
      });

      $("#keywords").keyup(function(event){
          if(event.keyCode == 13){
              $("#btn_search").click();
          }
      });

      //Extraído de: http://stackoverflow.com/questions/133925/javascript-post-request-like-a-form-submit
        function requisitar(path, params, method)
        {
            method = method || "post"; // Set method to post by default if not specified.
            var caminho;

            // The rest of this code assumes you are not using a library.
            // It can be made less wordy if you use one.
            var form = document.createElement("form");
            form.setAttribute("method", method);
            form.setAttribute("action", path);

            for (var key in params) {
                if (params.hasOwnProperty(key)) {
                    var hiddenField = document.createElement("input");
                    hiddenField.setAttribute("type", "hidden");
                    hiddenField.setAttribute("name", key);
                    hiddenField.setAttribute("value", params[key]);
                    caminho += "/" + params[key];

                    form.appendChild(hiddenField);
                }
            }

            document.body.appendChild(form);
            form.submit();
        }
