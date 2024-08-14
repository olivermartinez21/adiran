var successTitle = "Procedimiento exitoso";
var failureTitle = "Error";
var warningTitle = "Advertencia"
var confirmTitle = "Se requiere confirmacion"
var emptyString = "";
var modul="";




function clearCombo(combo) {
	for(var i = combo.options.length - 1; i >= 0 ; i--) {
		combo.removeChild(combo.options[i]);
	}
	var option = document.createElement('option');
	option.appendChild(document.createTextNode("Selecciona una opción"));
	combo.appendChild(option);
}

function clearComboContainer(combo) {
	for(var i = combo.options.length - 1; i >= 0 ; i--) {
		combo.removeChild(combo.options[i]);
	}
}


function fillCombo(combo,response) {
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].description.toString()));
		option.value = response[i].id;
		combo.appendChild(option);
	}
}
function fillComboDamage(combo,response) {
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].description.toString()));
		option.value = response[i].damageId;
		combo.appendChild(option);
	}
}


function fillComboConainer(combo,response) {
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].container.toString()));
		//option.value = response[i].containerId;
		combo.appendChild(option);
	}
}

function fillComboNomenclatura(combo,response) {
	
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].nomenclatura.toString()));
		//option.value = response[i].damageId;
		combo.appendChild(option);
	}
}


function fillComboComponentAjax(combo,response) {
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].component.toString()));
		option.value = response[i].componentId;
		combo.appendChild(option);
	}
}

function fillComboSection(combo,response) {
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].toString()));
		option.value = response[i];
		combo.appendChild(option);
	}
	}
function fillComboPart(combo,containerType) {
	let response = [
		{id: 0, description: "Seleccione una opcion"},
		];
		if(containerType == "3"||containerType == "6" ) {
		response.push(
		{id: 1, description: "INTERIOR"},
		{id: 2, description: "POSTERIOR"},
		{id: 3, description: "BASE"},
		{id: 4, description: "FRENTE"},
		{id: 5, description: "LADO DERECHO"},
		{id: 6, description: "LADO IZQUIERDO"},
		{id: 7, description: "TECHO	"}
		);
		if(containerType == "6"){
			response.push(
		{id: 8, description: "MAQUINARIA"},	 
		);
		}
		}
		if(containerType == "4"){
				response.push(
		{id: 8, description: "MAQUINARIA"},	
		{id: 9, description: "ESTRUCTURA"},	
		);
		}
		if(containerType == "1"){
				response.push(
		{id: 9, description: "ESTRUCTURA"},
		{id: 10, description: "SISTEMA DE ATERRIZAJE"},	
		{id: 11, description: "SISTEMA DE FRENOS"},
		{id: 12, description: "SISTEMA ELECTRICO"},	
		{id: 13, description: "SUSPENSIÓN"},
		{id: 14, description: "LLANTAS"}
		);
		}
		/*{id: 0, description: "INTERIOR"},
		{id: 0, description: "POSTERIOR"},
		{id: 0, description: "BASE"},
		{id: 0, description: "FRENTE"},
		{id: 0, description: "LADO DERECHO"},
		{id: 0, description: "LADO IZQUIERDO"},
		{id: 0, description: "TECHO	"},
		{id: 0, description: "MAQUINARIA"},	 
		{id: 0, description: "ESTRUCTURA"},
		{id: 0, description: "SISTEMA DE ATERRIZAJE"},	
		{id: 0, description: "SISTEMA DE FRENOS"},
		{id: 0, description: "SISTEMA ELECTRICO"},	
		{id: 0, description: "SUSPENSIÓN"},
		{id: 0, description: "LLANTAS"}*/
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].description.toString()));
		option.value = response[i].id;
		combo.appendChild(option);
	}
}

function fillComboComponent(combo,containerType,data){
	let response;
	switch(data) {
		case "1" : 
		if(containerType=="3"){
			response = [
		{id: 2, description: "Piso"},
		{id: 4, description: "Cuello de ganso"}, 
		{id: 5, description: "Pija"},
		{id: 6, description: "Anillo de amarre"},
			];
		}
		if(containerType=="6"){
			response = [
		{id: 7, description: "Piso “T”"},
		{id: 8, description: "Panel interior derecho"},
		{id: 9, description: "Panel interior izquierdo"},
		{id: 10, description: "Panel interior techo"},
		{id: 11, description: "Panel interior puerta"},
		{id: 12, description: "Panel interior maquina"},
		{id: 13, description: "Placa umbral"},
			];
		}
		
		if(containerType=="3"|| containerType=="6"){
				response.push(
		{id: 1, description: "Travesaño techo"},
		{id: 3, description: "Poste posterior interno"},
		{id: 14, description: "Otro"},
			);
		}
		
		response.sort(function(a,b) {
		if(a.id > b.id) {
			return 1;
		}
		if(a.id < b.id) {
			return -1;
		}
		});
		break;
		
		case "2" : 
		response = [
		{id: 15, description: "Travesaño superior"},
		{id: 16, description: "Travesaño inferior"},
		{id: 17, description: "Panel puerta"},
		{id: 18, description: "Barra guía"},
		{id: 19, description: "Empaque de puerta"},
		{id: 20, description: "Bisagra de puerta"}, 
		{id: 21, description: "Leva"},
		{id: 22, description: "Retenedor de leva"},
		{id: 23, description: "Abrazadera de barra guía"}, 
		{id: 24, description: "Manija de puerta "},
		{id: 25, description: "Retenedor de barra de puerta"},
		{id: 26, description: "Aldaba de retenedor de puerta"},
		{id: 27, description: "Sujetador de barra de puerta"},
		{id: 28, description: "Remache"},
		{id: 29, description: "Placa anti vandalismo"}, 
		{id: 30, description: "Placa CSC "},
		{id: 31, description: "Otro"},
			];
		
		break;
		
		case "3" : 
		if(containerType=="3"){
		response = [
		{id: 32, description: "Travesaño"},
		{id: 33, description: "Pasajes horquilla"},
		
		{id: 35, description: "Riel de cuello de ganso"},
		{id: 36, description: "Riel separador de piso"},
		{id: 37, description: "Otro"},
			];	
		}
		if(containerType=="6"){
		response = [
		{id: 32, description: "Travesaño"},
		{id: 34, description: "Hules de drenaje"},
		{id: 37, description: "Otro"},
			];	
		}

		break;
		case "4" :
		if(containerType=="3" || containerType=="6"){
		response = [
		{id: 38, description: "Travesaño superior"},
		{id: 39, description: "Travesaño inferior"},
		{id: 40, description: "Panel"},
		{id: 41, description: "Cantonera"},
		{id: 42, description: "Poste frente"},
		{id: 44, description: "Otro"},
			];	
		}
		
		if(containerType=="6"){
		response.push(
		{id: 43, description: "Marco de Maquinaria"},
			);
		}
		response.sort(function(a,b) {
		if(a.id > b.id) {
			return 1;
		}
		if(a.id < b.id) {
			return -1;
		}
		});
		
		break;
		case "5" : 
		if(containerType=="6" || containerType=="3"){
		response = [
		{id: 45, description: "Larguero Superior"},
		{id: 46, description: "Larguero Inferior "},
		{id: 47, description: "Panel"},
		{id: 48, description: "Cantonera"},
		{id: 49, description: "Poste frente"},
		{id: 50, description: "Poste posterior externo"},
		{id: 52, description: "Otro"},
			];	
		}
		
		if(containerType=="3"){
		response.push(
		{id: 51, description: "Ventila"},
			);
		}
		response.sort(function(a,b) {
		if(a.id > b.id) {
			return 1;
		}
		if(a.id < b.id) {
			return -1;
		}
		});

		break;
		case "6" : 
		if(containerType=="6" ||containerType=="3"){
		response = [
		{id: 53, description: "Larguero Superior"},
		{id: 54, description: "Larguero Inferior "},
		{id: 55, description: "Panel"},
		{id: 56, description: "Cantonera"},
		{id: 57, description: "Poste frente"},
		{id: 58, description: "Poste posterior externo"},
		{id: 59, description: "Otro "},
			];	
		}


		break;
		case "7" : 
		if(containerType=="6" || containerType=="3"){
		response = [
		{id: 60, description: "Panel/Lona"},
		{id: 61, description: "Placa de refuerzo esquinera"}, 
		{id: 62, description: "Lamina de extensión "},
		{id: 63, description: "Lamina de extensión de travesaño"},
		{id: 64, description: "Protecciones"},
		{id: 65, description: "Otro"},
			];	
		}


		break;
		case "8" : 
		if(containerType=="6"){
		response = [
		{id: 66, description: "Paneles de motores del evaporador"},
		{id: 67, description: "Puerta de elementos electrónicos"},
		{id: 68, description: "Protección del condensador"},
		{id: 69, description: "Condensador "},
		{id: 70, description: "Ventilador del condensador"}, 
		{id: 71, description: "Conector de interrogación"},
		{id: 72, description: "Protección del compresor"},
		{id: 73, description: "Compresor"},
		{id: 74, description: "Cable y Conector macho (Plug)"},
		{id: 75, description: "Ventila"},
		{id: 76, description: "Caja de control"},
		{id: 77, description: "Motor y propela"},
		{id: 78, description: "Elementos electrónicos"}, 
		{id: 79, description: "Difusor de aire (interior)"},
		{id: 80, description: "Controlador"},
		{id: 81, description: "Válvula de servicio"},
		{id: 82, description: "Otro"},
			];	
		}
		if(containerType=="4"){
		response = [
		{id: 83, description: "Switch de precalentamiento"},
		{id: 84, description: "Switch de arranque"},
		{id: 85, description: "Conjunto estator-rotor (Generador )"},
		{id: 86, description: "Motor de combustión"},
		{id: 87, description: "Amperímetro"},
		{id: 88, description: "Horómetro"},
		{id: 89, description: "Medidor de presión de aceite"},
		{id: 90, description: "Voltímetro"},
		{id: 91, description: "Medidor de temperatura de agua"},
		{id: 92, description: "Conector 440 volts."},
		{id: 93, description: "Breaker 440 volts"},
		{id: 94, description: "Batería y terminales"},
		{id: 95, description: "Cable de batería "},
		{id: 96, description: "Clips de montaje"},
		{id: 97, description: "Medidor de combustible"},
		{id: 98, description: "Tapón tanque de combustible"}, 
		{id: 99, description: "Bandas"},
		{id: 100, description: "Filtro de aire"},
		{id: 101, description: "Filtro de Aceite"},
		{id: 102, description: "Filtro de Combustible"},
		{id: 103, description: "Pre-filtro de Combustible"},
		{id: 104, description: "Estructura Inferior"},
		{id: 105, description: "Tapón de Radiador"},
		{id: 106, description: "Otro"},

			];	
		}
		
		break;
		case "9" : 
		
		if(containerType=="4"){
			
			response = [
		{id: 107, description: "Puertas"},
		{id: 108, description: "Base Inferior"}, 
		{id: 109, description: "Caja exterior "},
		{id: 110, description: "Tubo de escape "},
			];	
			}
		if(containerType=="1"){
			response = [
		{id: 111, description: "Largueros"},
		{id: 112, description: "Travesaños"},
		{id: 113, description: "Perno rey"},
		{id: 114, description: "Placa de perno rey"},
		{id: 115, description: "Llanta de refacción"},
		{id: 116, description: "Loderas"},
		{id: 117, description: "Porta documentos"},
		{id: 118, description: "Porta llanta de refacción"},
		{id: 119, description: "Llanta de refacción"},
		{id: 120, description: "Travesaño frontal"},
		{id: 121, description: "Travesaño trasero"},
		{id: 122, description: "Defensa"},
		{id: 123, description: "Topes de hule"},
		{id: 124, description: "Twist locks"},
		{id: 125, description: "Esquineros"},
		{id: 126, description: "Pintura"},
		{id: 127, description: "Placa de datos"}, 
		{id: 128, description: "Otro "},
			];
		}
		break;
		case "10" : 
		if(containerType=="1"){
			response = [
		{id: 129, description: "Sistema de elevación "},
		{id: 130, description: "Cilindros fijos"},
		{id: 131, description: "Cilindros móviles"},
		{id: 132, description: "Patín "},
		{id: 133, description: "Pies de patín"},
		{id: 134, description: "Pernos de patín"},
		{id: 135, description: "Estabilizadores"},
		{id: 136, description: "Caja de engranes"},
		{id: 137, description: "Manivela "},
			];
		}


		break;
		case "11" : 
		if(containerType=="1"){
			response = [
		{id: 138, description: "Conexiones de aire"},
		{id: 139, description: "Mangueras"},
		{id: 140, description: "Tambores"},
		{id: 141, description: "Balatas"},
		{id: 142, description: "Levas de ajuste"},
		{id: 143, description: "Tanques de aire"},
		{id: 144, description: "Válvulas"},
			];
		}


		break;
		case "12" : 
		if(containerType=="1"){
			response = [
		{id: 145, description: "Luces de placa"},
		{id: 146, description: "Calaveras/ Plafones"},
		{id: 147, description: "Direccionales"},
		{id: 148, description: "Porta cables"},
		{id: 149, description: "Conexión"},
		{id: 150, description: "Conector 7 vías"}, 
		{id: 151, description: "Reflectores"},
		{id: 152, description: "Luces laterales"},
		{id: 153, description: "Caja de conexión"},
			];
		}


		break;
		case "13" : 
		if(containerType=="1"){
			response = [
		{id: 154, description: "Ejes"},
		{id: 155, description: "Lubricantes"},
		{id: 156, description: "Tapas de eje"},
		{id: 157, description: "Soportes del eje"},
		{id: 158, description: "Resortes"},
		{id: 159, description: "Muelles "},
		{id: 160, description: "Balancín "},
		{id: 161, description: "Pernos “U”"},
		{id: 162, description: "Retenes "},
			]
		}


		break;
		case "14" : 
		if(containerType=="1"){
			response = [
		{id: 163, description: "IZQ-EXT-FRONTAL"},
		{id: 164, description: "IZQ-INT-FRONTAL"},
		{id: 165, description: "IZQ-EXT-TRASERA"},
		{id: 166, description: "IZQ-INT-TRASERA"},
		{id: 167, description: "DER-EXT-FRONTAL"},
		{id: 168, description: "DER-INT-FRONTAL"},
		{id: 169, description: "DER-EXT-TRASERA"},
		{id: 170, description: "DER-EXT-TRASERA"},
		{id: 171, description: "Rines"}, 
		{id: 172, description: "Birlos "},
			];
		}


		break;
		
		default:
		alert("default")
		break;
		
		
	}
	for(var i = 0; i < response.length; i++) {
		var option = document.createElement('option');
		option.appendChild(document.createTextNode(response[i].description.toString()));
		option.value = response[i].id;
		combo.appendChild(option);
	}
	
}

function fillComboLot(combo,response) {
	 for(var i = 0; i < response.length; i++) {
					var option = document.createElement('option');
					option.appendChild(document.createTextNode(response[i].lot.toString()));
					option.value = response[i].lot;
					combo.appendChild(option);
				}
}

function manageResponse(response) {
	if(response.success) {
		switch(response.operation){
		case 'INSERT': showAlert(1,successTitle, response.message, emptyString);
		break;
		case 'UPDATE': showAlert(1,successTitle, response.message, emptyString);
		break;
		case 'DELETE': showAlert(1,successTitle, response.message, emptyString);
		break;
		case 'TICKET': showAlert(1, "TICKET CREADO", "Su solicitud ha sido recibida satisfactoriamente", "en breve recibirá un correo con los detalles de su solicitud");
		break;
		case 'MAIL': showAlert(1,successTitle, "notificaciones enviadas por correo satisfactoriamente", "La notificacion fue enviada con éxito");
		break;
		case 'GET': showAlert(1,"Informacion Obtenida", "Se ha leido correctamente la infromación", emptyString);
		break;
		}
	} else {
		switch(response.operation){
		case 'INSERT': showAlert(3,failureTitle + " " + response.errorCode, "No se pudo crear el registro",response.message)
		break;
		case 'UPDATE': showAlert(3,failureTitle + " " + response.errorCode, "No se ha podido modificar el registro",response.message)
		break;
		case 'DELETE': showAlert(3,failureTitle + " " + response.errorCode, "No se pudo eliminar el registro",response.message)
		break;
		case 'TICKET': showAlert(3, failureTitle + " " + response.errorCode, "No ha sido posible procesar su solicitud de servico", response.message);
		break;
		case 'MAIL': showAlert(3, failureTitle + " " + response.errorCode, "No ha sido posible procesar el correo", response.message);
		break;
		case 'GET': showAlert(3,"Informacion NO Obtenida", "Error al leer el archivo", emptyString);
		break;
		}
		
	}
}

function showAlert(alertType, title, message, logging) {
	switch(alertType) {
	case 1:	$('#successModal').on('show.bs.modal', function(header, text, callback) {
		$('#successModal').find('.modal-header > h4').text(title).end();
		$('#successModal').find('.modal-body > p').text(message).end();
		$('#successModal').find('.modal-body > label').text("footer error").end();
	});
	$('#successModal').modal('show');
	$('#successModal').on('hidden.bs.modal', function () {
		location.reload();
	})

	break;
	case 2: $('#warningModal').on('show.bs.modal', function(header, text, callback) {
			$('#warningModal').find('.modal-header > h4').text(title).end();
			$('#warningModal').find('.modal-body > p').text(message).end();
			$('#warningModal').find('.modal-body > i').text(logging).end();
		});
		$('#warningModal').modal('show');
		break;
	case 3: $('#errorModal').on('show.bs.modal', function(header, text, callback) {
		
		$('#errorModal').find('.modal-header > h4').text(title).end();
		$('#errorModal').find('.modal-body > p').text(message).end();
		$('#errorModal').find('.modal-body > i').text(logging).end();
		});
	$('#errorModal').modal('show');
	break;
	case 4:
		$('#confirmModal').on('show.bs.modal', function(header, text, callback) {
			$('#confirmModal').find('.modal-header > h4').text(title).end();
			$('#confirmModal').find('.modal-body > p').text(message).end();
			$('#confirmModal').find('.modal-body > i').text(logging).end();
		});
		$('#confirmModal').modal('show');
		break;
	}
}


function getMonthString(monthNumber) {
	switch(monthNumber) {
	case 0: return "ENERO"; 
	case 1: return "FERBRERO"; 
	case 2: return "MARZO"; 
	case 3: return "ABRIL";
	case 4: return "MAYO";
	case 5: return "JUNIO";
	case 6: return "JULIO";
	case 7: return "AGOSTO";
	case 8: return "SEPTIEMBRE";
	case 9: return "OCTUBRE";
	case 10: return "NOVIEMBRE";
	case 11: return "DICIEMBRE";
	}
}

function manageErrorAjax(response){
	console.log(response);
	showAlert(3,response.error, "Codigo " + response.status, serverDescription(response.status, response.message))
}

function serverDescription(code, message){
	switch(code) {
	case 500: return "Excepción de apuntador a nulo: " + message;
	default: return "Excepcion Desconocida, contacte al equipo de desarrollo"
	}
}

function loading(value) {
	if(value){
		$('#loading').show();
	} else {
		$('#loading').hide();
	}
}

function parseDate(data) {
	if(data != null) {
	yyyy = data.substring(0,4);
	mm = data.substring(5,7);
	dd = data.substring(8,10);
	return dd + " / " + mm + " / " + yyyy;
	} else {
		return "N/A";
	}
}

function fillLeft(data,length,fillStr) {
	return data.toString().padStart(length,fillStr);
}

function isChecked(checkbox) {
	if(checkbox[0].checked) {
		return 1;
	} else {
		return 0;
	}
}

function checkIfTrue(value,checkbox) {
	if(value == 1){
		checkbox[0].checked = true;
	}
}

function toClientType(data) {
	switch(data) {
	case "NP": return "Persona Física Mexicana";
	case "FP": return "Persona Física Extranjera";
	case "NM": return "Persona Moral Mexicana";
	case "FM": return "Persona Moral Extranjera";
	default: return "DESCONOCIDO";
	}
}

function verifyNullDate(date) {
	var today = new Date();
	if(date == null) {
		return today.getFullYear() + "-" + String(today.getMonth() + 1).padStart(2, '0') + "-" + String(today.getDate()).padStart(2, '0');
	} else {
		return date;
	}
}

function parseWarehouse(data) {
	switch(data) {
		case "vill0102": return "Villahermosa";
		case "tiju0102": return "Tijuana";
		default : return "RENDER ERROR";
	}
}

function parseTimestamp(timestamp) {
	date = new Date(timestamp);
	return String(date.getDate()).padStart(2, '0') + "/" + String(date.getMonth() + 1).padStart(2, '0') + "/" + date.getFullYear() + " - " +
	date.getHours() + ":" + String(date.getMinutes()).padStart(2,'0') + ":" + String(date.getSeconds()).padStart(2,'0');
}

jQuery.fn.dataTable.Api.register( 'sum()', function ( ) {
	return this.flatten().reduce( function ( a, b ) {
		if ( typeof a === 'string' ) {
			a = a.replace(/[^\d.-]/g, '') * 1;
		}
		if ( typeof b === 'string' ) {
			b = b.replace(/[^\d.-]/g, '') * 1;
		}

		return a + b;
	}, 0 );
} );

function manageResponseNoRefresh(response) {
	if(response.success) {
		switch(response.operation){
		case 'INSERT': showAlertNoRefresh(1,successTitle, response.message, emptyString);
		break;
		case 'UPDATE': showAlertNoRefresh(1,successTitle, response.message, emptyString);
		break;
		case 'DELETE': showAlertNoRefresh(1,successTitle, response.message, emptyString);
		break;
		}
	} else {
		switch(response.operation){
		case 'INSERT': showAlertNoRefresh(3,failureTitle + " " + response.errorCode, "No se pudo crear el registro",response.message)
		break;
		case 'UPDATE': showAlertNoRefresh(3,failureTitle + " " + response.errorCode, "No se ha podido modificar el registro",response.message)
		break;
		case 'DELETE': showAlertNoRefresh(3,failureTitle + " " + response.errorCode, "No se pudo eliminar el registro",response.message)
		break;		
		}
		
	}
}

function showAlertNoRefresh(alertType, title, message, logging) {
	switch(alertType) {
	case 1:	$('#successModal').on('show.bs.modal', function(header, text, callback) {
		$('#successModal').find('.modal-header > h4').text(title).end();
		$('#successModal').find('.modal-body > p').text(message).end();
		$('#successModal').find('.modal-body > label').text("footer error").end();
	});
	$('#successModal').modal('show');
	$('#successModal').on('hidden.bs.modal', function () {
	})

	break;
	case 2: $('#warningModal').on('show.bs.modal', function(header, text, callback) {
			$('#warningModal').find('.modal-header > h4').text(title).end();
			$('#warningModal').find('.modal-body > p').text(message).end();
			$('#warningModal').find('.modal-body > i').text(logging).end();
		});
		$('#warningModal').modal('show');
		break;
	case 3: $('#errorModal').on('show.bs.modal', function(header, text, callback) {
		
		$('#errorModal').find('.modal-header > h4').text(title).end();
		$('#errorModal').find('.modal-body > p').text(message).end();
		$('#errorModal').find('.modal-body > i').text(logging).end();
		});
	$('#errorModal').modal('show');
	break;
	case 4:
		$('#confirmModal').on('show.bs.modal', function(header, text, callback) {
			$('#confirmModal').find('.modal-header > h4').text(title).end();
			$('#confirmModal').find('.modal-body > p').text(message).end();
			$('#confirmModal').find('.modal-body > i').text(logging).end();
		});
		$('#confirmModal').modal('show');
		break;
	}
}