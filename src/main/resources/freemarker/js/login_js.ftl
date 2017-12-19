<script type="text/javascript">
        function showLogin() {
            $('#loginShow').show();
            $('#signupShow').hide();
        }

        function showSignup() {
            $('#loginShow').hide();
            $('#signupShow').show();
        }
        $(document).ready(function() {
           <#if login??>
               showLogin();
           <#elseif signup??>
               showSignup();
           <#else>
               showLogin();
           </#if>
           $('#sign-up-info-form').bootstrapValidator();
           $('#login-form').bootstrapValidator();
        });
</script>