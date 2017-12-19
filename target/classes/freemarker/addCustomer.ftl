<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Add ${local["Customer"]} for ${APP_TITLE}</title>
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
                <#if key??>
                  <#assign nickName = key>
                </#if>
                <div class="alert alert-success" role="alert">Successfully ${operation!"Add"}ed ${local["Customer"]}: <b>${nickName}</b>
                <p>
                  Want to add one more ${local["Customer"]}: <a href="/add/customer">Click here</a>
                </p>
                </div>

                    <br>
                    Below are the details:<br>
                    <ul>
                        <#include "/common/person.ftl">
                        <li>${local["LoyaltyType"]}: <b>${loyaltyType!""}</b></li>
                        <li>${local["LoyaltyNumber"]}: <b>${loyaltyNumber!""}</b></li>
                    </ul>
                </p>

            </div>
        <#else>
            <div class="new_post_form">
                <form action="/add/customer" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Add ${local["Customer"]} to the system</h2>
                    <#if operation == "Update">
                    <div class="row"><div class="left">${local["NickName"]}: </div> <div class="right">${key!""}</div></div>
                    <input type="hidden" name="key" value="${key!""}">
                    <#else>
                    <div class="row required"><div class="left">${local["NickName"]}:</div> <div class="right"> <input type="text" placeholder="Friendly Name" name="nickName" size="120" id="${ENTITY_NAME}Id" value="${nickName!""}"></div></div>
                    </#if>
                    <div class="row"><div class="left">${local["LoyaltyType"]}:</div> <div class="right"> <input type="text" placeholder="Loyalty Type" name="loyaltyType" size="120" id="loyaltyTypeId" value="${loyaltyType!""}"></div></div>
                    <div class="row"><div class="left">${local["LoyaltyNumber"]}:</div> <div class="right"> <input type="text" placeholder="Loyalty Number" name="loyaltyNumber" size="120" id="loyaltyNumberId" value="${loyaltyNumber!""}"></div></div>
                    <#include "/common/personForm.ftl">
                    <input type="hidden" name="operation" value="${operation}">
                    <input type="submit" value="${operation!"Add"} ${local["Customer"]}" class="btn btn-info btn-block">
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

