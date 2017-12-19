<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Edit a ${local["Company"]} entry for ${APP_TITLE}</title>
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
        <#if success?? >
            <div>
                <p>
                <div class="alert alert-success" role="alert">Successfully updated ${local["Company"]}: <b>${nickName}</b>
                <br>
                Want to edit again: <a href="/edit/company/${nickName}">Click Here <i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i></a>
                <br>
                See details for the ${local["Company"]}: <a href="/company/${nickName}">Click Here <i class="fa fa-user fa-lg" aria-hidden="true"></i></a>

                <br>
                Add another ${local["Company"]}: <a href="/add/company">Click Here <i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a>
                </div>

                    <br>
                    Below are the details added:<br>
                    <ul>
                        <#include "/common/person.ftl">
                        <li>${local["Tinnumber"]}: <b>${tin!""}</b></li>
                    </ul>
                </p>

            </div>
        <#else>
            <div class="new_post_form">
                <form action="/edit/company" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Edit ${local["Company"]}: ${nickName} in the system</h2>
                    <div class="row required"><div class="left">${local["NickName"]}:</div> <div class="right"><strong>${nickName}</strong></div></div>
                    <input type="hidden" name="nickName" value="${nickName}" />
                    <#include "/common/personForm.ftl">
                    <div class="row"><div class="left">${local["Tinnumber"]}:</div> <div class="right"> <input type="text" id="tin" placeholder="tin card no" name="tin" size="120" value="${tin!""}"></div></div>
                    <input type="submit" value="Update ${local["Company"]}" class="btn btn-info btn-block">
                </form>
            </div>
        </#if>

    </div>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
    <#include "/js/auto_complete_js_entity.ftl">
</body>
</html>

