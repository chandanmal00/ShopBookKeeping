<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>Home for ${ENTITY_NAME}</title>
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

      <div class="masthead">
          <span>
              <a href="/"><img style="vertical-align: middle;" src="/images/logo-jainTraveller.png" alt="${APP_TITLE}" class="jainTravellerLogoSmall"><sup><b><small>(Beta!)</small></b></sup></a>
              <small><b> Welcome ${username!"Guest"}!! </b></small>
          </span>
          <#if username??>
              <nav>
                  <ul class="nav nav-justified">
                      <li><a href="/welcome">Home</a></li>
                      <li><a href="/add${ENTITY_NAME}">Add ${ENTITY_NAME}</a></li>
                      <li><a href="/remove${ENTITY_NAME}">Remove ${ENTITY_NAME}</a></li>
                      <li><a href="/list${ENTITY_NAME}">${ENTITY_NAME} List</a></li>
                      <li><a href="/search">Search</a></li>
                      <li><a href="/help">Contact Us</a></li>
                      <li><a href="/logout">Logout</a></li>
                  </ul>
              </nav>
          <#else>
               <nav>
                  <ul class="nav nav-justified">
                      <li><a href="/">Home</a></li>
                      <li><a href="/signup">SignUp</a></li>
                      <li><a href="/login">Login</a></li>
                  </ul>
               </nav>
          </#if>
       </div>
       <#include "/common/alerts.ftl">


    </div>
    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
</body>
</html>


