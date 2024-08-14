$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });
$(document).ready( function() {
	$('#loading').hide();
	showInformation()
	configDataTable()
	initComponents()
	
});

function configDataTable() {
	
	$("#assignmentTable").DataTable({
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
			url: "assignments/getDataTable",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns:[
			{ data: "assigntmentId",visible: false }, 
			{ data: "unitSize",visible: true }, 
			{ data: "unitType",visible: true }, 
			{ data: "booking",visible: true },
			{ data: "ownerCobrateA",visible: true },
			{ data: "dateEdi",visible: true }, 
			{ data: "expirationDate",visible: true }, 
			{ data: "quantityOfUnits",visible: true }, 
			{ data: "status",visible: true ,render : function(data){
			$("#statusSelect").val(data)
			return $("#statusSelect option:selected").html();
			}},
			{ data: "technology",visible: true },
			{ data: "ventilation",visible: true },
			{ data: "temperature",visible: true },
			{ data: "humidity",visible: true},
			{ data: "co2",visible: true},
			{ data: "o2",visible: true},
			{ data: "nitrogen",visible: true },
			{ data: "assigntmentId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Crear Orden de entrega" onclick="newDeliveryOrder(\'' +meta.row  + '\');"><i class="fas fa-truck"></i></button>&nbsp'+
				 '<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar" onclick="newAssigment(\'' + meta.row + '\');"><i class="fas fa-book"></i></button>&nbsp';
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
			url: "assignments/getDataTableFull",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},*/
		columns: [
		 { data: "id",visible: true },
		 { data: "no",visible: true },
		 { data: "type ",visible: true },
		 { data: "size ",visible: true },
		 { data: "quality",visible: true },
		 { data: "unitNumber",visible: true },
		 { data: "temperatuere",visible: true },
		 { data: "ventilation",visible: true },
		 { data: "humity",visible: true },
		 { data: "co2",visible: true },
		 { data: "o2",visible: true },
	 	 { data: "nitrogen",visible: true },
			 
		],
	}).columns.adjust();
	
	/*$("#assignmentUnitfullTable").DataTable({
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
				{text: 'Crear reserva', action: function() { addBookingFull(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "assignments/getDataTableFull",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "assigntmentFullId",visible: false }, 
			{ data: "consecutiveNumber",visible: true }, 
			{ data: "unit",visible: true }, 
			{ data: "size",visible: true },
			{ data: "plantDestination",visible: true },
			{ data: "platform",visible: true }, 
			{ data: "dateOfDelivery",visible: true }, 
			{ data: "deliverytime",visible: true }, 
			{ data: "status",visible: true },
			{ data: "assigntmentFullId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Crear Orden de entrega" onclick="newDeliveryOrder(\'' + data + '\');"><i class="fas fa-truck"></i></button>&nbsp'+
				 '<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar" onclick="newAssigmentFull(\'' + meta.row + '\');"><i class="fas fa-table"></i></button>&nbsp';
			}},
			
		],
	}).columns.adjust();*/
	
	
	
	}
	
	
	function addBooking() {
		$("#newLocationBooking").val($("#globalWarehouse").val())
		$("#newBookingModal").modal('show');
		
	
	}
	
	function addBookingFull() {
		$("#newBookingFullModal").modal('show');
		}
		
	function initComponents() {
			$("#newBookingForm").submit(function () {
				var data = {
					booking: $("#newBooking").val(),
					gradeQuality: $("#newQuality").val(),
					unitType: $("#newUnitType").val(),
					unitSize: $("#newUnitSize").val(),
					quantityOfUnits: $("#newQuantityUnits").val(),
					finalClient: $("#newFinalClient").val(),
					location: $("#newLocation").val(),
					ownerCobrateA: $("#newOwner").val(),
					conditionAssignment: $("#newCondition").val(),
					wo: $("#newWo").val(),
					expirationDate: $("#newExpirationDate").val(),
					dateOfRelease: $("#newDateOfRelease").val(),
					observations: $("#newObservations").val(),
					technology: $("#technology").val(),
					ventilation: $("#ventilation").val(),
					temperature: $("#temperature").val(),
					humidity: $("#humidity").val(),
					co2: $("#co2").val(),
					o2: $("#o2").val(),
					nitrogen: $("#nitrogen").val(),
				};
				
				console.log($("#bookingInformation").DataTable().rows().data());
				/*$.ajax({
			type: "POST",
			url: 'assignments/createBooking',
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
			});*/
			return false;
			});	
			
			$("#newBookingFulForm").submit(function () {
				var data = {
					
					consecutiveNumber: $("#consecutiveNumber").val(),
					unit: $("#unit").val(),
					size: $("#size").val(), 
					plantDestination: $("#plantDestination").val(), 
					platform: $("#platform").val(),  
					dateOfDelivery: $("#dateOfDelivery").val(),  
					deliverytime: $("#deliveryTime").val(),  
					status: $("#status").val(), 
					
				};
				$.ajax({
			type: "POST",
			url: 'assignments/createBookingFull',
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
				var data = {
					billTo: $("#billTo").val(),
					carrierCompany: $("#carrierCompany").val(),
					operator: $("#operator").val(),
					economicNumber: $("#economicNumber").val(),
					workOrder: $("#workOrder").val(),
					quantityOfUnits: $("#quantityOfUnits").val(),
					location: $("#globalWarehouse").val(),
					assignmentId:$("#assignmentId").val(),
				};
				$.ajax({
			type: "POST",
			url: 'assignments/createDeliveryOrder',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Se creo correctamente la Orden de entrega","", "success")
				.then(() => {
				tableDelivery()
				$("#newDeliveryOrderlModal").modal('hide');
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
			
			$("#newAssigmentForm").submit(function () {
				var data = {
					billTo: $("#unitAssig").val(),
					carrierCompany: $("#gradeQuality").val(),
					operator: $("#sizeAssig").val(),
					economicNumber: $("#ventilation").val(),
					workOrder: $("#temperature").val(),
					quantityOfUnits: $("#humidity").val(),
					location: $("#co2").val(),
					assignmentId:$("#o2").val(),
				};
				$.ajax({
			type: "POST",
			url: 'assignments/assignation',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("Se Assigno correctamente la unidad","", "success")
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
			
			
			$("#newBookingInformationModal").submit(function () {
					
	if($("#bookingInformation").DataTable().rows().count() >= $("#newQuantityUnitsBookings").val()){
		Swal.fire("La preasignacion Excede la cantidad de unidades del booking", "", "warning");
	}else{
		filas =  $("#bookingInformation").DataTable().rows().data().count()
				$("#bookingInformation").DataTable().row.add({
				 "id": filas+1 ,
				 "no":  $("#noBookingInformation").val(),
				 "type ": $("#typeBookingInformation").val(),
				 "size ": $("#sizeBookingInformation").val(),
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
			});	
			

		}
	function cleanBookingInformationForm(){
				 $("#noBookingInformation").val(""),
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
	
	function showInformation(){
		if( $("#globalWarehouse").val()=="AGUASCALIENTES"){
			document.getElementById("divAssigmentType").removeAttribute("hidden")
			document.getElementById("divTableAssigment").setAttribute("hidden",true)
		}else{
			document.getElementById("divAssigmentType").setAttribute("hidden",true)
			document.getElementById("divTableAssigment").removeAttribute("hidden")
			
		}
		
	}
	
	
	function showAssigments(){
		if($("#orderListCmb").val()=="Asignaciones o booking"){
			document.getElementById("divTableAssigment").removeAttribute("hidden")
			document.getElementById("divTableAssigmentUnitFull").setAttribute("hidden",true)
			
		}
		if($("#orderListCmb").val()=="Asignaciones de unidades llenas"){
			document.getElementById("divTableAssigment").setAttribute("hidden",true)
			document.getElementById("divTableAssigmentUnitFull").removeAttribute("hidden")
			
		}
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
		currentData = $("#assignmentTable").DataTable().row(data).data();
		if(currentData.expirationDate>date){
		$("#assignmentId").val(currentData.assigntmentId)
		$("#newDeliveryOrderlModal").modal('show');
		}else{
			Swal.fire("El booking Expiro","", "warning")
		}
	
		
		
			
		
		
	}
	
	function newAssigment(data){
		currentData = $("#assignmentTable").DataTable().row(data).data();
		$("#ventilationAssig").val(currentData.ventilation)
		$("#temperatureAssig").val(currentData.temperature)
		$("#humidityAssig").val(currentData.humidity)
		$("#co2Assig").val(currentData.co2)
		$("#o2Assig").val(currentData.o2)
		$("#newAssigmentlModal").modal('show');
	}
	
	function newAssigmentFull() {
			$("#newBookingFullModal").modal('show');
	}
	
	//$("#unit").val()
	$("#unitAssig").keyup(function(){
		if($(this).val().length==11){
			$.ajax({
		type: "GET",
		url: 'assignments/getInformationUnit',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:  {unit : $("#unitAssig").val()},
		success: function(response){
			$("#containerClasification").val(response.clasification)
			$("#containerCondition").val(response.condition)
			
			$("#gradeQuality").val($("#containerCondition option:selected").html()+" "+$("#containerClasification option:selected").html())
			$("#sizeAssig").val(response.containerSize)
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
		}
	})
	
	

/*$('#assignmentTable').click(function () {
	var table = $('#assignmentTable').DataTable();
	var rowdata = table.row('.selected').data();    
	if(rowdata!=null){
		$("#assignmentId").val(rowdata.assigntmentId)
		console.log(rowdata.assigntmentId)
		tableDelivery()
	
	}
	
	//console.log($(this))
} );*/

function tableDelivery(){
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
			url: "assignments/getDataTableOrdersByAssignmentID",
			type: 'GET',
			dataSrc: '',
			data:  {assigntmentId : $("#assignmentId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "booking",visible: true }, 
			{ data: "owner",visible: true }, 
			{ data: "typeOfService",visible: true }, 
			{ data: "remainingUnits",visible: true },
			{ data: "deliveryOrderId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Crear Orden de entrega" onclick="newDeliveryOrder(\'' + meta.row  + '\');"><i class="fas fa-truck"></i></button>&nbsp';
				
			}},
			{ data: "assignmentId",visible: false }, 
			
		],
	}).columns.adjust();
}

 function addInformationBooking() {
	$("#newBookingInformationModal").modal('show');
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


	
/*unit
gradeQuality
size
ventilation
temperature
humidity
cO2
o2*/



/*Swal.fire({
	title: "UNIDAD",
	text: "Captura la unidad",
	input: 'text',
	showCancelButton: true ,
	}).then((result) => {
	if (result.value) {
	    Swal.fire('Result:'+result.value);
	}});*/