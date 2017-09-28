$(document).ready(function(){
	$("#totop").click(function () {
		console.log('está andando');
	   $("html, body").animate({scrollTop: 0}, 1000);
	});
	

    $(window).on('load',function(){
    	if($("#errorShowModal").length != 0) {
    		$('#myModal').modal('show');
  		}
   
    	if($("#userCreated").length != 0) {
    		setTimeout(function(){ 
    			window.location.replace("/");
    		}, 2000);
    		
  		}
    });
})