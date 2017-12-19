
<script>
    var username="${username!""}";

    function checkUser(username) {
        if( username==undefined || username==null || username=="guest" || username.trim() == "") {
          alert("You need to login to use this feature, SignUp takes 5 sec!!");
          return true;
        }
        return false;
    }

    function showComments(d)  {
        $("#commentsDiv_"+d).show();
    }

    function hideComments(d) {
        $("#commentsDiv_"+d).hide();
    }

    function showUpload(d) {
        if(checkUser(username)) {
          return;
        }
        $("#upload_photo_form_"+d).show();
        $("#hideButton_"+d).show();
        $("#uploadButton_"+d).hide();
    }

    function hideUpload(d)
    {
        $("#upload_photo_form_"+d).hide();
        $("#hideButton_"+d).hide();
        $("#uploadButton_"+d).show();
    }

    function loadData(d) {
        if(checkUser(username)) {
          return;
        }
        iEmail=$("#email_input_"+d).val();
        iComments=$("#comments_input_"+d).val();
        if(iComments!=null && iComments.trim()!="") {
            iComments=iComments.trim();
            iPermalink=$("#permalink_input_"+d).val();
            $.post("/newcomment",{ email:iEmail, comments:iComments, permalink: iPermalink },function(ajaxresult,status){
                var jsonResponse = JSON.parse(ajaxresult);
                if(jsonResponse['redirect']!=null) {
                    window.location.href = jsonResponse['redirect'];
                } else {
                    $("#cnt_comments_"+d).html(parseInt($("#cnt_comments_"+d).html()) + 1);
                    //$("#success_"+d).html("Commented:<i>"+jsonResponse['contents']+"</i>");
                    $("#success_"+d).html("<span class=\"alert alert-success\" role=\"alert\">Commented: <i>"+jsonResponse['contents']+"</i></span>");
                    $("#success_"+d).show();
                   // $("#commentsDiv_"+d).hide();
                }
            });
        }

    }

    function upload(d) {
        iFileName=$("#fileToUpload_"+d).val();
        iPermalink=$("#permalink_input_"+d).val();
        jQuery.ajax({
            url: '/upload_listing?permalink='+iPermalink,
            data: { permalink: iPermalink },
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function(data){
            alert(data);
            }
        });

    }


    function likePlusOne(d,iPermalink) {
        if(checkUser(username)) {
          return;
        }
        $.post("/plus",{ permalink: iPermalink },function(ajaxresult){
          var jsonResponse = JSON.parse(ajaxresult);
          if(jsonResponse['redirect']!=null) {
             window.location.href = jsonResponse['redirect'];
          } else {
            $("#success_"+d).html("<span class=\"alert alert-success\" role=\"alert\">Like Posted!!</span>");
            $("#like_"+d).html("");
            $("#success_"+d).show();
            $("#done_like_"+d).show();
            $("#upvote_"+d).html(parseInt($("#upvote_"+d).html()) + 1);
            $("#networkupvote_"+d).html(parseInt($("#networkupvote_"+d).html()) + 1);
            $("#upvoters_"+d).append(" <i>"+username+"<i>,");
          }
        });
    }

    function jainPlusOne(d,iPermalink) {
        if(checkUser(username)) {
          return;
        }
        $.post("/jainVote",{ permalink: iPermalink },function(ajaxresult){
            var jsonResponse = JSON.parse(ajaxresult);
            if(jsonResponse['redirect']!=null) {
                window.location.href = jsonResponse['redirect'];
            } else {
                $("#success_"+d).html("<span class=\"alert alert-success\" role=\"alert\">Jain Option Posted!!</span>");
                $("#jain_"+d).html("");
                $("#success_"+d).show();
                $("#done_jain_"+d).show();
            }

        });
    }

    </script>



