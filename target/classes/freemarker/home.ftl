<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Book Keeping for ShopKeepers</title>
    <#if username??>
        <#else>
        <style type="text/css">
            .fade-carousel {
            position: relative;
            height: 100vh;
            }
            .fade-carousel .carousel-inner .item {
            height: 100vh;
            }
            .fade-carousel .carousel-indicators > li {
            margin: 0 2px;
            background-color: #f39c12;
            border-color: #f39c12;
            opacity: .7;
            }
            .fade-carousel .carousel-indicators > li.active {
            width: 10px;
            height: 10px;
            opacity: 1;
            }

            /********************************/
            /*          Hero Headers        */
            /********************************/
            .hero {
            position: absolute;
            top: 50%;
            left: 50%;
            z-index: 3;
            color: #fff;
            text-align: center;
            text-transform: uppercase;
            text-shadow: 1px 1px 0 rgba(0,0,0,.75);
            -webkit-transform: translate3d(-50%,-50%,0);
            -moz-transform: translate3d(-50%,-50%,0);
            -ms-transform: translate3d(-50%,-50%,0);
            -o-transform: translate3d(-50%,-50%,0);
            transform: translate3d(-50%,-50%,0);
            }
            .hero h1 {
            font-size: 6em;
            font-weight: bold;
            margin: 0;
            padding: 0;
            }

            .fade-carousel .carousel-inner .item .hero {
            opacity: 0;
            -webkit-transition: 2s all ease-in-out .1s;
            -moz-transition: 2s all ease-in-out .1s;
            -ms-transition: 2s all ease-in-out .1s;
            -o-transition: 2s all ease-in-out .1s;
            transition: 2s all ease-in-out .1s;
            }
            .fade-carousel .carousel-inner .item.active .hero {
            opacity: 1;
            -webkit-transition: 2s all ease-in-out .1s;
            -moz-transition: 2s all ease-in-out .1s;
            -ms-transition: 2s all ease-in-out .1s;
            -o-transition: 2s all ease-in-out .1s;
            transition: 2s all ease-in-out .1s;
            }

            /********************************/
            /*            Overlay           */
            /********************************/
            .overlay {
            position: absolute;
            width: 100%;
            height: 100%;
            z-index: 2;
            background-color: #080d15;
            opacity: .7;
            }

            /********************************/
            /*          Custom Buttons      */
            /********************************/
            .btn.btn-lg {padding: 10px 40px;}
            .btn.btn-hero,
            .btn.btn-hero:hover,
            .btn.btn-hero:focus {
            color: #f5f5f5;
            background-color: #1abc9c;
            border-color: #1abc9c;
            outline: none;
            margin: 20px auto;
            }

            /********************************/
            /*       Slides backgrounds     */
            /********************************/
            .fade-carousel .slides .slide-1,
            .fade-carousel .slides .slide-2,
            .fade-carousel .slides .slide-4,
            .fade-carousel .slides .slide-3 {
            height: 100vh;
            background-size: cover;
            background-position: center center;
            background-repeat: no-repeat;
            }
            .fade-carousel .slides .slide-1 {
            background-image: url(/images/landscape.jpg);
            }
            .fade-carousel .slides .slide-2 {
            background-image: url(/images/winter.jpg);
            }
            .fade-carousel .slides .slide-3 {
            background-image: url(/images/grass.jpg);
            }

            .fade-carousel .slides .slide-4 {
            background-image: url(/images/vietnam.jpg);
            }

            /********************************/
            /*          Media Queries       */
            /********************************/
            @media screen and (min-width: 980px){
            .hero { width: 980px; }
            }
            @media screen and (max-width: 640px){
            .hero h1 { font-size: 4em; }
            }

        </style>
        </#if>
  </head>
  <body>


<#if username??>
<#include "/common/header.ftl">
<div class="container">
            <div class="row">
                              <div id="custom-search-input">
                                 <form action="/multiSearch" method="POST" role="search">
                                               <div class="input-group col-md-12">
                                                   <input type="text" class=" search-query form-control" name="query" placeholder="Search everywhere..." />
                                                   <span class="input-group-btn">
                                                       <button class="btn btn-info" type="submit">
                                                           <span class=" glyphicon glyphicon-search"></span>
                                                       </button>
                                                   </span>
                                               </div>
                                 </form>
                               </div>
            </div>

</div> <!-- /container -->

<#else>
<div class="carousel fade-carousel slide" data-ride="carousel" data-interval="3200" id="bs-carousel">
    <!-- Overlay -->
    <div class="overlay">
    <a href="/login" role="button" class="btn btn-hero btn-lg">${local["login"]}!</a>
            <a href="/signup" role="button" class="btn btn-hero btn-lg">${local["signup"]}!</a>
    </div>


    <!-- Indicators -->
    <ol class="carousel-indicators">

        <li data-target="#bs-carousel" data-slide-to="0" class="active"></li>
        <li data-target="#bs-carousel" data-slide-to="1"></li>
        <li data-target="#bs-carousel" data-slide-to="2"></li>
        <li data-target="#bs-carousel" data-slide-to="3"></li>
    </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner">
        <div class="item slides active">
            <div class="slide-1"></div>
            <div class="hero">
                <hgroup>
                    <h1>${local["inventoryBookkeeping"]}</h1>
                    <h3>Get started with your records at one place</h3>
                </hgroup>
                <a href="/login" role="button" class="btn btn-hero btn-lg">${local["learnMore"]}</a>
            </div>
        </div>
        <div class="item slides">
            <div class="slide-2"></div>
            <div class="hero">
                <hgroup>
                    <h1>${local["inventoryBookkeeping"]}</h1>
                    <h3>Manage things easily</h3>
                </hgroup>
                <a href="/login" role="button" class="btn btn-hero btn-lg">${local["learnMore"]}</a>
            </div>
        </div>
        <div class="item slides">
            <div class="slide-3"></div>
            <div class="hero">
                <hgroup>
                    <h1>${local["inventoryBookkeeping"]}</h1>
                    <h3>Finding things gotten easier, free from calculators</h3>
                </hgroup>
                <a href="/login" role="button" class="btn btn-hero btn-lg">${local["learnMore"]}</a>
            </div>
        </div>
        <div class="item slides">
            <div class="slide-4"></div>
            <div class="hero">
                <hgroup>
                    <h1>${local["inventoryBookkeeping"]}</h1>
                    <h3>Everything is just one click now</h3>
                </hgroup>
                <a href="/login" role="button" class="btn btn-hero btn-lg">${local["learnMore"]}</a>
            </div>
        </div>
    </div>


</div>
    <div class="container-fluid">
        <#include "/common/keyboard.ftl">
    </div>

</#if>



    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
  </body>

<script>
$('.help').show();
</script>
</html>