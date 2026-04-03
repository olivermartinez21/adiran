$(document).ajaxStart(function(){
    $('#loading').show();
}).ajaxStop(function(){
    $('#loading').hide();
});

let historicInventoryTable;

$(document).ready( function() {
    $('#loading').hide();
    configDataTable();
    initComponents();
});

function configDataTable() {
    if ( $.fn.DataTable.isDataTable('#historicInventoryTable') ) {
        $('#historicInventoryTable').DataTable().destroy();
    }
    historicInventoryTable = $("#historicInventoryTable").DataTable({
        dom:  "<'row'<'col-sm-6 'l><'col-sm-4 '><'col-sm-2 dt-right'f>>" +
            "<'row'<'col-sm-12'B>>" +
            "<'row'<'col-sm-12'tr>>" +
            "<'row'<'col-sm-5'i><'col-sm-7 text-right'p>>",
        fixedHeader: true,
        responsive: true,
        autoWidth: true,
        select: {
            style: 'single'
        },
        ajax: {
            url: "historicInventory/getDataTable",
            type: 'GET',
            data: function(d) {
                const startDate = $('#startDate').val();
                const endDate = $('#endDate').val();
                if (startDate) { d.startDate = startDate; }
                if (endDate) { d.endDate = endDate; }
            },
            dataSrc: '',
            error: function(response) {
                if (typeof manageErrorAjax === 'function') {
                    manageErrorAjax(response);
                }
            }
        },
        columns: [
            { data: "inventoryHistoricId",visible: false },
            { data: "shippingCompanyDescription",visible: true },
            { data: "container",visible: true },
            { data: "condition",visible: true },
            { data: "containerType",visible: true },
            { data: "nomenclatura",visible: true },
            { data: "conditionPregate",visible: true },
            { data: "location",visible: true },
            { data: "dateInspection",visible: true },
            { data: "clasification",visible: true },
            { data: "daysOfStay",visible: true },
            { data: "finalDate",visible: true },
            { data: "statusQuote",visible: true },
            { data: "aptTo",visible: true },
            { data: "typeServicePregate",visible: true },
            { data: "registerDate",visible: true },
            { data: "comments",visible: true },
            { data: "uploadDate",visible: true },
        ],
        order: [[17, 'desc']] // Ordenar por uploadDate descendente (columna 17 index base 0)
    }).columns.adjust();
}

function initComponents() {
    $('#btnSearch').on('click', function() {
        if (validateDateRange()) {
            historicInventoryTable.ajax.reload();
        }
    });
    $('#btnClear').on('click', function() {
        $('#startDate').val('');
        $('#endDate').val('');
        hideDateError();
        historicInventoryTable.ajax.reload();
    });

    $('#startDate, #endDate').on('change', function() {
        validateDateRange(false); // validar en vivo sin recargar
    });

    $('#startDate, #endDate').on('keyup', function(e) {
        if (e.key === 'Enter') {
            if (validateDateRange()) {
                historicInventoryTable.ajax.reload();
            }
        }
    });
}

function validateDateRange(showFocus = true) {
    const startDate = $('#startDate').val();
    const endDate = $('#endDate').val();
    if (startDate && endDate) {
        if (endDate < startDate) {
            showDateError('La fecha fin no puede ser anterior a la fecha inicio.');
            if (showFocus) { $('#endDate').focus(); }
            return false;
        }
    }
    hideDateError();
    return true;
}

function showDateError(msg) {
    $('#dateRangeError').text(msg).show();
}

function hideDateError() {
    $('#dateRangeError').hide().text('');
}
