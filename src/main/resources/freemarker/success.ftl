<html class="notie"><!--<![endif]--><head>
    <#include "/common/meta.ftl">
    <#include "/css/head.css">
    <title>${APP_TITLE} Reset Password</title>

    <style type="text/css">
        .info-card-padding {
        padding: 15px;
        }
        .info-card {
        background-color: #fff;
        border: 10px solid #d8d8d8;
        box-sizing: border-box;
        }
        .roundify {
        border-radius: 5px;
        overflow: hidden;
        }

        nav .jainTraveller-logo {
        line-height: 25px;
        margin-bottom: 7px;
        margin-top: 24px;
        }


        .jainTraveller-logo {
        background-position: 0 center;
        background-repeat: no-repeat;
        background-size: 100% auto;
        height: 18px;
        margin: 0;
        width: 174px;
        }
        h1, h2, h3 {
        color: #2a2a2a;
        font-weight: normal;
        }

    </style>


<body>
    <nav role="navigation" class="navbar navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <h1 class="jain-traveller-logo">
                    <a href="/">${APP_TITLE}</a>
                </h1>
            </div>

            <div class="collapse navbar-collapse navbar-right">
                <ul class="nav navbar-nav">
                    <li><a href="/login">Login</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <br>
    <div class="content">
        <div class="content-section content-section-dark">
            <div class="container">
                <div class="row">
                    <div class="col-xs-2"></div>

                    <div class="col-xs-8">
                        <div class="info-card info-card-padding roundify">
                            <div class="content-section-headings">
                                <h3 class="content-section-header">Reset Password</h3>
                            </div>

                            <p class="center-text">
                                Please specify your email address to receive instructions for resetting it.
                                If an account exists by that email, we will send a password reset.
                            </p>

                            <form class="form-horizontal" method="POST" action="/reset">
                            <form class="form-horizontal" method="POST" action="/reset">
                              <div style="display:none">
                                <input type="hidden" name="sessionId" value="${session!""}">
                              </div>
                              <div class="form-group">
                                <label class="col-xs-3 control-label" for="id_email">Email Address:</label>
                                <div class="col-xs-8">
                                  <div class="input-group">
                                    <input type="email" name="email" maxlength="254" id="id_email" class="form-control">
                                    <span class="input-group-btn">
                                      <button value="Reset password" class="btn btn-default" style="submit">
                                        Reset Password</button>
                                    </span>
                                  </div>

                                </div>
                              </div>
                            </form>

                        </div>
                    </div>

                    <div class="col-xs-2"></div>

                </div>
            </div>
        </div>
    </div>

    <#include "/js/footer_js.ftl">
    <#include "/js/alerts_js.ftl">
        <script type="text/javascript">
        $(document).ready(function() {
           $('#password-reset-form').bootstrapValidator()
        });
    </script>
</body>
</html>