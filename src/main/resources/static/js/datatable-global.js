// datatable-global.js
// Configuración y ajustes globales para todas las DataTables

;(function ($) {
    "use strict";

    // Si DataTables no está cargado, no hacer nada
    if (!$.fn.dataTable) {
        console.warn("DataTables no está cargado. datatable-global.js no se aplicará.");
        return;
    }

    // 1) Defaults globales para TODAS las DataTables
    $.extend(true, $.fn.dataTable.defaults, {
        responsive: true,   // todas las tablas serán responsive por defecto
        autoWidth: false,   // ayuda a que Responsive calcule mejor anchos
        // fixedHeader: true // descomenta si quieres fixedHeader globalmente
    });

    // 2) Función global para recalcular TODAS las DataTables visibles
    function recalcVisibleDataTables() {
        var api = $.fn.dataTable.tables({ visible: true, api: true });
        if (api.length) {
            api
                .columns.adjust()
                .responsive.recalc();
        }
    }

    // 3) Cada vez que se muestra un modal / tab / collapse de Bootstrap,
    //    recalculamos las tablas visibles (simula el efecto de abrir el inspector)
    $(document).on('shown.bs.modal shown.bs.tab shown.bs.collapse', function () {
        recalcVisibleDataTables();
    });

    // 4) Opcional: también al redimensionar la ventana
    $(window).on('resize', function () {
        recalcVisibleDataTables();
    });

    // 5) Exponer la función por si quieres llamarla manualmente desde tu código
    window.recalcVisibleDataTables = recalcVisibleDataTables;

})(jQuery);
