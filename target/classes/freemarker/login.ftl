<!DOCTYPE html>
<html lang="en">
  <head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <#include "/css/login.css">
    <#include "/js/footer_js.ftl">
    <#include "/js/login_js.ftl">
    <style>
       body {
         background: url(/images/winter.jpg);
       }
     </style>
</head>
<body>

    <div class="container" style="margin-top:150px;">
        <nav class="navbar navbar-default navbar-fixed-top"
             role="navigation">
            <div class="container">
                <div class="navbar-header">
                    <h1>
                    <a href="/"><img style="vertical-align: middle;" src="/images/bookkeeper.jpg" alt="${APP_TITLE}" class="jainTravellerLogoSmall"><sup><b><small>(Beta!)</small></b></sup></a>
                    </h1>

                </div>

                <div class="collapse navbar-collapse navbar-right">
                    <ul class="nav navbar-nav">
                        <li><a href="#" onclick="showLogin();">${local["login"]}</a></li>
                        <li><a href="#" onclick="showSignup();">${local["signup"]}</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <#include "/common/alerts.ftl">

        <div class="content">
            <div class="content-section content-section-dark">
                <div class="container">
                    <div class="row">
                        <div id="loginShow">
                            <div id="login-section">

                                <div class="login info-card info-card-padding roundify">
                                    <div class="content-section-headings" style="text-align:center;">
                                        <h3 class="content-section-header">${local["jaiJinendra"]}!!</h3>
                                        <h5 class="content-section-subheader">${local["greeting"]} ${local["newUser"]} <a onclick="showSignup();" href="#">${local["signup"]}</a> ${local["please"]}!!</h5>
                                    </div>

                                    <form id="login-form"
                                          class="form-horizontal"
                                          method="post"
                                          action="/login"
                                          data-bv-message="This value is not valid"
                                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh"
                                            >
                                          <#if login_error??>
                                          <div class="alert alert-danger">
                                                ${login_error!""}
                                          </div>
                                          </#if>
                                        <input type="hidden" name="sessionId" value="sessionId" />
                                        <input type="hidden" name="permalink" value="${permalink!""}" />
                                        <div class="form-group">
                                            <label class="control-label col-xs-4" for="email">${local["global.email"]}</label>
                                            <div class="col-xs-7">
                                                <input type="email" class="form-control"
                                                       name="email" value="${username!""}" placeholder="Email" required>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-xs-4" for="password">${local["global.password"]}</label>
                                            <div class="col-xs-7">
                                                <input type="password" class="form-control"
                                                       name="password" placeholder="Password" required>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <div class="col-xs-offset-4 col-xs-7">
                                                <button type="submit" class="btn btn-primary">${local["signIn"]}</button>
                                                <span>${local["newUser"]} <a onclick="showSignup();" href="#">${local["signup"]}</a> ${local["please"]}!!</span>
                                            </div>
                                        </div>


                                    </form>


                                    <center>
                                        ${local["forgotPassword"]}? <a href="/reset">${local["clickToReset"]}</a>
                                    </center>
                                </div>
                            </div>

                        </div>


                        <div id="signupShow">
                            <div id="signup-section">
                                <div id="sign-up-info" class="info-card info-card-padding roundify">
                                    <div class="content-section-headings" style="text-align:center;">
                                        <h3 class="content-section-header">${local["createAccount"]}</h3>
                                        <h5 class="content-section-subheader">${local["signupProcess"]}!! ${local["alreadyRegistered"]}, <a onclick="showLogin();" href="#">${local["login"]}</a>!!</h5>
                                    </div>

                                    <form id="sign-up-info-form" class="form-horizontal"
                                          action="/signup"
                                          method="post"
                                          data-bv-message="This value is not valid"
                                          data-bv-feedbackicons-valid="glyphicon glyphicon-ok"
                                          data-bv-feedbackicons-invalid="glyphicon glyphicon-remove"
                                          data-bv-feedbackicons-validating="glyphicon glyphicon-refresh">
                                          <#if signup_error??>
                                              <div class="alert alert-danger">
                                                    ${signup_error!""}
                                              </div>
                                          </#if>
                                        <div class="form-group">
                                            <label class="control-label col-xs-4" for="email">${local["global.email"]}</label>
                                            <div class="col-xs-7">
                                                <input type="email" class="form-control" id="email"
                                                       name="email" placeholder="Email address" value="${username!""}" required/>
                                            </div>
                                        </div>

                                        <div class="form-group">
                                            <label class="control-label col-xs-4" for="password">${local["global.password"]}</label>
                                            <div class="col-xs-7">
                                                <input type="password" class="form-control" id="password"
                                                       name="password" placeholder="Password"
                                                       data-bv-stringlength="true"
                                                       data-bv-stringlength-min="8" required/>
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="control-label col-xs-4" for="password_confirm">${local["global.password"]} (confirm)</label>
                                            <div class="col-xs-7">
                                                <input type="password" class="form-control" id="password_confirm"
                                                       name="password_confirm" placeholder="Password"
                                                       data-bv-identical="true"
                                                       data-bv-identical-field="password" required/>
                                            </div>
                                        </div>
                                        <input type="hidden" name="permalink" value="${permalink!""}" />
                                        <#if username_error?? || email_error?? || verify_error?? || password_error?? >
                                            <div class="alert alert-danger">
                                                 ${username_error!""}
                                                 ${email_error!""}
                                                 ${verify_error!""}
                                                 ${password_error!""}
                                            </div>
                                        </#if>
                                        <div class="form-group">
                                            <div class="col-xs-offset-4 col-xs-7">
                                                <button type="submit" class="btn btn-primary">${local["signup"]}!</button>
                                                <span>
                                                      ${local["bySignup"]} <a href="/tos">${local["terms"]}</a>!
                                                </span>
                                                <div class="disclaimer">
                                                      ${local["please"]} ${local["alsoRead"]} <a href="/disclaimer">${local["disclaimer"]}</a>!
                                                </div>
                                                <span>${local["alreadyRegistered"]}, <a onclick="showLogin();" href="#">${local["login"]}</a>!!</span>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
        <#include "/common/footer.ftl">
        <#include "/js/alerts_js.ftl">
</body>

</html>