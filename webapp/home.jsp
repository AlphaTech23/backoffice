<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <title>home</title>
</head>
<body>
  <h1><%= request.getAttribute("test") %></h1>

  <form action="<%= request.getContextPath() %>/hello" method="get">
    <label for="message">Message :</label>
    <input type="text" name="message.message" id="message"><br>

    <label>Choisissez vos options :</label><br>
    <input type="checkbox" name="message.options[].option" value="option1" id="opt1">
    <label for="opt1">Option 1</label><br>

    <input type="checkbox" name="message.options[].option" value="option2" id="opt2">
    <label for="opt2">Option 2</label><br>

    <input type="checkbox" name="message.options[].option" value="option3" id="opt3">
    <label for="opt3">Option 3</label><br><br>

    <label>Choisissez le nom de vos options :</label><br>
    <input type="checkbox" name="message.options[].name[]" value="nom1" id="name1">
    <label for="name1">Nom 1</label><br>

    <input type="checkbox" name="message.options[].name[]" value="nom2" id="name2">
    <label for="name2">Nom 2</label><br>

    <input type="checkbox" name="message.options[].name[]" value="nom3" id="name3">
    <label for="name3">Nom 3</label><br>

    <input type="checkbox" name="message.options[].name[]" value="nom4" id="name4">
    <label for="name4">Nom 4</label><br>
    
    <input type="checkbox" name="message.options[].name[]" value="nom5" id="name5">
    <label for="name5">Nom 5</label><br>

    <input type="submit" value="Envoyer" name="action">
  </form>
</body>
</html>
