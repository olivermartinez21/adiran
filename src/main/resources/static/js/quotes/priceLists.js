$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });
$(document).ready( function() {
	$('#loading').hide();
	configDataTable();

	$('#newShippingConpanyInspection').on('change', function() {
		var selectedId = $(this).val();
		configDataTable(selectedId);

		if (selectedId != 0) {
			$.ajax({
				url: 'priceLists/getLaborByShippingCompany',
				type: 'GET',
				data: { shippingCompanyId: selectedId },
				success: function(response) {
					$('#jobcodeLabor').val(response.labor);
					$('#maneuverCost').val(response.maneuverCost);
				},
				error: function() {
					$('#jobcodeLabor').val('');
				}
			});

            // Obtener tipo de cambio
            $.ajax({
                url: 'priceLists/getExchangeByShippingCompany',
                type: 'GET',
                data: { shippingCompanyId: selectedId },
                success: function(response) {
                    $('#exchangeLabor').val(response);
                },
                error: function() {
                    $('#exchangeLabor').val('');
                }
            });
		} else {
			$('#jobcodeLabor').val('');
			$('#maneuverCost').val('');
            $('#exchangeLabor').val('');
		}
	});

});

function configDataTable(shippingCompanyId = 0) {
	if ($.fn.DataTable.isDataTable('#jobcodesTable')) {
		$('#jobcodesTable').DataTable().destroy();
	}
	$("#jobcodesTable").DataTable({
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
		// buttons: {
		// 	buttons: [
		// 		//{extend: 'excelHtml5', title: 'Unidades'},
		// 		//{text: 'Descargar Inventario Excel', className: 'btn btn-dark', action: function() { downloadInventoryReport()}},
		// 		{text: 'Busqueda Cliente Inventario', className: 'btn btn-info', action: function() { filterShippigModal(); } },
		// 		//{text: 'Descargar reporte De Maniobra Excel', className: 'btn btn-dark', action: function() { downloadManeuverReport()}},
		// 		{text: 'Busqueda Reporte de Maniobras', className: 'btn btn-info', action: function() { filtersModal(); } },
		// 		{text: 'Anuncios HAPAG', className: 'btn btn-info', action: function() { filtersAds(); } },
		// 	],
		// },
		ajax: {
			url: "priceLists/getDataTable",
			type: 'GET',
			data: function(d) {
				d.shippingCompanyId = shippingCompanyId;
			},
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		// 1. Agrega la columna del botón en la definición de columnas
		columns: [
			{ data: "jobcodeId", visible: true },
			{ data: "jobcodeRepair", visible: true, createdCell: function(td) { $(td).addClass('editable'); } },
			{ data: "jobcodeDescription", visible: true, createdCell: function(td) { $(td).addClass('editable'); } },
			{ data: "jobcodeMaterial", visible: true, createdCell: function(td) { $(td).addClass('editable'); } },
			{ data: "jobcodeHh", visible: true, createdCell: function(td) { $(td).addClass('editable'); } },
			{ data: "jobcodeExchange", visible: true },
			{ data: "jobcodeShippingId", visible: true},
			{
				data: null,
				orderable: false,
				defaultContent: '<button class="btn btn-success btn-save-row">Guardar</button>',
				className: 'dt-center'
			}
		],
		order: [[0, 'asc']] // Ordenar por la columna de fecha (registerDate) en orden ascendente
	}).columns.adjust();

	// Permite editar celdas al hacer clic
	$('#jobcodesTable tbody').on('click', 'td.editable', function() {
		// Fuerza blur en cualquier input abierto antes de editar otra celda
		$('#jobcodesTable tbody input').each(function() {
			$(this).blur();
		});

		var cell = $(this);
		if (cell.find('input').length > 0) return; // Ya está en modo edición

		var original = cell.text();
		var input = $('<input type="text" class="form-control form-control-sm" />').val(original);
		cell.empty().append(input);
		input.focus();

		input.on('blur', function() {
			var newValue = $(this).val();
			cell.text(newValue);

			var table = $('#jobcodesTable').DataTable();
			var row = table.row(cell.closest('tr'));
			var rowData = row.data();
			var colIdx = cell.index();
			var colName = table.settings().init().columns[colIdx].data;
			rowData[colName] = newValue;
			// No llamar a row.data(rowData).draw(false) aquí
		});
	});

	// 2. Evento para el botón "Guardar"
	$('#jobcodesTable tbody').on('click', '.btn-save-row', function() {
		var table = $('#jobcodesTable').DataTable();
		var row = table.row($(this).closest('tr'));
		var rowData = row.data();

		$.ajax({
			url: 'priceLists/updateJobcode',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify(rowData),
			success: function(response) {
				Swal.fire("Éxito", "Registro actualizado correctamente", "success");
			},
			error: function() {
				Swal.fire("Error", "No se pudo actualizar el registro", "error");
			}
		});
	});
}

function updateLabor() {
	var shippingCompanyId = $('#newShippingConpanyInspection').val();
	var maneuverCost = $('#maneuverCost').val();
	var labor = $('#jobcodeLabor').val();
    var exchange = $('#exchangeLabor').val();

	if (shippingCompanyId == 0 || labor.trim() === "" || exchange.trim() === "" || maneuverCost.trim() === "") {
		Swal.fire("Error", "Selecciona una naviera y escribe el valor de mano de obra y tipo de cambio", "warning");
		return;
	}

	$.ajax({
		url: 'priceLists/updateLaborAndExchangeByShippingCompany',
		type: 'POST',
		data: {
			shippingCompanyId: shippingCompanyId,
			maneuverCost: maneuverCost,
			labor: labor,
            exchange: exchange
		},
		success: function(response) {
			Swal.fire("Éxito", "Datos actualizados correctamente", "success");
			configDataTable();
		},
		error: function() {
			Swal.fire("Error", "No se pudo actualizar los datos", "error");
		}
	});
}

function chargeJobcodes(){
	var formData = new FormData();
	formData.append('file', $("#file").prop('files')[0]);
		$.ajax({
		type : "POST",
		url : 'priceLists/upload',
		cache : false, 
		contentType : false,
		processData : false,
		data : formData,
		success : function(response) {
			Swal.fire("INFORMACION GUARDADA", response.message, "success")
			configDataTable();
			$("#file").val('');
		},
		error : function() {
			Swal.fire("ERRO 902","el documento no pudo cargarse", "warning")
		}
	});
	
}

