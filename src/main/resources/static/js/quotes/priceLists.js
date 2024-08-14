$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });
$(document).ready( function() {
	$('#loading').hide();
});

function chargeDoc(){
	if( $("#orderListCmb").val()!="Selecciona una opcion"){
	var formData = new FormData();
	formData.append('file', $("#file").prop('files')[0]);
	formData.append('type', $("#orderListCmb").val());
		$.ajax({
		type : "POST",
		url : 'priceLists/upload',
		cache : false, 
		contentType : false,
		processData : false,
		data : formData,
		success : function() {
			Swal.fire("INFORMACION GUARDADA","", "success")
		},
		error : function() {
			Swal.fire("ERRO 902","el documento no pudo cargarse", "warning")
		}
	});
	}else{
		Swal.fire("Selecciona una opcion","", "warning")
	}
	
}