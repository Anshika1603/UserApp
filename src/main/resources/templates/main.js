function main()
{
    console.log("cors-test-new- http://52.149.247.168");
    /*$.ajax
    ({
        url: "http://52.149.247.168/v1/data/brands-sse",
        success: function(data)
        {
            console.log(data);
        }
    });*/

  	var source = new EventSource('http://localhost:8080/v1/data/user/records');

	 // Define a callback function for the "item" event
       source.addEventListener('user', function(event) {
           // Get the data of the event as a JSON object
           //var data = JSON.parse(event.data);

           // Display the data in the console or on the page
           console.log(event.data);
           // document.getElementById('items').innerHTML += data.name + '<br>';
       });

       // Define a callback function for the "error" event
       source.addEventListener('error', function(event) {
           // Handle any error that may occur
           console.error(event);
       });
}
