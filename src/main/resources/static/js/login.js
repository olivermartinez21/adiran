$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });

$(document).ready( function() {
	$('#loading').hide();
	initComponents();
});


function saveRegister() {
	$("#operation").val("INSERT")
	$("#newRegisterModel").modal("show");
}

$("#user").keyup(function(){   
	if($("#user").val().charAt($("#user").val().length-1)==" "){
	swal("no se permiten espacios ", "", "warning");
	}           
         $("#user").val($("#user").val().replace(/ /g, ""))


}); 

function initComponents() {
	$("#newRegisterForm").submit(function () {
		if($("#password").val()==$("#confirmPassword").val()) {
			document.getElementById("btnAcepted").setAttribute("disabled", true);
			
			var data = {
				name: $("#name").val(),
				agency: $("#agency").val(),
				user: $("#user").val(),
				password: $("#password").val()
			};
			
			console.log(data)
			$.ajax({
			type: "POST",
			url: 'login/register',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response);
				if(response.success==true){
					swal("Proceso Exitoso", "", "success")
				.then(() => {
				self.location.reload();
				});
				}else{
					document.getElementById("btnAcepted").removeAttribute("disabled");
					swal(response.message, "", "error");
				}
			
			}, 
			error: function(){
				alert("AJAX ERROR");
				

			}
			});
		} else {
			swal("Las contraseñas deben ser iguales", "", "warning");
		}
			
		
		
	return false;
		});
	}
	
	


	
