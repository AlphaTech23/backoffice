<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Reservation" %>
<%@ page import="com.example.backoffice.model.Hotel" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    String date = request.getParameter("date");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Réservations non assignées</title>
</head>
<body>

<h2>Réservations non assignées</h2>

<form method="get" action="<%=request.getContextPath()%>/reservation/non-assigne">
    <label>Date :</label>
    <input type="date" name="date" value="<%=date != null ? date : ""%>">
    <button type="submit">Filtrer</button>
</form>

<br>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Client</th>
        <th>Nombre passagers</th>
        <th>Date arrivée</th>
        <th>Hotel</th>
    </tr>

<%
if(reservations != null){
    for(Reservation r : reservations){
%>

<tr>
    <td><%=r.getId()%></td>
    <td><%=r.getIdClient()%></td>
    <td><%=r.getNombrePassager()%></td>
    <td><%=r.getDateArrivee()%></td>
    <td><%=r.getHotel() != null ? r.getHotel().getLibelle() : ""%></td>
</tr>

<%
    }
}
%>

</table>

</body>
</html>