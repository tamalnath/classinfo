package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;

public class MethodInfo extends AbstractInfo {

    MethodInfo(DataInputStream stream, CPInfo[] pool) throws IOException {
        super(stream, pool);
    }

    @Override
    public String toString() {
        String flags = Utils.getMethodFlags(this.accessFlags);
        return flags + ' ' + super.toString();
    }
}