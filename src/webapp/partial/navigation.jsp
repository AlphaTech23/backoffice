<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Sidebar -->
<div class="w-64 h-screen bg-gray-100 shadow-lg fixed left-0 top-0 flex flex-col p-6 animate-fade-in">

    <!-- Titre -->
    <div class="mb-8 text-center">
        <h2 class="text-xl font-bold text-gray-800">Navigation</h2>
    </div>

    <!-- Menu -->
    <nav class="flex flex-col space-y-4">

        <a href="${pageContext.request.contextPath}/vehicules"
           class="flex items-center space-x-3 text-gray-600 hover:text-gray-900 hover:bg-gray-200 p-2 rounded transition">
            <i class="fas fa-car w-5"></i>
            <span>Véhicules</span>
        </a>

        <a href="${pageContext.request.contextPath}/trajets"
           class="flex items-center space-x-3 text-gray-600 hover:text-gray-900 hover:bg-gray-200 p-2 rounded transition">
            <i class="fas fa-route w-5"></i>
            <span>Trajets</span>
        </a>

        <a href="${pageContext.request.contextPath}/trajets/planifier"
           class="flex items-center space-x-3 text-gray-600 hover:text-gray-900 hover:bg-gray-200 p-2 rounded transition">
            <i class="fas fa-clock w-5"></i>
            <span>Planification</span>
        </a>

        <a href="${pageContext.request.contextPath}/reservation/form"
           class="flex items-center space-x-3 text-gray-600 hover:text-gray-900 hover:bg-gray-200 p-2 rounded transition">
            <i class="fas fa-ticket-alt w-5"></i>
            <span>Réservation</span>
        </a>

        <a href="${pageContext.request.contextPath}/reservations/non-assigner"
           class="flex items-center space-x-3 text-gray-600 hover:text-gray-900 hover:bg-gray-200 p-2 rounded transition">
            <i class="fas fa-times w-5"></i>
            <span>Non assignées</span>
        </a>

    </nav>

</div>