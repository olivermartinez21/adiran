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
	$("#usersTable").DataTable({
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
				{text: 'Usuario Nuevo', action: function() { addNewUser(); }},
				{text: 'Registrar Rol', action: function() { addNewRole(); }},
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "userAdministration/getDataTable",
			type: 'GET',
			dataSrc: '',
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{data: 'userId'},
			{data: 'userName'},
			{data: 'roleList'},
			{data: 'userId', render : function(data) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar nuevo Rol" onclick="assignRole(\'' + data + '\');"><i class="fas fa-arrows-alt-h"></i></button>&nbsp;'  +
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Baja de rol" onclick="removeRole(\'' + data + '\');"><i class="fas fa-user-minus"></i></button>&nbsp;' + 
				'<button type="button" class="btn btn-outline-dark btn-sm" title="Baja de usuario" onclick="deleteUser(\'' + data + '\');"><i class="fas fa-user-times"></i></button>';
			}}
		],
	}).columns.adjust();
}

function initComponents() {
	$("#newUserForm").submit(function() {
		if($("#password").val() == $("#passwordConfirm").val()) {
			data = {
				userName : $("#userName").val(),
				roleList : $("#roleSelect").val(),
				password : $("#password").val(),
				operation : "INSERT",
			}	
			$.ajax({
				type: "POST",
		    	url: "userAdministration/addNewUser",
		   		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		   		data: data,
		   		success: function(response) {
			   		manageResponse(response)
			   	},
			   	error: function(response) {
		   			manageAjaxResponse(response);
		   		}
			});	
		} else {
			showAlert(2, warningTitle, "Las contraseñas deben ser iguales", "");
		}
		return false;
	});
	
	$("#newRoleForm").submit(function () {
		if($("#roleName").val() == $("#confirmRoleName").val()) {
			$.ajax({
				type: "POST",
		    	url: "userAdministration/addNewRole",
		   		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		   		data: {roleName : $("#roleName").val(),
						operation : "INSERT",},
		   		success: function(response) {
			   		manageResponse(response)
			   	},
			   	error: function(response) {
		   			manageAjaxResponse(response);
		   		}
			});	
		} else {
			showAlert(2, warningTitle, "Los nombres del rol deben ser iguales ", "");
		}
		return false;
	});
	
	$("#roleAssignForm").submit(function () {
			$.ajax({
				type: "POST",
		    	url: "userAdministration/assignRole",
		   		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		   		data: {	userId : $("#userRoleSelect").val(),
						roleId : $("#roleAssignSelect").val(),
						operation : "INSERT",},
		   		success: function(response) {
			Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					//$("#inspectionModal").modal("hide");
				//configDataTablePregate()
				self.location.reload();
				});
			   		//manageResponse(response)
			   	},
			   	error: function(response) {
		   			//manageAjaxResponse(response);
		   		}
			});	
		return false;
	});
	
	$("#removeRoleForm").submit(function () {
		 
		console.log($("#removeRoleUserSelect").val())
		console.log($("#removeRolSelect").val())
		 
			$.ajax({
				type: "POST",
		    	url: "userAdministration/removeRole",
		   		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		   		data: {	userId : $("#removeRoleUserSelect").val(),
						roleId : $("#removeRolSelect").val(),
						operation : "UPDATE",},
		   		success: function(response) {
			console.log(response)
			   		Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
				self.location.reload();
				});
			   	},
			   	error: function(response) {
					console.log(response)
		   			Swal.fire("Proceso fallido", "", "warning")
		   		}
			});	
		return false;
	});
}

function addNewUser() {
	resetUserModal();
	$("#newUserModal").modal("show");
}

function resetUserModal() {
	$("#userName").val("");
	$("#roleSelect").val("");
	$("#password").val("");
	$("#passwordConfirm").val("");
}

function addNewRole() {
	resetRoleModal();
	$("#newRoleModal").modal("show");
}

function resetRoleModal() {
	$("#roleName").val("");
	$("#confirmRoleName").val("");
}

function assignRole(data) {
	$("#userRoleSelect").prop('disabled', false);
	$("#userRoleSelect").val(data);
	$("#userRoleSelect").prop('disabled', true);
	$("#assignRoleModal").modal("show");
}

function removeRole(data) { 
	$("#removeRoleUserSelect").prop('disabled', false);
	$("#removeRoleUserSelect").val(data);
	$("#removeRoleUserSelect").prop('disabled', true);
	var combo = document.getElementById("removeRolSelect");
	$.ajax({
		type: "GET",
		url: "userAdministration/getRoles",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {userId : data},
		success: function(response) {
			console.log(response)
			clearCombo(combo);
			fillCombo(combo,response);
	    	manageResponse(response);
		},
		error: function(response) {
			manageResponse(response);
		}
	});
	$("#removeRoleModal").modal('show');
}

function deleteUser(data) {
	$.ajax({
		type: "POST",
		url: "userAdministration/removeUser",
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {userId : data,
				operation : "UPDATE"},
		success: function(response) {
	    	manageResponse(response);
		},
		error: function(response) {
			manageResponse(response);
		}
	});
}