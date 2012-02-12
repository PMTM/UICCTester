package eu.mighty.javatools;

import java.util.Vector;

/**
 * TLV
 * @author VencekR
 */
public class TLV {

    @SuppressWarnings("unchecked")
	public static TLV[] parseData(byte[] data, int offset, int length) {
        @SuppressWarnings("rawtypes")
		Vector tlvs = new Vector();
        int pos = 0;
        while (pos < length) {
            TLV tlv = new TLV();
            pos += tlv.parse(data, offset + pos);
            tlvs.addElement(tlv);
        }
        if (pos != length) {
            throw new IllegalArgumentException("Invalid TLV structure");
        }

        // rearrange to array
        TLV[] result = new TLV[tlvs.size()];
        for (int i = 0; i < result.length; i++) {
            result [i] = (TLV) tlvs.elementAt(i);
        }
        return result;
    }

    private short t;
    private byte l;
    private byte[] v;
    private int vOffset;

    protected TLV() {
    }

    public TLV (short tag, byte[] data) {
        if (data.length > 255) {
            throw new IllegalArgumentException("Data too long");
        }
        this.t = tag;
        this.l = (byte) data.length;
        this.v = data;
        this.vOffset = 0;
    }

    public int parse(byte[] data, int offset) {
        int pos = offset;
        byte t1 = data [pos++];
        if ((t1 & 0x1f) == 0x1f) {
            t = (short) (((t1 << 8) | data [pos++]) & 0xffff);
        } else {
            t = (short) (t1 & 0xff);
        }
        l = data [pos++];
        if (l > data.length - pos) {
            throw new IllegalArgumentException("TLV length differs from expected value");
        }
        v = data;
        vOffset = pos;
        pos += l;
        return pos - offset;
    }

    public short getTag() {
        return t;
    }

    public byte getLength() {
        return l;
    }

    /**
     * Returns value byte array - valid data are available from getValueOffset array index!
     * @return
     */
    public byte[] getValue() {
        return v;
    }

    public int getValueOffset() {
        return vOffset;
    }

    public String toString() {
        return "TLV{t=" + String.format("%02x", t & 0xFF) + ", l=" + l + ", v=" + HexTools.ba2hs(v) + "}";
    }
}
