define({
    /**
     * Reads the an 32 bit integer at the given offset.
     *
     * @param dataView    The data view that contains the integer
     * @param offset      The offset for the data
     * @returns {number}  Returns the 32 bit integer
     */
    readLength: function (dataView, offset) {
        return dataView.getInt32(offset);
    },

    /**
     * Reads a string at the given offset and invokes the callback
     * with the read string.
     *
     * @param dataView  The data view that contains the string
     * @param offset    The offset where the string starts
     * @param callback  The callback with one string argument
     */
    readString: function (dataView, offset, callback) {
        // read the length of the string and create an array
        var length = this.readLength(dataView, offset);
        var data = new Uint8Array(length);

        // read the array
        for (var i = 0; i < length; i++) {
            data[i] = dataView.getInt8(offset + 4 + i);
        }

        // convert the array to a string
        callback(String.fromCharCode.apply(null, data));
    },

    /**
     * Writes a string to the buffer and invokes the callback
     * with the written buffer.
     *
     * @param buffer    The data view that will contain the string
     * @param offset    The offset where to start writing
     * @param text      The text that should be written
     * @param callback  The callback with one data view arugment
     */
    writeString: function (buffer, offset, text, callback) {
        var length = text.length;
        var dataView = new DataView(buffer);

        // Write the length of the string
        dataView.setInt32(offset, length);

        // Write the actual string
        for (var i = 0; i < length; i++) {
            dataView.setInt8(offset + 4 + i, text.charCodeAt(i));
        }

        // Invoke the callback
        callback(dataView);
    }
});