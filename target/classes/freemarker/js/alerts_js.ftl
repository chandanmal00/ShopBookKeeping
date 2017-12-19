<script>
    var cookieDisabled=false;
    function checkCookie(){
        var cookieEnabled=(navigator.cookieEnabled)? true : false;
        if (typeof navigator.cookieEnabled=="undefined" && !cookieEnabled){
            document.cookie="testcookie";
            cookieEnabled=(document.cookie.indexOf("testcookie")!=-1)? true : false;
        }
        return (cookieEnabled)?true:showCookieFail();
    }

    function checkProfileCookie(){
        sessionCookieEnabled=(document.cookie.indexOf("session")!=-1)? true : false;
        if(sessionCookieEnabled) {
            cookieEnabled=(document.cookie.indexOf("profile")!=-1)? true : false;
            return (cookieEnabled)?true:showCookieProfileFail();
        }
    }


    function showCookieFail(){
        cookieDisabled = true;
        $('#cookie_test').show();
        $('#profile_test').hide();
    }

    function showCookieProfileFail(){
        $('#profile_test').show();
    }

    $(document).ready(function() {
        checkCookie();
        if(cookieDisabled==false) {
           checkProfileCookie();
        }
    });


</script>