<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Trajet" %>
<%@ page import="com.example.backoffice.model.Vehicule" %>

<%
    List<Trajet> trajets = (List<Trajet>) request.getAttribute("trajets");
    String date = request.getParameter("date");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Liste des trajets</title>
</head>
<body>

<h2>Liste des trajets</h2>

<form method="get" action="<%=request.getContextPath()%>/trajet/liste">
    <label>Date :</label>
    <input type="date" name="date" value="<%=date != null ? date : ""%>">
    <button type="submit">Filtrer</button>
</form>

<br>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Date</th>
        <th>Heure départ</th>
        <th>Heure retour</th>
        <th>Véhicule</th>
        <th>Distance</th>
    </tr>

<%
if(trajets != null){
    for(Trajet t : trajets){
%>
<tr>
    <td><%=t.getId()%></td>
    <td><%=t.getDateTrajet()%></td>
    <td><%=t.getHeureDepart()%></td>
    <td><%=t.getHeureRetour()%></td>
    <td><%=t.getVehicule() != null ? t.getVehicule().getReference() : ""%></td>
    <td><%=t.getDistance()%> km</td>
</tr>
<%
    }
}
%>

</table>

</body>
</html>