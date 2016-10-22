define(["../protocolUtil"], function (protocolUtil) {

    return {
        read: function (dataView, callback) {
            // reads the string after the packet id
            protocolUtil.readString(dataView, 4, callback);
        },

        write: function (username, callback) {
            // New buffer with the length of two ids and the actual text
            var buffer = new ArrayBuffer(8 + username.length);

            protocolUtil.writeString(buffer, 4, username, function (dataView) {

                // Writes the id and invokes the callback
                dataView.setInt32(0, 0);
                callback(dataView);
            });
        }
    };

});