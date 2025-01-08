<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Profile</title>
</head>
<body>
<h1>Profile page</h1>
<h2>Your data:</h2>
<form>
    <label for="login">Your login:</label>
    <span id="login">${account.getLogin()}</span><br>

    <label for="firstName">Your first name:</label>
    <span id="firstName">${account.getFirstName()}</span><br>

    <label for="lastName">Your last name:</label>
    <span id="lastName">${account.getLastName()}</span>
</form>
</body>
</html>
