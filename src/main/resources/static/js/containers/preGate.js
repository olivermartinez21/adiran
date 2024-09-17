
$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });
var damage;
var cambioComponente=0;
$(document).ready( function() {
	$("#appointmentId").val(GetURLParameter('appointmentId'));
	$('#loading').hide();
	configDataTable();
	initComponents();
	validationCode($("#newContainerName")) 
	validationCode($("#unitAsosiateInspection")) 
	//validationCode($("#unitAsosiateInspectiongenset")) 
	$("#globalWarehouse").val()
	intermodalInformation();
});

function GetURLParameter(sParam) {

    var sPageURL = window.location.search.substring(1);
    var sURLVariables = sPageURL.split('&');
    for (var i = 0; i < sURLVariables.length; i++) 
    {
        var sParameterName = sURLVariables[i].split('=');
        if (sParameterName[0] == sParam) 
        {
            return sParameterName[1];
        }
    }
}

function intermodalInformation() {
	if($("#globalWarehouse").val()=="AGUASCALIENTES"){
		document.getElementById("divOriginPregate").removeAttribute("hidden");
		document.getElementById("divDestinityPregate").removeAttribute("hidden");
	}
}
function initComponents() {
		
	$("#pregateModel").submit(function () {
		$("#pregateModel").modal("hide");
		var data = {
			conditionPregate: $("#newCondition").val(), 
			typeServicePregate : $("#typeServicePregate").val(), 
			billTo : $("#newBillToPregate").val(),
			transportId : $("#newTransportCompanyPregate").val(),
			originPregate: $("#newOriginPregate").val(),
			buque : $("#newBuquePregate").val(),
			bl : $("#newBlPregate").val(),
			operatorName : $("#newOperatorNamePregate").val(),
			plate: $("#newPlate").val(),
			economicNumber: $("#newEconomicNumber").val(),
			containerId: $("#containerId").val(),
			
			}
			
			$.ajax({
			type: "POST",
			url: 'preGate/savePregate',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					Swal.fire({
				        title: "Proceso Exitoso",
				        text: "¿Desea Comenzar con la inspeccion?",
				        icon: 'success',
				        showCancelButton: true,
				        confirmButtonText: "Si",
				        cancelButtonText: "no",
				    }).then(resultado => {
				       if (resultado.value) {
				            // Hicieron click en "Sí"
							//configDataTablePregate()
							inspectionContainer($("#containerId").val())
							configDataTablePregate()
				        } else {
				            // Dijeron que no
							configDataTablePregate()
							//self.location.reload();
				        }
				    });
					/*Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
				self.location.reload();
				});*/
				}else{
					Swal.fire(response.message+" Error", "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
			
		});
		
		$("#conditionModel").submit(function () {
			$.ajax({
			type: "POST",
			url: "preGate/containerValidation",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerId :$("#containerId").val(),
			condition : $("#containerCondition").val(),
			clasification : $("#containerClasification").val() },
			success: function(response){
				console.log(response);
				if(response.success==true){
				Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
				self.location.reload();
				});
				}else{
					Swal.fire("Error", "", "error");
				}
			},
			error: function(){
				alert("AJAX ERROR");
			}
			
			});return false;
			
			});
			
	$("#newEventModal").submit(function () {
		var data = {
			eventType: $("#newEventType").val(), 
			eventDate: $("#newEventDate").val(),
			estimateRequired: $("#newEstimateRequired").val(),
			inspectedBy: $("#newInspectedBy").val(), 
			//booking: $("#containerId").val(),
			fillState: $("#newStatusEvent").val(),
			//alternateUnit: $("#containerId").val(),
			//associatedUnit: $("#containerId").val(),
			transportType: $("#newTransportType").val(),
			//sapSaleOrder: $("#containerId").val(),
			unitQuality: $("#newContainerCondition").val(),
			unitClasification: $("#newContainerClasification").val(),
			//sealNumber: $("#containerId").val(),
			customerIdentifier: $("#newBillTo").val(),
			type: $("#newContainerTypeEvent").val(),
			model: $("#newModel").val(),
			//location: $("#containerId").val(),
			container: $("#newContainerNameEvent").val(),
			containerId: $("#containerId").val(),
			idUser : $("#globalUserId").val(),
			transmit: $("#newTransmit").val()
			
		};
		
			$.ajax({
			type: "POST",
			url: 'preGate/saveEventInformation',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
				self.location.reload();
				});
				}else{
					Swal.fire("Error", "", "error");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
	});
	
	$("#newContainerModal").submit(function () {
	value=$("#newContainerName").val();
	if(value.length==11){
	if (!verify(value)) {
		Swal.fire("El codigo es incorrecto", "", "error");
	}else {
		var data = {
			container: $("#newContainerName").val().toUpperCase(),
			registerDate : $("#startDate").val(),
			containerType: $("#newContainerTypeSave").val(),
			containerSize: $("#newContainerSize").val(),
			shippingCompany: $("#newShippingConpanyContainer").val(),
			idUser : $("#globalUserId").val(), 
	};
	console.log(data)
		$.ajax({
			type: "POST",
			url: 'preGate/saveContainer',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#newContainerModal").modal("hide");
					configDataTablePregate()
				});
				}else{
					Swal.fire("Error "+response.message, "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
				
		
	}
	}else{
		Swal.fire("El codigo debe contener 11 caracteres ejemplo "+  '"XXXX1234567"', "", "warning");
	}
	return false;
		});	
	
	
/*	
	$("#inspectionContainerForm").submit(function () {
		
		if($("#newModel").val()!="Selecciona una opción"){
			var data = {
		containerId:$("#containerId").val(),
		dateInspection:$("#newDateInspection").val(),
		vessel:$("#newVessel").val().toUpperCase(),
		travel:$("#newTravel").val().toUpperCase(),
		aa:$("#newAa").val().toUpperCase(),
		definition:$("#newComents").val(),
		coments:$("#newComents").val().toUpperCase(),
		user:$("#globalUserId").val(),
		operation:$("#operation").val(),
		destination:$("#newDestination").val(),
		origin:$("#newOrigin").val(),
		chassis:$("#newChasssis").val(),
		genSet:$("#newGenSet").val(),
		mark:$("#newMark").val(),
		setPoint:$("#newSetPoint").val(),
		ventilation:$("#newVentilation").val(),
		condition:$("#containerConditionInspection").val(),
		clasification:$("#containerClasificationInspection").val(),
		modelYear:$("#newYear").val(),
		aptTo:$("#newAptTo").val(),
		technology:$("#tecnologyInspection").val(),
		mark:$("#newMarkInspection").val(),
		associateUnit:$("#unitAsosiateInspection").val().toUpperCase(),
		associateUnitGenset:$("#unitAsosiateInspectiongenset").val().toUpperCase(),
		horometro:$("#horometroInspection").val(),
		generatorType:$("#newGenetatorTypeInspection").val(),
		diesel:$("#dieselInpection").val(),
		idUser:$("#globalUserId").val(),
		nomenclatura:$("#newModel").val(),
		dataUrl:$("#draw-dataUrl").val()
	};
	$.ajax({
			type: "POST",
			url: 'preGate/newInspectionContainer',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					window.open('preGate/PDF_EIR?containerId='+ response.pdf+'')
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#inspectionModal").modal("hide");
				configDataTablePregate()
				});
				}else{
					Swal.fire("Error "+response.message, "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
		}else{
			Swal.fire("Por favor selecciona la Nomenclatura", "", "warning");
		}
		
	
				return false;
	});	
*/

$("#addNewDamageModel").submit(function () {
	
	if(damage==1){
		table = $("#imageTableInspection").DataTable().rows().data().toArray();
		
	var image = [];
	var formData = new FormData();
	table.forEach(function(item) {
			image.push(item.file);
			formData.append('file', item.file);
		formData.append('containerId',  $("#containerId").val());
		
		});
		
		var data = {
			inspectionId:  $("#inspectionId").val(),
			containerId:  $("#containerId").val(),
			 part:  $("#newPart").val(),
			 component:  $("#newComponentInspection").val(),
			 damage: $("#newDamage").val(),
			 location: $("#newLocationInspection").val(),
			 repair: $("#newRepair").val(),
			 reference: $("#newReferent").val(),
			 customerType: $("#inspectionCustomerType").val(),
			 //photo: item.photo,
			 length: $("#largeInspection").val(),
			 width:  $("#heigthInspection").val(),
			 depth: $("#depthInspection").val(),
			 otherLength:  $("#otherLargeInspection").val(),
			 extentLarge: $("#extentLarge").val(),
			 extentHeigth: $("#extentHeigth").val(),
			 extentDepth: $("#extentDepth").val(),
			 extentOtherLarge: $("#extentOtherLarge").val(),
			 quantity:  $("#quantityInspection").val(),
	};
	
	formData.append('inspection',JSON.stringify(data));
	
	if($("#newPart").val()=="Selecciona una opción"||$("#newComponentInspection").val()=="Selecciona una opción"||$("#newDamage").val()=="Selecciona una opción"||$("#inspectionCustomerType").val()=="Selecciona una opción"){
				Swal.fire("Llenar los datos que se requieren", "", "warning");
	}else{
		$.ajax({
		type : "POST",
		url : 'preGate/addDamage',
		cache : false, 
		contentType : false,
		processData : false,
		data :formData,
		success : function(response) {
			if(response.success==true){
					Swal.fire({
				        title: "¿Desea Registrar Daño?",
				        text: " ",
				        icon: 'warning',
				        showCancelButton: true,
				        confirmButtonText: "Si",
				        cancelButtonText: "no",
				    }).then(resultado => {
				       if (resultado.value) {
				            // Hicieron click en "Sí"
							
							addNewDamage();
				        } else {
				            // Dijeron que no
							//configDataTablePregate()
							//self.location.reload();
							$("#inspectionModal").modal("hide");
							console.log("no")
				        }
				    });
				}else{
					console.log("lLLEGO AL ELSE");
					//saveInspection()
				}
			/*if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#addNewDamageModel").modal("hide");
					getInspectionsData()
				//self.location.reload();
				});
				}else{
					console.log("error")
					Swal.fire("Error "+response.message, "", "error");
				}*/
		},
		error : function() {
			showAlert(3,"ERRO 902", "El documento no puede cargarse", "Intente nuevamente o contacte al equipo de desarrollo");
		}
	});
		}
		
	}else{
			table = $("#imageTableInspection").DataTable().rows().data().toArray();
		
	var image = [];
	var formData = new FormData();
	table.forEach(function(item) {
		console.log(item.photoId)
		if(item.photoId=="x"){
			image.push(item.file);
			formData.append('file', item.file);
		}
		});
		formData.append('inspectionId',  $("#inspectionId").val());
		if(cambioComponente==0){
			$("#componentSave").val($("#newComponentInspection2").val())
		}else{
			$("#componentSave").val($("#newComponentInspection").val())
		}
		var data = {
			inspectionId:  $("#inspectionId").val(),
			containerId:  $("#containerId").val(),
			 part:  $("#newPart").val(),
			 component:  $("#componentSave").val(),
			 damage: $("#newDamage").val(),
			 location: $("#newLocationInspection").val(),
			 repair: $("#newRepair").val(),
			 reference: $("#newReferent").val(),
			 customerType: $("#inspectionCustomerType").val(),
			 //photo: item.photo,
			 length: $("#largeInspection").val(),
			 width:  $("#heigthInspection").val(),
			 depth: $("#depthInspection").val(),
			 otherLength:  $("#otherLargeInspection").val(),
			 quantity:  $("#quantityInspection").val(),
	};
		formData.append('inspectionUpdate',JSON.stringify(data));
		
				$.ajax({
		type : "POST",
		url : 'preGate/addImages',
		cache : false, 
		contentType : false,
		processData : false,
		data :formData,
		success : function(response) {
			if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#addNewDamageModel").modal("hide");
					getInspectionsData()
				//self.location.reload();
				});
				}else{
					console.log("error")
					Swal.fire("Error "+response.message, "", "error");
				}
		},
		error : function() {
			showAlert(3,"ERRO 902", "El documento no puede cargarse", "Intente nuevamente o contacte al equipo de desarrollo");
		}
	});
	}
	
		
				return false;
		
		
		
	});	
	

}



function addNewContainer() {
	//$("#newContainerTypeSave").val($("#newContainerDescription").val());
	/*var data = {
			client_id: "MV6xiAf68JbCd6qrANYv5dFxFJMBYmWn",
			client_secret : "DyFK2kKA5cLAPx3C",
			grant_type : "clientCredentials"
	};
	$.ajax({
			type: "POST",
			url: 'https://iam-stage.maersk.com/acm/oauth2/realms/mau/access_token',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8 ; Access-Control-Allow-Origin",
			data:data,
			success: function(response){
				console.log(response)
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});*/
	eventDateValidator();
	$("#newContainerName").val("")
	
				
	
				$("#newContainerSize").attr("hidden", true);
				$("#newContainerDescription").attr("hidden", true);
				$("#newShippingConpanyContainer").attr("hidden", true);
				$("#newContainerTypeSave").attr("hidden", true);
	
				$("#newContainerSize").attr("disabled", false);
				$("#newContainerDescription").attr("disabled", false);
				$("#newShippingConpanyContainer").attr("disabled", false);
				$("#newContainerTypeSave").attr("disabled", false);
	$("#newContainerModal").modal("show");
	}
	
	function verify(container) {
	if (container.slice(10, container.length) == digitValidator(container)) {
		
		return true;
	} else {
		
		return false
	}
	
	
}
	
	
function addNewEvent(data) {
	$("#newStatusEvent").val("INBOUND")
	validationDate();
	/*typeZise=document.getElementById("newContainerTypeEvent");
	for (let i = typeZise.options.length; i >= 0; i--) {
    typeZise.remove(i);
  	}*/
	
	currentData = $("#containerTable").DataTable().row(data).data();
	$("#containerId").val(currentData.containerId)
	$("#newContainerNameEvent").val(currentData.container);
	$("#newContainerTypeEvent").val(currentData.containerType);
	$("#shippingCompanyEvent").val(currentData.shippingCompany);
	$("#newEventModal").modal("show");
	}
	
function setDescription() {
	$("#newContainerTypeSave").val($("#newContainerDescription").val());
	}

function setCodeDescription() {
	$("#newContainerDescription").val($("#newContainerTypeSave").val());
	}
	
function validationDate(){
	 fecha = new Date();
     year = fecha.getFullYear();
     day = fecha.getDate();
     month = fecha.getMonth();
     month = month + 1;
	if(day<10){
		var day = "0" + day;
	}
    if (month < 10){ 
	var month = "0" + month;
	}
    else{ 
	var month = month.toString;
	}
    document.getElementById("newEventDate").min = year+'-'+month+'-'+day; 
	document.getElementById("newEventDate").value = year+'-'+month+'-'+day; 
}
function configDataTablePregate(){
	$("#containerTable").DataTable().destroy();
	$("#containerTable").DataTable({
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
				{text: 'Alta de Unidades', action: function() { addNewContainer()}},
				{extend: 'excelHtml5', title: 'Pregate'},
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-info"
	            },
			}},
			ajax: {
			url: "preGate/getDataTable",
			type: 'GET',
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
			{ data: "registerDate",visible: false },
			{ data: "container",visible: true , render : function(data) {
						$("#containerName").val(data);
						return $("#containerName").val();
					}}, 
			{ data: "containerType",visible: true , render : function(data) {
						$("#newContainerTypeTable").val(data);
						return $("#newContainerTypeTable option:selected").html();
					}}, 
			{ data: "containerSize",visible: true },
			/*{ data: "containerSize", visible: true , render : function(data) {
						$("#newModelevent").val(data);
						return $("#newModelevent option:selected").html();
					}}, */
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#newShippingCompany").val(data);
						return $("#newShippingCompany option:selected").html();
					}}, 
			{ data: "clasification", visible: false , render : function(data) {
				if(data!=null){
					$("#containerClasification").val(data);
						return $("#containerClasification option:selected").html();
				}else{
					return null;
				}
						
					}},
			{ data: "condition", visible: true , render : function(data) {
				if(data!=null){
					var combo = document.getElementById("containerClasification");
				var selected = combo.options[combo.selectedIndex].text;
						$("#containerCondition").val(data);
						return $("#containerCondition option:selected").html()+" "+selected;
				}else{
					return null;
				}
				
					}},
			
			{ data: "status", visible: false , render : function(data) {
						$("#containerStatus").val(data);
						return $("#containerStatus option:selected").html();
					}},
			
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
				$("#containerId").val(data);
				if($("#globalUserRole").val()=="CAPTURISTA"){
				 return '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp';
				}else{
				if($("#containerStatus").val()==2){
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp'+
						'<button type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar" onclick="inspectionContainer(\'' +data + '\');"><i class="fas fa-eye"></i></button>&nbsp';	
				}else{
					return '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp';
				}	
				}
			}},
		],
	}).columns.adjust();
}

function configDataTable() {
	console.log("estoy yaendo aqui")
	$("#containerTable").DataTable({
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
				{text: 'Alta de Unidades', action: function() { addNewContainer()}},
				{extend: 'excelHtml5', title: 'Pregate'},
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-info"
	            },
			}},
			ajax: {
			url: "preGate/getDataTable",
			type: 'GET',
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
			{ data: "registerDate",visible: false },
			{ data: "container",visible: true , render : function(data) {
						$("#containerName").val(data);
						return $("#containerName").val();
					}}, 
			{ data: "containerType",visible: true , render : function(data) {
						$("#newContainerTypeTable").val(data);
						return $("#newContainerTypeTable option:selected").html();
					}}, 
			{ data: "contaierSize",visible: true },
			/*{ data: "containerSize", visible: true , render : function(data) {
						$("#newModelevent").val(data);
						return $("#newModelevent option:selected").html();
					}}, */
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#newShippingCompany").val(data);
						return $("#newShippingCompany option:selected").html();
					}}, 
			{ data: "clasification", visible: false , render : function(data) {
				if(data!=null){
					$("#containerClasification").val(data);
						return $("#containerClasification option:selected").html();
				}else{
					return null;
				}
						
					}},
			{ data: "condition", visible: true , render : function(data) {
				if(data!=null){
					var combo = document.getElementById("containerClasification");
				var selected = combo.options[combo.selectedIndex].text;
						$("#containerCondition").val(data);
						return $("#containerCondition option:selected").html()+" "+selected;
				}else{
					return null;
				}
				
					}},
			
			{ data: "status", visible: false , render : function(data) {
						$("#containerStatus").val(data);
						return $("#containerStatus option:selected").html();
					}},
			
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
				$("#containerId").val(data);
				if($("#containerStatus").val()==2){
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp'+
						'<button type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar" onclick="inspectionContainer(\'' + data + '\');"><i class="fas fa-eye"></i></button>&nbsp';	
				}else{
					return '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp';
				}
				
				
			}},
		],
		
		order: [[1, 'desc']] // Ordenar por la columna de fecha (registerDate) en orden ascendente
	}).columns.adjust();
		$("#inspectionTable").DataTable({
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
				{text: 'Agregar Daño', action: function() { addNewDamage(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		columns: [
			{ data: "inspectionId",visible: true },
			{ data: "part",visible: true , render : function(data) {
						$("#newPart").val(data);
						return $("#newPart option:selected").html();
					}}, 
					
			{ data: "component",visible: true , render : function(data) {
					$("#catComponent").val(data);
						return $("#catComponent option:selected").html();
			}}, 
			{ data: "damage",visible: true , render : function(data) {
						$("#catDamageNew").val(data);
						return $("#catDamageNew option:selected").html();
					}}, 
			{ data: "location",visible: false },
			{ data: "repair",visible: true , render : function(data) {
						$("#newRepair").val(data);
						return $("#newRepair option:selected").html();
					}}, 	
			
			{ data: "reference", visible: true },
			{ data: "customerType",visible: true , render : function(data) {
						$("#inspectionCustomerType").val(data);
						return $("#inspectionCustomerType option:selected").html();
					}}, 
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				//return data;
					return '<button type="button" class="btn btn-outline-dark btn-sm" title="Ver fotos" onclick="viewPhotos(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp';
				//'<a onclick="showPhoto(\'' + base64 + '\');" > <img  src="' + base64 + '" width="40" height="30" ></a>';
			}},
			{ data: "length",visible: false },
			{ data: "width",visible: false },
			{ data: "depth",visible: false },
			{ data: "otherLength",visible: false },
			{ data: "quantity",visible: false },
			{ data: "inspectionId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteInspection(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
	
	$("#estimateTable").DataTable({
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
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
			{ data: "inspectionId",visible: true },
		],
	}).columns.adjust();
	
	$("#eventTable").DataTable({
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
			{ data: "eventId",visible: false },
			{ data: "eventType",visible: true }, 
			{ data: "eventDate",visible: true },
			{ data: "estimateRequired",visible: true },
			{ data: "inspected",visible: true }, 
			{ data: "inspectedBy",visible: true }, 
			{ data: "booking",visible: true },
			{ data: "fillState",visible: true },
			{ data: "alternateUnit",visible: true },
			{ data: "associatedUnit",visible: true },
			{ data: "transportType",visible: true },
			{ data: "sapSaleOrder",visible: true },
			{ data: "unitQuality",visible: true },
			{ data: "unitClasification",visible: true },
			{ data: "sealNumber",visible: true },
			{ data: "customerIdentifier",visible: true },
			{ data: "type",visible: true },
			{ data: "model",visible: true },
			{ data: "location",visible: true },
			{ data: "container",visible: true },
			{ data: "modelYear",visible: true },
			{ data: "containerId",visible: true },
			{ data: "transmit",visible: true },
							
		],
	}).columns.adjust();
	
	
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
				if(data.length==36){
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="showPhoto(\'' + data + '\');"><i class="fas fa-eye"></i></button>&nbsp';
			}else{
				return '<a onclick="showPhotoOther(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>'
			}	
			}},
			{ data: "file", visible: false },
			{ data: "photoId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Foto" onclick="deleteImage(\'' + data + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
	
}

function getInspectionsData(){
	console.log("cae en este llamad--------")
	$("#inspectionTable").DataTable().destroy();
	$("#inspectionTable").DataTable({
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
				{text: 'Agregar Daño', action: function() { addNewDamage(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
				ajax: {
			url: "preGate/getInspections",
			type: 'GET',
			dataSrc: '',
			data: {containerId :  $("#containerId").val(),
					userId : $("#globalUserId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "part",visible: true },
			{ data: "component",visible: true , render : function(data) {
					$("#catComponent").val(data);
						return $("#catComponent option:selected").html();
			}}, 
			{ data: "damage",visible: true , render : function(data) {
						$("#catDamageNew").val(data);
						return $("#catDamageNew option:selected").html();
					}}, 
			{ data: "location",visible: false },
			{ data: "repair",visible: true , render : function(data) {
						$("#newRepair").val(data);
						return $("#newRepair option:selected").html();
					}}, 	
			
			{ data: "reference", visible: true },
			{ data: "customerType",visible: true , render : function(data) {
						$("#inspectionCustomerType").val(data);
						return $("#inspectionCustomerType option:selected").html();
					}}, 
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Ver fotos" onclick="viewPhotos(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp';
				
				//'<a onclick="showPhoto(\'' + base64 + '\');" > <img  src="' + base64 + '" width="40" height="30" ></a>';
			}},
				{ data: "length",visible: false },
			{ data: "width",visible: false },
			{ data: "depth",visible: false },
			{ data: "otherLength",visible: false },
			{ data: "quantity",visible: false },
			
			{ data: "extentLarge",visible: false },
			{ data: "extentHeigth",visible: false },
			{ data: "extentDepth",visible: false },
			{ data: "extentOtherLarge",visible: false },
			
			{ data: "inspectionId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteInspection(\'' + data + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
	}
	
	
	
function validation(data) {
	$("#containerId").val(data);
$("#conditionModel").modal("show");
	/*$.ajax({
			type: "POST",
			url: "preGate/containerValidation",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerId : data,
			clasification : value},
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
				self.location.reload();
				});
				}else{
					Swal.fire("Error", "", "error");
				}
			},
			error: function(){
				alert("AJAX ERROR");
			}
		});*/

		
	}

		
		
function showPhoto(data){
	window.open('preGate/IMAGE?photoId='+ data+'')
	
	
/*Swal.fire({
  imageUrl: data,
})*/
	}

function showPhotoOther(data){
	
Swal.fire({
  imageUrl: data,
})
	}
	
	
function showComponents(){
	data = $("#newPart").val();
	console.log(data)
	$("#seccionSave").val(data)
	
	clearCombo(document.getElementById("newComponentInspection"));
	//text = document.getElementById("newPart").options[data].text
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
	document.getElementById("newComponentInspection")
	$.ajax({
		type: "GET",
		url: 'preGate/getComponentIformation',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerType :textContainer,
		Section: data},
		success: function(response){
			
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

function saveComponent(){
	cambioComponente = 1;
	if ($('#newComponentInspection').is(':hidden')) {
		console.log("esta visible")
		document.getElementById('newComponentInspection').removeAttribute("hidden"); 
		document.getElementById('newComponentInspection2').setAttribute("hidden",true); 
		$("#newPart").val("Selecciona una opción")
		$("#newDamage").val("Selecciona una opción")
		$("#newComponentInspection").val("Selecciona una opción")
			
}

	$("#componentSave").val($("#newComponentInspection").val())
	
}

function saveDamage(){
	$("#damageSave").val($("#newDamage").val())
	
	
}

function getSection(){
	clearCombo(document.getElementById("newPart"));
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
$.ajax({
			type: "GET",
			url: 'preGate/getSectionInformation',
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
function getNomenclatura(){
	clearCombo(document.getElementById("newModel"))
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
	$.ajax({
			type: "GET",
			url: 'preGate/getNomenclatura',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerType : textContainer,
			size : $("#containerSize").val()},
			success: function(response){
				//clearCombo(document.getElementById("newPart"));
				fillComboNomenclatura(document.getElementById("newModel"),response);
			},
			error: function(){
				alert("AJAX ERROR");
			}
		});
}
	function getSingleData(data){	
		
		 return new Promise((resolve ,reject)=>{
		$.ajax({
		type: "GET",
		url: 'preGate/getSingleData',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerId :data,
		Section: data},
		success: function(response){
			 resolve(response)
		console.log(response)
			//$("#appointmentId").val(response.appointmentId),
			$("#newCondition").val(response.conditionPregate), 
			$("#typeServicePregate").val(response.typeServicePregate), 
			$("#newBillToPregate").val(response.billTo),
			$("#newTransportCompanyPregate").val(response.transportId),
			$("#newBuquePregate").val(response.buque),
			$("#newBlPregate").val(response.bl),
			$("#newOperatorNamePregate").val(response.operatorName),
			$("#name").val(response.operatorName),
			$("#newPlate").val(response.plate),
			$("#newEconomicNumber").val(response.economicNumber),
			$("#newOriginPregate").val(response.originPregate),
			
			$("#containerId").val(response.containerId)
			$("#containerType").val(response.containerType)
			$("#containerSize").val(response.containerSize)
			
			if(response.typeServicePregate==null){
			getAppointmentInfo()
		}
		},
		
		error: function(){
			reject(response)
			alert("AJAX ERROR");
		}
	});
	
	
		return false;
			});
     
   
			
		}
function pregate(data){	
	getSingleData(data)
	$("#containerId").val(data),
	$("#pregateModel").modal("show");
	$("#operation").val("UPDATE")

	
	}
	
	
function viewPhotos(data){
	damage = 2; 
	
document.getElementById('newComponentInspection2').removeAttribute("hidden"); 
document.getElementById('newComponentInspection').setAttribute("hidden",true); 

	if ($('#inspectionTable').DataTable().row('.selected').true){
		console.log("hola")
	}
	
	table = $("#inspectionTable").DataTable().row(data).data();
	console.log(table)
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

$("#extentLarge").val(table.extentLarge)
$("#extentHeigth").val(table.extentHeigth)
$("#extentDepth").val(table.extentDepth)
$("#extentOtherLarge").val(table.extentOtherLarge)

$("#imageTableInspection").DataTable().clear().draw();

	var info = {
			containerId: $("#inspectionId").val(),
			} 
		
	$.ajax({
		type: "GET",
		url: 'preGate/getPhotos',
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


function getAppointmentInfo(){
	$.ajax({
		type: "GET",
		url: 'preGate/getAppointmentData',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerId :$("#containerId").val()},
		success: function(response){
			console.log(response)
		if(response.appointmentId!=null){
			$("#newCondition").val("VACIO"), 
			$("#newBillToPregate").val(response.companyName), 
			$("#typeServicePregate").val("MERCHANT")
			$("#newTransportCompanyPregate").val(response.customer)
		}else{
			$("#newCondition").val("VACIO"), 
			$("#typeServicePregate").val("CARRIER")
		}
			
		},
		error: function(){
			reject(response)
			alert("AJAX ERROR");
		}
	});
	
}




async  function inspectionContainer(data){

	//Deshabilita el boton aceptar
	var botonAceptar = document.getElementById('btnAceptar');
            botonAceptar.disabled = true;
            
    //Limpia el recuadro de firma
   	var canvas = document.getElementById("draw-canvas");
	var ctx = canvas.getContext("2d");
	canvas.width = canvas.width;
	
	$("#inspectionTable").DataTable().clear().draw();
	 await getSingleData(data);
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
	if(textContainer=="RF"){
		document.getElementById("refferInspectionData").removeAttribute("hidden");
		document.getElementById("gensetInspectionData").setAttribute("hidden",true);
	}
	if(textContainer=="DC"){
		document.getElementById("refferInspectionData").setAttribute("hidden",true);
		document.getElementById("gensetInspectionData").setAttribute("hidden",true);
	}
	if(textContainer=="GS"){
		document.getElementById("gensetInspectionData").removeAttribute("hidden");
		document.getElementById("refferInspectionData").setAttribute("hidden",true);
	}
	getNomenclatura()
	getSingleData(data)
	getSection()
	eventDateValidator()
	getDamageInfotmation($("#newContainerDescription").val())
	getInspectionsData()
	$("#inspectionModal").modal("show");
	$("#operation").val("UPDATE")
	
}





function getDamageInfotmation(data){
			$.ajax({
		type: "GET",
		url: 'preGate/getDamageInformation',
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

function checkboxDestination(){
	document.getElementById("origin").checked=false
	document.getElementById("destination").checked=true
	document.getElementById("newDestination").removeAttribute("hidden")
	document.getElementById("newOrigin").setAttribute("hidden",true)
	$("#newOrigin").val("")
	
	}
function checkboxOrigin(){
	document.getElementById("origin").checked=true
	document.getElementById("destination").checked=false
	document.getElementById("newOrigin").removeAttribute("hidden")
	document.getElementById("newDestination").setAttribute("hidden",true)
	$("#newDestination").val("")
	}
	
editAdd=0;


function addNewDamage(){
	damage = 1;
	
	document.getElementById('newComponentInspection').removeAttribute("hidden"); 
	document.getElementById('newComponentInspection2').setAttribute("hidden",true); 
	
	getSection(),
	getDamageInfotmation($("#containerType").val())
	
	//Limpia los campos
	$("#newPart").val(""),
	$("#newComponentInspection2").val(""),
	$("#newDamage").val(""),
	$("#newLocationInspection").val(""),	
	$("#newReferent").val(""),
	$("#inspectionCustomerType").val("1"),
	$("#newImageCode").val(""),
	$("#largeInspection").val(""),
	$("#heigthInspection").val(""),
	$("#depthInspection").val(""),
	$("#otherLargeInspection").val(""),
	$("#extentOtherLarge").val("1"),
	$("#quantityInspection").val(""),
	
	
	document.getElementById("btnDamage").removeAttribute("hidden");
	$("#imageTableInspection").DataTable().clear().draw();
	$("#addNewDamageModel").modal("show");
	
	
}


function addImageToTable(){
	
	
	var files = $('#newImageCode').prop('files');
	for(var i=0;i<files.length;i++){
		getBase64FromFile(files[i], function(base64,img){
$("#imageTableInspection").DataTable().row.add({
				"photoId": "x",
				"image": base64,
				"file": img,
			}).draw(false);
		});
	}



}

function getBase64FromFile(img, callback){
  let fileReader = new FileReader();
  fileReader.addEventListener('load', function(evt){
    callback(fileReader.result,img);
  });
  fileReader.readAsDataURL(img);
}


function addInspection(){
	var files = $('#newImageCode').prop('files');
	var image= [];
	for(var i=0;i<files.length;i++){
		image.push(files[i]);
	}
	if($("#containerStatus").val() == null){
		Swal.fire("Por favor Selecciona una imagen", "", "warning");
	}else{
	if($("#newPart").val()=="Selecciona una opción"||$("#newComponentInspection").val()=="Selecciona una opción"||$("#newDamage").val()=="Selecciona una opción"||$("#newImageCode").val()==""){
				Swal.fire("Llenar los datos que se requieren", "", "warning");
	}else{	
		var dataUrl = $("#imagenData").val();
	if($("#containerStatus").val()=="7"){
		$("#inspectionTable").DataTable().row.add({
				"inspectionId": $("#inspectionId").val(),
				"part": $("#newPart").val(),
				"damage": $("#newDamageGenSet").val(),
				"component":  $("#newComponentInspection").val(),
				"location" :  $("#newLocationInspection").val(),
				"repair" : $("#newRepair").val(),
				"damageCode": "",
				"reference": $("#newReferent").val().toUpperCase(),
				"customerType": "",
				"photo": image,
				"length": $("#largeInspection").val(),
				"width": $("#heigthInspection").val(),
				"depth": $("#depthInspection").val(),
				"otherLength": $("#otherLargeInspection").val(),
				"quantity": $("#quantityInspection").val(),
			}).draw(false);
	}else{
		
	if(editAdd==1){
		editAdd=0;
		$("#inspectionTable").DataTable().row.add({
				"inspectionId": $("#inspectionId").val(),
				"part": $("#newPart").val(),
				"damage": $("#newDamage").val(),
				"component":  $("#newComponentInspection").val(),
				"location" :  $("#newLocationInspection").val(),
				"repair" : $("#newRepair").val(),
				"damageCode": "",
				"reference": $("#newReferent").val().toUpperCase(),
				"customerType": $("#inspectionCustomerType").val(),
				"photo":  image,
				"length": $("#largeInspection").val(),
				"width": $("#heigthInspection").val(),
				"depth": $("#depthInspection").val(),
				"otherLength": $("#otherLargeInspection").val(),
				"quantity": $("#quantityInspection").val(),
			}).draw(false);
			$("#inspectionId").val("")
	}else{
	if($("#newPart").val()!=0&&$("#newDamage").val()!="0"&&$("#newErrorCode").val()!="0"){
	filas =  $("#inspectionTable").DataTable().rows().data().count()
	$("#inspectionId").val(filas+1)
		$("#inspectionTable").DataTable().row.add({
				"inspectionId": $("#inspectionId").val(),
				"part": $("#newPart").val(),
				"damage": $("#newDamage").val(),
				"component":  $("#newComponentInspection").val(),
				"location" :  $("#newLocationInspection").val(),
				"repair" : $("#newRepair").val(),
				"damageCode": "",
				"reference": $("#newReferent").val(),
				"customerType": $("#inspectionCustomerType").val(),
				"photo":  image,
				"length": $("#largeInspection").val(),
				"width": $("#heigthInspection").val(),
				"depth": $("#depthInspection").val(),
				"otherLength": $("#otherLargeInspection").val(),
				"quantity": $("#quantityInspection").val(),
			}).draw(false);
		
	}else{
		Swal.fire("Llenar los datos que se requieren", "", "warning");
	}
	
	}
		 }	
	}
	}
	
			
		document.getElementById("newImageCode").value= null;
	document.getElementById("imagenData").value= null;	
	}
	


	
	
function setCode(){
	$("#newErrorCode").val($("#newDamage").val());
}

function setDamage(){
	$("#newDamage").val($("#newErrorCode").val());
}

/*function eventDateValidator(){
	console.log("llego")
	 fecha = new Date();
     year = fecha.getFullYear();
     day = fecha.getDate();
     month = fecha.getMonth();
     month = month + 1;
	if(day<10){
	var day = "0" + day;
	}
    if (month < 10){ 
	var month = "0" + month;
	}
    else{ 
	var month = month;
	}
    document.getElementById("newDateInspection").min = year+'-'+month+'-'+day; 
	document.getElementById("newDateInspection").value = year+'-'+month+'-'+day; 
}*/

function deleteInspection(data){
	console.log(data)
	$.ajax({
		type: "POST",
		url: 'preGate/deleteDamage',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {inspectionId : data},
		success: function(response){
			console.log(response)
			alert("Se borro la inspeccion")		
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	//$('#inspectionTable').DataTable().row('.selected').remove().draw(false);
}

function deleteImage(data){
	
	console.log(data)
	$.ajax({
		type: "POST",
		url: 'preGate/deleteImage',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {photoId : data},
		success: function(response){
			console.log(response)
			alert("Se borro la imagen")		
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	//$('#imageTableInspection').DataTable().row('.selected').remove().draw(false);
	
}




function stopVideo(){
document.getElementById('snaptake').removeAttribute("hidden"); 
document.getElementById('snap').setAttribute("hidden",true); 
var video = document.getElementById("video");
         video.pause();
         video.currentTime = 0;
 }

function playVideo(){
document.getElementById('snap').removeAttribute("hidden"); 
document.getElementById('snaptake').setAttribute("hidden",true); 
var video = document.getElementById("video");
         video.play();
         video.currentTime = 0;
  }


// foto	 --------------------------------------------------
function photo(){
		'use strict';

const video = document.getElementById('video');
const canvas = document.getElementById('canvas');
const snap = document.getElementById("snap");
const errorMsgElement = document.querySelector('span#errorMsg');

const constraints = {
  audio: false,
  video: {
    width: 1280, height: 720
  }
};

// Access webcam
async function init() {
  try {
    const stream = await navigator.mediaDevices.getUserMedia(constraints);
    handleSuccess(stream);
  } catch (e) {
    errorMsgElement.innerHTML = `navigator.getUserMedia error:${e.toString()}`;
  }
}

// Success
function handleSuccess(stream) {
  window.stream = stream;
  video.srcObject = stream;
}

// Load init
init();

// Draw image
var context = canvas.getContext('2d');
snap.addEventListener("click", function() {
        context.drawImage(video, 0, 0, 720, 720);
});

	}


// DATA URL A BLOB

function dataURItoBlob(dataUrl) {
    // convert base64 to raw binary data held in a string
    // doesn't handle URLEncoded DataURIs - see SO answer #6850276 for code that does this
    var byteString = atob(dataUrl.split(',')[1]);

    // separate out the mime component
    var mimeString = dataUrl.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to an ArrayBuffer
    var ab = new ArrayBuffer(byteString.length);
    var ia = new Uint8Array(ab);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    //Old Code
    //write the ArrayBuffer to a blob, and you're done
    //var bb = new BlobBuilder();
    //bb.append(ab);
    //return bb.getBlob(mimeString);

    //New Code
    return new Blob([ab], {type: mimeString});


}

	
	
	
function ConfirmEstimationAppointment(data){
	showInspections(data);
	//$("#appointmentId").val(data);
	$("#estimateModal").modal("show");
	
}

function showEvents(data){
	showEventInformation(data);
	//$("#appointmentId").val(data);
	$("#eventShowModal").modal("show");
	
}

function showEventInformation(data) {
	$("#eventTable").DataTable().destroy();
	$("#eventTable").DataTable({
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
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: 'preGate/getEvents',
			type: 'GET',
			dataSrc: '',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerId : data},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "eventId",visible: false },
			{ data: "eventType",visible: true , render : function(data) {
						$("#newEventType").val(data);
						return $("#newEventType option:selected").html();
					}}, 
			{ data: "eventDate",visible: true },
			{ data: "estimateRequired",visible: false },
			{ data: "inspected",visible: false }, 
			{ data: "inspectedBy",visible: false }, 
			{ data: "booking",visible: false },
			{ data: "fillState",visible: false },
			{ data: "alternateUnit",visible: false },
			{ data: "associatedUnit",visible: false },
			{ data: "transportType",visible: false },
			{ data: "sapSaleOrder",visible: false },
			{ data: "unitQuality",visible: false },
			{ data: "unitClasification",visible: false },
			{ data: "sealNumber",visible: false },
			{ data: "customerIdentifier",visible: false },
			{ data: "type",visible: false },
			{ data: "model",visible: false },
			{ data: "location",visible: false },
			{ data: "container",visible: false },
			{ data: "modelYear",visible: false },
			{ data: "containerId",visible: false },
			{ data: "transmit",visible: false },
			
		],
	}).columns.adjust();
	
} 

function showInspections(data) {
	$("#estimateTable").DataTable().destroy();
	$("#estimateTable").DataTable({
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
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: 'preGate/getInspections',
			type: 'GET',
			dataSrc: '',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {containerId : data},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true , render : function(data) {
						$("#newErrorCode").val(data);
						return $("#newErrorCode option:selected").html();
					}}, 
			{ data: "part",visible: true , render : function(data) {
						$("#newPart").val(data);
						return $("#newPart option:selected").html();
					}}, 
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			{ data: "damageCode",visible: true },
			
			
		],
	}).columns.adjust();
	
} 

function convertToBase64() {
        //Read File
        var selectedFile = document.getElementById("newImageCode").files;
        //Check File is not Empty
        if (selectedFile.length > 0) {
            // Select the very first file from list
            var fileToLoad = selectedFile[0];
            // FileReader function for read the file.
            var fileReader = new FileReader();
            var base64;
            // Onload of file read the file content

            fileReader.onload = function(fileLoadedEvent) {
                base64 = fileLoadedEvent.target.result;
                // Print data in console
				$("#imagenData").val("")
				$("#imagenData").val(base64)
				 document.getElementById("imagenPrevisualizacion").src = $("#imagenData").val();
            };
            // Convert data to base64
             fileReader.readAsDataURL(fileToLoad);
        }
    }


function convertToBase64data(data){
	//console.log(data.files)
	//var selectedFile = data.files;
	var base64;
	 if (data.length > 0) {
            // Select the very first file from list
            var fileToLoad = data[0];
            // FileReader function for read the file.
            var fileReader = new FileReader();
            // Onload of file read the file content

            fileReader.onload = function(fileLoadedEvent) {
                base64 = fileLoadedEvent.target.result;
                // Print data in console
				$("#imagenData").val("")
				$("#imagenData").val(base64)
				 document.getElementById("imagenPrevisualizacion").src = $("#imagenData").val();
            };
            // Convert data to base64
             fileReader.readAsDataURL(fileToLoad);
        }

}


function eventDateValidator(){
	 fecha = new Date();
     year = fecha.getFullYear();
     day = fecha.getDate();
     month = fecha.getMonth();
     month = month + 1;
	if(day<10){
		var day = "0" + day;
	}
    if (month < 10){ 
	var month = "0" + month;
	}
    else{ 
	var month = month;
	}
	
    document.getElementById("startDate").min = year+'-'+month+'-'+day; 
	document.getElementById("startDate").value = year+'-'+month+'-'+day; 
	
	 document.getElementById("newDateInspection").min = year+'-'+month+'-'+day; 
	document.getElementById("newDateInspection").value = year+'-'+month+'-'+day; 
	
}


function searchContainer(){

	$.ajax({
		type: "GET",
		url: 'preGate/getcontainerHistoric',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {container : $("#newContainerName").val()},
		success: function(response){
			
			console.log(response.containerId)
			
			if(response.containerId == null){
				
				document.getElementById("newContainerSize").removeAttribute("hidden");
				document.getElementById("newContainerDescription").removeAttribute("hidden");
				document.getElementById("newContainerTypeSave").removeAttribute("hidden");
				document.getElementById("newShippingConpanyContainer").removeAttribute("hidden");
				
				$("#newContainerSize").val();
				$("#newContainerDescription").val();
				$("#newShippingConpanyContainer").val();
				
				$("#newContainerSize").attr("disabled", false);
				$("#newContainerDescription").attr("disabled", false);
				$("#newShippingConpanyContainer").attr("disabled", false);
				$("#newContainerTypeSave").attr("disabled", false);
			
			Swal.fire("", "No hay informacion de la unidad", "");
			} else{
				document.getElementById("newContainerSize").removeAttribute("hidden");
				document.getElementById("newContainerDescription").removeAttribute("hidden");
				document.getElementById("newContainerTypeSave").removeAttribute("hidden");
				document.getElementById("newShippingConpanyContainer").removeAttribute("hidden");
				
				
			
				$("#newContainerSize").val(response.containerSize).attr("disabled", true);
				$("#newContainerDescription").val(response.containerType).attr("disabled", true);
				$("#newShippingConpanyContainer").val(response.shippingCompany).attr("disabled", true);
				$("#newContainerTypeSave").attr("disabled", true);
				
			Swal.fire("", "Unidad encontrada", "");
			setDescription()
			
			}
			
			
		},
		
		error: function(){
			alert("AJAX ERROR");
		}
	});
}

function requestDamage(){
	console.log($("#containerId").val());
	$.ajax({
		type: "GET",
		url: 'preGate/getRequestInspection',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerId : $("#containerId").val()},
		success: function(response){
			console.log(response)
			if(response.success==false){
					Swal.fire({
				        title: "No hay daño registrado",
				        text: "¿Desea Registrar Daño?",
				        icon: 'warning',
				        showCancelButton: true,
				        confirmButtonText: "Si",
				        cancelButtonText: "no",
				    }).then(resultado => {
				       if (resultado.value) {
				            // Hicieron click en "Sí"
							//configDataTablePregate()
							//inspectionContainer($("#containerId").val())
							//configDataTablePregate()
							console.log("si")
							addNewDamage();
				        } else {
				            // Dijeron que no
							//configDataTablePregate()
							//self.location.reload();
							console.log("no")
				        }
				    });
				}else{
					console.log("lLLEGO AL ELSE");
					saveInspection()
				}
	
		},
		
		error: function(){
			alert("AJAX ERROR");
		}
	}); 
	
}

function saveInspection(){
	console.log("ok saveInspection");
	
		if($("#newModel").val()!="Selecciona una opción"){
			var data = {
		containerId:$("#containerId").val(),
		dateInspection:$("#newDateInspection").val(),
		vessel:$("#newVessel").val().toUpperCase(),
		travel:$("#newTravel").val().toUpperCase(),
		aa:$("#newAa").val().toUpperCase(),
		definition:$("#newComents").val(),
		coments:$("#newComents").val().toUpperCase(),
		user:$("#globalUserId").val(),
		operation:$("#operation").val(),
		destination:$("#newDestination").val(),
		origin:$("#newOrigin").val(),
		chassis:$("#newChasssis").val(),
		genSet:$("#newGenSet").val(),
		mark:$("#newMark").val(),
		setPoint:$("#newSetPoint").val(),
		ventilation:$("#newVentilation").val(),
		condition:$("#containerConditionInspection").val(),
		clasification:$("#containerClasificationInspection").val(),
		modelYear:$("#newYear").val(),
		aptTo:$("#newAptTo").val(),
		technology:$("#tecnologyInspection").val(),
		mark:$("#newMarkInspection").val(),
		associateUnit:$("#unitAsosiateInspection").val().toUpperCase(),
		associateUnitGenset:$("#unitAsosiateInspectiongenset").val().toUpperCase(),
		horometro:$("#horometroInspection").val(),
		generatorType:$("#newGenetatorTypeInspection").val(),
		diesel:$("#dieselInpection").val(),
		idUser:$("#globalUserId").val(),
		nomenclatura:$("#newModel").val(),
		dataUrl:$("#draw-dataUrl").val()
	};
	$.ajax({
			type: "POST",
			url: 'preGate/newInspectionContainer',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					window.open('preGate/PDF_EIR?containerId='+ response.pdf+'')
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#inspectionModal").modal("hide");
				configDataTablePregate()
				});
				}else{
					Swal.fire("Error "+response.message, "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
		}else{
			Swal.fire("Por favor selecciona la Nomenclatura", "", "warning");
		}
		
	
				return false;
	
}