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
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    // Statistiques
    int totalReservations = reservations != null ? reservations.size() : 0;
    int totalPassagers = 0;
    if (reservations != null) {
        for (Reservation r : reservations) {
            totalPassagers += r.getNombrePassager();
        }
    }
    
    // Compter les hôtels uniques
    long totalHotels = reservations != null ? reservations.stream()
            .map(r -> r.getHotel() != null ? r.getHotel().getId() : null)
            .distinct()
            .count() : 0;
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
        
        .gradient-bg-orange {
            background: linear-gradient(135deg, #e67e22 0%, #f39c12 100%);
        }
        
        .gradient-bg-light {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }
        
        .badge-non-assigne {
            background: linear-gradient(135deg, #e67e22 0%, #f39c12 100%);
        }
        
        .table-row-hover:hover {
            background-color: #fff7ed;
            transition: background-color 0.2s ease;
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
                        <i class="fas fa-ticket-alt text-2xl"></i>
                    </div>
                    <h1 class="text-xl font-bold">Réservations non assignées</h1>
                </div>
                <div class="flex items-center space-x-4 text-sm">
                    <span class="bg-white/20 px-3 py-1 rounded-full">
                        <i class="far fa-clock mr-1"></i>
                        <%= LocalDateTime.now().format(timeFormatter) %>
                    </span>
                    <span class="bg-orange-500 px-3 py-1 rounded-full">
                        <i class="fas fa-exclamation-triangle mr-1"></i>
                        En attente
                    </span>
                </div>
            </div>
        </div>
    </nav>

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- En-tête avec statistiques -->
        <div class="mb-8 animate-slide-in">
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h2 class="text-3xl font-bold text-gray-900">Réservations sans trajet</h2>
                    <p class="text-gray-600 mt-1">Consultez les réservations qui n'ont pas encore été assignées à un trajet</p>
                </div>
                
                <!-- Filtre par date -->
                <form method="get" action="<%=request.getContextPath()%>/reservations/non-assigner" 
                      class="flex items-center space-x-3 bg-white p-2 rounded-xl shadow-sm">
                    <div class="relative">
                        <i class="fas fa-calendar absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400"></i>
                        <input type="date" 
                               name="date" 
                               value="<%= date != null ? date : "" %>"
                               class="pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-orange-500 focus:border-orange-500 transition-colors">
                    </div>
                    <button type="submit" 
                            class="gradient-bg-orange text-white px-4 py-2 rounded-lg hover:opacity-90 transition-all transform hover:scale-105 shadow-md flex items-center">
                        <i class="fas fa-filter mr-2"></i>
                        Filtrer
                    </button>
                    <% if (date != null && !date.isEmpty()) { %>
                        <a href="<%=request.getContextPath()%>/reservations/non-assigner" 
                           class="text-gray-500 hover:text-gray-700 transition-colors" 
                           title="Réinitialiser le filtre">
                            <i class="fas fa-times-circle text-xl"></i>
                        </a>
                    <% } %>
                </form>
            </div>

            <!-- Cartes de statistiques -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
                <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 reservation-card">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Réservations en attente</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalReservations %></p>
                        </div>
                        <div class="bg-orange-100 p-3 rounded-lg">
                            <i class="fas fa-clock text-orange-600 text-2xl"></i>
                        </div>
                    </div>
                </div>
                
                <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 reservation-card">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Total passagers</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalPassagers %></p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <i class="fas fa-users text-blue-600 text-2xl"></i>
                        </div>
                    </div>
                    <p class="text-xs text-gray-500 mt-2">
                        <i class="fas fa-info-circle mr-1"></i>
                        Moyenne de <%= totalReservations > 0 ? String.format("%.1f", (double) totalPassagers / totalReservations) : 0 %> pers./réservation
                    </p>
                </div>
                
                <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 reservation-card">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Hôtels concernés</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalHotels %></p>
                        </div>
                        <div class="bg-purple-100 p-3 rounded-lg">
                            <i class="fas fa-hotel text-purple-600 text-2xl"></i>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Message d'information -->
            <div class="mt-6 bg-orange-50 border-l-4 border-orange-500 p-4 rounded-r-lg">
                <div class="flex items-center">
                    <div class="flex-shrink-0">
                        <i class="fas fa-exclamation-circle text-orange-500 text-xl"></i>
                    </div>
                    <div class="ml-3">
                        <p class="text-sm text-orange-700">
                            <span class="font-medium">Attention :</span>
                            Ces réservations nécessitent d'être assignées à un trajet. 
                            <%= totalReservations %> réservation(s) en attente pour un total de <%= totalPassagers %> passager(s).
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Liste des réservations -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden animate-slide-in">
            <div class="px-6 py-4 border-b border-gray-100 bg-gray-50 flex justify-between items-center">
                <h3 class="text-lg font-semibold text-gray-800">
                    <i class="fas fa-list mr-2 text-orange-500"></i>
                    Liste des réservations non assignées
                </h3>
                <div class="flex items-center space-x-3">
                    <span class="bg-orange-100 text-orange-800 text-sm font-medium px-3 py-1 rounded-full">
                        <i class="fas fa-ticket-alt mr-1"></i>
                        <%= totalReservations %> réservation(s)
                    </span>
                    <% if (totalReservations > 0) { %>
                        <button onclick="exportTableToCSV()" class="text-gray-500 hover:text-gray-700 transition-colors" title="Exporter en CSV">
                            <i class="fas fa-download"></i>
                        </button>
                    <% } %>
                </div>
            </div>

            <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Client</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Passagers</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date d'arrivée</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Hôtel</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Statut</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <% if (reservations != null && !reservations.isEmpty()) { 
                            for (Reservation r : reservations) { 
                                String rowClass = "table-row-hover";
                        %>
                            <tr class="<%= rowClass %> transition-colors">
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <span class="bg-orange-100 text-orange-800 font-mono text-sm font-medium px-2.5 py-0.5 rounded">
                                            #<%= r.getId() %>
                                        </span>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <div class="flex-shrink-0 h-8 w-8 bg-blue-100 rounded-full flex items-center justify-center">
                                            <i class="fas fa-user text-blue-600 text-sm"></i>
                                        </div>
                                        <div class="ml-3">
                                            <p class="text-sm font-medium text-gray-900">Client #<%= r.getIdClient() %></p>
                                        </div>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <span class="bg-blue-100 text-blue-800 px-3 py-1 rounded-full text-sm font-medium">
                                            <i class="fas fa-user-friends mr-1"></i>
                                            <%= r.getNombrePassager() %>
                                        </span>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <i class="far fa-calendar-alt text-gray-400 mr-2"></i>
                                        <div>
                                            <p class="text-sm text-gray-900">
                                                <%= r.getDateArrivee() != null ? 
                                                    r.getDateArrivee().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "Non définie" %>
                                            </p>
                                            <p class="text-xs text-gray-500">
                                                <%= r.getDateArrivee() != null ? 
                                                    r.getDateArrivee().format(DateTimeFormatter.ofPattern("HH:mm")) : "" %>
                                            </p>
                                        </div>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <% if (r.getHotel() != null) { %>
                                        <div class="flex items-center">
                                            <div class="flex-shrink-0 h-8 w-8 bg-purple-100 rounded-lg flex items-center justify-center">
                                                <i class="fas fa-hotel text-purple-600 text-sm"></i>
                                            </div>
                                            <div class="ml-3">
                                                <p class="text-sm font-medium text-gray-900"><%= r.getHotel().getNom() %></p>
                                            </div>
                                        </div>
                                    <% } else { %>
                                        <span class="text-sm text-gray-400">-</span>
                                    <% } %>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <span class="badge-non-assigne text-white px-3 py-1 rounded-full text-xs font-medium">
                                        <i class="fas fa-hourglass-half mr-1"></i>
                                        Non assignée
                                    </span>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="6" class="px-6 py-12 text-center">
                                    <div class="flex flex-col items-center">
                                        <div class="bg-gray-100 p-3 rounded-full mb-4">
                                            <i class="fas fa-ticket-alt text-gray-400 text-4xl"></i>
                                        </div>
                                        <p class="text-gray-500 text-lg mb-2">Aucune réservation en attente</p>
                                        <p class="text-gray-400 text-sm mb-4">
                                            <%= (date != null && !date.isEmpty()) ? 
                                                "Aucune réservation non assignée pour la date sélectionnée" : 
                                                "Toutes les réservations sont assignées à des trajets" %>
                                        </p>
                                        <% if (date != null && !date.isEmpty()) { %>
                                            <a href="<%=request.getContextPath()%>/reservation/non-assigne" 
                                               class="gradient-bg-orange text-white px-4 py-2 rounded-lg text-sm font-medium hover:opacity-90 transition-colors">
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
            
            <% if (reservations != null && !reservations.isEmpty()) { %>
            <!-- Pied de tableau avec résumé -->
            <div class="bg-gray-50 px-6 py-3 border-t border-gray-200">
                <div class="flex items-center justify-between text-sm">
                    <div class="text-gray-600">
                        <i class="fas fa-chart-pie mr-2"></i>
                        <span class="font-medium">Résumé :</span>
                        <span class="ml-2"><%= totalReservations %> réservation(s)</span>
                        <span class="mx-2">•</span>
                        <span><%= totalPassagers %> passager(s)</span>
                    </div>
                    <div class="text-gray-500">
                        <i class="far fa-clock mr-1"></i>
                        Dernière mise à jour : <%= LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) %>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </main>

    <!-- JavaScript pour les interactions -->
    <script>
        // Animation d'entrée pour les lignes du tableau
        document.addEventListener('DOMContentLoaded', function() {
            const rows = document.querySelectorAll('tbody tr');
            rows.forEach((row, index) => {
                row.style.animation = `fadeIn 0.3s ease-out ${index * 0.05}s forwards`;
                row.style.opacity = '0';
            });
        });
        
        // Fonction d'export CSV
        function exportTableToCSV() {
            const rows = document.querySelectorAll('table tbody tr');
            const csv = [];
            
            // En-têtes
            const headers = ['ID', 'Client', 'Passagers', 'Date arrivée', 'Hôtel', 'Statut'];
            csv.push(headers.join(','));
            
            // Données
            rows.forEach(row => {
                if (row.querySelector('td[colspan]')) return; // Ignorer les lignes "Aucune donnée"
                
                const rowData = [];
                const cells = row.querySelectorAll('td');
                cells.forEach(cell => {
                    let text = cell.textContent.trim().replace(/,/g, ';'); // Éviter les conflits CSV
                    rowData.push(text);
                });
                csv.push(rowData.join(','));
            });
            
            // Téléchargement
            const blob = new Blob([csv.join('\n')], { type: 'text/csv' });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'reservations_non_assignees_<%= date != null ? date : "toutes" %>.csv';
            a.click();
        }
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