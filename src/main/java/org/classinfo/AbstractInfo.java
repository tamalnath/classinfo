package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;

abstract class AbstractInfo {

    int accessFlags;
    private int nameIndex;
    private int descriptorIndex;
    private AttributeInfo[] attributes;
    private CPInfo[] pool;

    AbstractInfo(DataInputStream stream, CPInfo[] pool) throws IOException {
        accessFlags = stream.readUnsignedShort();
        nameIndex = stream.readUnsignedShort();
        descriptorIndex = stream.readUnsignedShort();
        int count = stream.readUnsignedShort();
        attributes = new AttributeInfo[count];
        for (int i = 0; i < count; i++) {
            attributes[i] = new AttributeInfo(stream, pool);
        }
        this.pool = pool;
    }

    @Override
    public String toString() {
        String name = pool[nameIndex].toString();
        String descriptor = pool[descriptorIndex].toString();
        return Utils.getSignature(name, descriptor);
    }

    void validate() {
        if(pool[nameIndex].tag != CPInfo.UTF8) {
            throw new ClassFormatError("invalid nameIndex");
        }
        if(pool[descriptorIndex].tag != CPInfo.UTF8) {
            throw new ClassFormatError("invalid descriptorIndex");
        }
        for (AttributeInfo attributeInfo : attributes) {
            attributeInfo.validate();
        }
    }

}
