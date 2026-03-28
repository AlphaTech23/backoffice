<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.dto.TrajetDTO" %>
<%@ page import="com.example.backoffice.model.*" %>
<%@ page import="java.time.*" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<%
    List<TrajetDTO> trajets = (List<TrajetDTO>) request.getAttribute("trajets");
    String date = request.getParameter("date");
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    
    // Statistiques
    int totalTrajets = trajets != null ? trajets.size() : 0;
    double totalDistance = 0;
    int totalReservations = 0;
    
    if (trajets != null) {
        for (TrajetDTO t : trajets) {
            if (t.getDistance() != null) {
                totalDistance += t.getDistance();
            }
            if (t.getTrajetReservations() != null) {
                totalReservations += t.getTrajetReservations().size();
            }
        }
    }
%>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Trajets | Parc Auto</title>
    
    <!-- Tailwind CSS -->
    <script src="https://cdn.tailwindcss.com"></script>
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <!-- Animation CSS -->
    <style>
        .reservation-row {
            overflow: hidden;
            max-height: 0;
            opacity: 0;
            transition: max-height 0.4s ease, opacity 0.4s ease;
        }

        .reservation-row.show {
            max-height: 1000px; /* assez grand pour contenir toutes les réservations */
            opacity: 1;
        }
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
        
        .trajet-card:hover {
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
        
        .reservation-subtable {
            background-color: #f9fafb;
            border-radius: 0.5rem;
            margin: 0.5rem 0;
        }
    </style>
</head>
<body class="bg-gray-50">
    <!-- Navigation -->
    <nav class="gradient-bg text-white shadow-lg sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between items-center h-16">
                <div class="flex items-center space-x-4">
                    <div class="bg-white/10 p-2 rounded-lg">
                        <i class="fas fa-route text-2xl"></i>
                    </div>
                    <h1 class="text-xl font-bold">Gestion des Trajets ETU-3301 3309 3340</h1>
                </div>
                <div class="flex items-center space-x-4 text-sm">
                    <span class="bg-white/20 px-3 py-1 rounded-full">
                        <i class="far fa-clock mr-1"></i>
                        <%= LocalDateTime.now().format(timeFormatter) %>
                    </span>
                </div>
            </div>
        </div>
    </nav>
    
    <%@ include file="../partial/navigation.jsp" %>

    <main class="ml-64 p-8">
        <div class="max-w-7xl mx-auto animate-slide-in">
            <!-- En-tête avec statistiques -->
            <div class="mb-8 animate-slide-in">
                <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                    <div>
                        <h2 class="text-3xl font-bold text-gray-900">Trajets</h2>
                        <p class="text-gray-600 mt-1">Consultez et gérez l'ensemble des trajets effectués</p>
                    </div>
                    
                    <!-- Filtre par date -->
                    <form method="get" action="${pageContext.request.contextPath}/trajets" 
                        class="flex items-center space-x-3 bg-white p-2 rounded-xl shadow-sm">
                        <div class="relative">
                            <i class="fas fa-calendar absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                            <input type="date" 
                                name="date" 
                                value="<%= date != null ? date : LocalDate.now() %>"
                                class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors">
                        </div>
                        <button type="submit" 
                                class="gradient-bg text-white px-4 py-2 rounded-lg hover:opacity-90 transition-all transform hover:scale-105 shadow-md flex items-center">
                            <i class="fas fa-filter mr-2"></i>
                            Filtrer
                        </button>
                        <% if (date != null && !date.isEmpty()) { %>
                            <a href="${pageContext.request.contextPath}/trajets" 
                            class="text-gray-500 hover:text-gray-700 transition-colors" 
                            title="Réinitialiser le filtre">
                                <i class="fas fa-times-circle"></i>
                            </a>
                        <% } %>
                    </form>
                </div>

                <!-- Cartes de statistiques -->
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
                    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 trajet-card">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-sm font-medium text-gray-500">Total trajets</p>
                                <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalTrajets %></p>
                            </div>
                            <div class="bg-blue-100 p-3 rounded-lg">
                                <i class="fas fa-route text-blue-600 text-2xl"></i>
                            </div>
                        </div>
                    </div>
                    
                    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 trajet-card">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-sm font-medium text-gray-500">Distance totale</p>
                                <p class="text-3xl font-bold text-gray-900 mt-2"><%= String.format("%.1f", totalDistance) %> km</p>
                            </div>
                            <div class="bg-green-100 p-3 rounded-lg">
                                <i class="fas fa-road text-green-600 text-2xl"></i>
                            </div>
                        </div>
                    </div>
                    
                    <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 trajet-card">
                        <div class="flex items-center justify-between">
                            <div>
                                <p class="text-sm font-medium text-gray-500">Réservations totales</p>
                                <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalReservations %></p>
                            </div>
                            <div class="bg-purple-100 p-3 rounded-lg">
                                <i class="fas fa-ticket-alt text-purple-600 text-2xl"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Liste des trajets -->
            <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden animate-slide-in">
                <div class="px-6 py-4 border-b border-gray-100 bg-gray-50 flex justify-between items-center">
                    <h3 class="text-lg font-semibold text-gray-800">
                        <i class="fas fa-list mr-2 text-blue-500"></i>
                        Liste des trajets
                    </h3>
                    <%if(date != null) { %>
                        <span class="bg-blue-100 text-blue-800 text-sm font-medium px-3 py-1 rounded-full">
                            <i class="far fa-calendar mr-1"></i>
                            <%= date %>
                        </span>
                    <% } %>
                </div>

                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-gray-200">
                        <thead class="bg-gray-50">
                            <tr>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date & Horaires</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Véhicule</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Distance</th>
                                <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Réservations</th>
                            </tr>
                        </thead>
                        <tbody class="bg-white divide-y divide-gray-200">
                            <% if (trajets != null && !trajets.isEmpty()) { 
                                for (TrajetDTO t : trajets) { 
                            %>
                                <tr class="hover:bg-gray-50 transition-colors">
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <span class="text-sm font-medium text-gray-900">#<%= t.getId() %></span>
                                    </td>
                                    <td class="px-6 py-4">
                                        <div class="flex items-center space-x-2">
                                            <div class="bg-blue-100 p-2 rounded-lg">
                                                <i class="fas fa-calendar-alt text-blue-600"></i>
                                            </div>
                                            <div>
                                                <p class="text-sm font-medium text-gray-900">
                                                    <%= t.getDateTrajet() %>
                                                </p>
                                                <p class="text-xs text-gray-500">
                                                    <i class="far fa-clock mr-1"></i>
                                                    Départ: <%= t.getHeureDepart() %> - Retour: <%= t.getHeureRetour() %>
                                                </p>
                                            </div>
                                        </div>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <% if (t.getVehicule() != null) { %>
                                            <div class="flex items-center">
                                                <div class="flex-shrink-0 h-8 w-8 gradient-bg-light rounded-lg flex items-center justify-center">
                                                    <i class="fas fa-car text-blue-600 text-sm"></i>
                                                </div>
                                                <div class="ml-3">
                                                    <p class="text-sm font-medium text-gray-900"><%= t.getVehicule().getReference() %> (<%= t.getVehicule().getTypeCarburant().getLibelle() %>)</p>
                                                    <p class="text-xs text-gray-500">Capacité: <%= t.getVehicule().getCapacite() %> pers.</p>
                                                </div>
                                            </div>
                                        <% } else { %>
                                            <span class="text-sm text-gray-400">Non assigné</span>
                                        <% } %>
                                    </td>
                                    <td class="px-6 py-4 whitespace-nowrap">
                                        <% if (t.getDistance() != null) { %>
                                            <span class="px-3 py-1 bg-green-100 text-green-800 rounded-full text-sm font-medium">
                                                <i class="fas fa-road mr-1"></i>
                                                <%= t.getDistance() %> km
                                            </span>
                                        <% } else { %>
                                            <span class="text-sm text-gray-400">-</span>
                                        <% } %>
                                    </td>
                                    <td class="px-6 py-4">
                                        <% 
                                            List<TrajetReservation> resList = t.getTrajetReservations();
                                            if(resList != null && !resList.isEmpty()){ 
                                        %>
                                            <div class="space-y-2">
                                                <div class="flex items-center justify-between">
                                                    <span class="text-sm font-medium text-gray-700">
                                                        <i class="fas fa-ticket-alt text-blue-500 mr-1"></i>
                                                        <%= resList.size() %> réservation(s)
                                                    </span>
                                                    <button onclick="toggleReservations(<%= t.getId() %>)" 
                                                            class="text-blue-600 hover:text-blue-800 text-sm transition-colors">
                                                        <i class="fas fa-chevron-down" id="chevron-<%= t.getId() %>"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        <% } else { %>
                                            <div class="flex items-center text-gray-400">
                                                <i class="fas fa-ticket-alt mr-2"></i>
                                                <span class="text-sm">Aucune réservation</span>
                                            </div>
                                        <% } %>
                                    </td>
                                </tr>
                                <tr id="reservations-<%= t.getId() %>" class="hidden">
                                    <td colspan="5" class="p-2 bg-gray-50" style="overflow:hidden">
                                        <table class="min-w-full text-sm" style="margin-left: 8%;">
                                            <thead>
                                                <tr class="text-xs text-gray-500 uppercase">
                                                    <th class="px-2 py-1 text-left">Resevation</th>
                                                    <th class="px-2 py-1 text-left">Client</th>
                                                    <th class="px-2 py-1 text-left">Passagers</th>
                                                    <th class="px-2 py-1 text-left">Hôtel</th>
                                                </tr>
                                            </thead>
                                            <tbody class="divide-y divide-gray-200">
                                                <% for(TrajetReservation r : resList){ %>
                                                    <tr>
                                                        <td class="px-2 py-2">
                                                            <div class="flex items-center">
                                                                <i class="fas fa-user text-gray-400 mr-1 text-xs"></i>
                                                                <span class="text-xs">R<%= r.getReservation().getId() %></span>
                                                            </div>
                                                        </td>
                                                        <td class="px-2 py-2">
                                                            <div class="flex items-center">
                                                                <i class="fas fa-user text-gray-400 mr-1 text-xs"></i>
                                                                <span class="text-xs"><%= r.getReservation().getIdClient() %></span>
                                                            </div>
                                                        </td>
                                                        <td class="px-2 py-2">
                                                            <span class="bg-blue-100 text-blue-800 px-2 py-0.5 rounded-full text-xs">
                                                                <%= r.getNombrePassager() %>
                                                            </span>
                                                        </td>
                                                        <td class="px-2 py-2 text-xs">
                                                            <% if (r.getReservation().getHotel() != null) { %>
                                                                <span class="bg-purple-100 text-purple-800 px-2 py-0.5 rounded-full">
                                                                    <%= r.getReservation().getHotel().getNom() %>
                                                                </span>
                                                            <% } else { %>
                                                                <span class="text-gray-400">-</span>
                                                            <% } %>
                                                        </td>
                                                    </tr>
                                                <% } %>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            <% } } else { %>
                                <tr>
                                    <td colspan="5" class="px-6 py-12 text-center">
                                        <div class="flex flex-col items-center">
                                            <div class="bg-gray-100 p-3 rounded-full mb-4">
                                                <i class="fas fa-route text-gray-400 text-4xl"></i>
                                            </div>
                                            <p class="text-gray-500 text-lg mb-2">Aucun trajet trouvé</p>
                                            <p class="text-gray-400 text-sm mb-4">
                                                <%= (date != null && !date.isEmpty()) ? 
                                                    "Aucun trajet pour la date sélectionnée" : 
                                                    "Aucun trajet n'a été enregistré" %>
                                            </p>
                                            <% if (date != null && !date.isEmpty()) { %>
                                                <a href="${pageContext.request.contextPath}/trajets" 
                                                class="gradient-bg text-white px-4 py-2 rounded-lg text-sm font-medium hover:opacity-90 transition-colors">
                                                    <i class="fas fa-times mr-2"></i>
                                                    Réinitialiser le filtre
                                                </a>
                                            <% } %>
                                        </div>
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <!-- JavaScript pour les interactions -->
    <script>
        function toggleReservations(trajetId) {
            const reservationsDiv = document.getElementById('reservations-' + trajetId);
            const chevron = document.getElementById('chevron-' + trajetId);
            
            if (reservationsDiv.classList.contains('hidden')) {
                reservationsDiv.classList.remove('hidden');
                chevron.classList.remove('fa-chevron-down');
                chevron.classList.add('fa-chevron-up');
            } else {
                reservationsDiv.classList.add('hidden');
                chevron.classList.remove('fa-chevron-up');
                chevron.classList.add('fa-chevron-down');
            }
        }
        
        // Animation d'entrée pour les lignes du tableau
        document.addEventListener('DOMContentLoaded', function() {
            const rows = document.querySelectorAll('tbody tr');
            rows.forEach((row, index) => {
                row.style.animation = `fadeIn 0.3s ease-out ${index * 0.1}s forwards`;
                row.style.opacity = '0';
            });
        });
    </script>

    <!-- Footer -->
    <footer class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 mt-8">
        <p class="text-center text-sm text-gray-500">
            <i class="far fa-copyright mr-1"></i>
            <%= java.time.LocalDate.now().getYear() %> Gestion de Parc Automobile. Tous droits réservés.
        </p>
    </footer>
</body>
</html>