$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });
$(document).ready( function() {
	$('#loading').hide();
	configDataTable();
	initComponents();

    // Evento para guardar factura
    $("#invoiceForm").submit(function(e) {
        e.preventDefault();
        var containerId = $("#invoiceContainerId").val();
        var invoiceNumber = $("#invoiceNumberInput").val();
        $.ajax({
            type: "POST",
            url: 'quote/saveInvoiceNumber',
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",
            data: {containerId: containerId, invoiceNumber: invoiceNumber},
            success: function(response){
                if(response.success){
                    $("#invoiceModal").modal("hide");
                    Swal.fire("Factura guardada", "", "success");
                    configDataTable();
                } else {
                    Swal.fire(response.message+" Error", "", "warning");
                }
            },
            error: function(){
                alert("AJAX ERROR");
            }
        });
    });
});
function configDataTable() {
	$("#quoteTable").DataTable().destroy();
	$("#quoteTable").DataTable({
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
			buttons: {
			buttons: [
				{text: 'Reporte de Estimados', className: 'btn btn-info', action: function() { quoteReport(); } },
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "quote/getDataTable",
			type: 'GET',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			dataSrc: '',
			data: {appointmentId :  $("#appointmentId").val(),
					userId : $("#globalUserId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			//{ data: "orderDate",visible: false },
			{ data: "containerId",visible: false },
			{ data: "container",visible: true },
			{ data: "dateInspection",visible: true },
			{ data: "containerType", visible: true , render : function(data) {
						$("#containerDescriptionQuote").val(data);
						return $("#containerDescriptionQuote option:selected").html();
					}}, 
			{ data: "nomenclatura",visible: true },
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#shippingCompanyCatalog").val(data);
						return $("#shippingCompanyCatalog option:selected").html();
					}}, 
			{ data: "condition", visible: true , render : function(data) {
						$("#containerConditionQuote").val(data);
						return $("#containerConditionQuote option:selected").html();
					}}, 
			{ data: "quoteName", visible: true , render : function(data) {
						return "";
					}}, 
			{ data: "typeServicePregate",visible: true },
			{ data: "billTo",visible: true },
			{ data: "statusQute", visible: false , render : function(data) {
				$("#containerDescriptionQuoteCatalog").val(data)
						return data;
					}}, 
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
						return  $("#containerDescriptionQuoteCatalog option:selected").html() +"<br>"+ 
						'&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="Actualizar Información" onclick="newQuote(\'' + meta.row + '\');"><i class="fas fa-pen"></i></button>'+
						'&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="Cambiar Estatus" onclick="changeStatus(\'' + data + '\');"><i class="fas fa-edit"></i></button>'+
						'&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="#Factura" onclick="noQuote(\'' + data + '\');"><i class="fas fa-file-invoice-dollar"></i></button>';
					}},
			{ data: "containerId", visible: false , render : function(data) {
						return "";
					}},
			{ data: "containerId", visible: false , render : function(data) {
						return   '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="changeStatus(\'' + data + '\');"><i class="fas fa-pen"></i></button>';
					}},
			{ data: "containerId", visible: false , render : function(data) {
						return "";
					}},
			{ data: "quoteName2", visible: false },
			{ data: "noInvoice", visible: true },
			{ data: "containerId",visible: false },
		],
		order: [[2, 'desc']]
	}).columns.adjust();
	
	$("#imageTableInspection").DataTable().destroy();
	$("#imageTableInspection").DataTable({
		dom:  
		"<'row'<'col-sm-12'B>>" +
		"<'row'<'col-sm-12'tr>>" + 
		"<'row'<'col-sm-5'i><'col-sm-7 text-right'p>>",
		fixedHeader: true,
		responsive: true,
		autoWidth: true, 
		select: {
			style: 'single'
		},
		buttons: {
			buttons: [
				//{text: 'Agregar Inspeccion', action: function() { addInspection(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		columns: [
			{ data: "photoId",visible: false },
			
			{ data: "image", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Ver Foto" onclick="showPhoto(\'' + data + '\');"><i class="fas fa-eye"></i></button>&nbsp';
				//return '<a onclick="showPhoto(\'' + meta.row + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				//;
			}},
			{ data: "file", visible: false },
			{ data: "photoId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Foto" onclick="deleteImage(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
	
	}
	
		
function newQuote(data) {
	console.log(data)
	currentData = $("#quoteTable").DataTable().row(data).data();
	$("#containerType").val(currentData.containerType)
	document.getElementById("btAcept").setAttribute("hidden",true)	
	if(currentData.statusQute!=1){ 
		document.getElementById("quotePrint").removeAttribute("hidden")
		document.getElementById("btAcept").setAttribute("hidden",true)
		}else{
				document.getElementById("quotePrint").setAttribute("hidden",true) 
		}
	if(currentData.quoteName2 != null){
		document.getElementById("quotePrint2").removeAttribute("hidden")
	} else{
		document.getElementById("quotePrint2").setAttribute("hidden",true)
	}
	$("#containerId").val(currentData.containerId);
	dataTableInpection()
	$("#quoteCondition").val(currentData.statusQute);
	getSection(),
	getDamageInfotmation($("#containerType").val())
	$.ajax({
		type: "GET",
		url: 'quote/validationInspection',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerId :$("#containerId").val()},
		success: function(response){
			if(response==0){
				//document.getElementById("quotePrint").removeAttribute("hidden")
					
					if(currentData.statusQute!=1){
							document.getElementById("btAcept").setAttribute("hidden",true)
					}else{
						document.getElementById("btAcept").removeAttribute("hidden")
					}
			}
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
	$("#quoteModel").modal("show");
		
	}
	
function changeStatus(data) {
	
	console.log(data)
	$("#containerId").val(data);
	$("#quoteStatusModel").modal("show");
	validationStatus(data)
	}
	
function validationStatus(data){
	$.ajax({
		type: "GET",
		url: 'quote/validationStatus',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerId : data},
		success: function(response){
			console.log(response)
			if(response != 0){
				$("#statusQuoteEdit option[value=3]").prop("disabled", false);
			}else{
				$("#statusQuoteEdit option[value=3]").prop("disabled", true);
			}
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
}
	
	
	
	function dataTableInpection() {
	//$("#inspectionTable").DataTable().clear().draw();
	$("#inspectionTable").DataTable().destroy();
	$("#inspectionTable").DataTable({
		dom:  "<'row'<'col-sm-6 'l><'col-sm-4 '><'col-sm-2 dt-right'>>" +
		"<'row'<'col-sm-12'B>>" +
		"<'row'<'col-sm-12'tr>>" + 
		"<'row'<'col-sm-5'i><'col-sm-7 text-right'p>>",
		fixedHeader: true,
		responsive: true,
		autoWidth: true, 
		select: {
			style: 'single'
		},
			buttons: {
			buttons: [
				//{text: '+', action: function() { addNewContainer()}},
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "quote/dataTableInpectionComplete",
			type: 'GET',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			dataSrc: '',
			data: {containerId : $("#containerId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
			
		createdRow: function( row, data ) {
				if(data.status==3){
				$(row).addClass('green');
				}
        		
        },

		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "part",visible: true },
			{ data: "component", visible: true , render : function(data) {
						$("#catComponent").val(data);
						return $("#catComponent option:selected").html();
					}}, 
			{ data: "damage", visible: true , render : function(data) {
						$("#catDamage").val(data);
						return $("#catDamage option:selected").html();
					}}, 
			{ data: "repair", visible: true , render : function(data) {
						$("#catRepair").val(data);
						return $("#catRepair option:selected").html();
					}}, 
			{ data: "length", visible: true , render : function(data) {
					$("#length").val(data);
						return data;
						
					}},
			{ data: "width", visible: true , render : function(data) {
					$("#width").val(data);
						return data;
						
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return $("#length").val()*$("#width").val();
						
					}},
			{ data: "otherLength", visible: true },
			{ data: "customerType",visible: true , render : function(data) {
						$("#inspectionCustomerType").val(data);
						return $("#inspectionCustomerType option:selected").html();
					}},
			{ data: "customerName",visible: true },
			{ data: "location",visible: true },
			{ data: "quantity",visible: true },
			
//			{ data: "inspectionId", visible: true , render : function(data) {
//						return "hola";
//					}},
//			{ data: "inspectionId", visible: false , render : function(data) {
//						return "";
//					}},
//			{ data: "inspectionId", visible: true , render : function(data) {
//						return "";
//					}},
//			{ data: "inspectionId", visible: true , render : function(data) {
//						return "";
//					}},	
			{ data: "workCode", visible: true},
			{ data: "repairDescription", visible: true},
			{ data: "hours", visible: true},
			{ data: "labor", visible: true },
			{ data: "material", visible: true},
			{ data: "tarifa", visible: true},
			{data: "extentLarge", visible: false},
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				$("#imagenData").val(data)
				//return '<a onclick="showPhoto(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Ver fotos" onclick="viewPhotos(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp';
			}},
			{ data: "inspectionId", visible: false , render : function(data) {
						return "";
					}},	
			{ data: "status", visible: false , render : function(data) {
						return data;
					}},
			{ data: "inspectionId", visible: true , render : function(data, type, full, meta) {
					if (full && (full.extentLarge === 1 || full.extentLarge === '1')) {
						return "";
					}
					return '<button type="button" class="btn btn-outline-dark btn-sm" title="Seleccionar Código de Trabajo" onclick="inspectionCap(\'' + data + '\');"><i class="fas fa-file"></i></button>' +
						'<button type="button" class="btn btn-outline-dark btn-sm" title="Modificar dato Cobrar A" onclick="changeBillTo(\'' + data + '\');"><i class="fas fa-retweet"></i></button>';
				}},
		],


	
	}).columns.adjust();
	}
	
	
function inspectionCap(data){
	$("#inspectionIdent").val(data),
	
	$.ajax({
		type: "GET",
		url: 'quote/getPreLabor',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {inspectionId : data},
				 
		success: function(response){
			
			console.log(response)
			
				$("#preLabor").val(response.labor)
				$("#shippingCompanyIdRef").val(response.shippingCompany)
			loadJobcodes(response.shippingCompany);


			
		},
		
		error: function(){
			alert("AJAX ERROR");
		}
	});

	$.ajax({
		type: "GET",
		url: 'quote/getQuoteDetail',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {inspectionId : data},

		success: function(response){

			console.log(response)

			$("#newWorkCode").val(response.workCode)
			$("#newRepairDescription").val(response.repairDescription)
			$("#newHours").val(response.hours)
			$("#newLabor").val(response.labor)
			$("#newMaterial").val(response.material)
			$("#newTarifa").val(response.tarifa)

		},

		error: function(){
			alert("AJAX ERROR");
		}
	});
	

	$("#inspectionId").val(data)
	$("#inspectionCapModel").modal("show")
		
	}
	
	
		
function showPhoto(data){
	window.open('quote/IMAGE?photoId='+ data+'')
	
	
/*Swal.fire({
  imageUrl: data,
})*/
	}
		
	
function initComponents(){
	$("#inspectionCapModel").submit(function () {
		if ($("#newWorkCode").val() === "" || $("#newWorkCode").val() === null) {
			Swal.fire("El campo Codigo/Descripcion no puede ir vacio", "", "warning");
			return false;
		}

		var data = {
			jobcodeId: $("#jobcodeId").val(),
			workCode: $("#newWorkCode").val(),
			repairDescription : $("#newRepairDescription").val(), 
			hours : $("#newHours").val(),
			labor : $("#newLabor").val(),
			material : $("#newMaterial").val(),
			tarifa : $("#newTarifa").val(),
			tarifa : $("#newTarifa").val(),
			exchange : $("#newExchange").val(),
			inspectionId: 	$("#inspectionId").val(),
			idUser: $("#globalUserId").val()
			}
			
			$.ajax({
			type: "POST",
			url: 'quote/saveInformationQuote',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					$("#inspectionCapModel").modal("hide")
					Swal.fire("Proceso Exitoso", "", "success")
					.then(() => {
					dataTableInpection()
					configDataTable()
					if(response.num==0){
					//document.getElementById("quotePrint").removeAttribute("hidden")
					document.getElementById("btAcept").removeAttribute("hidden")
					}
				});
				}else{
					console.log(response.message);
					Swal.fire(response.message+" Error", "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
			
		});
		 
		$("#quoteModel").submit(function () {
			
			
			$.ajax({
			type: "POST",
			url: 'quote/changeStatus',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerId : $("#containerId").val()},
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					//$("#quoteCondition").val(response.num)
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#quoteModel").modal("hide");
					configDataTable()
						});
				}else{
					console.log(response.message);
					Swal.fire(response.message+" Error", "", "warning");
					
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
		});
			 
		$("#quoteStatusModel").submit(function () {
			$.ajax({
			type: "POST",
			url: 'quote/editStatus',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerId : $("#containerId").val(),
			status : $("#statusQuoteEdit").val()},
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					//$("#quoteCondition").val(response.num)
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#quoteStatusModel").modal("hide");
					configDataTable()
						});
				}else{
					console.log(response.message);
					Swal.fire(response.message+" Error", "", "warning");
					
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
		});


	// JavaScript
	$("#changeBilltoModel").submit(function (e) {
	    e.preventDefault();

		var newBillTo = ($("#newBillTo").val() || "").trim();

		// Validación: vacío o solo números -> mostrar advertencia y no enviar
		if (newBillTo === "" || /^\d+$/.test(newBillTo)) {
			Swal.fire("Campo inválido", "El campo no puede estar vacío, ni contener solo números\n Recuerda registarlo directamente del catalogo", "warning");
			return false;
		}

	    Swal.fire({
	        title: "¿Está seguro?",
	        text: "Confirma cambiar el cliente a quien se le cobrará para esta inspección?",
	        icon: "warning",
	        showCancelButton: true,
	        confirmButtonText: "Sí, cambiar",
	        cancelButtonText: "Cancelar",
	        reverseButtons: true
	    }).then((result) => {
	        if (result.isConfirmed) {
	            $.ajax({
	                type: "POST",
	                url: 'quote/editBillTo',
	                cache: false,
	                contentType: "application/x-www-form-urlencoded; charset=UTF-8",
	                data: {
	                    inspectionId: $("#inspectionBilltoId").val(),
	                    newBillTo: $("#newBillTo").val()
	                },
	                success: function (response) {
	                    console.log(response.success);
	                    if (response.success == true) {
	                        Swal.fire("Cambio Realizado", "Recuerda actualizar las tarifas para evitar discrepancias en los datos", "info")
	                            .then(() => {
	                                $("#changeBilltoModel").modal("hide");
	                                dataTableInpection();
	                            });
	                    } else {
	                        console.log(response.message);
	                        Swal.fire(response.message + " Error", "", "warning");
	                    }
	                },
	                error: function () {
	                    alert("AJAX ERROR");
	                }
	            });
	        }
	    });

	    return false;
	});

	$("#quoteReportForm").submit( () => {
		data = {
			//shippingCompany : $("#filterShippingCompany").val(),
			dateInit : $("#filterDateInit").val(),
			dateEnd : $("#filterDateEnd").val(),

		}
		console.log("soy data" + JSON.stringify(data));
		$.ajax({
			type: "GET",
			url: "quote/quoteReport",
			//contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			xhrFields: {
				responseType: 'blob' // Set response type to blob to handle binary data
			},
			success: (data, status, xhr) => {
				// Create a new Blob object using the response data
				const blob = new Blob([data], { type: xhr.getResponseHeader('Content-Type') });

				// Create a link element
				const link = document.createElement('a');
				link.href = window.URL.createObjectURL(blob); // Create a URL for the blob
				link.download = 'ReporteEstimados.xlsx'; // Set the file name

				// Append to the body and trigger the download
				document.body.appendChild(link);
				link.click();

				// Clean up and remove the link
				setTimeout(() => {
					document.body.removeChild(link);
					window.URL.revokeObjectURL(link.href);
				}, 100);
			},
			error: (xhr, status, error) => {
				console.error('Error generating Excel report:', error);
				alert('Error generating report. Please try again.');
			}
		});
		return false;
	});
		
}


function openPdf(){
	
	window.open('quote/PDF_QUOTE?containerId='+ $("#containerId").val() +'')
			configDataTable()
			return false;
	
}
function openPdf2(){

	window.open('quote/PDF_QUOTE2?containerId='+ $("#containerId").val() +'')
	configDataTable()
	return false;

}


function viewPhotos(data){
	
	table = $("#inspectionTable").DataTable().row(data).data();
$("#newPart").val(table.part)
$("#newDamage").val(table.damage)
$("#newComponentInspection2").val(table.component)
$("#newLocationInspection").val(table.location)
$("#newReferent").val(table.reference)
$("#largeInspection").val(table.length)
$("#heigthInspection").val(table.width)
$("#depthInspection").val(table.depth)
$("#otherLargeInspection").val(table.otherLength)
$("#quantityInspection").val(table.quantity)
$("#inspectionCustomerType").val(table.customerType)
$("#inspectionId").val(table.inspectionId)
$("#imageTableInspection").DataTable().clear().draw();

	var info = {
			containerId: $("#inspectionId").val(),
			} 
		
	$.ajax({
		type: "GET",
		url: 'quote/getPhotos',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: info,
		success: function(response){
			console.log(response)

	for(var i=0;i<response.length;i++){
$("#imageTableInspection").DataTable().row.add({
				"photoId":response[i].photoId,
				"image":response[i].photoId,
				"file": "x",
			}).draw(false);
			
	}
	
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
			
			
	
	$("#addNewDamageModel").modal("show");	
}

function getSection(){
	clearCombo(document.getElementById("newPart"));
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
$.ajax({
			type: "GET",
			url: 'quote/getSectionInformation',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerType : textContainer},
			success: function(response){
				//clearCombo(document.getElementById("newPart"));
				fillComboSection(document.getElementById("newPart"),response);
			},
			error: function(){
				alert("AJAX ERROR");
			}
		});
}

function getDamageInfotmation(data){
			$.ajax({
		type: "GET",
		url: 'quote/getDamageInformation',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerType : data},
		success: function(response){
			clearCombo(document.getElementById("newDamage"))
			fillComboDamage(document.getElementById("newDamage"),response)
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
			
			
			return false;
	}


function showComponents(){
		data = $("#newPart").val();
	console.log(data)
	$("#seccionSave").val(data)

	//text = document.getElementById("newPart").options[data].text
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
	document.getElementById("newComponentInspection")
	$.ajax({
		type: "GET",
		url: 'quote/getComponentIformation',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerType :textContainer,
		Section: data},
		success: function(response){
			clearCombo(document.getElementById("newComponentInspection"));
			fillComboComponentAjax(document.getElementById("newComponentInspection"),response);
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
			
			
			return false;
	//clearCombo(document.getElementById("newComponentInspection"));
	//fillComboComponent(document.getElementById("newComponentInspection"), $("#containerType").val(),data);
}

function loadJobcodes(data){
	console.log(data)
	$.ajax({
		type: "GET",
		url: 'quote/catJobcode',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {shippingCompanyId: data},
		success: function(response) {
			console.log('Jobcodes recibidos:', response);

			// Vacía las opciones previas
			var $datalist = $('#newJobcodeList');
			$datalist.empty();

			// Recorre el array y añade <option> por cada elemento
			$.each(response, function(i, opt) {
				$('<option>')
					.val(opt.jobcodeRepair)
					.text(opt.jobcodeDescription)
					.attr('data-id', opt.jobcodeId)
					.appendTo($datalist);
			});
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
}

console.log(newJobcodeList)
document.getElementById("newJobcodeList").addEventListener("change", myFunction);

function myFunction() {
	
	var input = document.getElementById('newWorkCode');
    var selectedOption = document.querySelector('#newJobcodeList option[value="' + input.value + '"]');	
	
   var jobcodeId = selectedOption.getAttribute('data-id');
   
   console.log(jobcodeId)
   $.ajax({
		type: "GET",
		url: 'quote/getJobcodeDescription',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {jobcodeId : jobcodeId},
				 
		success: function(response){
			
			console.log(response.jobcodeId)
			
			if(response.jobcodeId == null){
				
			alert("No se encontro registro")
			} else{

				$("#jobcodeId").val(response.jobcodeId)
				$("#newRepairDescription").val(response.jobcodeDescription)
				$("#newHours").val(response.jobcodeHh)
				$("#newMaterial").val(response.jobcodeMaterial)
				$("#newExchange").val(response.jobcodeExchange)
					calcularLabor()
							
			}
			
			
		},
		
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
	
   
}

function calcularLabor(){
	
	var preLabor = document.getElementById('preLabor').value;
	
	var newHours = document.getElementById('newHours').value;
	
	console.log(preLabor, newHours)
	
	var preFloat = parseFloat(preLabor);
	
	var newFloat = parseFloat(newHours);
	
	var operacionLabor = preFloat*newFloat;
	
	console.log(operacionLabor)
	$("#newLabor").val(Math.round(operacionLabor * 100) / 100);
	
	calcularTarifa()
}

function calcularTarifa(){
	var calcularLabor = document.getElementById('newLabor').value;
	
	var newMaterial = document.getElementById('newMaterial').value;
	
	var parseLabor = parseFloat(calcularLabor);
	
	var parseMaterial = parseFloat(newMaterial);
	
	var calcularTarifa = parseLabor+parseMaterial;
	
	$("#newTarifa").val(Math.round(calcularTarifa * 100) / 100);
}

function noQuote(containerId) {
    // Obtener datos actuales de la fila
    var rowData = $("#quoteTable").DataTable().rows().data().toArray().find(function(row) {
        return row.containerId == containerId;
    });
    if(rowData && rowData.invoiceNumber) {
        $("#invoiceNumberInput").val(rowData.invoiceNumber);
        $("#invoiceNumberInput").prop("readonly", false);
    } else if(rowData && rowData.noInvoice) {
        $("#invoiceNumberInput").val(rowData.noInvoice);
        $("#invoiceNumberInput").prop("readonly", false);
    } else {
        $("#invoiceNumberInput").val("");
        $("#invoiceNumberInput").prop("readonly", false);
    }
    $("#invoiceContainerId").val(containerId);
    $("#invoiceModal").modal("show");
}

function changeBillTo (data){
	console.log(data);
	$("#inspectionBilltoId").val(data);
	$("#newBillTo").val("");
	$("#changeBilltoModel").modal("show");
}

function quoteReport(){
	$("#quoteReportModal").modal("show");
}


