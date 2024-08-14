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
			url: "deliveryOrders/getDataTable",
			type: 'GET',
			dataSrc: '',
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
	
	
	function initComponents(){
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
					deliveryOrderId : $("#deliverOrderId").val()
				};
				$.ajax({
			type: "POST",
			url: 'deliveryOrders/editDeliveryOrder',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					Swal.fire("los cambios se guardaron de forma correcta","", "success")
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
	}
	
	function newDeliveryOrder(data){
		currentData = $("#deliveryOrdersTable").DataTable().row(data).data();
		$("#deliverOrderId").val(currentData.deliveryOrderId)
		getInfotmation()
		$("#newDeliveryOrderlModal").modal('show');
	}
	
	function getInfotmation(){
		$.ajax({
		type: "GET",
		url: 'deliveryOrders/getInformation',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data:  {deliveryOrderId : $("#deliverOrderId").val()},
		success: function(response){
			$("#billTo").val(response.billTo)
			$("#carrierCompany").val(response.carrierCompany)
			$("#operator").val(response.operator)
			$("#economicNumber").val(response.economicNumber)
			$("#workOrder").val(response.workOrder)
			$("#quantityOfUnits").val(response.quantityOfUnits)
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	}
	
