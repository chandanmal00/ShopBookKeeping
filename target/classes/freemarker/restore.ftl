<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Restore database for ${APP_TITLE}</title>
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
                <form action="/restore" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Restore ${ENTITY_NAME} for the system</h2>
                    <div class="row required">
                        <div class="left">${ENTITY_NAME} key:</div>
                        <div class="right"> <input type="text" placeholder="key to remove/delete" name="dateStr" size="120" id="${ENTITY_NAME}Id"  value="${entityvalue!""}"></div>
                    </div>
                    <input type="hidden" name="entity" value="${entity!""}">
                    <input type="submit" class="search" value="Restore ${ENTITY_NAME}">
                </form>
            </div>

    </div>

    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
    <#include "/js/auto_complete_js_entity.ftl">

</body>

</html>

