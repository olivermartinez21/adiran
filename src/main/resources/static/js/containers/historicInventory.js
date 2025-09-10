
$(document).ajaxStart(function(){
    $('#loading').show();
}).ajaxStop(function(){
    $('#loading').hide();
});

$(document).ready( function() {
    $('#loading').hide();
    configDataTable();
    initComponents();
});

function configDataTable() {
    // $('#historicInventoryTable').DataTable({
    //     "ajax": {
    //         "url": "/historicInventory",
    //         "dataSrc": ""
    //     },
    //     "columns": [
    //         { "data": "id" },
    //         { "data": "name" },
    //         { "data": "quantity" },
    //         { "data": "price" },
    //         { "data": "date" },
    //         { "data": "description" }
    //     ],
    //     "columnDefs": [
    //         {
    //             "targets": 5,
    //             "data": null,
    //             "defaultContent": "<button class='btn btn-primary'>Edit</button>"
    //         }
    //     ]
    // });
    $("#historicInventoryTable").DataTable({
        language: { url: "//cdn.datatables.net/plug-ins/1.10.25/i18n/Spanish.json" },
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
            dataSrc: '',
            error: function(response) {
                console.log(response);
                manageErrorAjax(response);
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
        order: [[1, 'desc']] // Ordenar por la columna de fecha (registerDate) en orden ascendente
    }).columns.adjust();
}

function initComponents() {

}







