package net.md_5.bungee.protocol.packet;

import net.md_5.bungee.protocol.DefinedPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.OverflowPacketException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LoginPayloadResponse extends DefinedPacket
{

    private int id;
    private byte[] data;

    @Override
    public void read(ByteBuf buf)
    {
        id = readVarInt( buf );

        if ( buf.readBoolean() )
        {
            int len = buf.readableBytes();
            if ( len > 1048576 )
            {
                throw new OverflowPacketException( "Payload may not be larger than 1048576 bytes" );
            }
            data = new byte[ len ];
            buf.readBytes( data );
            System.out.println( new String( data, StandardCharsets.UTF_8 ) );

        }
    }

    @Override
    public void write(ByteBuf buf)
    {
        writeVarInt( id, buf );
        if ( data != null )
        {
            buf.writeBoolean( true );
            buf.writeBytes( data );
        } else
        {
            buf.writeBoolean( false );
        }
    }

    @Override
    public void handle(AbstractPacketHandler handler) throws Exception
    {
        handler.handle( this );
    }
}
