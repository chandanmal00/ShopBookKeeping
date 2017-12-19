<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${local["Search"]} within ${ENTITY_NAME} for ${APP_TITLE}</title>
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
            <div class="new_post_form">
                <form action="/searchEntity" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>${local["Search"]} ${ENTITY_NAME} in the system</h2>
                    <div class="row required">
                        <div class="left">${ENTITY_NAME} key:</div>
                        <div class="right"> <input type="text" placeholder="key to search" name="entityValue" size="120" id="${ENTITY_NAME}Id"  value="${entityValue!""}"></div>
                    </div>
                    <input type="hidden" name="entity" value="${entity!""}">
                    <input type="submit" value="${local["Search"]} ${ENTITY_NAME!""}" class="btn btn-info btn-block">
                </form>
            </div>

    </div>
    <#include "/common/footer.ftl">
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
    <#assign search=true>
    <#include "/js/auto_complete_js_entity.ftl">

</body>

</html>

