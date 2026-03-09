<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Hotel" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservation d'hôtel | Formulaire</title>
    
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
        
        .animate-slide-in {
            animation: slideIn 0.5s ease-out forwards;
        }
        
        .gradient-bg {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
        }
        
        .hover-lift:hover {
            transform: translateY(-2px);
            transition: transform 0.2s ease;
        }
        
        .input-focus-effect:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
    </style>
</head>
<body class="gradient-bg min-h-screen flex items-center justify-center p-4">
    <%
        String message = (String) request.getAttribute("message");
        String error = (String) request.getAttribute("error");
        List<Hotel> hotels = (List<Hotel>) request.getAttribute("hotels");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    %>

    <!-- Conteneur principal -->
    <div class="max-w-2xl w-full animate-slide-in">
        <!-- En-tête avec titre -->
        <div class="text-center mb-8">
            <div class="inline-block p-4 bg-white/10 backdrop-blur-lg rounded-full mb-4">
                <i class="fas fa-hotel text-white text-4xl"></i>
            </div>
            <h1 class="text-4xl font-bold text-white mb-2">Réservation d'hôtel</h1>
            <p class="text-white/80 text-lg">Complétez le formulaire ci-dessous pour réserver votre séjour</p>
        </div>

        <!-- Carte principale -->
        <div class="bg-white rounded-2xl shadow-2xl overflow-hidden">
            <!-- Barre de progression (optionnelle) -->
            <div class="bg-gray-50 px-8 py-4 border-b border-gray-200">
                <div class="flex items-center justify-between">
                    <div class="flex items-center space-x-2 text-sm">
                        <div class="w-2 h-2 bg-green-500 rounded-full"></div>
                        <span class="text-gray-600">Disponibilités en temps réel</span>
                    </div>
                    <div class="text-sm text-gray-500">
                        <i class="far fa-clock mr-1"></i>
                        <%= LocalDateTime.now().format(timeFormatter) %>
                    </div>
                </div>
            </div>

            <!-- Corps du formulaire -->
            <div class="p-8">
                <!-- Messages d'alerte -->
                <% if (message != null && !message.isEmpty()) { %>
                    <div class="mb-6 bg-green-50 border-l-4 border-green-500 p-4 rounded-lg flex items-start">
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
                    <div class="mb-6 bg-red-50 border-l-4 border-red-500 p-4 rounded-lg flex items-start">
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

                <form action="${pageContext.request.contextPath}/reservation/reserver" method="post" class="space-y-6" id="reservationForm">
                    <!-- ID Client avec validation visuelle -->
                    <div class="space-y-2">
                        <label class="block text-sm font-semibold text-gray-700">
                            <i class="fas fa-id-card text-indigo-500 mr-2"></i>
                            ID Client
                            <span class="text-red-500 ml-1">*</span>
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <i class="fas fa-hashtag text-gray-400"></i>
                            </div>
                            <input type="text" 
                                   name="idClient" 
                                   maxlength="4"
                                   pattern="[0-9]{4}"
                                   title="Veuillez saisir exactement 4 chiffres"
                                   placeholder="1234"
                                   class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
                                   required
                                   oninput="this.value = this.value.replace(/[^0-9]/g, '')">
                        </div>
                        <div class="flex items-center text-xs text-gray-500">
                            <i class="fas fa-info-circle mr-1"></i>
                            Saisissez exactement 4 chiffres
                        </div>
                    </div>

                    <!-- Nombre de passagers avec sélecteur visuel -->
                    <div class="space-y-2">
                        <label class="block text-sm font-semibold text-gray-700">
                            <i class="fas fa-users text-indigo-500 mr-2"></i>
                            Nombre de passagers
                            <span class="text-red-500 ml-1">*</span>
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <i class="fas fa-user text-gray-400"></i>
                            </div>
                            <input type="number" 
                                   name="nombrePassager" 
                                   min="1"
                                   max="10"
                                   value="1"
                                   class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
                                   required>
                        </div>
                        <div class="flex justify-between text-xs">
                            <span class="text-gray-500"><i class="fas fa-info-circle mr-1"></i>Minimum 1 personne</span>
                            <span class="text-gray-500">Maximum 10 personnes</span>
                        </div>
                    </div>

                    <!-- Date et heure d'arrivée avec design amélioré -->
                    <div class="space-y-2">
                        <label class="block text-sm font-semibold text-gray-700">
                            <i class="fas fa-calendar-alt text-indigo-500 mr-2"></i>
                            Date et heure d'arrivée
                            <span class="text-red-500 ml-1">*</span>
                        </label>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
                            <div class="relative">
                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <i class="fas fa-calendar text-gray-400"></i>
                                </div>
                                <input type="date" 
                                       id="dateArrivee"
                                       class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
                                       required>
                            </div>
                            <div class="relative">
                                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                    <i class="fas fa-clock text-gray-400"></i>
                                </div>
                                <input type="time" 
                                       id="timeArrive"
                                       class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors"
                                       required>
                            </div>
                        </div>
                        <!-- Champ caché pour la combinaison date+time -->
                        <input type="hidden" name="dateArrivee" id="dateArriveeCombined">
                    </div>

                    <!-- Sélection hôtel avec design moderne -->
                    <div class="space-y-2">
                        <label class="block text-sm font-semibold text-gray-700">
                            <i class="fas fa-building text-indigo-500 mr-2"></i>
                            Hôtel
                            <span class="text-red-500 ml-1">*</span>
                        </label>
                        <div class="relative">
                            <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                <i class="fas fa-map-marker-alt text-gray-400"></i>
                            </div>
                            <select name="idHotel" class="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-xl focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-colors appearance-none bg-white" required>
                                <option value="" selected disabled>Sélectionnez un hôtel</option>
                                <% if (hotels != null && !hotels.isEmpty()) { 
                                    for (Hotel h : hotels) { 
                                        // Simulation de disponibilité (à remplacer par des données réelles)
                                        boolean disponible = Math.random() > 0.3;
                                %>
                                    <option value="<%= h.getId() %>" class="py-2">
                                        🏨 <%= h.getNom() %>
                                        <% if (disponible) { %>
                                            <span class="text-green-500 text-xs ml-2">● Disponible</span>
                                        <% } %>
                                    </option>
                                <% } } else { %>
                                    <option value="" disabled>Aucun hôtel disponible</option>
                                <% } %>
                            </select>
                            <div class="absolute inset-y-0 right-0 pr-3 flex items-center pointer-events-none">
                                <i class="fas fa-chevron-down text-gray-400"></i>
                            </div>
                        </div>
                        <% if (hotels != null && !hotels.isEmpty()) { %>
                            <div class="flex items-center text-xs text-gray-500">
                                <i class="fas fa-info-circle mr-1"></i>
                                <%= hotels.size() %> hôtel(s) disponible(s) à la réservation
                            </div>
                        <% } %>
                    </div>

                    <!-- Récapitulatif rapide (optionnel) -->
                    <div class="bg-indigo-50 rounded-xl p-4 mt-6">
                        <h3 class="text-sm font-semibold text-indigo-900 mb-2 flex items-center">
                            <i class="fas fa-clipboard-list mr-2"></i>
                            Récapitulatif de votre réservation
                        </h3>
                        <div class="text-xs text-indigo-700 space-y-1" id="recap">
                            <p><i class="fas fa-id-card mr-1"></i> Client: <span id="recapClient">Non spécifié</span></p>
                            <p><i class="fas fa-users mr-1"></i> Passagers: <span id="recapPassagers">1</span></p>
                            <p><i class="fas fa-calendar mr-1"></i> Arrivée: <span id="recapDate">Non spécifiée</span></p>
                            <p><i class="fas fa-building mr-1"></i> Hôtel: <span id="recapHotel">Non sélectionné</span></p>
                        </div>
                    </div>

                    <!-- Bouton de soumission -->
                    <button type="submit" 
                            class="w-full gradient-bg text-white py-4 px-6 rounded-xl font-semibold text-lg hover:opacity-90 transition-all duration-200 transform hover:scale-[1.02] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 flex items-center justify-center">
                        <i class="fas fa-check-circle mr-3"></i>
                        Confirmer la réservation
                    </button>

                    <!-- Lien retour -->
                    <div class="text-center mt-4">
                        <a href="${pageContext.request.contextPath}/" 
                           class="inline-flex items-center text-sm text-gray-500 hover:text-gray-700 transition-colors">
                            <i class="fas fa-arrow-left mr-2"></i>
                            Retour à l'accueil
                        </a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Pied de page avec informations -->
        <div class="mt-6 text-center text-white/80 text-sm">
            <div class="flex items-center justify-center space-x-4">
                <span><i class="fas fa-shield-alt mr-1"></i> Paiement sécurisé</span>
                <span>•</span>
                <span><i class="fas fa-lock mr-1"></i> Données confidentielles</span>
                <span>•</span>
                <span><i class="fas fa-clock mr-1"></i> Confirmation immédiate</span>
            </div>
            <p class="mt-2 text-white/60 text-xs">
                Les champs marqués d'un astérisque (*) sont obligatoires
            </p>
        </div>
    </div>

    <!-- JavaScript pour les fonctionnalités interactives -->
    <script>
        // Configuration de la date minimum (aujourd'hui)
        document.addEventListener('DOMContentLoaded', function() {
            const dateInput = document.getElementById('dateArrivee');
            const timeInput = document.getElementById('timeArrive');
            const hiddenInput = document.getElementById('dateArriveeCombined');
            
            // Set minimum date to today
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            dateInput.min = `${yyyy}-${mm}-${dd}`;
            
            // Set default time to now + 1 hour (rounded to nearest hour)
            const now = new Date();
            now.setHours(now.getHours() + 1);
            now.setMinutes(0);
            const hours = String(now.getHours()).padStart(2, '0');
            timeInput.value = `${hours}:00`;

            // Mettre à jour le champ caché et le récapitulatif
            function updateDateTime() {
                if (dateInput.value && timeInput.value) {
                    hiddenInput.value = `${dateInput.value}T${timeInput.value}`;
                    updateRecap();
                }
            }

            dateInput.addEventListener('change', updateDateTime);
            timeInput.addEventListener('change', updateDateTime);
            
            // Initial update
            updateDateTime();

            // Gestion de l'ID client pour le récapitulatif
            document.querySelector('input[name="idClient"]').addEventListener('input', function(e) {
                document.getElementById('recapClient').textContent = this.value || 'Non spécifié';
            });

            // Gestion du nombre de passagers
            document.querySelector('input[name="nombrePassager"]').addEventListener('input', function(e) {
                document.getElementById('recapPassagers').textContent = this.value || '0';
            });

            // Gestion de l'hôtel
            document.querySelector('select[name="idHotel"]').addEventListener('change', function(e) {
                const selectedOption = this.options[this.selectedIndex];
                const hotelName = selectedOption ? selectedOption.text.replace(/[● Disponible]/g, '').trim() : 'Non sélectionné';
                document.getElementById('recapHotel').textContent = hotelName;
            });

            function updateRecap() {
                const dateValue = dateInput.value;
                const timeValue = timeInput.value;
                if (dateValue && timeValue) {
                    const [year, month, day] = dateValue.split('-');
                    document.getElementById('recapDate').textContent = `${day}/${month}/${year} à ${timeValue}`;
                }
            }

            // Validation du formulaire
            document.getElementById('reservationForm').addEventListener('submit', function(e) {
                const idClient = document.querySelector('input[name="idClient"]').value;
                if (!/^\d{4}$/.test(idClient)) {
                    e.preventDefault();
                    alert('L\'ID client doit contenir exactement 4 chiffres.');
                    return;
                }

                if (!dateInput.value || !timeInput.value) {
                    e.preventDefault();
                    alert('Veuillez sélectionner une date et une heure d\'arrivée.');
                    return;
                }

                // Vérifier que la date n'est pas dans le passé
                const selectedDate = new Date(hiddenInput.value);
                if (selectedDate < new Date()) {
                    e.preventDefault();
                    alert('La date d\'arrivée ne peut pas être dans le passé.');
                }
            });
        });
    </script>
</body>
</html>