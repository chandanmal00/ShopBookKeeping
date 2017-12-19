<html>
<head>
    <title>BAD PRINT PAGE</title>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">

    <style>
        #state {
        display: block;
        font-weight: bold;
        margin-bottom: 1em;
        }
    </style>
</head>

<body>
<div class="container-fluid">
     <#include "/common/header.ftl">
     <br>
     <#include "/common/errors.ftl">
This page happens when you try to play with printing key for an entity which does not exist, please notify the developer in such case!!

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>