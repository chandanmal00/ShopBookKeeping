
<html>
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} - Mongo Admin Page</title>
</head>

<body>

    <div class="container-fluid">
        <#include "/common/header.ftl">
        <br>
        <#if success??>
            <h4>Status of ${operation!""}: <font color=green><strong>SUCCESS</strong></h4>
            <p>
                <div class="alert alert-success" role="alert">Success: We were able to apply ${operation!""} successfully</div><br>
                ${operation} was made for date:${dateStr!""}, path on system used: ${dir!""}
            </p>
        <#else>
            <h4>Status of ${operation!""}:<font color="red"><strong> ERROR</strong></font></h4>
            <p>
                <div class="alert alert-danger" role="alert">Error: We were unable to ${operation!""} for ${dateStr!""}</div>
                <#include "/common/errors.ftl">
            </p>
        </#if>

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">

</body>
</html>