
var TheInnerHTML = "";
var dataTable = document.getElementById("dataTable");


var subscribeEvents = function() {

	var eventSource = new EventSource("/sse/receive");

	eventSource.addEventListener("eventSaved", function(event) {


		var json_data = JSON.parse(event.data);
		console.log("event.data: " + json_data.teamHome + " vs " + json_data.teamAway  + 
		" ("+json_data.scoreHome+"-"+json_data.scoreAway+")");
		//call toast, passing the value
		$("#toasEventCreated").trigger("click", ["New event: " + json_data.teamHome + " vs " + json_data.teamAway]);

		/*TheInnerHTML += "<tr><td>"+formatEvent(json_data)+"</td>"+
						 "<td>"+formatScore(json_data)+"</td></tr>";
		document.getElementById("TheBody").innerHTML = TheInnerHTML;*/

		var eventNotification =  "[" + json_data.id + "] " + json_data.updatingDate + ": " + formatEvent(json_data) + formatScore(json_data);
		dataTable.innerHTML = eventNotification + "<br/>" + dataTable.innerHTML;

	});
}

const formatEvent = (data) => {
	return data.teamHome +
		" vs " +
		data.teamAway;
}
const formatScore = (data) => {
	return " (" + data.scoreHome +
		" - " +
		data.scoreAway +
		")";
}


document.getElementById("sendRequest").onclick = async () => {
	var url = '/api/events';

	if (validateFields()) {
		const data = dataHtmlToJson();
		const response = await fetch(url, {
			method: 'POST',
			credentials: 'same-origin',
			headers: {
				'Content-Type': 'application/json',
			},
			// Body data type must match "Content-Type" header        
			body: JSON.stringify(data),
		});

		try {
			const newData = await response.json();
			cleanFields();
			return newData;
		} catch (error) {
			console.log("error", error);
		}
	} else {
		$("#toastInvalidInput").trigger("click");
	}
};


document.getElementById("nameHome").required = false;

const dataHtmlToJson = () => {
	var nameA = document.getElementById("nameHome").value;
	var nameB = document.getElementById("nameAway").value;
	var scoreA = document.getElementById("scoreHome").value;
	var scoreB = document.getElementById("scoreAway").value;


	//create the object
	const eventScore = {
		teamHome: nameA,
		teamAway: nameB,
		scoreHome: scoreA,
		scoreAway: scoreB
	};
	return eventScore;
}

const cleanFields = () => {
	$("#nameHome").val("");
	$("#nameAway").val("");
	$("#scoreHome").val("");
	$("#scoreAway").val("");
	$("#nameHome").attr("required", false);
	$("#nameAway").attr("required", false);
	$("#scoreHome").attr("required", false);
	$("#scoreAway").attr("required", false);

}

//validates input data
const validateFields = () => {
	var isValid = true;
	var regex=/^[0-9]+$/;
	if ($("#nameHome").val() == "") {
		document.getElementById("nameHome").setAttribute("required", true);
		isValid = false;
	}
	if ($("#nameAway").val() == "") {
		document.getElementById("nameAway").setAttribute("required", true);
		isValid = false;
	}
	if ($("#scoreHome").val() == "") {
		document.getElementById("scoreHome").setAttribute("required", true);
		isValid = false;
	}
	
	if ($("#scoreAway").val() == "") {
		document.getElementById("scoreAway").setAttribute("required", true);
		isValid = false;
	}

	return isValid;
}





window.onload = subscribeEvents();
window.onbeforeunload = function() {
	eventSource.close();
};








