package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClassInfo {

    private final int minorVersion;
    private final int majorVersion;
    private final CPInfo[] pool;
    private final int accessFlags;
    private final int thisClass;
    private final int superClass;
    private final int[] interfaces;
    private final FieldInfo[] fields;
    private final MethodInfo[] methods;
    private final AttributeInfo[] attributes;

    public ClassInfo(Class cls) throws IOException {
        this(cls.getResourceAsStream('/' + cls.getName().replace('.', '/') + ".class"));
    }

    public ClassInfo(InputStream inputStream) throws IOException {
        try (DataInputStream stream = new DataInputStream(inputStream)) {
            int magic = stream.readInt();
            if (magic != 0xCAFEBABE) {
                throw new ClassFormatError("Invalid magic number " + magic);
            }
            minorVersion = stream.readUnsignedShort();
            majorVersion = stream.readUnsignedShort();
            pool = CPInfo.readConstantPool(stream);
            accessFlags = stream.readUnsignedShort();
            thisClass = stream.readUnsignedShort();
            superClass = stream.readUnsignedShort();
            int interfaces_count = stream.readUnsignedShort();
            interfaces = new int[interfaces_count];
            for (int i = 0; i < interfaces_count; i++) {
                interfaces[i] = stream.readUnsignedShort();
            }
            int count = stream.readUnsignedShort();
            fields = new FieldInfo[count];
            for (int i = 0; i < count; i++) {
                fields[i] = new FieldInfo(stream, pool);
            }
            count = stream.readUnsignedShort();
            methods = new MethodInfo[count];
            for (int i = 0; i < count; i++) {
                methods[i] = new MethodInfo(stream, pool);
            }
            count = stream.readUnsignedShort();
            attributes = new AttributeInfo[count];
            for (int i = 0; i < count; i++) {
                attributes[i] = new AttributeInfo(stream, pool);
            }
            if (inputStream.read() != -1) {
                throw new ClassFormatError("Additional bytes found at the end of the stream");
            }
        }
    }

    private String getClassName(int poolIndex) {
        return pool[poolIndex].toString().replace('/', '.');
    }

    public void validate() {
        CPInfo.validate(pool, majorVersion);
        if (pool[thisClass].tag != CPInfo.CLASS) {
            throw new ClassFormatError("class name not found");
        }
        if (superClass != 0 && pool[superClass].tag != CPInfo.CLASS) {
            throw new ClassFormatError("super class name not found");
        }
        for (int interfaceIndex : interfaces) {
            if (pool[interfaceIndex].tag != CPInfo.CLASS) {
                throw new ClassFormatError("interface name not found");
            }
        }
        for (FieldInfo fieldInfo : fields) {
            fieldInfo.validate();
        }
        for (MethodInfo methodInfo : methods) {
            methodInfo.validate();
        }
        for (AttributeInfo attributeInfo : attributes) {
            attributeInfo.validate();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        String flag = Utils.getClassFlags(accessFlags);
        String className = getClassName(thisClass);
        sb.append(flag).append(' ').append(className);
        if (superClass != 0) {
            String superClassName = getClassName(superClass);
            if (!"java.lang.Object".equals(superClassName)) {
                sb.append(" extends ").append(superClassName);
            }
        }
        if (interfaces.length != 0) {
            sb.append(" implements ");
            for (int i : interfaces) {
                sb.append(getClassName(i)).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        return sb.toString();
    }

}
