<!DOCTYPE html>
<html lang="en">
<head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Help and Support page for ${APP_TITLE}</title>
</head>
<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <h2>Contact Us</h2>
        <hr class="hr_class"/>
        <br>
        <div>
        We would love to have your feedback for our site, Please email us and we would love to hear from you.
        <br>Please send us an email at <b><a href="mailto:${INFO_EMAIL}">${INFO_EMAIL}</a></b> and let us know, we will do our best to make it happen! :)
        <br>
        <br>
        ${APP_LINK}s mission is to help shopkeepers bookkeep effectively and efficiently.
        </div>

    </div>
    <#include "/common/footer.ftl">
</body>
</html>