package ua.nanit.limbo.connection.pipeline;

import alix.common.utils.netty.BufUtils;
import alix.common.utils.netty.FastNettyUtils;
import io.netty.buffer.ByteBuf;

final class VarIntLengthEncoder {// extends MessageToByteEncoder<ByteBuf> {

    static ByteBuf encode(ByteBuf msg, boolean pooled) {
        int readableBytes = msg.readableBytes();
        byte varIntBytes = FastNettyUtils.countBytesToEncodeVarInt(readableBytes);

        //prefix the buf with the VarInt length of it's byte length
        int newCapacity = readableBytes + varIntBytes;
        ByteBuf out = pooled ? BufUtils.pooledBuffer(newCapacity) : BufUtils.unpooledBuffer(newCapacity); //Unpooled.directBuffer(msg.capacity() + varIntBytes);

        FastNettyUtils.writeVarInt0(out, readableBytes, varIntBytes);
        out.writeBytes(msg);

        msg.release();

        return out;
    }

    /*@Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buf, ByteBuf out) {
        ByteMessage msg = new ByteMessage(out);
        msg.writeVarInt(buf.readableBytes());
        msg.writeBytes(buf);
    }

    @Override
    protected ByteBuf allocateBuffer(ChannelHandlerContext ctx, ByteBuf msg, boolean preferDirect) {
        int anticipatedRequiredCapacity = 5 + msg.readableBytes();
        return ctx.alloc().heapBuffer(anticipatedRequiredCapacity);
    }*/
}
