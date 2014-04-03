package org.spoutcraft.client.packet;

import org.spoutcraft.api.io.SpoutInputStream;
import org.spoutcraft.api.io.SpoutOutputStream;

import java.io.IOException;
import java.net.URL;

public class PacketSendLink implements SpoutPacket {
    protected URL link;

    public PacketSendLink() {
        link = null;
    }

    @Override
    public void readData(SpoutInputStream input) throws IOException {
        link = new URL(input.readString());
    }

    @Override
    public void writeData(SpoutOutputStream output) throws IOException {
        throw new IOException("The client may not send a link to the server!");
    }

    @Override
    public void run(int playerId) {
        //TODO Do something interesting here Dockter
    }

    @Override
    public void failure(int playerId) {

    }

    @Override
    public PacketType getPacketType() {
        return PacketType.PacketSendLink;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public String toString() {
        return "PacketSendLink{ version= " + getVersion() + ", link= " + (link == null ? "null" : link.toString()) + " }";
    }
}
