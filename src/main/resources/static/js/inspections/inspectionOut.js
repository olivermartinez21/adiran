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

function configDataTable(){
	console.log("si entraaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
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
				],
			dom: {
				button:{
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			
		ajax: {
			url: 'inspectionOut/getContainersAssignments',
			type: 'GET',
			dataSrc: '',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			//data: {containerId : data},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		columns: [
			{ data: "containerId",visible: false },
			{ data: "dateInspection",visible: true },
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#shippingCompany").val(data);
						return $("#shippingCompany option:selected").html();
					}}, 
			{ data: "billTo",visible: true }, 
			{ data: "container",visible: true },
			{ data: "nomenclatura",visible: true },
			{ data: "containerType", visible: true , render : function(data) {
						$("#containerType").val(data);
						return $("#containerType option:selected").html();
					}}, 
			{ data: "condition", visible: true , render : function(data) {
						$("#containerCondition").val(data);
						return $("#containerCondition option:selected").html();
					}}, 
			{ data: "clasification", visible: true , render : function(data) {
						$("#containerClasification").val(data);
						return $("#containerClasification option:selected").html();
					}}, 
			{ data: "booking",visible: true },
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
				$("#containerId").val(data)
						return   '<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="inspectionOut(\'' + meta.row + '\');"><i class="fas fa-file"></i></button>&nbsp';
					}},
			
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
				if(data.length==36){
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="showPhoto(\'' + data + '\');"><i class="fas fa-eye"></i></button>&nbsp';
			}else{
				return '<a onclick="showPhotoOther(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>'
			}	
			}},
			{ data: "file", visible: false },
			{ data: "photoId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteImage(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
}

function initComponents(){
	
	
	$("#inspectionContainerForm").submit(function () {
		table = $("#imageTableInspection").DataTable().rows().data().toArray();
		
	var formData = new FormData();
	table.forEach(function(item) {
			formData.append('file', item.file);
		});
		var data = {
			
			
			containerId: $("#containerId").val(),
			qualityStamp: $("#newqualityStamp").val(),
			securityStamp: $("#newSecurityStamp").val(),
			dateInspection: $("#newDateInspection").val(),
			ventilation: $("#newVentilation").val(),
			temperature: $("#newTemperature").val(),
			humiity: $("#newHumity").val(),
			co2: $("#newCo2").val(),
			o2: $("#newO2").val(),
			ni: $("#newNi").val(),
			diesel: $("#newDiesel").val(),
			horometro: $("#newHorometro").val(),
			associateUnit: $("#newAsocieteUnit").val(), 
			
			operatorName: $("#name").val(), 
			dataUrl : $("#draw-dataUrl").val(),
	};
	
	formData.append('container',JSON.stringify(data));
	
	$.ajax({
		type : "POST",
		url : 'inspectionOut/saveInspectionOut',
		cache : false, 
		contentType : false,
		processData : false,
		data :formData,
		success : function(response) {
			if(response.success==true){
				window.open('inspectionOut/PDF_EIR_OUT?containerId='+ response.pdf+'')
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					configDataTable()
					$("#inspectionModal").modal("hide");
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
	
		return false;
	});	
	
	
}

function inspectionOut(data){
	currentData = $("#containerTable").DataTable().row(data).data();
	$("#containerId").val(currentData.containerId);
	console.log(currentData.containerType)
	if(currentData.containerType==6){
		document.getElementById("gensetDiv").setAttribute("hidden",true);
		document.getElementById("reeferDiv").removeAttribute("hidden");
		document.getElementById("unitAsociate").removeAttribute("hidden");
	}else if(currentData.containerType==4){
		document.getElementById("reeferDiv").setAttribute("hidden",true);
		document.getElementById("gensetDiv").removeAttribute("hidden");
		document.getElementById("unitAsociate").removeAttribute("hidden");
	}else {
		document.getElementById("reeferDiv").setAttribute("hidden",true);
		document.getElementById("gensetDiv").setAttribute("hidden",true);
		document.getElementById("unitAsociate").setAttribute("hidden",true);
	}
	$("#newShippinCompany").val(currentData.shippingCompany);
	$("#newFinalClient").val(currentData.billTo);
	$("#newUnit").val(currentData.container);
	$("#newNomenclatura").val(currentData.nomenclatura);
	$("#newTypeUnit").val(currentData.containerType);
	$("#newCondition").val(currentData.clasification);
	$("#newQuality").val(currentData.condition);
	$("#newBooking").val(currentData.booking);
	eventDateValidator();
	
	$("#inspectionModal").modal("show");
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


function addInspection(){
	var dataUrl = $("#imagenData").val();
	$("#inspectionTable").DataTable().row.add({
				"photo": dataUrl,
			}).draw(false);
			$("#inspectionId").val("")
}


function showPhoto(data){
Swal.fire({
  imageUrl: data,
})
}


function selectType(){
	if($("#newTypeUnit").val()==6){
		document.getElementById("gensetDiv").setAttribute("hidden",true);
		document.getElementById("reeferDiv").removeAttribute("hidden");
		document.getElementById("unitAsociate").removeAttribute("hidden");
	}else if($("#newTypeUnit").val()==4){
		document.getElementById("reeferDiv").setAttribute("hidden",true);
		document.getElementById("gensetDiv").removeAttribute("hidden");
		document.getElementById("unitAsociate").removeAttribute("hidden");
	}else {
		document.getElementById("reeferDiv").setAttribute("hidden",true);
		document.getElementById("gensetDiv").setAttribute("hidden",true);
		document.getElementById("unitAsociate").setAttribute("hidden",true);
	}
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

function getBase64FromFile(img, callback){
  let fileReader = new FileReader();
  fileReader.addEventListener('load', function(evt){
    callback(fileReader.result,img);
  });
  fileReader.readAsDataURL(img);
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
	
	 document.getElementById("newDateInspection").min = year+'-'+month+'-'+day; 
	document.getElementById("newDateInspection").value = year+'-'+month+'-'+day; 
	
}
