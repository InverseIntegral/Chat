define(["packets/messagePacket", "packets/loginPacket"], function (messagePacket, loginPacket) {

    // protocol array contains all packets (used for de/serialization)
    var protocol = {};
    protocol[0] = loginPacket;
    protocol[1] = messagePacket;

    return {
        /**
         * Get a packet by its id
         * @param id    The id of the packet
         * @returns {*} Returns the packet object or undefined
         */
        getPacketById: function (id) {
            return protocol[id];
        },

        /**
         * Get a packet by its id from a data view
         * @param dataView  The data view that contains the packet id (at the offset 0)
         * @returns {*}     Returns the packet object or undefined
         * @see             #getPacketById
         */
        getPacket: function (dataView) {
            var id = dataView.getInt32(0);
            return this.getPacketById(id);
        }
    }

});