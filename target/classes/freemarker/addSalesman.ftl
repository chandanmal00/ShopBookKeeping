<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Add ${local["Salesman"]} for ${APP_TITLE}</title>
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
                <div class="alert alert-success" role="alert">Successfully added ${local["Salesman"]}: <b>${nickName}</b>
                <p>
                  Want to add one more ${local["Salesman"]}: <a href="/add/salesman">Click here</a>
                </p>
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
                <form action="/add/salesman" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Add ${local["Salesman"]} to the system</h2>
                    <div class="row required"><div class="left">${local["NickName"]}:</div> <div class="right"> <input type="text" placeholder="Friendly Name" name="nickName" size="120" id="${ENTITY_NAME}Id" value="${nickName!""}"></div></div>
                    <#include "/common/personForm.ftl">
                    <div class="row"><div class="left">${local["Commission"]}:</div> <div class="right"> <input type="text" id="commission" placeholder="commission" name="commission" size="120" value="${commission!""}"></div></div>
                    <input type="submit" value="Add ${local["Salesman"]}" class="btn btn-info btn-block">
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

