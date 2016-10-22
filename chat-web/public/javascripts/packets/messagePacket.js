define(["../protocolUtil"], function (protocolUtil) {

    return {
        read: function (dataView, callback) {
            // reads the string after the packet id
            protocolUtil.readString(dataView, 4, callback);
        },

        write: function (text, callback) {
            // New buffer with the length of two ids and the actual text
            var buffer = new ArrayBuffer(8 + text.length);

            protocolUtil.writeString(buffer, 4, text, function (dataView) {

                // Writes the id and invokes the callback
                dataView.setInt32(0, 1);
                callback(dataView);
            });
        }
    };

});