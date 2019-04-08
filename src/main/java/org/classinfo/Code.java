package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;

public class Code {
    private int maxStack;
    private int maxLocals;
    private byte[] code;
    private ExceptionTable[] exceptionTable;
    private AttributeInfo[] attributes;
    private CPInfo[] pool;

    Code(DataInputStream stream, CPInfo[] pool) throws IOException {
        this.pool = pool;
        maxStack = stream.readUnsignedShort();
        maxLocals = stream.readUnsignedShort();
        int length = stream.readInt();
        if (length <= 0 || length >= 65536) {
            throw new ClassFormatError("Invalid code length: " + length);
        }
        code = new byte[length];
        if (stream.read(code) != code.length) {
            throw new ClassFormatError("Could not read Code");
        }
        exceptionTable = new ExceptionTable[stream.readUnsignedShort()];
        for (int i = 0; i < exceptionTable.length; i++) {
            ExceptionTable table = new ExceptionTable(stream);
            exceptionTable[i] = table;
        }
        attributes = new AttributeInfo[stream.readUnsignedShort()];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = new AttributeInfo(stream, pool);
        }
    }

    void validate() {
        for (ExceptionTable table : exceptionTable) {
            if (table.endPc > code.length) {
                throw new ClassFormatError("Invalid End PC");
            }
            if (table.startPc > table.endPc) {
                throw new ClassFormatError("Invalid Start PC");
            }
            if (table.handlerPc > code.length) {
                throw new ClassFormatError("Invalid Handler PC");
            }
            if (table.catchType != 0 && pool[table.catchType].tag != CPInfo.CLASS) {
                throw new ClassFormatError("Invalid CatchType");
            }
        }
        for (AttributeInfo attribute : attributes) {
            attribute.validate();
        }
    }

    static class ExceptionTable {
        int startPc;
        int endPc;
        int handlerPc;
        int catchType;

        ExceptionTable(DataInputStream stream) throws IOException {
            startPc = stream.readUnsignedShort();
            endPc = stream.readUnsignedShort();
            handlerPc = stream.readUnsignedShort();
            catchType = stream.readUnsignedShort();
        }
    }
}
