$(document).ready(function() {

var options = {
autoClose: true,
progressBar: true,
enableSounds: true,
sounds: {

info: "https://res.cloudinary.com/dxfq3iotg/video/upload/v1557233294/info.mp3",
// path to sound for successfull message:
success: "https://res.cloudinary.com/dxfq3iotg/video/upload/v1557233524/success.mp3",
// path to sound for warn message:
warning: "https://res.cloudinary.com/dxfq3iotg/video/upload/v1557233563/warning.mp3",
// path to sound for error message:
error: "https://res.cloudinary.com/dxfq3iotg/video/upload/v1557233574/error.mp3",
},
};

var toast = new Toasty(options);
toast.configure(options);

$("#toasEventCreated").on( "click", function(event,txt) {
	
toast.success(txt);

});

$('#toastInvalidInput').on( "click", function(event,txt) {
	
toast.error("Fields should not be empty" + txt);

});






$('#successtoast').click(function(txt) {

toast.success("This toast notification with sound:" + txt);

});

$('#infotoast').click(function() {

toast.info("This toast notification with sound");

});

$('#warningtoast').click(function() {

toast.warning("This toast notification with sound");

});

$('#errortoast').click(function() {

toast.error("This toast notification with sound");

});

});