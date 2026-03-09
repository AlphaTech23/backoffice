<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Reservation" %>
<%@ page import="com.example.backoffice.model.Hotel" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
    String date = request.getParameter("date");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // Statistiques
    int totalReservations = reservations != null ? reservations.size() : 0;
    int totalPassagers = 0;
    if (reservations != null) {
        for (Reservation r : reservations) {
            totalPassagers += r.getNombrePassager();
        }
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservations non assignées | Parc Auto</title>
    
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <!-- Animation CSS -->
    <style>
        @keyframes slideIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }
        
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        
        .animate-slide-in {
            animation: slideIn 0.5s ease-out forwards;
        }
        
        .animate-fade-in {
            animation: fadeIn 0.3s ease-out forwards;
        }
        
        .reservation-card:hover {
            transform: translateY(-4px);
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            transition: all 0.3s ease;
        }
        
        .gradient-bg {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
        }
        
        .gradient-bg-light {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }
        
        .gradient-warning {
            background: linear-gradient(135deg, #f39c12 0%, #f1c40f 100%);
        }
        
        .badge-non-assigne {
            background: linear-gradient(135deg, #e67e22 0%, #f39c12 100%);
        }
    </style>
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
    <td><%=r.getDateArrive()%></td>
    <td><%=r.getHotel() != null ? r.getHotel().getLibelle() : ""%></td>
</tr>

<%
    }
}
%>

</table>

</body>
</html>