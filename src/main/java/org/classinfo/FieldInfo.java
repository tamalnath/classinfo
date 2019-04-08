package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;

public class FieldInfo extends AbstractInfo {

    FieldInfo(DataInputStream stream, CPInfo[] pool) throws IOException {
        super(stream, pool);
    }

    @Override
    public String toString() {
        String flags = Utils.getFieldFlags(this.accessFlags);
        return flags + ' ' + super.toString();
    }
}
