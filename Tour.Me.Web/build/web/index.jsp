<%-- 
    Document   : index
    Created on : Apr 3, 2013, 2:41:55 PM
    Author     : Heshitha
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script src="http://code.jquery.com/jquery-latest.js"></script>

    </head>
    <script type='text/javascript'>
       
$(function() {
   // Run when the DOM is ready
  // var username=$("#Text1").val();
 //  var password=$("#Text2").val();
 //  var email=$("#Text3").val();
   var dataString = '{"userName":"Hetti","passWord":"kaputa"}';

   $.ajax({
      type: 'POST',
      url: 'http://localhost:8080/JsonTest/IndexController',
      data: dataString,
      dataType: 'json',
     success: function(data) {
         $('#output').html(data);
      }
   });
});
        
</script>
<div id='output'></div>

    <body>
      

    </body>
</html>
