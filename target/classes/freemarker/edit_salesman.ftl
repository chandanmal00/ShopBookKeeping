<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Edit a ${local["Salesman"]} entry"]} for ${APP_TITLE}</title>
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
                <div class="alert alert-success" role="alert">Successfully updated ${local["Salesman"]}: <b>${nickName}</b>
                <br>
                Want to edit again: <a href="/edit/salesman/${nickName}">Click Here <i class="fa fa-pencil-square-o fa-lg" aria-hidden="true"></i></a>
                <br>
                See details for the ${local["Salesman"]}: <a href="/salesman/${nickName}">Click Here <i class="fa fa-user fa-lg" aria-hidden="true"></i></a>

                <br>
                Add another ${local["Salesman"]}: <a href="/add/salesman">Click Here <i class="fa fa-plus-square fa-lg" aria-hidden="true"></i></a>
                </div>

                    <br>
                    Below are the details added:<br>
                    <ul>
                        <#include "/common/person.ftl">
                        <li>${local["Commission"]}: <b>${commission!""}</b></li>
                    </ul>
                </p>

            </div>
        <#else>
            <div class="new_post_form">
                <form action="/edit/salesman" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Edit ${local["Salesman"]}: ${nickName} in the system</h2>
                    <div class="row required"><div class="left">${local["NickName"]}:</div> <div class="right"><strong>${nickName}</strong></div></div>
                    <input type="hidden" name="nickName" value="${nickName}" />
                    <#include "/common/personForm.ftl">
                    <div class="row"><div class="left">${local["Commission"]}:</div> <div class="right"> <input type="text" id="commission" placeholder="commission" name="commission" size="120" value="${commission!""}"></div></div>
                    <input type="submit" value="Update ${local["Salesman"]}" class="btn btn-info btn-block">
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

