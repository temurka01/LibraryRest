<!DOCTYPE html>
<html lang = "en">
<head>
    <meta charset = "UTF-8">
    <title>Register</title>
</head>
<body>
<h1>Registration Page</h1>
<h2>Please Enter your data:</h2>
<form action = "/register" method="post">
    <label for="login">Enter login:</label>
    <input id="login" name="login" placeholder="Your login">
    <br>
    <label for="firstName">Enter your first name:</label>
    <input id="firstName" name="firstName" placeholder="Your first name">
    <br>
    <label for="lastName">Enter last name:</label>
    <input id="lastName" name="lastName" placeholder="Your last name">
    <br>
    <label for="password">Enter password:</label>
    <input type="password" id="password" name="password" placeholder="Your password">
    <input type="submit" value="Register">
</form>
</body>
</html>