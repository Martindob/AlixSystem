/*
package nanolimbo.alix.connection.pipeline;

import io.netty.util.ByteProcessor;

public final class VarIntByteDecoder implements ByteProcessor {

    private int readVarInt;
    private byte bytesRead;
    private DecodeResult result = DecodeResult.TOO_SHORT;

    @Override
    public boolean process(byte k) {
        readVarInt |= (k & 0x7F) << bytesRead++ * 7;
        if (bytesRead > 3) {
            result = DecodeResult.TOO_BIG;
            return false;
        }
        if ((k & 0x80) != 128) {
            result = DecodeResult.SUCCESS;
            return false;
        }
        return true;
    }

    public void reset() {
        this.readVarInt = 0;
        this.bytesRead = 0;
        this.result = DecodeResult.TOO_SHORT;
    }

    public int getReadVarInt() {
        return readVarInt;
    }

    public byte getBytesRead() {
        return bytesRead;
    }

    public DecodeResult getResult() {
        return result;
    }

    public enum DecodeResult {
        SUCCESS,
        TOO_SHORT,
        TOO_BIG
    }
}*/
