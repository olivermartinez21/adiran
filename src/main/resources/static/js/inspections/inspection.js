$(document).ajaxStart(function(){
    $('#loading').show();
 }).ajaxStop(function(){
    $('#loading').hide();
 });

$(document).ready( function() {
	$("#containerId").val(GetURLParameter('containerId'));
	$('#loading').hide();
	configDataTable();
	initComponents();
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
function configDataTable() {
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
				//{text: '+', action: function() { addNewContainer()}},
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
			ajax: {
			url: "inspection/getDataTable",
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
			{ data: "shippingCompany", visible: true , render : function(data) {
						$("#shippingCompanyCatalogInspecction").val(data);
						return $("#shippingCompanyCatalogInspecction option:selected").html();
					}}, 
			{ data: "container",visible: true },
			{ data: "statusQute", visible: true , render : function(data) {
				$("#containerDescriptionQuoteCatalog").val(data)
						return $("#containerDescriptionQuoteCatalog option:selected").html();
					}},
			{ data: "containerType", visible: true , render : function(data) {
						$("#containerDescriptionInspecction").val(data);
						return $("#containerDescriptionInspecction option:selected").html();
					}}, 
			{ data: "containerSize",visible: true },
			{ data: "daysStay", visible: true , render : function(data) {
						return data;
					}},
			{ data: "assignedTo", visible: true , render : function(data) {
						return data;
					}},
			{ data: "startDate", visible: true , render : function(data) {
						return data;
					}},
			{ data: "finalDate", visible: true , render : function(data) {
						return data;
					}},
			{ data: "condition", visible: false , render : function(data) {
						$("#containerConditionInspecction").val(data);
						return   $("#containerConditionInspecction option:selected").html() + '&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="actualizar informacion" onclick="changeStatus(\'' + data + '\');"><i class="fas fa-pen"></i></button>';
					}},
			{ data: "clasification", visible: true , render : function(data) {
				$("#qualityGradeInspection").val(data);
						return   $("#containerConditionInspecction option:selected").html() +" "+$("#qualityGradeInspection option:selected").html();
					}},
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
						return  '&nbsp<button type="button" class="btn btn-outline-dark btn-sm" title="Asignar" onclick="asigned(\'' + meta.row + '\');"><i class="fas fa-pen"></i></button>';
					}},	
			{ data: "containerId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "containerId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "comnetsQuote", visible: true , render : function(data) {
						return data;
					}},
							
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
						$("#containerStatus").val(data);
						return   '<button type="button" class="btn btn-outline-dark btn-sm" title="Daños a reparar" onclick="newDamage(\'' + meta.row + '\');"><i class="fas fa-pen"></i></button>';
					}},
			{ data: "statusQute", visible: false , render : function(data) {
					$("#statusContainer").val(data);
						return data;
					}},
			{ data: "containerId", visible: false , render : function(data) {
						return "";
					}},
			{ data: "containerId", visible: true , render : function(data, type, full, meta) {
						$("#containerStatus").val(data);
							return   '<button type="button" class="btn btn-outline-dark btn-sm" title="Liberar" onclick="changeStatus(\'' + meta.row  + '\');"><i class="fas fa-check"></i></button>';
						
					}}, 
					
			
		], 
	}).columns.adjust();
	$("#imageTableInspectionView").DataTable().destroy();
	$("#imageTableInspectionView").DataTable({
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
				//return '<a onclick="showPhoto(\'' + meta.row + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				//;
			}},
			{ data: "file", visible: false },
			{ data: "photoId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteImage(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
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
				//return '<a onclick="showPhoto(\'' + meta.row + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				//;
			}},
			{ data: "file", visible: false },
			{ data: "photoId", visible: true , render : function(data, type, full, meta) {
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Eliminar Cita" onclick="deleteImage(\'' + meta.row + '\');"><i class="fas fa-trash"></i></button>&nbsp';
				
			}},
		],
	}).columns.adjust();
	}
	
	
	function starRepair(data) {
		$.ajax({
			type: "POST",
			url: "inspection/inspectionValidation",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {inspectionId : data},
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
	}
		
		
	function showPhoto(data){
	document.getElementById("imagenPrevisualizacion").setAttribute("src", data);
	$("#imageShowModal").modal("show");
	}	
	
	
	
function asigned(data){
	currentData = $("#containerTable").DataTable().row(data).data();
	$("#containerId").val(currentData.containerId)
	$("#assignedTo").val(currentData.assignedTo)
	$("#startDate").val(currentData.startDate)
	$("#finalDate").val(currentData.finalDate)
	
	
	$("#containerStatusModel").modal("show");
}
function changeStatus(data){
	
	currentData = $("#containerTable").DataTable().row(data).data();
	$("#containerId").val(currentData.containerId)
	console.log(currentData)
		$("#containerCondition").val(currentData.condition),
		$("#qualityGrade").val(currentData.clasification),
	
	$("#statusModel").modal("show");
	}	
	
function newDamage(data){
	currentData = $("#containerTable").DataTable().row(data).data();
	$("#containerId").val(currentData.containerId)
	$("#containerDescriptionInspecction").val(currentData.containerType)
	getSection()
	dataTableInpection()
	getDamageInfotmation(currentData.containerType)
	
		$.ajax({
		type: "GET",
		url: 'inspection/validationInspection',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: {containerId :$("#containerId").val()},
		success: function(response){
			if(response==0){
					document.getElementById("btnAcept").removeAttribute("hidden")
			}
		},
		error: function(){
			alert("AJAX ERROR");
		}
	});
	
	
	$("#damageModel").modal("show");
	}		
	
		
	function dataTableInpection() {
	
	$("#newInspectionTable").DataTable().destroy();
	$("#newInspectionTable").DataTable({
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
					{text: '+', action: function() { addNewDamage()}},
				],
			dom: {
				
				button:{
					
	                tag:"button",
	                className:"btn btn-dark"
	            },
			}},
		ajax: {
			url: "inspection/dataTableInpection",
			type: 'GET',
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			dataSrc: '',
			data: {containerId : $("#containerId").val()},
			error: function(response) {
				console.log(response);
				manageErrorAjax(response);
			}
		},
		
		createdRow: function( row, data ) {
				if(data.status==4){
				$(row).addClass('green');
				}
        		
        },

			
		columns: [
			{ data: "inspectionId",visible: false },
			{ data: "part",visible: true },
			{ data: "component", visible: true , render : function(data) {
						$("#component").val(data);
						return $("#component option:selected").html();
					}}, 
			{ data: "damage", visible: true , render : function(data) {
						$("#damage").val(data);
						return $("#damage option:selected").html();
					}}, 
			{ data: "repair", visible: true , render : function(data) {
						$("#repair").val(data);
						return $("#repair option:selected").html();
					}}, 
			{ data: "length", visible: true , render : function(data) {
					$("#largeInspection").val(data);
						return data;
						
					}},
			{ data: "width", visible: true , render : function(data) {
					$("#heigthInspection").val(data);
						return data;
						
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return $("#largeInspection").val()*$("#heigthInspection").val();
						
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "customerType",visible: true , render : function(data) {
						$("#inspectionCustomerType").val(data);
						return $("#inspectionCustomerType option:selected").html();
					}}, 
			{ data: "location",visible: true },
			{ data: "quantity",visible: true },
			
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: false , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},			
			{ data: "photo", visible: true , render : function(data, type, full, meta) {
				$("#imagenData").val(data)
				//return '<a onclick="showPhoto(\'' + data + '\');" > <img  src="' + data + '" width="40" height="30" ></a>';
				return '<button type="button" class="btn btn-outline-dark btn-sm" title="Ver fotos" onclick="viewPhotos(\'' + meta.row + '\');"><i class="fas fa-eye"></i></button>&nbsp';
			}},
			{ data: "inspectionId", visible: true , render : function(data) {
						return "";
					}},	
			{ data: "status", visible: false , render : function(data) {
						return data;
					}},	
			{ data: "inspectionId", visible: true , render : function(data, meta) {
						return   '<button type="button" class="btn btn-outline-dark btn-sm" title="Daño Reparado" onclick="repairDamage(\'' + data + '\');"><i class="fas fa-check"></i></button>';
					}},	
		],


	
	}).columns.adjust();
	
	
	}
function addNewDamage(){
	$("#newDamageAddModel").modal("show")
}

function repairDamage(data){
	
	$.ajax({
			type: "POST",
			url: 'inspection/repairDamage',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data: {inspectionId: data},
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
					.then(() => {
					dataTableInpection()
					if(response.num==0){
					document.getElementById("btnAcept").removeAttribute("hidden")
					configDataTable()
					}
				});
				}else{
					console.log(response.message);
					Swal.fire(response.message+" Error", "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
	
}
function initComponents(){
	
	$("#containerStatusModel").submit(function () {
		var data = {
			assignedTo: $("#assignedTo").val(), 
			startDate : $("#startDate").val(), 
			finalDate : $("#finalDate").val(),
			containerId: $("#containerId").val(),
			}
			
			$.ajax({
			type: "POST",
			url: 'inspection/saveInformationRepair',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					$("#inspectionCapModel").modal("hide")
					Swal.fire("Proceso Exitoso", "", "success")
					.then(() => {
					configDataTable()
					$("#containerStatusModel").modal("hide");
				});
				}else{
					console.log(response.message);
					Swal.fire(response.message+" Error", "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
	})
	
$("#newDamageAddModel").submit(function () {
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
			 quantity:  $("#quantityInspection").val(),
			repairInspection: 1,
	};
	
	formData.append('inspection',JSON.stringify(data));
	
	if($("#newPart").val()=="Selecciona una opción"||$("#newComponentInspection").val()=="Selecciona una opción"||$("#newDamage").val()=="Selecciona una opción"||$("#newImageCode").val()==""){
				Swal.fire("Llenar los datos que se requieren", "", "warning");
	}else{
		$.ajax({
		type : "POST",
		url : 'inspection/addDamage',
		cache : false, 
		contentType : false,
		processData : false,
		data :formData,
		success : function(response) {
			if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
				.then(() => {
					$("#newDamageAddModel").modal("hide");
					dataTableInpection()
					configDataTable()
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
	})
	
	$("#damageModel").submit(function () {
			//configDataTable()
				$("#damageModel").modal("hide")
				$("#statusModel").modal("show");
			return false;
	})
	
	
$("#statusModel").submit(function () {
		
		var data = {
			containerId:  $("#containerId").val(),
			 condition:  $("#containerCondition").val(),
			 clasification:  $("#qualityGrade").val(),
	};
	
		$.ajax({
			type: "POST",
			url: 'inspection/changeStatus',
			cache: false,
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",
			data:data,
			success: function(response){
				console.log(response.success);
				if(response.success==true){
					Swal.fire("Proceso Exitoso", "", "success")
					.then(() => {
					configDataTable()
					$("#statusModel").modal("hide")
				});
				}else{
					console.log(response.message);
					Swal.fire(response.message+" Error", "", "warning");
				}
			}, 
			error: function(){
				alert("AJAX ERROR");
			}
			});
			
			return false;
	})
	
}
	
	
	
function getSection(){
	clearCombo(document.getElementById("newPart"));
	textContainer =   $("#containerDescriptionInspecction option:selected").html()
$.ajax({
			type: "GET",
			url: 'inspection/getSectionInformation',
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

function showComponents(){
	data = $("#newPart").val();
	console.log(data)
	$("#seccionSave").val(data)
	textContainer = $("#containerDescriptionInspecction option:selected").html()	
	console.log(textContainer) 
	document.getElementById("newComponentInspection")
	$.ajax({
		type: "GET",
		url: 'inspection/getComponentIformation',
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
	}
	
	function getDamageInfotmation(data){
			$.ajax({
		type: "GET",
		url: 'inspection/getDamageInformation',
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

function viewPhotos(data){
table = $("#newInspectionTable").DataTable().row(data).data();
$("#inspectionId").val(table.inspectionId)
$("#imageTableInspectionView").DataTable().clear().draw();

	var info = {
			containerId: $("#inspectionId").val(),
			} 
		
	$.ajax({
		type: "GET",
		url: 'inspection/getPhotos',
		contentType : "application/x-www-form-urlencoded; charset=UTF-8",
		data: info,
		success: function(response){
			console.log(response)

	for(var i=0;i<response.length;i++){
$("#imageTableInspectionView").DataTable().row.add({
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
			
			
	
	$("#photosModel").modal("show");	
}


		
function showPhoto(data){
	window.open('inspection/IMAGE?photoId='+ data+'')
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

function showPhotoOther(data){
	
Swal.fire({
  imageUrl: data,
})
	}