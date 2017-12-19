
/* Globals */

/* Document Ready Init */

$(document).ready(function(){
  $('#sign-up-info-form').bootstrapValidator().on('success.form.bv', function(e) {
    e.preventDefault();

    var form = $(e.target);
    var signUpData = form.serializeObject();
    delete signUpData.password_confirm;

    var request = $.ajax({
      url: getApiUrlForPath("/consumer/", "v2"),
      method: "POST",
      contentType: 'application/json',
      data: JSON.stringify(signUpData)
    });

    $("#sign-up-info").addClass("page-loading");

    request.done(function(data) {
      analytics.identify(data.id, {
        firstName: data.first_name,
        lastName: data.last_name,
        email: data.email
      });
      analytics.track('signup_success_event');
      setTimeout(function() {
        window.location.replace(NEXT_URL);
      }, 100);
    });

    request.fail(function(jqXHR, textStatus) {
      var response = jqXHR.responseJSON;
      if (response.constructor == Object) {
        for (var key in response) alert(response[key]);
      } else {
        alert("Sign up failed. Please try again");
      }
      $("#sign-up-info").removeClass("page-loading");
    });
    return false;
  })
});