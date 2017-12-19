<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} Language Preferences</title>
</head>

<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#include "/common/errors.ftl">
        <#if success??>
           <h4>Language preferences set to <strong>${langDisplay}</strong></h4>
        </#if>

    </div>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>

