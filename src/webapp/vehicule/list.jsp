<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Vehicule" %>
<%@ page import="com.example.backoffice.model.TypeCarburant" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Véhicules | Parc Auto</title>
    
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
        
        .vehicle-card:hover {
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
        
        .modal-transition {
            transition: opacity 0.3s ease;
        }
    </style>
</head>
<body class="bg-gray-50">
    <%
        String message = (String) request.getAttribute("message");
        String error = (String) request.getAttribute("error");
        List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
        List<TypeCarburant> types = (List<TypeCarburant>) request.getAttribute("typeCarburants");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        // Statistiques
        int totalVehicules = vehicules != null ? vehicules.size() : 0;
        long totalCapacite = vehicules != null ? 
            vehicules.stream().mapToInt(Vehicule::getCapacite).sum() : 0;
        long typesCarburant = types != null ? types.size() : 0;
    %>

    <!-- Navigation -->
    <nav class="gradient-bg text-white shadow-lg sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div class="flex justify-between items-center h-16">
                <div class="flex items-center space-x-4">
                    <div class="bg-white/10 p-2 rounded-lg">
                        <i class="fas fa-car text-2xl"></i>
                    </div>
                    <h1 class="text-xl font-bold">Gestion du Parc Automobile</h1>
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

    <main class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <!-- En-tête avec statistiques -->
        <div class="mb-8 animate-slide-in">
            <div class="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
                <div>
                    <h2 class="text-3xl font-bold text-gray-900">Véhicules</h2>
                    <p class="text-gray-600 mt-1">Gérez l'ensemble de votre flotte de véhicules</p>
                </div>
                <button onclick="openAddModal()" 
                        class="gradient-bg text-white px-6 py-3 rounded-xl font-semibold hover:opacity-90 transition-all transform hover:scale-105 shadow-lg flex items-center justify-center">
                    <i class="fas fa-plus-circle mr-2"></i>
                    Nouveau véhicule
                </button>
            </div>

            <!-- Cartes de statistiques -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mt-8">
                <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 vehicle-card">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Total véhicules</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalVehicules %></p>
                        </div>
                        <div class="bg-blue-100 p-3 rounded-lg">
                            <i class="fas fa-car text-blue-600 text-2xl"></i>
                        </div>
                    </div>
                </div>
                
                <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 vehicle-card">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Capacité totale</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2"><%= totalCapacite %></p>
                        </div>
                        <div class="bg-green-100 p-3 rounded-lg">
                            <i class="fas fa-users text-green-600 text-2xl"></i>
                        </div>
                    </div>
                </div>
                
                <div class="bg-white rounded-xl shadow-sm p-6 border border-gray-100 vehicle-card">
                    <div class="flex items-center justify-between">
                        <div>
                            <p class="text-sm font-medium text-gray-500">Types carburant</p>
                            <p class="text-3xl font-bold text-gray-900 mt-2"><%= typesCarburant %></p>
                        </div>
                        <div class="bg-purple-100 p-3 rounded-lg">
                            <i class="fas fa-gas-pump text-purple-600 text-2xl"></i>
                        </div>
                    </div>
                </div>
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

        <!-- Liste des véhicules -->
        <div class="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden animate-slide-in">
            <div class="px-6 py-4 border-b border-gray-100 bg-gray-50 flex justify-between items-center">
                <h3 class="text-lg font-semibold text-gray-800">
                    <i class="fas fa-list mr-2 text-blue-500"></i>
                    Liste des véhicules
                </h3>
                <span class="bg-blue-100 text-blue-800 text-sm font-medium px-3 py-1 rounded-full">
                    <%= totalVehicules %> véhicule(s)
                </span>
            </div>

            <div class="overflow-x-auto">
                <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                        <tr>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Référence</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Capacité</th>
                            <th class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Carburant</th>
                            <th class="px-6 py-3 text-center text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                        </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                        <% if (vehicules != null && !vehicules.isEmpty()) { 
                            for (Vehicule v : vehicules) { 
                        %>
                            <tr class="hover:bg-gray-50 transition-colors">
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <span class="text-sm font-medium text-gray-900">#<%= v.getId() %></span>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <div class="flex-shrink-0 h-10 w-10 gradient-bg-light rounded-lg flex items-center justify-center">
                                            <i class="fas fa-car text-blue-600"></i>
                                        </div>
                                        <div class="ml-4">
                                            <p class="text-sm font-medium text-gray-900"><%= v.getReference() %></p>
                                            <p class="text-xs text-gray-500">Référence unique</p>
                                        </div>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <div class="flex items-center">
                                        <i class="fas fa-user text-gray-400 mr-2"></i>
                                        <span class="text-sm text-gray-900"><%= v.getCapacite() %> passagers</span>
                                    </div>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap">
                                    <% if (v.getTypeCarburant() != null) { 
                                        String colorClass = "";
                                        switch(v.getTypeCarburant().getLibelle().toLowerCase()) {
                                            case "essence": colorClass = "bg-orange-100 text-orange-800"; break;
                                            case "diesel": colorClass = "bg-blue-100 text-blue-800"; break;
                                            case "électrique": colorClass = "bg-green-100 text-green-800"; break;
                                            default: colorClass = "bg-gray-100 text-gray-800";
                                        }
                                    %>
                                        <span class="px-2 py-1 text-xs font-medium <%= colorClass %> rounded-full">
                                            <i class="fas fa-gas-pump mr-1"></i>
                                            <%= v.getTypeCarburant().getLibelle() %>
                                        </span>
                                    <% } else { %>
                                        <span class="text-sm text-gray-400">Non spécifié</span>
                                    <% } %>
                                </td>
                                <td class="px-6 py-4 whitespace-nowrap text-center">
                                    <button onclick="openEditModal(<%= v.getId() %>, '<%= v.getReference() %>', <%= v.getCapacite() %>, <%= v.getTypeCarburant() != null ? v.getTypeCarburant().getId() : "null" %>)" 
                                            class="text-indigo-600 hover:text-indigo-900 mx-2 transition-colors">
                                        <i class="fas fa-edit"></i>
                                    </button>
                                    <form action="${pageContext.request.contextPath}/vehicules" method="post" class="inline">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="<%= v.getId() %>">
                                        <button type="submit" 
                                                onclick="return confirm('Êtes-vous sûr de vouloir supprimer ce véhicule ? Cette action est irréversible.')"
                                                class="text-red-600 hover:text-red-900 mx-2 transition-colors">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr>
                                <td colspan="5" class="px-6 py-12 text-center">
                                    <div class="flex flex-col items-center">
                                        <div class="bg-gray-100 p-3 rounded-full mb-4">
                                            <i class="fas fa-car text-gray-400 text-4xl"></i>
                                        </div>
                                        <p class="text-gray-500 text-lg mb-2">Aucun véhicule trouvé</p>
                                        <p class="text-gray-400 text-sm mb-4">Commencez par ajouter un nouveau véhicule</p>
                                        <button onclick="openAddModal()" 
                                                class="gradient-bg text-white px-4 py-2 rounded-lg text-sm font-medium hover:opacity-90 transition-colors">
                                            <i class="fas fa-plus-circle mr-2"></i>
                                            Ajouter un véhicule
                                        </button>
                                    </div>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    </main>

    <!-- Modal d'ajout/modification -->
    <div id="vehicleModal" class="fixed inset-0 z-50 hidden overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
        <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
            <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity modal-transition" onclick="closeModal()"></div>

            <span class="hidden sm:inline-block sm:align-middle sm:h-screen">&#8203;</span>

            <div class="inline-block align-bottom bg-white rounded-2xl text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full animate-slide-in">
                <div class="gradient-bg px-6 py-4">
                    <div class="flex items-center justify-between">
                        <h3 class="text-lg font-semibold text-white" id="modal-title">
                            <i class="fas fa-plus-circle mr-2"></i>
                            <span id="modalTitle">Ajouter un véhicule</span>
                        </h3>
                        <button onclick="closeModal()" class="text-white hover:text-gray-200 transition-colors">
                            <i class="fas fa-times text-xl"></i>
                        </button>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/vehicules" method="post" class="p-6">
                    <input type="hidden" name="action" id="formAction" value="create">
                    <input type="hidden" name="id" id="vehicleId">

                    <div class="space-y-4">
                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                <i class="fas fa-tag text-blue-500 mr-2"></i>
                                Référence <span class="text-red-500">*</span>
                            </label>
                            <input type="text" 
                                   id="reference"
                                   name="reference" 
                                   class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors"
                                   placeholder="Ex: VH-2024-001"
                                   required>
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                <i class="fas fa-users text-blue-500 mr-2"></i>
                                Capacité <span class="text-red-500">*</span>
                            </label>
                            <input type="number" 
                                   id="capacite"
                                   name="capacite" 
                                   min="1"
                                   max="50"
                                   class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors"
                                   placeholder="Nombre de passagers"
                                   required>
                            <p class="text-xs text-gray-500 mt-1">Maximum 50 passagers</p>
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-700 mb-2">
                                <i class="fas fa-gas-pump text-blue-500 mr-2"></i>
                                Type de carburant <span class="text-red-500">*</span>
                            </label>
                            <select id="idTypeCarburant" name="idTypeCarburant" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition-colors" required>
                                <option value="" disabled selected>Sélectionner un type...</option>
                                <% if (types != null) {
                                    for (TypeCarburant t : types) { 
                                        String icon = "";
                                        switch(t.getLibelle().toLowerCase()) {
                                            case "essence": icon = "⛽"; break;
                                            case "diesel": icon = "🛢️"; break;
                                            case "électrique": icon = "⚡"; break;
                                            default: icon = "🔋";
                                        }
                                %>
                                    <option value="<%= t.getId() %>"><%= icon %> <%= t.getLibelle() %></option>
                                <% } } %>
                            </select>
                        </div>
                    </div>

                    <div class="mt-6 flex justify-end space-x-3">
                        <button type="button" 
                                onclick="closeModal()"
                                class="px-4 py-2 border border-gray-300 rounded-lg text-gray-700 hover:bg-gray-50 transition-colors">
                            Annuler
                        </button>
                        <button type="submit" 
                                class="gradient-bg text-white px-6 py-2 rounded-lg font-semibold hover:opacity-90 transition-colors">
                            <i class="fas fa-save mr-2"></i>
                            <span id="submitButtonText">Créer le véhicule</span>
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- JavaScript pour les interactions -->
    <script>
        function openAddModal() {
            document.getElementById('modalTitle').textContent = 'Ajouter un véhicule';
            document.getElementById('formAction').value = 'create';
            document.getElementById('submitButtonText').textContent = 'Créer le véhicule';
            document.getElementById('reference').value = '';
            document.getElementById('capacite').value = '';
            document.getElementById('idTypeCarburant').value = '';
            document.getElementById('vehicleModal').classList.remove('hidden');
        }

        function openEditModal(id, reference, capacite, typeCarburantId) {
            document.getElementById('modalTitle').textContent = 'Modifier le véhicule #' + id;
            document.getElementById('formAction').value = 'update';
            document.getElementById('submitButtonText').textContent = 'Mettre à jour';
            document.getElementById('vehicleId').value = id;
            document.getElementById('reference').value = reference;
            document.getElementById('capacite').value = capacite;
            document.getElementById('idTypeCarburant').value = typeCarburantId;
            document.getElementById('vehicleModal').classList.remove('hidden');
        }

        function closeModal() {
            document.getElementById('vehicleModal').classList.add('hidden');
        }

        // Fermer le modal avec la touche Echap
        document.addEventListener('keydown', function(e) {
            if (e.key === 'Escape') {
                closeModal();
            }
        });

        // Validation du formulaire avant soumission
        document.querySelector('#vehicleModal form').addEventListener('submit', function(e) {
            const reference = document.getElementById('reference').value;
            const capacite = document.getElementById('capacite').value;
            const typeCarburant = document.getElementById('idTypeCarburant').value;

            if (!reference.trim()) {
                e.preventDefault();
                alert('La référence est obligatoire.');
                return;
            }

            if (!capacite || capacite < 1 || capacite > 50) {
                e.preventDefault();
                alert('La capacité doit être comprise entre 1 et 50 passagers.');
                return;
            }

            if (!typeCarburant) {
                e.preventDefault();
                alert('Veuillez sélectionner un type de carburant.');
                return;
            }
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