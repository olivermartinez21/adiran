$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });
$(document).ready( function() {
	$('#loading').hide();
	configDataTable()
	initComponents()
	//showMoreInformation() 
	$("#divTableHapag").hide();
});



function configDataTable() {
		$("#bookignTable").DataTable({
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
				{text: 'Crear Booking', action: function() { addBooking(); }},
				{text: 'Bookings HAPAG ', action: function() { showBookignHapag(); }},
				{extend: 'excelHtml5', title: 'Asignaciones'},
				//{extend: 'csvHtml5', title: 'Inventario'},
				//{extend: 'pdfHtml5', title: 'Inventario', download: 'open', orientation: 'landscape',pageSize: 'LEGAL'},
				//{text: 'Imprimir', extend: 'print', title: 'Inventarios'},
				//{text: 'Columnas', extend: 'colvis' },
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "bookings/getDataTable",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns:[
			{ data: "bookingId",visible: false }, 
			{ data: "type",visible: false }, 
			{ data: "size",visible: false }, 
			{ data: "booking",visible: true },
			{ data: "status", visible: true , render : function(data) {
						$("#statusBookingCatalog").val(data);
						return $("#statusBookingCatalog option:selected").html();
					}}, 
					
			{ data: "quality",visible: false }, 
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#shippingCompanyCatalog").val(data);
						return $("#shippingCompanyCatalog option:selected").html();
					}}, 
			{ data: "quantityUnits",visible: true },
			{ data: "creationDate",visible: true }, 
			{ data: "bookingId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Crear Orden de entrega" onclick="newDeliveryOrder(\'' +meta.row  + '\');"><i class="fas fa-truck"></i></button>&nbsp';
				 //'<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar" onclick="newAssigment(\'' + meta.row + '\');"><i class="fas fa-book"></i></button>&nbsp';
			}},
			
		],
	}).columns.adjust();
	
	$("#bookingInformation").DataTable({
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
				{text: '+', action: function() { addInformationBooking(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			/*ajax: {
			url: "bookings/bookingAssignation",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},*/
		columns: [
		 { data: "id",visible: false },
		 { data: "no",visible: true },
		 { data: "type",visible: true },
		 { data: "size",visible: true },
		 { data: "quality",visible: true },
		 { data: "unitNumber",visible: true },
		 { data: "temperatuere",visible: true },
		 { data: "ventilation",visible: true },
		 { data: "humity",visible: true },
		 { data: "co2",visible: true },
		 { data: "o2",visible: true },
	 	 { data: "nitrogen",visible: true },
		 { data: "no", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Crear Orden de entrega" onclick="deleteAssignment(\'' +meta.row  + '\');"><i class="fas fa-trash"></i></button>&nbsp';
			}},
		],
	}).columns.adjust();
	
	
	}
	
function initComponents() {
	$("#newBookingInformationModal").submit(function () {
		if($("#assigmentType").val()=="MODIFICAR"){
			var data = {
				 id:$("#assignmentId").val(),
				 type: $("#typeBookingInformation").val(),
				 size: $("#sizeBookingInformation").val(),
				 quality: $("#qualityBookingInformation").val(),
				 unitNumber: $("#unitNumberBookingInformation").val(),
				 temperatuere: $("#temperatureBookingInformation").val(),
				 ventilation: $("#ventilationBookingInformation").val(),
				 humity: $("#humidityBookingInformation").val(),
				 co2: $("#co2BookingInformation").val(),
				 o2: $("#o2BookingInformation").val(), 
				 nitrogen: $("#nitrogenBookingInformation").val(),
				};
				$.ajax({
			type: "POST",
			url: 'bookings/assignation',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					$("#newBookingInformationModal").modal('hide');
					Swal.fire("Se asigno la unidad","", "success")
				.then(() => {
					bookingTableOrder($("#bookingId").val())
					bookingTableOrderEdit()
				//self.location.reload();
				});
				}else{
					Swal.fire(response.message, "", "warning");
				}
			},
			error: function(){
				alert("AJAX ERROR");
			}
			});
			return false;
		}else{
			
	if($("#bookingInformation").DataTable().rows().count() >= $("#newQuantityUnitsBookings").val()){
		Swal.fire("La preasignacion Excede la cantidad de unidades del booking", "", "warning");
	}else{
		filas =  $("#bookingInformation").DataTable().rows().data().count()
				$("#bookingInformation").DataTable().row.add({
				 "id": "",
				 "no":  filas+1,
				 "type": $("#typeBookingInformation").val(),
				 "size": $("#sizeBookingInformation").val(),
				 "quality": $("#qualityBookingInformation").val(),
				 "unitNumber": $("#unitNumberBookingInformation").val(),
				 "temperatuere": $("#temperatureBookingInformation").val(),
				 "ventilation": $("#ventilationBookingInformation").val(),
				 "humity": $("#humidityBookingInformation").val(),
				 "co2": $("#co2BookingInformation").val(),
				 "o2": $("#o2BookingInformation").val(), 
				 "nitrogen": $("#nitrogenBookingInformation").val(),
				 }).draw(false);
			cleanBookingInformationForm();
		   $("#newBookingInformationModal").modal('hide');
			}
				return false;
		}
	
			});	
			
$("#newBookingModal").submit(function () {
				if($("#bookingInformation").DataTable().rows().count()!=$("#newQuantityUnitsBookings").val()){
					count =  $("#bookingInformation").DataTable().rows().data().count()
					filas = $("#newQuantityUnitsBookings").val() - count
					console.log(count)
					for (var i = 0; i < filas; i++) {
						count++
				 $("#bookingInformation").DataTable().row.add({
				 "id": "",
				 "no":  count,
				 "type": "",
				 "size":"",
				 "quality": "",
				 "unitNumber": "",
				 "temperatuere": "",
				 "ventilation": "",
				 "humity": "",
				 "co2": "",
				 "o2":"", 
				 "nitrogen":"",
				 }).draw(false);
					}
				}
					
			
				
				table = $("#bookingInformation").DataTable().rows().data().toArray();
				var dataT = [];
				table.forEach(function(item) {
				
					var obj = {
						id: item.id,
						no: item.no,
						type: item.type,
						size: item.size,
						quality: item.quality,
						unitNumber: item.unitNumber,
						temperatuere: item.temperatuere,
						ventilation: item.ventilation,
						humity: item.humity,
						co2: item.co2,
						o2: item.o2,
					 	nitrogen: item.nitrogen,
				 }
			     dataT.push(obj);
				 });
			console.log(dataT)
				var data = {
					booking:  $("#newBooking").val(),
					shippingCompany:  $("#newShippingConpanyBooking").val(),
					quantityUnits:  $("#newQuantityUnitsBookings").val(),
					finalClient:  $("#newFinalClientBooking").val(),
					billTo:  $("#newBilTo").val(),
					location:  $("#newLocationBooking").val(),
					workOrder:  $("#newWorkOrderBooking").val(),
					expirationDate:  $("#newExpirationDateBooking").val(),
					releaseDate:  $("#newReleaseDateBooking").val(),
					asignmentList: JSON.stringify(dataT)
				};
				
				$.ajax({
			type: "POST",
			url: 'bookings/createBooking',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Se creo correctamente la reserva","", "success")
				.then(() => {
				self.location.reload();
				});
				}else{
					Swal.fire(response.message, "", "warning");
				}
			},
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
				
				return false;
				});
				
				
				
					
			$("#newDeliveryOrderForm").submit(function () {
				$("#shippingCompanyCatalog").val($("#newShippngCompanyOrder").val());
				
				totalRows = $("#bookingInformationOrder").DataTable().rows().count();
				var assignmentList = [];
				for(i = 0; i < totalRows; i++) {
					if($("#checkRow"+i).is(':checked')) {
						assignmentList.push($("#checkRow"+i).val());
					}
				}
				$("#newUnitDeliver").val(assignmentList.length)
				console.log($("#newShippngCompanyOrder").val())
				var data = {
					booking: $("#newBookingOrder").val(),
					owner:  $("#newShippngCompanyOrder").val(),
					typeOfService: $("#newTypeServiceOrder").val(),
					remainingUnits: $("#newUnitRemainingOrder").val(),
					billTo: $("#newBillToOrder").val(),
					carrierCompany: $("#newCarrierCompanyOrder").val(),
					operator: $("#newOperatorOrder").val(),
					economicNumber: $("#newEconomicNumberOrder").val(),
					workOrder: $("#newWorkOrderOrder").val(),
					quantityOfUnits:$("#newUnitDeliver").val(),
					location: $("#globalWarehouse").val(),
					assignmentId:$("#bookingId").val(),
					containerType:$("#newTypeUnitOrder").val(),
					assignmentList: assignmentList,
				};
				console.log(data);
			$.ajax({
			type: "POST",
			url: 'bookings/createDeliveryOrder',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Se creo correctamente la Orden de entrega","", "success")
				.then(() => {
				bookingTableOrder($("#bookingId").val())
				tableDelivery($("#bookingId").val())
				//$("#newDeliveryOrderlModal").modal('hide');
				//self.location.reload();
				});
				}else{
					Swal.fire(response.message, "", "warning");
				}
			},
			error: function(){
				alert("AJAX ERROR");
			}
			});
			return false;
			});	
			
			$("#newDeliveryOrderEditForm").submit(function () {
				
					var data = {
					booking: $("#newBookingOrderEdit").val(),
					owner: $("#newShippngCompanyOrderEdit").val(),
					typeOfService: $("#newTypeServiceOrderEdit").val(),
					remainingUnits: $("#newUnitRemainingOrderEdit").val(),
					billTo: $("#newBillToOrderEdit").val(),
					carrierCompany: $("#newCarrierCompanyOrderEdit").val(),
					operator: $("#newOperatorOrderEdit").val(),
					economicNumber: $("#newEconomicNumberOrderEdit").val(),
					workOrder: $("#newWorkOrderOrderEdit").val(),
					quantityOfUnits: $("#newUnitDeliverEdit").val(),
					location: $("#globalWarehouse").val(),
					assignmentId:$("#bookingId").val(),
					deliveryOrderId:$("#deliveryOrderId").val()
				};
				$.ajax({
			type: "POST",
			url: 'bookings/updateDeliveryOrder',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Se edito correctamente la Orden de entrega","", "success")
				.then(() => {
				bookingTableOrder($("#bookingId").val())
				tableDelivery($("#bookingId").val())
				$("#newDeliveryOrderEditlModal").modal('hide');
				
				//self.location.reload();
				});
				}else{
					Swal.fire(response.message, "", "warning");
				}
			},
			error: function(){
				alert("AJAX ERROR");
			}
			});
			return false;
				
				
			});	
			}
	
function cleanBookingInformationForm(){
				 $("#typeBookingInformation").val(""),
				 $("#sizeBookingInformation").val(""),
				 $("#qualityBookingInformation").val(""),
				 $("#unitNumberBookingInformation").val(""),
				 $("#temperatureBookingInformation").val(""),
				 $("#ventilationBookingInformation").val(""),
				 $("#humidityBookingInformation").val(""),
				 $("#co2BookingInformation").val(""),
				 $("#o2BookingInformation").val(""),
				 $("#nitrogenBookingInformation").val("")
	}
	
function addBooking() {
		$("#newLocationBooking").val($("#globalWarehouse").val())
		$("#newBookingModal").modal('show');
		
	
	}
	
function deleteAssignment() {
		$('#bookingInformation').DataTable().row('.selected').remove().draw(false);
	
	}
	
	
	function bookingValidation(){
 value=$("#newBooking").val();
	if(value.length == 8 ){
		$("#newShippingConpanyBooking").val(3);
	}
	if(value.length == 9 ){
			$("#newShippingConpanyBooking").val(2);
	}
	if(value.length == 14 ){
			$("#newShippingConpanyBooking").val(6);
	}
   

}

function addInformationBooking() {
	$("#assigmentType").val("AGREGAR");
	$.ajax({
		type: "GET",
		url: 'bookings/getContainersStock',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {	location: $("#globalWarehouse").val()},
		success: function(response){
			clearComboContainer(document.getElementById("newContainerList"));
			fillComboConainer(document.getElementById("newContainerList"),response);
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	$("#newBookingInformationModal").modal('show');
}

function showMoreInformation() {
	if($("#typeBookingInformation").val()=="RF"){
		document.getElementById("divReffer1").removeAttribute("hidden");
		document.getElementById("divReffer").removeAttribute("hidden");
	}else{
		
		document.getElementById("divReffer1").setAttribute("hidden",true);
		document.getElementById("divReffer").setAttribute("hidden",true);
	}	
document.getElementById("sizeBookingInformation").removeAttribute("disabled");	
}


function nexInput() {
	document.getElementById("qualityBookingInformation").removeAttribute("disabled");	
}
	
function getContainersEspecifications() {
	console.log($("#typeBookingInformation").val());
	console.log($("#sizeBookingInformation").val())
	console.log($("#qualityBookingInformation").val())
	$.ajax({
		type: "GET",
		url: 'bookings/getUnitsFilter',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {	
			type: $("#typeBookingInformation").val(),
			size: $("#sizeBookingInformation").val(),
			clasification: $("#qualityBookingInformation").val()},
		success: function(response){
			console.log(response)
			clearComboContainer(document.getElementById("newContainerList"));
			fillComboConainer(document.getElementById("newContainerList"),response);
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
}
	
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
date = year+'-'+month+'-'+day 
	
	
function newDeliveryOrder(data){

	
		currentData = $("#bookignTable").DataTable().row(data).data();
		if(currentData.expirationDate>date){
		bookingTableOrder(currentData.bookingId)
		tableDelivery(currentData.bookingId);
		$("#bookingId").val(currentData.bookingId)
		$("#newBookingOrder").val(currentData.booking);
		$("#newUnitRemainingOrder").val(currentData.quantityUnits);
		$("#newBillToOrder").val(currentData.billTo);
		$("#shippingCompanyCatalog").val(currentData.shippingCompany);
		$("#newShippngCompanyOrder").val( $("#shippingCompanyCatalog option:selected").html());
			
		$("#newDeliveryOrderlModal").modal('show');
		
		}else{
			Swal.fire("El booking Expiro","", "warning")
		}
		
}
	
	
function bookingTableOrder(data){
	$("#bookingInformationOrder").DataTable().destroy();
	$("#bookingInformationOrder").DataTable({
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
				//{text: '+', action: function() { addInformationBooking(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "bookings/assignmentInformation",
			type: 'GET',
			data:{bookingId: data},
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
		 { data: "id",visible: false },
		 { data: "no",visible: true },
	 	 { data: "unitNumber",visible: true },
		 { data: "type", visible: true , render : function(data) {
						console.log(data)
						if(data!=' '){
							return data;
						}else{
							$("#containerTypeBooking").val(data);
							return $("#containerTypeBooking option:selected").html();
						}
						
					}}, 
		 { data: "size",visible: true },
		 { data: "quality", visible: true , render : function(data) {
					
						if(data!=' '){
							return data;
						}else{
								$("#containerQualityBooking").val(data);
							return $("#containerQualityBooking option:selected").html();
						}
						
					}}, 
		 { data: "id", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar Unidad" onclick="unitCap(\'' +meta.row  + '\');"><i class="fas fa-check"></i></button>&nbsp'+
				'<input class="form-check-input" value='+data+' type="checkbox" id="checkRow'+meta.row+'" style="margin-left:auto; margin-right:auto;">';
			}},
		],
	}).columns.adjust();
}
//style="overflow-y: scroll;"

function unitCap(data){
	    currentData = $("#bookingInformationOrder").DataTable().row(data).data();
		$("#newUnitInformation").val(currentData.unitNumber);
		$("#assignmentId").val(currentData.id);
		$("#assigmentType").val("MODIFICAR");
	$.ajax({
		type: "GET",
		url: 'bookings/getContainersStock',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {	location: $("#globalWarehouse").val()},
		success: function(response){
			console.log(response)
			clearComboContainer(document.getElementById("newContainerList"));
			fillComboConainer(document.getElementById("newContainerList"),response);
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
		$("#newBookingInformationModal").modal('show')
		//$("#newDeliveryOrderlinformationModal").modal('show')
		
}

function selectContainer(){
	if($("#unitNumberBookingInformation").val().length==11){
			$.ajax({
		type: "GET",
		url: 'bookings/getUnitInfo',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:{containerId :  $("#unitNumberBookingInformation").val()},	
		success: function(response){
			console.log(response)
			$("#typeBookingInformation").val(response.containerType)
			$("#sizeBookingInformation").val(response.containerSize)
			$("#qualityBookingInformation").val(response.clasification)
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	}
	
	
}

function tableDelivery(data){
	$("#deliveryOrdersTable").DataTable().destroy();
	$("#deliveryOrdersTable").DataTable({
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
				//{text: 'Crear reserva', action: function() { addBooking(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "bookings/getDataTableOrdersByBookingId",
			type: 'GET',
			dataSrc: '',
			data:  {bookingId : data},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "booking",visible: true }, 
			{ data: "quantityOfUnits",visible: true }, 
			{ data: "owner",visible: true }, 
			{ data: "typeOfService",visible: true }, 
			{ data: "remainingUnits", visible: false , render : function(data, type, full, meta) {
				$("#newUnitRemainingOrder").val(data);
				return data;
			}},
			{ data: "deliveryOrderId",visible: false }, 
			{ data: "assignmentId",visible: false }, 
			{ data: "deliveryOrderId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Editar Orden de entrega" onclick="editOrder(\'' +data + '\');"><i class="fas fa-pen"></i></button>&nbsp'+
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Borrar Orden de entrega" onclick="removeOrder(\'' +data + '\');"><i class="fas fa-trash"></i></button>&nbsp'+
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Imprimir Orden de entrega " onclick="printOrder(\'' +data + '\');"><i class="fas fa-print"></i></button>&nbsp';
			}},
			
		],
	}).columns.adjust();
}

function printOrder(data){
	$.ajax({
		type: "POST",
		url: 'bookings/printDeliveryOrder',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:  {deliveryOrderId : data},
		success: function(response){
			console.log(response)
			window.open('bookings/PDF_order?deliveryOrderId='+ response.pdf+'')
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
}

function removeOrder(data){
	
	$.ajax({
		type: "POST",
		url: 'bookings/deleteDeliveryOrder',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:  {deliveryOrderId : data},
		success: function(response){
			console.log(response)
				//bookingTableOrderEdit()
				tableDelivery($("#bookingId").val())
				bookingTableOrder($("#bookingId").val())
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
}
function editOrder(data){
	
	$("#deliveryOrderId").val(data)
	bookingTableOrderEdit()
	$.ajax({
		type: "GET",
		url: 'bookings/getInformationDelivery',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:  {deliveryOrderId : data},
		success: function(response){
			console.log(response)
			$("#newBookingOrderEdit").val(response.booking)
			$("#newShippngCompanyOrderEdit").val(response.owner);
			$("#newTypeServiceOrderEdit").val(response.typeOfService)
			$("#newUnitRemainingOrderEdit").val(response.remainingUnits)
			$("#newBillToOrderEdit").val(response.billTo)
			$("#newTypeUnitOrderEdit").val(response.containerType)
			$("#newCarrierCompanyOrderEdit").val(response.carrierCompany)
			$("#newOperatorOrderEdit").val(response.operator)
			$("#newEconomicNumberOrderEdit").val(response.economicNumber)
			$("#newWorkOrderOrderEdit").val(response.workOrder)
			$("#newUnitDeliverEdit").val(response.quantityOfUnits)
			
			
			if($("#newUnitRemainingOrderEdit").val()==0){
			$("#newUnitDeliverEdit").prop( "disabled", true );
			}else{
			$("#newUnitDeliverEdit").prop( "disabled", false );
			
			//bookingTableOrderEdit(response.booking)
	}
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
	
		$("#newDeliveryOrderEditlModal").modal('show')
}

function bookingTableOrderEdit(){
	$("#bookingInformationOrderEdit").DataTable().destroy();
	$("#bookingInformationOrderEdit").DataTable({
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
				//{text: '+', action: function() { addInformationBooking(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "bookings/assignmentInformationEdit",
			type: 'GET',
			data:{deliveryOrderId: $("#deliveryOrderId").val()},
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
		 { data: "id",visible: false },
		 { data: "no",visible: true },
	 	 { data: "unitNumber",visible: true },
		{ data: "type", visible: true , render : function(data) {
						console.log(data)
						if(data!=' '){
							return data;
						}else{
							$("#containerTypeBooking").val(data);
							return $("#containerTypeBooking option:selected").html();
						}
						
					}}, 
		 { data: "size",visible: true },
		 { data: "quality", visible: true , render : function(data) {
					
						if(data!=' '){
							return data;
						}else{
								$("#containerQualityBooking").val(data);
							return $("#containerQualityBooking option:selected").html();
						}
						
					}}, 
		 { data: "id", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Editar Orden de entrega" onclick="editUnitCap(\'' +meta.row  + '\');"><i class="fas fa-check"></i></button>&nbsp'+
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Orden de entrega" onclick="deleteAssignmet(\'' +data  + '\');"><i class="fas fa-trash"></i></button>&nbsp';
			}},
		],
	}).columns.adjust();
}
function editUnitCap(data){
	    currentData = $("#bookingInformationOrderEdit").DataTable().row(data).data();
		$("#newUnitInformation").val(currentData.unitNumber);
		$("#assignmentId").val(currentData.id);
		$("#assigmentType").val("MODIFICAR");
	$.ajax({
		type: "GET",
		url: 'bookings/getContainersStock',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {	location: $("#globalWarehouse").val()},
		success: function(response){
			console.log(response)
			clearComboContainer(document.getElementById("newContainerList"));
			fillComboConainer(document.getElementById("newContainerList"),response);
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
		$("#newBookingInformationModal").modal('show')
		//$("#newDeliveryOrderlinformationModal").modal('show')
		
}
function deleteAssignmet(data){
	$.ajax({
		type: "POST",
		url: 'bookings/deleteAssignemnt',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:  {assignmentId : data},
		success: function(response){
			console.log(response)
				bookingTableOrderEdit()
				tableDelivery($("#bookingId").val())
				bookingTableOrder($("#bookingId").val())
		
			
		
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
}


function showBookignHapag(){
	$("#divTablebooking").hide();
	$("#divTableHapag").show();
	$("#bookignTablehapag").DataTable().destroy();
	$("#bookignTablehapag").DataTable({
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
				{text: 'Bookings', action: function() { showBookigs(); }},
				{extend: 'excelHtml5', title: 'Asignaciones'},
				//{extend: 'csvHtml5', title: 'Inventario'},
				//{extend: 'pdfHtml5', title: 'Inventario', download: 'open', orientation: 'landscape',pageSize: 'LEGAL'},
				//{text: 'Imprimir', extend: 'print', title: 'Inventarios'},
				//{text: 'Columnas', extend: 'colvis' },
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			/*ajax: {
			url: "bookings/getDataTable",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},*/
		columns:[
			{ data: "Contenedor",visible: true }, 
			{ data: "Tipo",visible: true }, 
			{ data: "Model",visible: true }, 
			{ data: "Booking",visible: true }, 
			{ data: "FechaEDI",visible: true }, 
			{ data: "CantidadUnidades",visible: true }, 
			{ data: "EDIFilename",visible: true }, 
			{ data: "ProcessTimeStamp",visible: true }, 
			{ data: "CustomerIdentifier",visible: true }, 
			{ data: "CarrierCustomer",visible: true }, 
			{ data: "CarrierShipper",visible: true }, 
			{ data: "Status",visible: true }, 
			{ data: "Shipment",visible: true }, 
			{ data: "AdviceNum",visible: true }, 
			{ data: "ReleaseDate",visible: true }, 
			{ data: "ExpirationDate",visible: true }, 
			{ data: "Remark",visible: true }, 
			{ data: "FreeTxT",visible: true }, 
			{ data: "Assigned",visible: true }, 
			{ data: "Sequence",visible: true }, 
			{ data: "Haulage",visible: true }, 
			{ data: "TareWeight",visible: true }, 
			{ data: "TareWeightUnit",visible: true }, 
			{ data: "Temperature",visible: true }, 
			{ data: "TemperatureUnit",visible: true }, 
			{ data: "VentilationOption",visible: true }, 
			{ data: "VentilationOptionUnit",visible: true }, 
			{ data: "FreshAir",visible: true }, 
			{ data: "FreshAirUnit",visible: true }, 
			{ data: "Humidity",visible: true }, 
			{ data: "HumidityUnit",visible: true }, 
			{ data: "CO2",visible: true }, 
			{ data: "CO2Unit",visible: true }, 
			{ data: "O2",visible: true }, 
			{ data: "O2Unit",visible: true }, 
			{ data: "N",visible: true }, 
			{ data: "NUnit",visible: true }, 
			{ data: "Localidad",visible: true }, 
			
		],
	}).columns.adjust();
}

function showBookigs(){
	$("#divTablebooking").show();
	$("#divTableHapag").hide();
	$("#bookignTable").DataTable().destroy();
		$("#bookignTable").DataTable({
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
				{text: 'Crear Booking', action: function() { addBooking(); }},
				{text: 'Bookings HAPAG ', action: function() { showBookignHapag(); }},
				{extend: 'excelHtml5', title: 'Asignaciones'},
				//{extend: 'csvHtml5', title: 'Inventario'},
				//{extend: 'pdfHtml5', title: 'Inventario', download: 'open', orientation: 'landscape',pageSize: 'LEGAL'},
				//{text: 'Imprimir', extend: 'print', title: 'Inventarios'},
				//{text: 'Columnas', extend: 'colvis' },
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "bookings/getDataTable",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns:[
			{ data: "bookingId",visible: false }, 
			{ data: "type",visible: true }, 
			{ data: "size",visible: true }, 
			{ data: "booking",visible: true },
			{ data: "status", visible: true , render : function(data) {
						$("#statusBookingCatalog").val(data);
						return $("#statusBookingCatalog option:selected").html();
					}}, 
					
			{ data: "quality",visible: true }, 
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#shippingCompanyCatalog").val(data);
						return $("#shippingCompanyCatalog option:selected").html();
					}}, 
			{ data: "quantityUnits",visible: true }, 
			{ data: "bookingId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar Unidad" onclick="newDeliveryOrder(\'' +meta.row  + '\');"><i class="fas fa-truck"></i></button>&nbsp';
				 //'<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar" onclick="newAssigment(\'' + meta.row + '\');"><i class="fas fa-book"></i></button>&nbsp';
			}},
			
		],
	}).columns.adjust();
}

