<!DOCTYPE html>
<html lang="en">
    <head>
        <#include "/common/meta.ftl">
        <#include "/css/head.css">
        <#include "/css/profile.css">
        <title>Search Page for Jain Traveller</title>
        <#include "/css/extra_search.css">
          <style type="text/css">

.searchImage {
      	width: 400px;
      	height: 100px;
}

          </style>
    </head>
    <body>
        <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <div id="image">
            <img src="/images/jainTraveller.png" alt="${APP_TITLE}" class="searchImage">
        </div>
        <br>
        <div class="search_form">
            <form action="/search" method="POST">
                <p class="error">${errors!""}</p>
                <input type="text" id="search" name="search" size="150" value="${search!""}" placeholder="Search for your friends recommendations here...">
                <input type="submit" class="search" value="Go">
            </form>
        </div>


        </div>
        <#include "/js/footer_js.ftl">
        <#include "/js/alerts_js.ftl">
        <#include "/js/carousel_js.ftl">
        <#include "/js/ajax_refresh_js.ftl">
    </body>
</html>