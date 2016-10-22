require(["protocol"], function (protocol) {

    // Send upgrade request and obtain data using array buffers.
    var socket = new WebSocket('ws://localhost:1337/websocket');
    socket.binaryType = 'arraybuffer';

    socket.onopen = function () {
        var element = document.getElementById("chat-input");
        var label = document.getElementById("chat-label");
        element.disabled = false;

        var initialized = false;

        // Add the on key press listener
        element.onkeypress = function (e) {
            if (e.keyCode == 13) {

                if (!initialized) {

                    var login = protocol.getPacketById(0);
                    login.write(element.value, function (dataView) {
                        element.value = '';
                        label.innerHTML = 'Message';

                        socket.send(dataView);
                        initialized = true;
                    });

                    return;
                }

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
            chat.innerHTML += "<p>> " + message + "</p>";
        });
    };

    socket.onerror = function (error) {
        Materialize.toast('Unable to contact the server', 4000);
    }

});