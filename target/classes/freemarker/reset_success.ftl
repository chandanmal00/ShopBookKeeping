<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Reset password page</title>
</head>
<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <#if success??>
        <h4>Reset password for email: '${email}'</h4>
        <hr class="hr_class"/>
        <br><br>
        <p>
            <span class="label label-success fa-lg">
                Password reset send to Admin!! Try default password: <i>${defaultPassword}</i>
            </span>
            <br>
        </p>
        <#else>
           <#include "/common/errors.ftl">
        </#if>

    </div>
    <br>
    <br>
    <br>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>