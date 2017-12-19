<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <#include "/css/profile.css">
    <title>Profile Page for ${username}</title>
</head>
<body>
    <div class="container-fluid">
        <#include "/common/header.ftl">
        <form action="/profile" method="post" enctype="multipart/form-data">
            <div class="centered">
                <div class="columns">
                    <div class="red" style="float:right;width:550px;">
                        <div id="message">
                            <span class="error">${errors!""}</span>
                            <span class="success">${success!""}</span>
                        </div>
                        <#if userProfile??>
                            <p style="text-align:center;">Profile Page for ${username}</p>
                            <div class="row required"><div class="left">First Name:</div> <div class="right"><input type="text" placeholder="your legal first name" name="firstname" size="120" value="${userProfile["firstname"]!""}"></div></div>
                            <div class="row required"><div class="left">Last Name:</div> <div class="right"> <input type="text" placeholder="your legal last name" name="lastname" size="120" value="${userProfile["lastname"]!""}"></div></div>
                            <div class="row"><div class="left">Email :</div> <div class="right"> <input type="text" placeholder="you@good_domain.com" name="email" size="120" value="${userProfile["email"]!""}"></div></div>
                            <div class="row"><div class="left">Mobile:</div> <div class="right"> <input type="text" placeholder="(xxx) xxx xxxx" name="mobile" size="120" value="${userProfile["mobile"]!""}"></div></div>
                            <div class="row"><div class="left">Address:</div> <div class="right"> <input type="text"  placeholder="address" name="address" value="${userProfile["address"]!""}" size="120"></div></div>
                            <div class="row"><div class="left">City:</div> <div class="right"> <input type="text"  placeholder="city" name="city" value="${userProfile["city"]!""}" size="120"></div></div>
                            <div class="row"><div class="left">State:</div> <div class="right"> <input type="text"  placeholder="state" name="state" value="${userProfile["state"]!""}" size="120"></div></div>
                            <div class="row"><div class="left">Country:</div> <div class="right"> <input type="text" placeholder="country" name="country" size="120" value="${userProfile["country"]!""}"></div></div>
                        <#else>
                            <p style="text-align:center;">Create your Profile Page ${username}</p>
                            <div class="row required"><div class="left">First Name:</div> <div class="right"><input type="text" placeholder="your legal first name" name="firstname" size="120" value="${firstname!""}"></div></div>
                            <div class="row required"><div class="left">Last Name:</div> <div class="right"> <input type="text" placeholder="your legal last name" name="lastname" size="120" value="${lastname!""}"></div></div>
                            <div class="row"><div class="left">Email :</div> <div class="right"> <input type="text" placeholder="you@good_domain.com" name="email" size="120" value="${email!""}"></div></div>
                            <div class="row"><div class="left">Mobile:</div> <div class="right"> <input type="text" placeholder="(xxx) xxx xxxx" name="mobile" size="120" value="${mobile!""}"></div></div>
                            <div class="row"><div class="left">Address:</div> <div class="right"> <input type="text"  placeholder="address" name="address" value="${address!""}" size="120"></div></div>
                            <div class="row"><div class="left">City:</div> <div class="right"> <input type="text"  placeholder="city" name="city" value="${city!""}" size="120"></div></div>
                            <div class="row"><div class="left">State:</div> <div class="right"> <input type="text"  placeholder="state" name="state" value="${state!""}" id="state" size="120"></div></div>
                            <div class="row"><div class="left">Country:</div> <div class="right"> <input type="text" placeholder="country" name="country" size="120" value="${country!""}"></div></div>
                        </#if>
                    </div>
                    <div class="grey" style="float:left;width:350px;">
                        <div id="photo" class="tileset">
                            <#if userProfile?? && userProfile["profilephoto"]??>
                                 <p style="text-align:center;">your latest pic</p>
                                 <img src="/thumbnail.${userProfile["profilephoto"]}" alt="${userProfile["profilephoto"]}" class="builtBy">
                            <#else>
                                 <p style="text-align:center;"> update your latest avatar</p>
                                 <img src="/thumbnail.${defaultPhoto}" alt="${defaultPhoto}" class="builtBy">
                            </#if>
                        </div>
                        <div class="image-rightbar">
                            Update Your profile Photo:
                            <p>
                                <input type="file" class="search" name="fileToUpload" id="fileToUpload">
                                <#if userProfile?? && userProfile["profilephoto"]??>
                                    <input type="hidden" name="oldFileName" value="${userProfile["profilephoto"]}">
                                <#else>
                                    <input type="hidden" name="oldFileName" value="${defaultPhoto}">
                                </#if>
                            </p>
                        </div>
                    </div>
                </div>
                <div class="clear">
                    <input type="submit" class="search" value="Update Profile Info">
                </div>
            </div>
        </form>

    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
    <#include "/js/auto_complete_js.ftl">

</body>
</html>

