<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">
    <style>
        /*!
         * IE10 viewport hack for Surface/desktop Windows 8 bug
         * Copyright 2014-2015 Twitter, Inc.
         * Licensed under MIT (https://github.com/twbs/bootstrap/blob/master/LICENSE)
         */

        /*
         * See the Getting Started docs for more information:
         * http://getbootstrap.com/getting-started/#support-ie10-width
         */
        @-webkit-viewport { width: device-width; }
        @-moz-viewport    { width: device-width; }
        @-ms-viewport     { width: device-width; }
        @-o-viewport      { width: device-width; }
        @viewport         { width: device-width; }

        body {
          min-height: 20000px;
        }

        .navbar-static-top {
          margin-bottom: 19px;
        }
    </style>

    <link rel="stylesheet" href="http://getbootstrap.com/examples/starter-template/starter-template.css">

</head>
<body>

 <!-- Static navbar -->
    <nav class="navbar navbar-default navbar-static-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Book Manager</a>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
            <li class="dropdown">
              <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Dropdown <span class="caret"></span></a>
              <ul class="dropdown-menu">
                <li><a href="#">Action</a></li>
                <li><a href="#">Another action</a></li>
                <li><a href="#">Something else here</a></li>
                <li role="separator" class="divider"></li>
                <li class="dropdown-header">Nav header</li>
                <li><a href="#">Separated link</a></li>
                <li><a href="#">One more separated link</a></li>
              </ul>
            </li>
          </ul>
          <!--
          <ul class="nav navbar-nav navbar-right">
            <li><a href="../navbar/">Default</a></li>
            <li class="active"><a href="./">Static top <span class="sr-only">(current)</span></a></li>
            <li><a href="../navbar-fixed-top/">Fixed top</a></li>
          </ul>
          -->
        </div><!--/.nav-collapse -->
      </div>
    </nav>




<div class="container">

 <div class="jumbotron">

${message?if_exists}

<#if book?exists>
    ${book.getTitle()}
</#if>

<div class="row">
    <div class="col-md-8">
        <div class="input-group">

            <span class="input-group-addon" id="isbn-addon">
                <span class="glyphicon glyphicon-search" aria-hidden="true"></span>
                ISBN
            </span>
            <input id="isbn" name="isbn" type="text" class="form-control" placeholder="ISBN"  aria-describedby="isbn-addon"/>


        </div>
    </div> <!-- /col-md-8 -->
    <div class="col-md-2">
        <button class="btn btn-default" id="search_execute">SEARCH</button>
    </div> <!-- /col-md-2 -->
</div> <!-- /row -->


<div class="row">
    <div class='col-md-10'>
        <div id="book_result">
            <table id="book_result_table" class="table table-condensed">
                <tr>
                    <th>제목</th>
                    <th>저자</th>
                    <th>출판사</th>
                    <th>표지</th>
                </tr>
                <tr class='comic'></tr>

            </table>
        </div>
    </div>

</div> <!-- /row -->
 </div> <!-- /jubotron -->


</div> <!-- /container -->

<!--
<script src="/webjars/jquery/2.1.4/jquery.min.js"></script>
<script src="/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
-->

<script src="http://cdnjs.cloudflare.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>
<script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script>

    $(document).ready(function() {

        $('#isbn').focus();

        var prevISBN = 0;

        $('#isbn').on("change paste keyup", function() {
            var isbn = $('#isbn').val();
            console.log(isbn);

            if(isbn.length >= 13) {

                if(prevISBN != isbn) {
                    $('#search_execute').click();
                    prevISBN = isbn;
                }
            }



        });

        $('#search_execute').click(function() {

            var isbn = $('#isbn').val();
            console.log(isbn);

            if(isbn.length < 10) {
                console.log("Wrong lenth ISBN");
                alert("Wrong");
                $('#isbn').val('');
                $('#isbn').focus();
                return;
            }

            $.ajax({
                url: '/search.do',
                type: 'post',
                dataType: 'json',
                data: {'isbn': isbn },
                success: function(data) {

                    if(data == null) {
                        // clear input form
                        $('#isbn').val('');
                        $('#isbn').focus();
                        return;
                       }

                    var image = data.imageFile;
                    var imageTag = "<img src='" + image + "' border='0'/>";



                    var tableTag = "<tr class='comic'><td>" + data.title + "</td><td>" + data.author + "</td><td>" + data.publisher + "</td><td>" + imageTag + "</td></tr>";
                    //$('#book_result').append(imageTag);
                    //$('#book_result_table').append(tableTag);
                    $('.comic').first().before(tableTag);
                    console.log(data);



                    // clear input form
                    $('#isbn').val('');
                    $('#isbn').focus();
                }
            });
        });
    });

</script>

</body>
</html>