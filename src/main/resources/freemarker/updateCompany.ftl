<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${operation} Company for ${APP_TITLE}</title>
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
                <div class="alert alert-success" role="alert">Successfully ${operation}ed Company: <b>${nickName}</b></div>
                    <br>
                    Below are the details added:<br>
                    <ul>
                        <li>firstName: <b>${firstName!""}</b></li>
                        <li>lastName: <b>${lastName!""}</b></li>
                        <li>age: <b>${age!""}</b></li>
                        <li>aadhar: <b>${aadhar!""}</b></li>
                        <li>place: <b>${place!""}</b></li>
                        <li>address: <b>${address!""}</b></li>
                        <li>taluka: <b>${taluka!""}</b></li>
                        <li>district: <b>${district!""}</b></li>
                        <li>state: <b>${state!""}</b></li>
                    </ul>
                </p>

            </div>
        <#else>
            <div class="new_post_form">
                <form action="/update/company" method="POST">
                    <#include "/common/errors.ftl">
                    <h2>Add Company to the system</h2>
                    <div class="row"><div class="left">NickName:</div> <div class="right">${nickName!""}</div></div>
                    <input type="hidden" name="nickName" value="${nickName!""}">
                    <#include "/common/name.ftl">
                    <#include "/common/location.ftl">
                    <div class="row"><div class="left">Age:</div> <div class="right"> <input type="text" id="age" placeholder="Age" name="age" size="120" value="${age!""}"></div></div>
                    <div class="row"><div class="left">AadharCardNumber:</div> <div class="right"> <input type="text" id="aadhar" placeholder="aadhar card no." name="aadhar" size="120"></div></div>
                    <div class="row"><div class="left">PAN number:</div> <div class="right"> <input type="text" id="pan" placeholder="pan card no" name="pan" size="120"></div></div>
                    <div class="row"><div class="left">TIN number:</div> <div class="right"> <input type="text" id="tinId" placeholder="tin card no" name="tin" size="120"></div></div>
                    <input type="submit" class="search" value="${operation} Company">
                </form>
            </div>
        </#if>

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
    <#include "/js/auto_complete_js_entity.ftl">
</body>
</html>

