<%@ page import="java.net.URL" %>
<%@ page import="java.net.URLConnection" %>
<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.net.URLEncoder" %>
<%@ page import="java.io.InputStreamReader" %>
<style>

    div#globalNav ul.subNav-2cols li:hover,
    div#dropdownWrap ul.subNav li:hover {
        background-color:red !important;
    }
</style>

<%
    String APP_ID="6753221261";
    String App_secret="d83ecc7141ed682adc437feddbeb2d6b";

    String code = request.getParameter("code");
    if (code == null || code.equals("")) {
        // an error occurred, handle this
    }
//    out.println(code);
    String token = null;
    try {
        String g = "https://graph.facebook.com/oauth/access_token?client_id="+APP_ID+"&redirect_uri=" + URLEncoder.encode("http://cuoi5s.com/admin/video/login_facebook.html", "UTF-8") + "&client_secret="+App_secret+"&code=" + code;
        URL u = new URL(g);
        URLConnection c = u.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
        String inputLine;
        StringBuffer b = new StringBuffer();
        while ((inputLine = in.readLine()) != null)
            b.append(inputLine + "\n");
        in.close();
        token = b.toString();
        token = token.replace("access_token=", "");
        if (token.startsWith("{"))
            throw new Exception("error on requesting token: " + token + " with code: " + code);
    } catch (Exception e) {
        // an error occurred, handle this
    }

//    String graph = null;
//    String g = "";
//    try {
//        g = "https://graph.facebook.com/me/feed?message=Hello_Testing&" + token;
//        out.println("========="+g);
//        URL u = new URL(g);
//        URLConnection c = u.openConnection();
//        BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
//        String inputLine;
//        StringBuffer b = new StringBuffer();
//        while ((inputLine = in.readLine()) != null)
//            b.append(inputLine + "\n");
//        in.close();
//        graph = b.toString();
//    } catch (Exception e) {
//        // an error occurred, handle this
//    }

//    out.println(token);
//    out.println("graph"+graph);

%>

<%--<script>--%>
    <%--$(function() {--%>
        <%--$.ajax({--%>
            <%--type: "POST",--%>
            <%--url: '<%=g%>',--%>
            <%--data: data,--%>
            <%--success: function() {--%>
                <%--alert (1);--%>
            <%--},--%>
            <%--dataType: dataType--%>
        <%--});--%>
    <%--});--%>
<%--</script>--%>


<!DOCTYPE html>
<html>
<head>
    <title>Facebook Login JavaScript Example</title>
    <meta charset="UTF-8">
</head>
<body>
<script>
    // This is called with the results from from FB.getLoginStatus().
    function statusChangeCallback(response) {
        console.log('statusChangeCallback');
        console.log(response);
        // The response object is returned with a status field that lets the
        // app know the current login status of the person.
        // Full docs on the response object can be found in the documentation
        // for FB.getLoginStatus().
        if (response.status === 'connected') {
            // Logged into your app and Facebook.
            testAPI();
        } else if (response.status === 'not_authorized') {
            // The person is logged into Facebook, but not your app.
            document.getElementById('status').innerHTML = 'Please log ' +
                    'into this app.';
        } else {
            // The person is not logged into Facebook, so we're not sure if
            // they are logged into this app or not.
            document.getElementById('status').innerHTML = 'Please log ' +
                    'into Facebook.';
        }
    }

    // This function is called when someone finishes with the Login
    // Button.  See the onlogin handler attached to it in the sample
    // code below.
    function checkLoginState() {
        FB.getLoginStatus(function(response) {
            statusChangeCallback(response);
        });
    }

    window.fbAsyncInit = function() {
        FB.init({
            appId      : '<%=APP_ID%>',
            cookie     : true,  // enable cookies to allow the server to access
            // the session
            xfbml      : true,  // parse social plugins on this page
            version    : 'v2.0' // use version 2.0
        });

        // Now that we've initialized the JavaScript SDK, we call
        // FB.getLoginStatus().  This function gets the state of the
        // person visiting this page and can return one of three states to
        // the callback you provide.  They can be:
        //
        // 1. Logged into your app ('connected')
        // 2. Logged into Facebook, but not your app ('not_authorized')
        // 3. Not logged into Facebook and can't tell if they are logged into
        //    your app or not.
        //
        // These three cases are handled in the callback function.

        FB.getLoginStatus(function(response) {
            statusChangeCallback(response);
        });

    };

    // Load the SDK asynchronously
    (function(d, s, id) {
        var js, fjs = d.getElementsByTagName(s)[0];
        if (d.getElementById(id)) return;
        js = d.createElement(s); js.id = id;
        js.src = "//connect.facebook.net/en_US/sdk.js";
        fjs.parentNode.insertBefore(js, fjs);
    }(document, 'script', 'facebook-jssdk'));

    // Here we run a very simple test of the Graph API after login is
    // successful.  See statusChangeCallback() for when this call is made.
    function testAPI() {
        console.log('Welcome!  Fetching your information.... ');
        FB.api('/me', function(response) {
            console.log('Successful login for: ' + response.name);
            document.getElementById('status').innerHTML =
                    'Thanks for logging in, ' + response.name + '!';
        });
    }
</script>

<!--
  Below we include the Login Button social plugin. This button uses
  the JavaScript SDK to present a graphical Login button that triggers
  the FB.login() function when clicked.
-->

<fb:login-button scope="public_profile,email" onlogin="checkLoginState();">
</fb:login-button>

<div id="status">
</div>

</body>
</html>