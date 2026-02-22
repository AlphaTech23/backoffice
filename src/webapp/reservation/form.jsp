<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Hotel" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Réservation d'hôtel</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            backdrop-filter: blur(10px);
            background: rgba(255, 255, 255, 0.95);
        }
        
        .card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 20px;
            border: none;
        }
        
        .card-header h2 {
            margin: 0;
            font-size: 1.8rem;
            font-weight: 300;
        }
        
        .form-label {
            font-weight: 500;
            color: #495057;
            margin-bottom: 0.5rem;
        }
        
        .form-control, .form-select {
            border: 2px solid #e9ecef;
            border-radius: 10px;
            padding: 12px;
            transition: all 0.3s;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .input-group-text {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px 0 0 10px;
        }
        
        .btn-reserver {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 10px;
            padding: 12px 30px;
            font-weight: 500;
            font-size: 1.1rem;
            transition: transform 0.3s, box-shadow 0.3s;
            width: 100%;
        }
        
        .btn-reserver:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 20px rgba(102, 126, 234, 0.4);
            color: white;
        }
        
        .btn-reserver i {
            margin-right: 10px;
        }
        
        .alert {
            border-radius: 10px;
            border: none;
            padding: 15px 20px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%);
            color: #155724;
        }
        
        .alert-danger {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: #721c24;
        }
        
        .hotel-option {
            padding: 10px;
        }
        
        .required-field::after {
            content: " *";
            color: #dc3545;
            font-weight: bold;
        }
        
        .form-text {
            color: #6c757d;
            font-size: 0.85rem;
            margin-top: 5px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card">
                <div class="card-header text-center">
                    <h2>
                        <i class="bi bi-calendar-check"></i>
                        Réservation d'hôtel
                    </h2>
                    <p class="mb-0 mt-2">Complétez le formulaire ci-dessous</p>
                </div>
                
                <div class="card-body p-4">
                    
                    <!-- Messages de succès -->
                    <%
                        String message = (String) request.getAttribute("message");
                        if (message != null) {
                    %>
                        <div class="alert alert-success alert-dismissible fade show" role="alert">
                            <i class="bi bi-check-circle-fill me-2"></i>
                            <%= message %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <%
                        }
                    %>

                    <!-- Messages d'erreur -->
                    <%
                        String error = (String) request.getAttribute("error");
                        if (error != null) {
                    %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i>
                            <%= error %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <%
                        }
                    %>

                    <form action="${pageContext.request.contextPath}/reservation/reserver" method="post">
                        
                        <!-- ID Client -->
                        <div class="mb-4">
                            <label class="form-label required-field">
                                <i class="bi bi-person-badge"></i>
                                ID Client
                            </label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="bi bi-hash"></i>
                                </span>
                                <input type="text" 
                                       class="form-control" 
                                       name="idClient" 
                                       maxlength="4" 
                                       pattern="[0-9]{4}" 
                                       title="Veuillez saisir exactement 4 chiffres"
                                       placeholder="Ex: 1234"
                                       required />
                            </div>
                            <div class="form-text">
                                <i class="bi bi-info-circle"></i>
                                Saisissez 4 chiffres exactement
                            </div>
                        </div>

                        <!-- Nombre de passagers -->
                        <div class="mb-4">
                            <label class="form-label required-field">
                                <i class="bi bi-people"></i>
                                Nombre de passagers
                            </label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="bi bi-person-plus"></i>
                                </span>
                                <input type="number" 
                                       class="form-control" 
                                       name="nombrePassager" 
                                       value="1"
                                       placeholder="Nombre de personnes"
                                       required />
                            </div>
                            <div class="form-text">
                                <i class="bi bi-info-circle"></i>
                                Minimum 1 personne
                            </div>
                        </div>

                        <!-- Date d'arrivée -->
                        <div class="mb-4">
                            <label class="form-label required-field">
                                <i class="bi bi-calendar-date"></i>
                                Date et heure d'arrivée
                            </label>
                            <div class="input-group">
                                <span class="input-group-text">
                                    <i class="bi bi-clock"></i>
                                </span>
                                <input type="datetime-local" 
                                       class="form-control" 
                                       name="dateArrive" 
                                       required />
                            </div>
                        </div>

                        <!-- Sélection hôtel -->
                        <div class="mb-4">
                            <label class="form-label required-field">
                                <i class="bi bi-building"></i>
                                Hôtel
                            </label>
                            <select class="form-select" name="idHotel" required>
                                <option value="" selected disabled>
                                    <i class="bi bi-building"></i> Choisissez un hôtel...
                                </option>
                                <%
                                    List<Hotel> hotels = (List<Hotel>) request.getAttribute("hotels");
                                    if (hotels != null && !hotels.isEmpty()) {
                                        for (Hotel h : hotels) {
                                %>
                                    <option value="<%= h.getId() %>" class="hotel-option">
                                        🏨 <%= h.getNom() %>
                                    </option>
                                <%
                                        }
                                    } else {
                                %>
                                    <option value="" disabled>Aucun hôtel disponible</option>
                                <%
                                    }
                                %>
                            </select>
                            <% if (hotels != null && !hotels.isEmpty()) { %>
                                <div class="form-text">
                                    <i class="bi bi-info-circle"></i>
                                    <%= hotels.size() %> hôtel(s) disponible(s)
                                </div>
                            <% } %>
                        </div>

                        <!-- Bouton de soumission -->
                        <button type="submit" class="btn btn-reserver">
                            <i class="bi bi-check-circle"></i>
                            Confirmer la réservation
                        </button>
                        
                        <!-- Lien de retour (optionnel) -->
                        <div class="text-center mt-3">
                            <a href="${pageContext.request.contextPath}/" class="text-decoration-none">
                                <i class="bi bi-arrow-left"></i>
                                Retour à l'accueil
                            </a>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Pied de page avec information -->
            <div class="text-center mt-4 text-white">
                <small>
                    <i class="bi bi-shield-check"></i>
                    Les champs marqués d'un astérisque (*) sont obligatoires
                </small>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS et Popper.js -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Validation côté client (optionnel) -->
<script>
    document.querySelector('form').addEventListener('submit', function(e) {
        const idClient = document.querySelector('input[name="idClient"]').value;
        if (!/^\d{4}$/.test(idClient)) {
            e.preventDefault();
            alert('L\'ID client doit contenir exactement 4 chiffres.');
        }
    });
    
    // Mettre la date minimum à aujourd'hui
    document.addEventListener('DOMContentLoaded', function() {
        const dateInput = document.querySelector('input[name="dateArrive"]');
        if (dateInput) {
            const now = new Date();
            const year = now.getFullYear();
            const month = String(now.getMonth() + 1).padStart(2, '0');
            const day = String(now.getDate()).padStart(2, '0');
            const hours = String(now.getHours()).padStart(2, '0');
            const minutes = String(now.getMinutes()).padStart(2, '0');
            
            const minDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;
            dateInput.min = minDateTime;
        }
    });
</script>

</body>
</html>