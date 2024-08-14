
$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });

$(document).ready( function() {
	$("#appointmentId").val(GetURLParameter('appointmentId'));
	$('#loading').hide();
	configDataTable();
	initComponents();
	validationCode($("#newContainerName")) 
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
function initComponents() {
	
	
		
		$("#gateOutModel").submit(function () {
		var data = {
			condition: $("#newCondition").val(), 
			typeServicePregate : $("#typeServicePregate").val(), 
			billTo : $("#newBillTo").val(),
			transportId : $("#newTransportCompanyPregate").val(),
			buque : $("#newBuquePregate").val(),
			bl : $("#newBlPregate").val(),
			containerId: $("#containerId").val(),
			location: $("#globalWarehouse").val(),
			transmit: $("#eventTransmitOut").val(),
			}
			
			$.ajax({
			type: "POST",
			url: 'gateOut/gateOutEvent',
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
					Swal.fire(response.message+" Error", "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
			
		});
		
	$("#pregateModel").submit(function () {
		var data = {
			condition: $("#newCondition").val(), 
			typeServicePregate : $("#typeServicePregate").val(), 
			billTo : $("#newBillTo").val(),
			transportId : $("#newTransportCompanyPregate").val(),
			buque : $("#newBuquePregate").val(),
			bl : $("#newBlPregate").val(),
			containerId: $("#containerId").val(),
			}
			
			$.ajax({
			type: "POST",
			url: 'gateOut/saveEventInformation',
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
			url: "gateOut/containerValidation",
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
			url: 'gateOut/saveEventInformation',
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
			url: 'gateOut/saveContainer',
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
			
				
		
	}
	}else{
		Swal.fire("El codigo debe contener 11 caracteres ejemplo "+  '"XXXX1234567"', "", "warning");
	}
	return false;
		});	
	
	
	
	$("#inspectionContainerForm").submit(function () {
	if($("#containerStatus").val()=="7"){
			
	filas =  $("#inspectionTable").DataTable().rows().data().count()
	for (var i = 0; i < filas; i++) {
		
		table = $("#inspectionTable").DataTable().rows(i).data().toArray();
		table.forEach(function(item) {
		var dataT = [];
			var obj = {
			inspectionId: item.inspectionId,
			part: item.part,
			damageGenSet: item.damage,
			reference: item.reference,
			photo : item.photo,
		}
		dataT.push(obj);
		var data = {
			containerId: $("#containerId").val(),
			dateInspection : $("#newDateInspection").val(),
			coments : $("#newComents").val().toUpperCase(),
			user : $("#globalUserId").val(),
			operation: $("#operation").val(),
			genSet: $("#newGenSet").val(),
			mark: $("#newMark").val(),
			diesel : $("#newDiesel").val(),
			cms : $("#newCms").val(),
			hours : $("#newHours").val(),
			condition : $("#containerConditionInspection").val(),
			clasification : $("#containerClasificationInspection").val(),
			modelYear :$("#newYear").val(),
			idUser : $("#globalUserId").val(),
			inspectionList: JSON.stringify(dataT),
	};
	console.log(data)
		$.ajax({
			type: "POST",
			url: 'gateOut/saveUpdateContainer',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
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
			});
			
		});
		}
			
		}else{
			filas =  $("#inspectionTable").DataTable().rows().data().count()
	for (var i = 0; i < filas; i++) {
		
		table = $("#inspectionTable").DataTable().rows(i).data().toArray();
		table.forEach(function(item) {
		var dataT = [];
			var obj = {
			 inspectionId: item.inspectionId,
			 part: item.part,
			 component: item.component,
			 damage: item.damage,
			 location: item.location,
			 repair: item.repair,
			 reference: item.reference,
			 customerType: item.customerType,
			 photo: item.photo,
		}
		dataT.push(obj);
			console.log(dataT)
		var data = {
			containerId: $("#containerId").val(),
			dateInspection : $("#newDateInspection").val(),
			vessel : $("#newVessel").val().toUpperCase(),
			travel : $("#newTravel").val().toUpperCase(),
			aa : $("#newAa").val().toUpperCase(),
			definition : $("#newComents").val(),
			coments : $("#newComents").val().toUpperCase(),
			user : $("#globalUserId").val(),
			operation: $("#operation").val(),
			destination: $("#newDestination").val(),
			origin: $("#newOrigin").val(),
			chassis: $("#newChasssis").val(),
			genSet: $("#newGenSet").val(),
			mark: $("#newMark").val(),
			setPoint: $("#newSetPoint").val(),
			ventilation: $("#newVentilation").val(),
			condition : $("#containerConditionInspection").val(),
			clasification : $("#containerClasificationInspection").val(),
			modelYear :$("#newYear").val(),
				idUser : $("#globalUserId").val(),
			inspectionList: JSON.stringify(dataT),
	};
	console.log(data)
		$.ajax({
			type: "POST",
			url: 'gateOut/saveUpdateContainer',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response)
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
		}
		
		textContainer = document.getElementById("newContainerTypeSave").options[$("#containerType").val()-1].text	
		
		var doc = new jsPDF();
		doc.text(65, 20, "EIR "+textContainer);
		doc.save('EIR '+textContainer+'.pdf');
		//doc.autoPrint();
		
		/*var ticket = new jsPDF();
		ticket.text("TMM LOGISTIC, S.A DE C.V");
		ticket.save('tiket '+textContainer+'.pdf');
		ticket.autoPrint();*/
						
		}
	
	
		return false;
	});	
	
	
	

	

}
function addNewContainer() {
	eventDateValidator();
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


function configDataTable() {
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
				//{text: 'Alta de contenedores', action: function() { addNewContainer()}}
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "gateOut/getDataTable",
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
			{ data: "container",visible: true , render : function(data) {
						$("#containerName").val(data);
						return $("#containerName").val();
					}}, 
			{ data: "containerType",visible: true , render : function(data) {
						$("#newContainerTypeSave").val(data);
						return $("#newContainerTypeSave option:selected").html();
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
						return data;
					}},
			
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
				$("#containerId").val(data);
				return  '<button type="button" class="btn btn-outline-dark btn-sm" title="Gate Out" onclick="gatOut(\'' + meta.row + '\');"><i class="fas fa-file"></i></button>&nbsp';
				/*if($("#containerStatus").val()=="1"){
					return  '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp';
					}else if($("#containerStatus").val()=="2"){
						return '<a href="/myre/inspections/inspection?containerId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="ingresar al taller"><i class="fas fa-warehouse"></i></button></a>&nbsp;';
					}else if ($("#containerStatus").val()=="3"){
						return '<a href="/myre/inspections/inspection?containerId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="ingresar al taller"><i class="fas fa-warehouse"></i></button></a>&nbsp;';
					}else if ($("#containerStatus").val()=="7"){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Prueba Tecnica" onclick="inspectionContainer(\'' + meta.row + '\');"><i class="fas fa-file"></i></button>&nbsp';
					}else if ($("#containerStatus").val()=="5"){
						return null;
					}else{
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Visto Bueno" onclick="validation(\'' + data + '\');"><i class="fas fa-thumbs-up"></i></button>&nbsp';
					}*/
				
				
				
				
				
				/*if($("#appointmentId").val()==""){
				return  '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="pregate(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp'+
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar" onclick="inspectionContainer(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp'+
				'<button tydpe="button" class="btn btn-outline-dark btn-sm" title="Crear Evento" onclick="addNewEvent(\'' + meta.row + '\');"><i class="fa fa-plus"></i></button>&nbsp'+
				//'<button tydpe="button" class="btn btn-outline-dark btn-sm" title="Editar Unidad" onclick="editContainer(\'' + meta.row + '\');"><i class="fas fa-edit"></i></button>&nbsp'+
				'<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;'+
				'<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Eventos" onclick="showEvents(\'' + data + '\');"><i class="fas fa-list"></i></button>&nbsp;'+
				'<a href="/myre/inspections/inspection?containerId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-warehouse"></i></button></a>&nbsp;'+
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Visto Bueno" onclick="validation(\'' + data + '\');"><i class="fas fa-thumbs-up"></i></button>&nbsp';
				
				}else{
					if($("#containerStatus").val()=="1"){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar" onclick="inspectionContainer(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp';
					}else if($("#containerStatus").val()=="2"){
						return '<a href="/myre/inspections/inspection?containerId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="ingresar al taller"><i class="fas fa-warehouse"></i></button></a>&nbsp;';
					}else if ($("#containerStatus").val()=="3"){
						return '<a href="/myre/inspections/inspection?containerId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="ingresar al taller"><i class="fas fa-warehouse"></i></button></a>&nbsp;';
					}else if ($("#containerStatus").val()=="7"){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Prueba Tecnica" onclick="inspectionContainer(\'' + meta.row + '\');"><i class="fas fa-file"></i></button>&nbsp';
					}else if ($("#containerStatus").val()=="5"){
						return null;
					}else{
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Visto Bueno" onclick="validation(\'' + data + '\');"><i class="fas fa-thumbs-up"></i></button>&nbsp';
					}
			}*/
				
				
			}},
		],
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
				{text: 'Agregar Inspección', action: function() { addInspection(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "part",visible: true , render : function(data) {
						$("#newPart").val(data);
						return $("#newPart option:selected").html();
					}}, 
					
			{ data: "component",visible: true , render : function(data) {
					$("#newComponentInspection").val(data);
						return $("#newComponentInspection option:selected").html();
			}}, 
			{ data: "damage",visible: true , render : function(data) {
						$("#newDamage").val(data);
						return $("#newDamage option:selected").html();
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
				$("#imagenData").val(data)
				return '<a onclick="showPhoto(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
			}},
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
}


function gatOut(data){
	
	currentData = $("#containerTable").DataTable().row(data).data();
	console.log(currentData)
	$("#containerId").val(currentData.containerId)
	$("#newShippingCompanyOut").val(currentData.shippingCompany);
	$("#clientFinalOut").val(currentData.billTo);
	$("#unitOut").val(currentData.container);
	$("#ziseOut").val(currentData.containerSize);
	$("#tyoeOut").val(currentData.containerType);
	$("#conditionOut").val(currentData.condition);
	$("#qualityOut").val(currentData.clasification);
	$("#bookingOut").val(currentData.booking);
	$("#billToOut").val(currentData.billTo);
	//$("#typeDeliveryOut").val(currentData.);
	$("#gateOutModel").modal("show");
}
function validation(data) {
	$("#containerId").val(data);
$("#conditionModel").modal("show");
	/*$.ajax({
			type: "POST",
			url: "gateOut/containerValidation",
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
Swal.fire({
  imageUrl: data,
})
	}

function showComponents(data){
	console.log(data)
	//text = document.getElementById("newPart").options[data].text
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
	document.getElementById("newComponentInspection")
	$.ajax({
		type: "GET",
		url: 'gateOut/getComponentIformation',
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
function getSection(){
	textContainer = document.getElementById("newContainerDescription").options[$("#containerType").val()-1].text	
$.ajax({
			type: "GET",
			url: 'gateOut/getSectionInformation',
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
	
function pregate(data){	
	$("#containerId").val(data),
	
	$("#pregateModel").modal("show");
	$("#operation").val("UPDATE")
	}
function inspectionContainer(data){
	$("#inspectionTable").DataTable().clear().draw();
	currentData = $("#containerTable").DataTable().row(data).data();
	
	//clearCombo(document.getElementById("newPart"));
    //fillComboPart(document.getElementById("newPart"),currentData.containerType);
		
	$("#containerType").val(currentData.containerType)
	$("#containerId").val(currentData.containerId)
	getSection()
	getDamageInfotmation(currentData.containerType)
	eventDateValidator()
	if(currentData.containerType==1){
		document.getElementById("refferData").setAttribute("hidden",true);
	}else if(currentData.containerType==6){
		document.getElementById("refferData").removeAttribute("hidden");
		if($("#containerStatus").val()=="7"){
		document.getElementById("divVessel").setAttribute("hidden",true);
		document.getElementById("divTravel").setAttribute("hidden",true);
		document.getElementById("divDestOrigin").setAttribute("hidden",true);
		document.getElementById("divAa").setAttribute("hidden",true);
		document.getElementById("divDefinition").setAttribute("hidden",true);
		document.getElementById("divChassis").setAttribute("hidden",true);
		document.getElementById("divSetPoint").setAttribute("hidden",true);
		document.getElementById("divVentilation").setAttribute("hidden",true);
		document.getElementById("divPart").setAttribute("hidden",true);
		document.getElementById("divDamage").setAttribute("hidden",true);
		document.getElementById("divDamagecode").setAttribute("hidden",true);
		document.getElementById("divInspectionCustomerType").setAttribute("hidden",true);
		document.getElementById("divComponent").removeAttribute("hidden");
		document.getElementById("divDamageGenSet").removeAttribute("hidden");
		document.getElementById("divDiesel").removeAttribute("hidden");
		document.getElementById("divHours").removeAttribute("hidden");
		document.getElementById("divCms").removeAttribute("hidden");
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
				{text: 'Agregar Inspeccion', action: function() { addInspection(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "part",visible: true , render : function(data) {
						$("#newComponent").val(data);
						return $("#newComponent option:selected").html();
					}}, 
			{ data: "damage",visible: true , render : function(data) {
						$("#newDamageGenSet").val(data);
						return $("#newDamageGenSet").val();
					}}, 
			{ data: "damageCode", visible: false },
			{ data: "reference", visible: true },
			{ data: "customerType", visible: false },
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				$("#imagenData").val(data)
				return '<a onclick="showPhoto(\'' + meta.row + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
			}},
			{ data: "inspectionId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteInspection(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
	
		}
	}else if(currentData.containerType==3){
		document.getElementById("refferData").setAttribute("hidden",true);
	}
	
	$("#inspectionModal").modal("show");
	$("#operation").val("UPDATE")
	
}

function getDamageInfotmation(data){
			$.ajax({
		type: "GET",
		url: 'gateOut/getDamageInformation',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerType : data},
		success: function(response){
			clearCombo(document.getElementById("newDamage"))
			fillCombo(document.getElementById("newDamage"),response)
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
function addInspection(){
	console.log($("#containerStatus").val() )
	if($("#containerStatus").val() == null){
		Swal.fire("Por favor Selecciona una imagen", "", "warning");
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
				"photo": dataUrl,
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
				"photo": dataUrl,
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
				"photo": dataUrl,
			}).draw(false);
		
	}else{
		Swal.fire("Llenar los datos que se requieren", "", "warning");
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
	var month = month.toString;
	}
    document.getElementById("newDateInspection").min = year+'-'+month+'-'+day; 
	document.getElementById("newDateInspection").value = year+'-'+month+'-'+day; 
}

function deleteInspection(){
	$('#inspectionTable').DataTable().row('.selected').remove().draw(false);
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
			url: 'gateOut/getEvents',
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
			url: 'gateOut/getInspections',
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
	var month = month.toString;
	}
	
    document.getElementById("startDate").min = year+'-'+month+'-'+day; 
	document.getElementById("startDate").value = year+'-'+month+'-'+day; 
}
