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
				//{text: '+', action: function() { addNewContainer()}},
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
			{ data: "containerId",visible: false },
			{ data: "container",visible: true },
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
						'&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="newQuote(\'' + meta.row + '\');"><i class="fas fa-pen"></i></button>'+
						'&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="Cambair status" onclick="changeStatus(\'' + data + '\');"><i class="fas fa-edit"></i></button>';
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
			{ data: "containerId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "containerId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "containerId",visible: false },
		],
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
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="showPhoto(\'' + data + '\');"><i class="fas fa-eye"></i></button>&nbsp';
				//return '<a onclick="showPhoto(\'' + meta.row + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				//;
			}},
			{ data: "file", visible: false },
			{ data: "photoId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteImage(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
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
			url: "quote/dataTableInpection",
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
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "customerType",visible: true , render : function(data) {
						$("#inspectionCustomerType").val(data);
						return $("#inspectionCustomerType option:selected").html();
					}}, 
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
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},				
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				$("#imagenData").val(data)
				//return '<a onclick="showPhoto(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Ver fotos" onclick="viewPhotos(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp';
			}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "status", visible: false , render : function(data) {
						return data;
					}},	
			{ data: "inspectionId", visible: true , render : function(data, meta) {
						return   '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="inspectionCap(\'' + data + '\');"><i class="fas fa-file"></i></button>';
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
			
			
		},
		
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
	$("#newWorkCode").val(""), 
	$("#newRepairDescription").val(""), 
	$("#newHours").val(""),
	$("#newLabor").val(""),
	$("#newMaterial").val(""),
	$("#newTarifa").val(""),
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
		var data = {
			workCode: $("#newWorkCode").val(), 
			repairDescription : $("#newRepairDescription").val(), 
			hours : $("#newHours").val(),
			labor : $("#newLabor").val(),
			material : $("#newMaterial").val(),
			tarifa : $("#newTarifa").val(),
			tarifa : $("#newTarifa").val(),
			exchange : $("#newExchange").val(),
			inspectionId: 	$("#inspectionId").val()
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
		
		
}


function openPdf(){
	
	window.open('quote/PDF_QUOTE?containerId='+ $("#containerId").val() +'')
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
	$("#newLabor").val(Math.round(operacionLabor));
	
	calcularTarifa()
}

function calcularTarifa(){
	var calcularLabor = document.getElementById('newLabor').value;
	
	var newMaterial = document.getElementById('newMaterial').value;
	
	var parseLabor = parseFloat(calcularLabor);
	
	var parseMaterial = parseFloat(newMaterial);
	
	var calcularTarifa = parseLabor+parseMaterial;
	
	$("#newTarifa").val(Math.round(calcularTarifa));
}
