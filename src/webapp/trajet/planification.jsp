<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Planification des trajets | Parc Auto</title>
    
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
        
        @keyframes pulse {
            0%, 100% { transform: scale(1); }
            50% { transform: scale(1.05); }
        }
        
        @keyframes spin {
            from { transform: rotate(0deg); }
            to { transform: rotate(360deg); }
        }
        
        .animate-slide-in {
            animation: slideIn 0.5s ease-out forwards;
        }
        
        .animate-fade-in {
            animation: fadeIn 0.3s ease-out forwards;
        }
        
        .animate-pulse-slow {
            animation: pulse 2s infinite;
        }
        
        .gradient-bg {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
        }
        
        .gradient-bg-light {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
        }
        
        .gradient-success {
            background: linear-gradient(135deg, #43a047 0%, #66bb6a 100%);
        }
        
        .card-hover:hover {
            transform: translateY(-4px);
            box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
            transition: all 0.3s ease;
        }
        
        .loading-spinner {
            animation: spin 1s linear infinite;
        }
    </style>
</head>
<body class="bg-gray-50">
    <%
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String message = (String) request.getAttribute("message");
        String error = (String) request.getAttribute("error");
    %>

    <!-- Navigation -->
    <nav class="gradient-bg text-white shadow-lg sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between items-center h-16">
                <div class="flex items-center space-x-4">
                    <div class="bg-white/10 p-2 rounded-lg">
                        <i class="fas fa-calendar-alt text-2xl"></i>
                    </div>
                    <h1 class="text-xl font-bold">Planification des trajets</h1>
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
        <div class="max-w-4xl mx-auto animate-slide-in">
            <!-- En-tête -->
            <div class="mb-8 animate-slide-in">
                <div class="text-center">
                    <h2 class="text-3xl font-bold text-gray-900">Planification automatique</h2>
                    <p class="text-gray-600 mt-2 max-w-2xl mx-auto">
                        Lancez l'assignation automatique des véhicules pour optimiser la gestion de votre flotte
                    </p>
                </div>
            </div>

            <!-- Messages d'alerte -->
            <% if (message != null && !message.isEmpty()) { %>
                <div class="mb-6 bg-green-50 border-l-4 border-green-500 p-4 rounded-lg flex items-start animate-fade-in">
                    <i class="fas fa-check-circle text-green-500 mt-0.5 mr-3"></i>
                    <div>
                        <p class="text-green-800 font-medium">Succès !</p>
                        <p class="text-green-600 text-sm"><%= message %></p>
                    </div>
                    <button onclick="this.parentElement.remove()" class="ml-auto text-green-600 hover:text-green-800">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            <% } %>

            <% if (error != null && !error.isEmpty()) { %>
                <div class="mb-6 bg-red-50 border-l-4 border-red-500 p-4 rounded-lg flex items-start animate-fade-in">
                    <i class="fas fa-exclamation-circle text-red-500 mt-0.5 mr-3"></i>
                    <div>
                        <p class="text-red-800 font-medium">Erreur</p>
                        <p class="text-red-600 text-sm"><%= error %></p>
                    </div>
                    <button onclick="this.parentElement.remove()" class="ml-auto text-red-600 hover:text-red-800">
                        <i class="fas fa-times"></i>
                    </button>
                </div>
            <% } %>

            <!-- Carte principale -->
            <div class="bg-white rounded-2xl shadow-xl border border-gray-100 overflow-hidden animate-slide-in">
                <!-- Corps de la carte -->
                <div class="p-8">
                    <!-- Formulaire d'assignation -->
                    <form action="assigner" method="post" id="assignmentForm" class="space-y-6">
                        <!-- Zone d'action principale -->
                        <div class="flex flex-col items-center space-y-4">
                            <button type="submit" 
                                    id="submitBtn"
                                    class="gradient-success text-white px-12 py-4 rounded-xl font-semibold hover:opacity-90 transition-all transform hover:scale-105 shadow-lg flex items-center justify-center space-x-3 w-full md:w-auto min-w-[300px]">
                                <i class="fas fa-play text-xl" id="buttonIcon"></i>
                                <span id="buttonText">Lancer l'assignation automatique</span>
                            </button>
                            
                            <p class="text-xs text-gray-500 flex items-center">
                                <i class="fas fa-info-circle mr-1 text-blue-500"></i>
                                Cette opération peut prendre quelques secondes
                            </p>
                        </div>
                    </form>

                    <!-- Informations complémentaires -->
                    <div class="mt-8 pt-6 border-t border-gray-200">
                        <div class="flex items-start space-x-4 text-sm text-gray-600">
                            <div class="bg-blue-100 p-2 rounded-lg flex-shrink-0">
                                <i class="fas fa-lightbulb text-blue-600"></i>
                            </div>
                            <div>
                                <p class="font-medium text-gray-700 mb-1">Comment fonctionne l'assignation ?</p>
                                <p class="text-xs leading-relaxed">
                                    L'algorithme analyse automatiquement toutes les réservations en cours et assigne 
                                    les véhicules les plus adaptés en fonction de leur capacité, du type de carburant 
                                    et de la distance à parcourir. Les trajets sont ensuite créés automatiquement.
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal de chargement -->
    <div id="loadingModal" class="fixed inset-0 z-50 hidden overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
        <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
            <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity modal-transition"></div>

            <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>

            <div class="inline-block align-bottom bg-white rounded-2xl text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full">
                <div class="gradient-bg px-6 py-4">
                    <h3 class="text-lg font-semibold text-white text-center">
                        <i class="fas fa-cog loading-spinner mr-2"></i>
                        Traitement en cours
                    </h3>
                </div>
                <div class="p-8 text-center">
                    <div class="flex justify-center mb-4">
                        <div class="gradient-bg w-20 h-20 rounded-full flex items-center justify-center">
                            <i class="fas fa-robot text-white text-4xl animate-pulse"></i>
                        </div>
                    </div>
                    <p class="text-gray-700 font-medium mb-2">Assignation automatique en cours...</p>
                    <p class="text-sm text-gray-500">Notre algorithme optimise vos trajets</p>
                    <div class="mt-6 w-full bg-gray-200 rounded-full h-2">
                        <div class="gradient-bg h-2 rounded-full animate-pulse" style="width: 60%"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- JavaScript -->
    <script>
        // Gestion de la soumission du formulaire avec animation de chargement
        document.getElementById('assignmentForm').addEventListener('submit', function(e) {
            const submitBtn = document.getElementById('submitBtn');
            const buttonText = document.getElementById('buttonText');
            const buttonIcon = document.getElementById('buttonIcon');
            
            // Désactiver le bouton pour éviter les doubles soumissions
            submitBtn.disabled = true;
            submitBtn.classList.remove('gradient-success', 'hover:scale-105');
            submitBtn.classList.add('bg-gray-400', 'cursor-not-allowed');
            
            // Changer le texte et l'icône
            buttonText.textContent = 'Traitement en cours...';
            buttonIcon.className = 'fas fa-spinner loading-spinner text-xl';
            
            // Afficher le modal de chargement
            document.getElementById('loadingModal').classList.remove('hidden');
            
            // Le formulaire sera soumis normalement
        });

        // Animation d'entrée pour les cartes de statistiques
        document.addEventListener('DOMContentLoaded', function() {
            const cards = document.querySelectorAll('.card-hover');
            cards.forEach((card, index) => {
                card.style.animation = `fadeIn 0.3s ease-out ${index * 0.1 + 0.3}s forwards`;
                card.style.opacity = '0';
            });
        });

        // Gestion des messages qui disparaissent automatiquement
        setTimeout(function() {
            const alerts = document.querySelectorAll('.bg-green-50, .bg-red-50');
            alerts.forEach(alert => {
                alert.style.transition = 'opacity 0.5s ease';
                alert.style.opacity = '0';
                setTimeout(() => alert.remove(), 500);
            });
        }, 5000);
    </script>

    <!-- Footer -->
    <footer class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 mt-12">
        <p class="text-center text-sm text-gray-500">
            <i class="far fa-copyright mr-1"></i>
            <%= java.time.LocalDate.now().getYear() %> Gestion de Parc Automobile. Tous droits réservés.
        </p>
    </footer>
</body>
</html>