<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.backoffice.model.Vehicule" %>
<%@ page import="com.example.backoffice.model.TypeCarburant" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestion des Véhicules</title>
    
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    
    <style>
        body {
            background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.1);
            background: rgba(255, 255, 255, 0.95);
            margin-bottom: 20px;
        }
        
        .card-header {
            background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
            color: white;
            border-radius: 15px 15px 0 0 !important;
            padding: 20px;
            border: none;
        }
        
        .table-custom {
            background: white;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 5px 15px rgba(0,0,0,0.05);
        }
        
        .table-custom thead {
            background-color: #f8f9fa;
        }
        
        .btn-action {
            padding: 5px 10px;
            border-radius: 5px;
            transition: all 0.2s;
        }

        .alert {
            border-radius: 10px;
            border: none;
        }
    </style>
</head>
<body>

<div class="container mt-4">
    <h1 class="mb-4 text-center" style="color: #1e3c72; font-weight: bold;">
        <i class="bi bi-car-front-fill me-2"></i>Gestion des Véhicules
    </h1>

    <!-- Messages -->
    <%
        String message = (String) request.getAttribute("message");
        if (message != null) {
    %>
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i><%= message %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <%
        }
    %>

    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i><%= error %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <%
        }
    %>

    <div class="row">
        <!-- Formulaire d'ajout -->
        <div class="col-md-4">
            <div class="card">
                <div class="card-header text-center">
                    <h5 class="mb-0"><i class="bi bi-plus-circle me-2"></i>Ajouter un véhicule</h5>
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/vehicule" method="post">
                        <input type="hidden" name="action" value="create">
                        
                        <div class="mb-3">
                            <label class="form-label fw-bold">Référence <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" name="reference" required>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label fw-bold">Capacité <span class="text-danger">*</span></label>
                            <input type="number" class="form-control" name="capacite" min="1" required>
                        </div>
                        
                        <div class="mb-4">
                            <label class="form-label fw-bold">Type Carburant <span class="text-danger">*</span></label>
                            <select class="form-select" name="idTypeCarburant" required>
                                <option value="" selected disabled>Choisir un type...</option>
                                <%
                                    List<TypeCarburant> types = (List<TypeCarburant>) request.getAttribute("typeCarburants");
                                    if (types != null) {
                                        for (TypeCarburant t : types) {
                                %>
                                    <option value="<%= t.getId() %>"><%= t.getLibelle() %></option>
                                <%
                                        }
                                    }
                                %>
                            </select>
                        </div>
                        
                        <button type="submit" class="btn btn-primary w-100" style="background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%); border: none;">
                            <i class="bi bi-save me-2"></i>Enregistrer
                        </button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Liste des véhicules -->
        <div class="col-md-8">
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><i class="bi bi-list-ul me-2"></i>Liste des véhicules</h5>
                    <span class="badge bg-light text-primary">
                    <%
                        List<Vehicule> vehicules = (List<Vehicule>) request.getAttribute("vehicules");
                        if (vehicules != null) { out.print(vehicules.size()); } else { out.print("0"); }
                    %> Total
                    </span>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover table-custom mb-0">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Référence</th>
                                    <th>Capacité</th>
                                    <th>Carburant</th>
                                    <th class="text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    if (vehicules != null && !vehicules.isEmpty()) {
                                        for (Vehicule v : vehicules) {
                                %>
                                <tr>
                                    <td class="align-middle fw-bold">#<%= v.getId() %></td>
                                    <td class="align-middle"><%= v.getReference() %></td>
                                    <td class="align-middle"><%= v.getCapacite() %> passagers</td>
                                    <td class="align-middle">
                                        <% if (v.getTypeCarburant() != null) { %>
                                            <span class="badge bg-secondary"><%= v.getTypeCarburant().getLibelle() %></span>
                                        <% } else { %>
                                            <span class="text-muted">-</span>
                                        <% } %>
                                    </td>
                                    <td class="align-middle text-center">
                                        <!-- Bouton Edit Modal -->
                                        <button type="button" class="btn btn-sm btn-outline-warning btn-action" data-bs-toggle="modal" data-bs-target="#editModal<%= v.getId() %>">
                                            <i class="bi bi-pencil-square"></i>
                                        </button>
                                        
                                        <!-- Formulaire Supression Rapide -->
                                        <form action="${pageContext.request.contextPath}/vehicule" method="post" class="d-inline" onsubmit="return confirm('Voulez-vous vraiment supprimer ce véhicule?');">
                                            <input type="hidden" name="action" value="delete">
                                            <input type="hidden" name="id" value="<%= v.getId() %>">
                                            <button type="submit" class="btn btn-sm btn-outline-danger btn-action">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </form>

                                        <!-- Modal de Modification -->
                                        <div class="modal fade" id="editModal<%= v.getId() %>" tabindex="-1" aria-labelledby="editModalLabel<%= v.getId() %>" aria-hidden="true">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <div class="modal-header bg-warning text-dark">
                                                        <h5 class="modal-title" id="editModalLabel<%= v.getId() %>"><i class="bi bi-pencil-square me-2"></i>Modifier Véhicule #<%= v.getId() %></h5>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                                    </div>
                                                    <form action="${pageContext.request.contextPath}/vehicule" method="post">
                                                        <div class="modal-body text-start">
                                                            <input type="hidden" name="action" value="update">
                                                            <input type="hidden" name="id" value="<%= v.getId() %>">
                                                            
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold">Référence <span class="text-danger">*</span></label>
                                                                <input type="text" class="form-control" name="reference" value="<%= v.getReference() %>" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold">Capacité <span class="text-danger">*</span></label>
                                                                <input type="number" class="form-control" name="capacite" value="<%= v.getCapacite() %>" min="1" required>
                                                            </div>
                                                            <div class="mb-3">
                                                                <label class="form-label fw-bold">Type Carburant <span class="text-danger">*</span></label>
                                                                <select class="form-select" name="idTypeCarburant" required>
                                                                    <%
                                                                        if (types != null) {
                                                                            for (TypeCarburant t : types) {
                                                                                boolean isSelected = (v.getTypeCarburant() != null && v.getTypeCarburant().getId().equals(t.getId()));
                                                                    %>
                                                                        <option value="<%= t.getId() %>" <%= isSelected ? "selected" : "" %>><%= t.getLibelle() %></option>
                                                                    <%
                                                                            }
                                                                        }
                                                                    %>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="modal-footer">
                                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Annuler</button>
                                                            <button type="submit" class="btn btn-warning">Sauvegarder</button>
                                                        </div>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <%
                                        }
                                    } else {
                                %>
                                <tr>
                                    <td colspan="5" class="text-center py-4 text-muted">
                                        <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                        Aucun véhicule enregistré.
                                    </td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
