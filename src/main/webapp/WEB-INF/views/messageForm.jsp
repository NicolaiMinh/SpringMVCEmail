<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Email Form Test</title>

<style type="text/css">
#sendEmailBtn {
	float: left;
	margin-top: 22px;
}
</style>

</head>
<body>
<center>
            <h2>Spring MVC Email-Message Example</h2>
            <a href="emailForm">Send Email</a>
            <form id="sendMessageForm" method="post" action="sendMessage">
                <table id="emailFormBeanTable" border="0" width="80%">
                    <tr>
                        <td>Phone Number To: </td>
                        <td><input id="receiverMessage" type="text" name="messageTo" size="65" /></td>
                    </tr>
                    <tr>
                        <td>Message text: </td>
                        <td><textarea id="messageText" cols="50" rows="10" name="messageText"></textarea></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center"><input id="sendMessageBtn" type="submit" value="Send Message" /></td>
                    </tr>
                </table>
            </form>
        </center>

</body>
</html>