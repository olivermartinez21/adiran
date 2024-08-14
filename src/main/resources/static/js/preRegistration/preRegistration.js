$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });

$(document).ready( function() {
	$('#loading').hide();
	configDataTable();
	initComponents();
	validationCode($("#newContainer")) 
});



editAdd=0;
function initComponents() {
	$("#newAppointmentForm").submit(function () {
		table = $("#containerTable").DataTable().rows().data().toArray();
		filas =  $("#containerTable").DataTable().rows().data().count()
	if(filas==0){
			Swal.fire("Por favor Registra al menos un contenedor", "", "warning");
	}else{
 	
	var dataT = [];
	table.forEach(function(item) {
		
		var obj = {
			containerId: item.containerId,
			container: item.container.toUpperCase(),
			containerType: item.containerType,
			containerSize: item.containerSize,
			shippingCompany: item.shippingCompany,
			booking : item.booking,
			bookingQuantity : item.bookingQuantity
		};
		dataT.push(obj);
	});
	console.log(dataT)
	
		var data = {
			appointmentId : $("#appointmentId").val(),
			location : $("#newLocation").val(),
			date : $("#dateAppointment").val(),
			telephone : $("#newTelephone").val(),
			agency : $("#newAgency").val(),
			customer : $("#newCustomer").val().toUpperCase(),
			customerType : $("#newCustomerType").val(),
			invoincingType : $("#newInvoicingType").val(),
			eventType : $("#newEventType").val(),
			user : $("#globalUserId").val(),
			buque :  $("#nweBuque").val(),
			origin : $("#newOrigin").val(),
			paymentType:$("#payment").val(),
			companyName:$("#companyName").val().toUpperCase(),
			fiscalAdress:$("#fiscalAdress").val().toUpperCase(),
			rfc:$("#rfc").val().toUpperCase(),
			shippingCompany : $("#typeForAppointmentOut").val(),
			operation: $("#operation").val(),
			paymentCheck: $("#voucher64").val(),
			idUser : $("#globalUserId").val(),
			warehouse : $("#globalWarehouse").val(),
			//status : $("#status").val(),
			//name:$("#payment").val().toUpperCase(),
			//signature: $("#draw-dataUrl").val(),
			containersList: JSON.stringify(dataT),
	};
	console.log(data)
		$.ajax({
			type: "POST",
			url: 'appointment/saveUpdateAppointment',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);

				if(response.success==true){
					console.log(response.pdf)
					window.open('appointment/PDF_folio?appointmentId='+ response.pdf+'')
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#newAppointmentModal").modal("hide");
				  refresh()
				});
				}else{
					Swal.fire(response.message, "", "warning");
				}
					
				
			},
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			
			}
			
			
		return false;
	});	
	
	$("#inspectionForm").submit(function () {
		
			$.ajax({
			type: "POST",
			url: 'appointment/appointmentValidation',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {appointmentId : $("#appointmentId").val()},
			success: function(response){
				console.log(response);
				manageResponse(response);
			},
			error: function(){
				alert("AJAX ERROR");
			}
			});
		return false;
		});	
		
	

	

}

function searchAppointmentByDate() {
	startDate = document.getElementById("startDate").value
	lastDate = document.getElementById("lastDate").value
	if(isNaN(startDate)&&isNaN(lastDate)){
		//$("#appointmentTable").DataTable().clear().draw();
		$("#appointmentTable").DataTable().destroy();
		
		$("#appointmentTable").DataTable({
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
				{text: 'Nueva Cita', action: function() { addNewAppointment(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "appointment/getFilterDate",
			type: 'GET',
			dataSrc: '',
			data: {startDate:  startDate,
			lastDate: lastDate },
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{data: 'appointmentId' , visible: false},
			{data: 'location' , visible: true},
			{data: 'folio'},
			{data: 'date'},
			{data: 'telephone'},
			{data: 'agency'},
			{data: 'customer'},
			{data: 'customerType', visible : true, render : function(data) {
							$("#newCustomerType").val(data);
							return $("#newCustomerType>option:selected").html();
						}},
			{data: 'containers'},
			{data: 'invoincingType', visible : true, render : function(data) {
							$("#newInvoicingType").val(data);
							return $("#newInvoicingType>option:selected").html();
						}},
			{data: 'eventType', visible : true, render : function(data) {
							$("#newEventType").val(data);
							return $("#newEventType>option:selected").html();
						}},
			{data: 'paymentType', visible : true, render : function(data) {
							$("#payment").val(data);
							return $("#payment>option:selected").html();
						}},
			{data: 'user', visible : true, render : function(data) {
							$("#usersDescriptions").val(data);
							return $("#usersDescriptions>option:selected").html();
						}},
			{ data: "paymentCheck", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="ver el Comprobante" onclick="showPaymentCheck(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp;';
			}},
			{data: 'status', visible : true, render : function(data) {
							$("#status").val(data);
							return $("#status>option:selected").html();
						}},
			{data: 'appointmentId', render : function(data) {
				$("#appointmentId").val(data)
				switch ($("#globalUserRole").val()) {
					case "ADMINSHOW":
					if($("#status").val()==7){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Cita salida" onclick="addNewAppointmentOut(\'' + data + '\');"><i class="fas fa-calendar"></i></button>&nbsp;';
					}
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;'+
					'<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Validar cita" onclick="getIn(\'' + data + '\');"><i class="fas fa-check"></i></button>&nbsp;'+
					'<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;';
					case "RECEPCION":
					return '<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Validar cita" onclick="getIn(\'' + data + '\');"><i class="fas fa-check"></i></button>&nbsp;';
					case "INSPECTOR":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;';
					case "TALLER":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-eye"></i></button></a>&nbsp;';
					case "TECNICO":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-eye"></i></button></a>&nbsp;';
					default:
					if($("#status").val()==1){
						return '<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;';
					}if($("#status").val()==4){
						return '<button disabled type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;' +
						 '<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;';
					}else if($("#status").val()==7){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Cita salida" onclick="addNewAppointmentOut(\'' + data + '\');"><i class="fas fa-calendar"></i></button>&nbsp;';
					}else if($("#status").val()==9){
						return null;
					}else{
						return '<button disabled type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;';
					}
					
				}
			}}
			
		],
	
		
	}).columns.adjust();
	}
	
	}
function configDataTable() {
		$("#appointmentTable").DataTable().destroy();
	$("#appointmentTable").DataTable({
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
				{text: 'PreRegistro Entrada', action: function() { addNewPreExit(); }},
				{text: 'PreRegistro Salida', action: function() { addNewAppointment(); }},
				
				{extend: 'excelHtml5', title: 'Citas'},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "",
			type: 'GET',
			dataSrc: '',
			data: {userId :  $("#globalUserId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{data: 'appointmentId' , visible: false},
			{data: 'location' , visible: true},
			{data: 'folio'},
			{data: 'date'},
			{data: 'telephone'},
			{data: 'agency'},
			{data: 'customer'},
			{data: 'customerType', visible : true, render : function(data) {
							$("#newCustomerType").val(data);
							return $("#newCustomerType>option:selected").html();
						}},
			{data: 'containers'},
			{data: 'invoincingType', visible : true, render : function(data) {
							$("#newInvoicingType").val(data);
							return $("#newInvoicingType>option:selected").html();
						}},
			{data: 'eventType', visible : true, render : function(data) {
							$("#newEventType").val(data);
							return $("#newEventType>option:selected").html();
						}},
			{data: 'paymentType', visible : true, render : function(data) {
							$("#payment").val(data);
							return $("#payment>option:selected").html();
						}},
			{data: 'user', visible : true, render : function(data) {
							$("#usersDescriptions").val(data);
							return $("#usersDescriptions>option:selected").html();
						}},
			{ data: "paymentCheck", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="ver el Comprobante" onclick="showPaymentCheck(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp;';
			}},
			{data: 'status', visible : true, render : function(data) {
							$("#status").val(data);
							return $("#status>option:selected").html();
						}},
			{data: 'appointmentId', render : function(data) {
				$("#appointmentId").val(data)
				
				//return //'<a href="/myre/containers/preGate?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;'+
			return	'<a href="appointment/PDF_folio?appointmentId='+data+'" target="_blank"><button type="button" class="btn btn-outline-dark btn-sm" title="Ver Documento"><i class="fas fa-file-contract"></i></button></a> &nbsp;&nbsp;' ;
				
				/*switch ($("#globalUserRole").val()) {
					case "ADMINSHOW":
					if($("#status").val()==7){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Cita salida" onclick="addNewAppointmentOut(\'' + data + '\');"><i class="fas fa-calendar"></i></button>&nbsp;';
					}
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;'+
					'<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Validar cita" onclick="getIn(\'' + data + '\');"><i class="fas fa-check"></i></button>&nbsp;'+
					'<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;';
					case "RECEPCION":
					return '<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Validar cita" onclick="getIn(\'' + data + '\');"><i class="fas fa-check"></i></button>&nbsp;';
					case "INSPECTOR":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;';
					case "TALLER":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-eye"></i></button></a>&nbsp;';
					case "TECNICO":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-eye"></i></button></a>&nbsp;';
					default:
					if($("#status").val()==1){
						return '<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;';
					}if($("#status").val()==4){
						return '<button disabled type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;' +
						 '<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;';
					}else if($("#status").val()==7){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Cita salida" onclick="addNewAppointmentOut(\'' + data + '\');"><i class="fas fa-calendar"></i></button>&nbsp;';
					}else if($("#status").val()==9){
						return null;
					}else{
						return '<button disabled type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;';
					}
					
				}*/
			}}
			
		],
	
		
	}).columns.adjust();
	
	
	$("#containerTable").DataTable().destroy();
	$("#containerTable").DataTable({
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
				{text:'Agregar Unidad', action: function() { addContainer(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		columns: [
			{ data: "containerId",visible: false },
			{ data: "container", visible: true },
			{ data: "booking", visible: false}, 
			{ data: "bookingQuantity", visible: false },
			{ data: "containerType",visible: true , render : function(data) {
						$("#newContainerType").val(data);
						return $("#newContainerType option:selected").html();
					}}, 
			{ data: "containerSize", visible: true , render : function(data) {
						$("#newContainerSize").val(data);
						return $("#newContainerSize option:selected").html();
					}}, 
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#newShippingCompany").val(data);
						return $("#newShippingCompany option:selected").html();
					}}, 
			
			{ data: "price", visible: false , render : function(data) {
						return "$100";
					}}, 
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteContainer(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp'+
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Editar Unidad" onclick="editContainer(\'' + meta.row + '\');"><i class="fas fa-edit"></i></button>';
				
			}},
		],
	}).columns.adjust();
	
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
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "containerId",visible: true },
			{ data: "part", visible: true , render : function(data) {
						$("#part").val(data);
						return $("#part option:selected").html();
					}}, 
			{ data: "damage", visible: false },
			{ data: "damageCode", visible: true , render : function(data) {
						$("#ErrorCode").val(data);
						return $("#ErrorCode option:selected").html();
					}}, 
			{ data: "reference", visible: true },
			{ data: "customerType", visible: false },
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				$("#imagenData").val(data)
				return '<a onclick="showPhoto(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
			}},
			{ data: "cost", visible: true },
		],
	}).columns.adjust();
	
	
}

function refresh(){
	
	$("#appointmentTable").DataTable().destroy();
	$("#appointmentTable").DataTable({
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
				{text: 'Nueva Cita', action: function() { addNewAppointment(); }},
				{extend: 'excelHtml5', title: 'Citas'},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "appointment/getDataTable",
			type: 'GET',
			dataSrc: '',
			data: {userId :  $("#globalUserId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{data: 'appointmentId' , visible: false},
			{data: 'location' , visible: true},
			{data: 'folio'},
			{data: 'date'},
			{data: 'telephone'},
			{data: 'agency'},
			{data: 'customer'},
			{data: 'customerType', visible : true, render : function(data) {
							$("#newCustomerType").val(data);
							return $("#newCustomerType>option:selected").html();
						}},
			{data: 'containers'},
			{data: 'invoincingType', visible : true, render : function(data) {
							$("#newInvoicingType").val(data);
							return $("#newInvoicingType>option:selected").html();
						}},
			{data: 'eventType', visible : true, render : function(data) {
							$("#newEventType").val(data);
							return $("#newEventType>option:selected").html();
						}},
			{data: 'paymentType', visible : true, render : function(data) {
							$("#payment").val(data);
							return $("#payment>option:selected").html();
						}},
			{data: 'user', visible : true, render : function(data) {
							$("#usersDescriptions").val(data);
							return $("#usersDescriptions>option:selected").html();
						}},
			{ data: "paymentCheck", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="ver el Comprobante" onclick="showPaymentCheck(\'' + data + '\');"><i class="fas fa-file"></i></button>&nbsp;';
			}},
			{data: 'status', visible : true, render : function(data) {
							$("#status").val(data);
							return $("#status>option:selected").html();
						}},
			{data: 'appointmentId', render : function(data) {
				$("#appointmentId").val(data)
				
				//return //'<a href="/myre/containers/preGate?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;'+
			return	'<a href="appointment/PDF_folio?appointmentId='+data+'" target="_blank"><button type="button" class="btn btn-outline-dark btn-sm" title="Ver Documento"><i class="fas fa-file-contract"></i></button></a> &nbsp;&nbsp;' ;
				
				/*switch ($("#globalUserRole").val()) {
					case "ADMINSHOW":
					if($("#status").val()==7){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Cita salida" onclick="addNewAppointmentOut(\'' + data + '\');"><i class="fas fa-calendar"></i></button>&nbsp;';
					}
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;'+
					'<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Validar cita" onclick="getIn(\'' + data + '\');"><i class="fas fa-check"></i></button>&nbsp;'+
					'<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;';
					case "RECEPCION":
					return '<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Validar cita" onclick="getIn(\'' + data + '\');"><i class="fas fa-check"></i></button>&nbsp;';
					case "INSPECTOR":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Inspeccionar"><i class="fas fa-eye"></i></button></a>&nbsp;';
					case "TALLER":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-eye"></i></button></a>&nbsp;';
					case "TECNICO":
					return '<a href="/myre/containers/container?appointmentId='+data+'"><button  type="button" class="btn btn-outline-dark btn-sm" title="Informacion Containers"><i class="fas fa-eye"></i></button></a>&nbsp;';
					default:
					if($("#status").val()==1){
						return '<button  type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;';
					}if($("#status").val()==4){
						return '<button disabled type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;' +
						 '<button  type="button" class="btn btn-outline-dark btn-sm" title="ver Estimados" onclick="ConfirmEstimationAppointment(\'' + data + '\');"><i class="fas fa-coins"></i></button>&nbsp;';
					}else if($("#status").val()==7){
						return '<button type="button" class="btn btn-outline-dark btn-sm" title="Cita salida" onclick="addNewAppointmentOut(\'' + data + '\');"><i class="fas fa-calendar"></i></button>&nbsp;';
					}else if($("#status").val()==9){
						return null;
					}else{
						return '<button disabled type="button" class="btn btn-outline-dark btn-sm" title="Editar Cita" onclick="editAppointment(\'' + data + '\');"><i class="fas fa-edit"></i></button>&nbsp;';
					}
					
				}*/
			}}
			
		],
	
		
	}).columns.adjust();
}

function showPaymentCheck(data){
	
				var iframe = "<iframe width='100%' height='100%' tiltle src='" + data + "'></iframe>"
				var x = window.open();
				x.document.open();
				x.document.write(iframe);
				x.document.close();	
	}
	
function getIn(data){
	$.ajax({
			type: "POST",
			url: "appointment/appointmentValidation",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {appointmentId : data},
			success: function(response){
				console.log(response);
				manageResponse(response);
			},
			error: function(){
				alert("AJAX ERROR");
			}
		});
	
}

function ConfirmEstimationAppointment(data){
	showInspections(data);
	$("#appointmentId").val(data);
	$("#inspectionModal").modal("show");
	
}

function showInspections(data) {
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
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: 'appointment/getInspections',
			type: 'GET',
			dataSrc: '',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {appointmentId : data},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "containerId", visible: true , render : function(data) {
						$("#containers").val(data);
						return $("#containers option:selected").html();
					}}, 
			
			{ data: "part", visible: true , render : function(data) {
						$("#part").val(data);
						return $("#part option:selected").html();
					}}, 
			{ data: "damage", visible: false },
			{ data: "damageCode", visible: true , render : function(data) {
						$("#ErrorCode").val(data);
						return $("#ErrorCode option:selected").html();
					}}, 
			{ data: "reference", visible: true },
			{ data: "customerType", visible: false },
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				$("#imagenData").val(data)
				return '<a onclick="showPhoto(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
			}},
			{ data: "damageCode", visible: true , render : function(data) {
						$("#precios").val(data);
						return '$'+$("#precios option:selected").html()+' MXN';
					}}, 
			
		],
	}).columns.adjust();
	
} 

function editAppointment(data){
	document.getElementById("addOrderModalLabel").innerHTML = "EDITAR CITA";
	cleanForm()
	$("#operation").val("UPDATE")
	$.ajax({
		type: "GET",
		url: 'appointment/getSingleData',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {appointmentId : data},
		success: function(response){
			document.getElementById("dateAppointment").min = response.date; 
			document.getElementById("dateAppointment").value = response.date;
			
			$("#appointmentId").val(response.appointmentId),
			$("#newLocation").val(response.location),
			$("#dateAppointment").val(response.date),
			$("#newTelephone").val(response.telephone),
			$("#newAgency").val(response.agency),
			$("#newCustomer").val(response.customer),
			$("#newCustomerType").val(response.customerType),
			$("#newInvoicingType").val(response.invoincingType),
			$("#newEventType").val(response.eventType),
			$("#payment").val(response.paymentType),
			$("#companyName").val(response.companyName),
			$("#fiscalAdress").val(response.fiscalAdress),
			$("#rfc").val(response.rfc),
			$("#nweBuque").val(response.buque),
			$("#newOrigin").val(response.origin),
			//signature: $("#draw-dataUrl").val(),
			//JSON.stringify(containerList),
			
			$("#newAppointmentModal").modal("show");
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
		if($("#newEventType").val()==1){
			document.getElementById("divBooking").setAttribute("hidden",true)
			document.getElementById("divOutPutData").setAttribute("hidden",true)
		}else{
			document.getElementById("divBooking").removeAttribute("hidden")
			document.getElementById("divOutPutData").removeAttribute("hidden")
		}
	showContainers(data)
	
}

function showContainers(data) {
	$("#containerTable").DataTable().destroy();
	$("#containerTable").DataTable({
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
				{text: 'Agregar Unidad', action: function() { addContainer(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: 'appointment/getContainers',
			type: 'GET',
			dataSrc: '',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {appointmentId : data,
			userId : $("#globalUserId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "containerId",visible: false },
			{ data: "container", visible: $("#newEventType").val()==1 ? true : false  },
			{ data: "booking", visible: $("#newEventType").val()==1 ? false : true }, 
			{ data: "bookingQuantity", visible: $("#newEventType").val()==1 ? false : true }, 
			{ data: "containerType",visible: true , render : function(data) {
						$("#newContainerType").val(data);
						return $("#newContainerType option:selected").html();
					}}, 
			{ data: "containerSize", visible: true , render : function(data) {
						$("#newContainerSize").val(data);
						return $("#newContainerSize option:selected").html();
					}}, 
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#newShippingCompany").val(data);
						return $("#newShippingCompany option:selected").html();
					}}, 
			{ data: "containerId", visible: false , render : function(data) {
						return "$100";
					}}, 
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Editar Unidad" onclick="editContainer(\'' + meta.row + '\');"><i class="fas fa-edit"></i></button>'; 
				//'<button type="button" class="btn btn-outline-dark btn-sm" title="Quitar Unidad" onclick="deleteContainer(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp'+
				
			
			}},
			
		],
	}).columns.adjust();
	
	$("#newAppointmentModal").modal("show");
}

function deleteAppointment(data){
	
}
function printAppointment(data){
	
}

function cleanForm(){
			$("#containerTable").DataTable().clear().draw();
			$("#newTelephone").val(""),
			$("#newAgency").val(""),
			$("#newCustomer").val(""),
			$("#companyName").val(""),
			$("#fiscalAdress").val(""),
			$("#rfc").val("")
}

function selectContainer(){
	
	var container = document.getElementById("containersExist");
	text = container.options[container.selectedIndex].text;
	$("#newContainer").val(text)
	$("#containersType").val($("#containersExist").val())
	$("#containersSize").val($("#containersExist").val())
	$("#containersShipping").val($("#containersExist").val())
	
	
	var containerType = document.getElementById("containersType");
	textType = containerType.options[containerType.selectedIndex].text;
	$("#newContainerType").val(textType)
	var containerSize = document.getElementById("containersSize");
	textSize = containerSize.options[containerSize.selectedIndex].text;
	$("#newContainerSize").val(textSize)
	var shipping = document.getElementById("containersShipping");
	textShipping = shipping.options[shipping.selectedIndex].text;
	$("#newShippingCompany").val(textShipping)
	
	}
	
function inOrOut(){
	if(document.getElementById("addOrderModalLabel").innerHTML=="EDITAR CITA"){
		if($("#newEventType").val()==1){
			document.getElementById("divBooking").setAttribute("hidden",true)
			document.getElementById("divOutPutData").setAttribute("hidden",true)
			$("#newBooking").val("")
			$("#nweBuque").val("")
			$("#newOrigin").val("")
		}else{
			document.getElementById("divBooking").removeAttribute("hidden")
			document.getElementById("divOutPutData").removeAttribute("hidden")
		}
		showContainers($("#appointmentId").val());
	}else{
		if($("#newEventType").val()==1){
			document.getElementById("datelabel").innerHTML = "Fecha de descarga";
			document.getElementById("divBooking").setAttribute("hidden",true)
			document.getElementById("divOutPutData").setAttribute("hidden",true)
			document.getElementById("divContainerQuantity").setAttribute("hidden",true);
			document.getElementById("divContainers").removeAttribute("hidden");
			$("#newBooking").val("")
			$("#nweBuque").val("")
			$("#newOrigin").val("")
		}else{
			document.getElementById("datelabel").innerHTML = "Fecha de carga";
			document.getElementById("divBooking").removeAttribute("hidden")
			document.getElementById("divOutPutData").removeAttribute("hidden")
			document.getElementById("divContainerQuantity").removeAttribute("hidden");
			document.getElementById("divContainers").setAttribute("hidden",true);
		}
		$("#containerTable").DataTable().clear().draw();
			$("#containerTable").DataTable().destroy();
			$("#containerTable").DataTable({
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
					{text: 'Agregar Unidad', action: function() { addContainer(); }},
					],
				dom: {
					button:{
		                tag:"button",
		                className:"btn btn-dark"
		            },
				}},
			columns: [
				{ data: "containerId",visible: false },
				{ data: "container", visible: $("#newEventType").val()==1 ? true : false },
				{ data: "booking", visible: $("#newEventType").val()==1 ? false : true }, 
				{ data: "bookingQuantity", visible: $("#newEventType").val()==1 ? false : true }, 
				{ data: "containerType",visible: true , render : function(data) {
							$("#newContainerType").val(data);
							return $("#newContainerType option:selected").html();
						}}, 
				{ data: "containerSize", visible: true , render : function(data) {
							$("#newContainerSize").val(data);
							return $("#newContainerSize option:selected").html();
						}}, 
				{ data: "shippingCompany", visible: true , render : function(data) {
							$("#newShippingCompany").val(data);
							return $("#newShippingCompany option:selected").html();
						}}, 
				{ data: "containerId", visible: false , render : function(data) {
						return "$100";
					}}, 
				{ data: "containerId", visible: true , render : function(data, type, full, meta) {
					return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteContainer(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp'+
					'<button type="button" class="btn btn-outline-dark btn-sm" title="Editar Unidad" onclick="editContainer(\'' + meta.row + '\');"><i class="fas fa-edit"></i></button>';
					
				}},
			],
		}).columns.adjust();
		
	}
	
	
}

 function addNewAppointmentOut(data){
	document.getElementById("addOrderModalLabel").innerHTML = "Pre Entrada";
	cleanForm();
	$("#newEventType").val(2)
	$("#typeForAppointmentOut").val("APOINTMENT_OUT")
	$("#appointmentId").val(data)
	inOrOut()
	getInformation()
	document.getElementById("newEventType").setAttribute("disabled",true);
	//cleanForm();
	eventDateValidator()
	$("#operation").val("INSERT")
	$("#newAppointmentModal").modal('show');
}
 function customerType(){
	if($("#newCustomerType").val()==2){
		document.getElementById("divAgency").setAttribute("hidden",true);
		$("#newAgency").val("")
	}else{
		document.getElementById("divAgency").removeAttribute("hidden");
	}
	}

 function addNewAppointment(){
	$("#newLocation").val($("#globalWarehouse").val())
	
	document.getElementById("addOrderModalLabel").innerHTML = "NUEVA CITA";
	cleanForm();
	$("#typeForAppointmentOut").val("")
	$("#newEventType").val(1)
	inOrOut()
	getInformation()
	document.getElementById("newEventType").removeAttribute("disabled");
	//cleanForm();
	eventDateValidator()
	$("#operation").val("INSERT")
	$("#newAppointmentModal").modal('show');
}

function addNewPreExit(){
	$("#newLocation").val($("#globalWarehouse").val())
	
	document.getElementById("addOrderModalLabel").innerHTML = "NUEVA CITA";
	cleanForm();
	$("#typeForAppointmentOut").val("")
	$("#newEventType").val(1)
	inOrOut()
	getInformation()
	document.getElementById("newEventType").removeAttribute("disabled");
	//cleanForm();
	eventDateValidator()
	$("#operation").val("INSERT")
	$("#newPreExitModal").modal('show');
}

function getInformation(){
	cleanForm();
	$.ajax({
		type: "GET",
		url: 'appointment/getSingleData',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {appointmentId :$("#appointmentId").val(),
		idUser:$("#globalUserId").val()},
		success: function(response){
			$("#newTelephone").val(response.telephone),
			$("#newAgency").val(response.agency),
			$("#newCustomer").val(response.customer),
			//$("#newTransportCompany").val(response.transportCompany),
			$("#companyName").val(response.companyName),
			$("#fiscalAdress").val(response.fiscalAdress),
			$("#rfc").val(response.rfc),
			$("#newAppointmentModal").modal("show");
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
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
	
    document.getElementById("dateAppointment").min = year+'-'+month+'-'+day; 
	document.getElementById("dateAppointment").value = year+'-'+month+'-'+day; 
}


function deleteContainer(){
	$('#containerTable').DataTable().row('.selected').remove().draw(false);
}


function editContainer(){
	editAdd=1;	
	table = $('#containerTable').DataTable().row('.selected').data();
				$("#containerId").val(table.containerId),
				$("#newContainer").val(table.container),
				$("#newContainerType").val(table.containerType),
				$("#newContainerSize").val(table.containerSize),
				$("#newShippingCompany").val(table.shippingCompany)
				$("#newBooking").val(table.booking)
				$("#bookingQuantity").val(table.bookingQuantity) 
	$('#containerTable').DataTable().row('.selected').remove().draw(false);
}

function showPhoto(data){
	document.getElementById("imagenPrevisualizacion").setAttribute("src", data);
	$("#imageShowModal").modal("show");
	}
	
/*function setId(){
	var agency = document.getElementById("newAgency").value;
	$("#newAgencyList").val(agency)
		console.log(agency)
		
		console.log($("#newAgencyList").val())
		
	var newAgencyList = document.getElementById("newAgencyList");
	agencyText = newAgencyList.options[newAgencyList.selectedIndex].text;
	console.log(agencyText)
	
		}*/
		
/*$("#newAgencySelect").on('input', function () {
   var val=$('#newAgencySelect').val();
   var ejemplo = $('#newAgencyList').find('option[value="'+val+'"]').data("id");
  $('#newAgency').val(ejemplo);
});*/
		
function addContainer(){
	if($("#newEventType").val()==2){
		if(editAdd==1){
		editAdd=0;
		$("#containerTable").DataTable().row.add({
				"containerId": $("#containerId").val(),
				"container": $("#newContainer").val().toUpperCase( ),
				"containerType": $("#newContainerType").val(),
				"containerSize": $("#newContainerSize").val(),
				"shippingCompany": $("#newShippingCompany").val(),
				"booking" : $("#newBooking").val(),
				"bookingQuantity" : $("#bookingQuantity").val(),
				"price": "$100",
			}).draw(false);
			$("#containerId").val("")
			$("#newContainer").val("")
			//.fire("El codigo es correcto", "", "success");
	}else{
	if($("#newContainerType").val()!="0"&&$("#newShippingCompany").val()!="0"){
	filas =  $("#containerTable").DataTable().rows().data().count()
	$("#containerId").val(filas+1)
	if(filas==0){
		$("#containerTable").DataTable().row.add({
				"containerId": $("#containerId").val(),
				"container": $("#newContainer").val().toUpperCase( ),
				"containerType": $("#newContainerType").val(),
				"containerSize": $("#newContainerSize").val(),
				"shippingCompany": $("#newShippingCompany").val(),
				"booking" : $("#newBooking").val(),
				"bookingQuantity" : $("#bookingQuantity").val(),
				"price": "$100",
			}).draw(false);
			//Swal.fire("El codigo es correcto", "", "success");
	}else{
		contador=0;
		for (var i = 0; i < filas; i++) {
		currentData = $("#containerTable").DataTable().row(i).column(2).data();
		if($("#newBooking").val()==currentData[i]){
			Swal.fire("El booking ya esta registrado ingrese un numero de contenedor diferente", "", "warning");
			contador++;
		}
		
	}
	if(contador==0){
			$("#containerTable").DataTable().row.add({
				"containerId": $("#containerId").val(),
				"container": $("#newContainer").val().toUpperCase( ),
				"containerType": $("#newContainerType").val(),
				"containerSize": $("#newContainerSize").val(),
				"shippingCompany": $("#newShippingCompany").val(),
				"booking" : $("#newBooking").val(),
				"bookingQuantity" : $("#bookingQuantity").val(),
				"price": "$100",
			}).draw(false);
			//Swal.fire("El codigo es correcto", "", "success");
	}
	
	}
		
	}else{
		Swal.fire("Llenar los datos que se requieren", "", "warning");
	}
	$("#newContainer").val("")
	}
	}else{
	value=$("#newContainer").val();
	if(value.length==11){
	if (!verify(value)) {
		Swal.fire("El codigo es incorrecto", "", "error");
	}else{
	if(editAdd==1){
		editAdd=0;
		$("#containerTable").DataTable().row.add({
				"containerId": $("#containerId").val(),
				"container": $("#newContainer").val().toUpperCase( ),
				"containerType": $("#newContainerType").val(),
				"containerSize": $("#newContainerSize").val(),
				"shippingCompany": $("#newShippingCompany").val(),
				"booking" : $("#newBooking").val(),
				"bookingQuantity" : $("#bookingQuantity").val(),
				"price": "$100",
			}).draw(false);
			$("#containerId").val("")
			$("#newContainer").val("")
			//Swal.fire("El codigo es correcto", "", "success");
	}else{
	if($("#newContainer").val()!=""&&$("#newContainerType").val()!="0"&&$("#newShippingCompany").val()!="0"){
	filas =  $("#containerTable").DataTable().rows().data().count()
	$("#containerId").val(filas+1)
	if(filas==0){
		$("#containerTable").DataTable().row.add({
				"containerId": $("#containerId").val(),
				"container": $("#newContainer").val().toUpperCase(),
				"containerType": $("#newContainerType").val(),
				"containerSize": $("#newContainerSize").val(),
				"shippingCompany": $("#newShippingCompany").val(),
				"booking" : $("#newBooking").val(),
				"bookingQuantity" : $("#bookingQuantity").val(),
				"price": "$100",
			}).draw(false);
			//Swal.fire("El codigo es correcto", "", "success");
	}else{
		contador=0;
		for (var i = 0; i < filas; i++) {
		currentData = $("#containerTable").DataTable().row(i).column(1).data();
		if($("#newContainer").val().toUpperCase()==currentData[i]){
			Swal.fire("El contenedor ya esta registrado ingrese un numero de contenedor diferente", "", "warning");
			contador++;
		}
		
	}
	if(contador==0){
			$("#containerTable").DataTable().row.add({
				"containerId": $("#containerId").val(),
				"container": $("#newContainer").val().toUpperCase( ),
				"containerType": $("#newContainerType").val(),
				"containerSize": $("#newContainerSize").val(),
				"shippingCompany": $("#newShippingCompany").val(),
				"booking" : $("#newBooking").val(),
				"bookingQuantity" : $("#bookingQuantity").val(),
				"price": "$100",
			}).draw(false);
			//Swal.fire("El codigo es correcto", "", "success");
	}
	
	}
		
	}else{
		Swal.fire("Llenar los datos que se requieren", "", "warning");
	}
	$("#newContainer").val("")
	}
	}
	}else{
		Swal.fire("El codigo debe contener 11 caracteres ejemplo "+  '"XXXX1234567"', "", "warning");
	}	
	}

			
	}
	

function verify(container) {
	if (container.slice(10, container.length) == digitValidator(container)) {
		
		return true;
	} else {
		
		return false
	}
}


function getBase64Image(img) {
  var canvas = document.createElement("canvas");
  canvas.width = img.width;
  canvas.height = img.height;
  var ctx = canvas.getContext("2d");
  ctx.drawImage(img, 0, 0);
  var dataURL = canvas.toDataURL();
  return dataURL;
}



function convertToBase64() {
        //Read File
        var selectedFile = document.getElementById("voucher").files;
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
				$("#voucher64").val(base64)
            };
            // Convert data to base64
             fileReader.readAsDataURL(fileToLoad);
        }


    }




