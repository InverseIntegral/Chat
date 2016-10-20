require(["protocol"], function (protocol) {

    // Send upgrade request and obtain data using array buffers.
    var socket = new WebSocket('ws://localhost:1337/websocket');
    socket.binaryType = 'arraybuffer';

    socket.onopen = function () {
        var element = document.getElementById("chat-input");

        // Add the on key press listener
        element.onkeypress = function (e) {
            if (e.keyCode == 13) {

                // Create a new packet
                var packet = protocol.getPacketById(1);
                packet.write(element.value, function (dataView) {

                    // Send the data view to the websocket
                    element.value = '';
                    socket.send(dataView);
                });

            }
        };
    };

    socket.onmessage = function (message) {
        // Get the data and create a data view representation
        var data = message.data;
        var dataView = new DataView(data);

        // Get the received packet
        var packet = protocol.getPacket(dataView);

        packet.read(dataView, function (message) {
            // Print the message to the dom
            var chat = document.getElementById("chat");
            chat.innerHTML += "<p>" + message + "</p>";
        });
    };

});