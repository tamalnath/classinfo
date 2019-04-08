package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;

public class CPInfo {

    static final int UTF8 = 1;
    static final int INTEGER = 3;
    static final int FLOAT = 4;
    static final int LONG = 5;
    static final int DOUBLE = 6;
    static final int CLASS = 7;
    static final int STRING = 8;
    static final int FIELD_REF = 9;
    static final int METHOD_REF = 10;
    static final int INTERFACE_METHOD_REF = 11;
    static final int NAME_AND_TYPE = 12;
    static final int METHOD_HANDLE = 15;
    static final int METHOD_TYPE = 16;
    static final int DYNAMIC = 17;
    static final int INVOKE_DYNAMIC = 18;
    static final int MODULE = 19;
    static final int PACKAGE = 20;

    private static final int REF_getField = 1;
    private static final int REF_getStatic = 2;
    private static final int REF_putField = 3;
    private static final int REF_putStatic = 4;
    private static final int REF_invokeVirtual = 5;
    private static final int REF_invokeStatic = 6;
    private static final int REF_invokeSpecial = 7;
    private static final int REF_newInvokeSpecial = 8;
    private static final int REF_invokeInterface = 9;

    final int tag;

    int classIndex;
    int nameAndTypeIndex;
    int nameIndex;
    int descriptorIndex;
    int stringIndex;
    int referenceIndex;
    int bootstrapMethodAttrIndex;

    int intBytes;
    float floatBytes;
    long longBytes;
    double doubleBytes;
    String stringBytes;
    int referenceKind;

    private CPInfo[] pool;

    private CPInfo(DataInputStream stream, CPInfo[] pool) throws IOException {
        this.pool = pool;
        tag = stream.readUnsignedByte();
        switch (tag) {
            case UTF8:
                stringBytes = stream.readUTF();
                break;
            case INTEGER:
                intBytes = stream.readInt();
                break;
            case FLOAT:
                floatBytes = stream.readFloat();
                break;
            case LONG:
                longBytes = stream.readLong();
                break;
            case DOUBLE:
                doubleBytes = stream.readDouble();
                break;
            case STRING:
                stringIndex = stream.readUnsignedShort();
                break;
            case CLASS:
            case MODULE:
            case PACKAGE:
                nameIndex = stream.readUnsignedShort();
                break;
            case FIELD_REF:
            case METHOD_REF:
            case INTERFACE_METHOD_REF:
                classIndex = stream.readUnsignedShort();
                nameAndTypeIndex = stream.readUnsignedShort();
                break;
            case NAME_AND_TYPE:
                nameIndex = stream.readUnsignedShort();
                descriptorIndex = stream.readUnsignedShort();
                break;
            case METHOD_HANDLE:
                referenceKind = stream.readUnsignedByte();
                referenceIndex = stream.readUnsignedShort();
                break;
            case METHOD_TYPE:
                descriptorIndex = stream.readUnsignedShort();
                break;
            case DYNAMIC:
            case INVOKE_DYNAMIC:
                bootstrapMethodAttrIndex = stream.readUnsignedShort();
                nameAndTypeIndex = stream.readUnsignedShort();
                break;
            default:
                throw new IOException("Invalid Tag: " + tag);
        }
    }

    static CPInfo[] readConstantPool(DataInputStream stream) throws IOException {
        int count = stream.readUnsignedShort();
        CPInfo[] pool = new CPInfo[count];
        for (int i = 1; i < count; i++) {
            pool[i] = new CPInfo(stream, pool);
            int tag = pool[i].tag;
            if (tag == LONG || tag == DOUBLE) {
                i++;
            }
        }
        return  pool;
    }

    static void validate(CPInfo[] pool, int majorVersion) {
        for (int i = 1; i < pool.length; i++) {
            CPInfo cp = pool[i];
            switch (cp.tag) {
                case UTF8:
                    if(cp.stringBytes == null) {
                        throw new ClassFormatError("invalid stringBytes");
                    }
                    break;
                case LONG:
                case DOUBLE:
                    i++;
                    break;
                case STRING:
                    if(pool[cp.stringIndex].tag != UTF8) {
                        throw new ClassFormatError("invalid stringIndex");
                    }
                    break;
                case CLASS:
                case MODULE:
                case PACKAGE:
                    if(pool[cp.nameIndex].tag != UTF8) {
                        throw new ClassFormatError("invalid nameIndex");
                    }
                    break;
                case FIELD_REF:
                case METHOD_REF:
                case INTERFACE_METHOD_REF:
                    if(pool[cp.classIndex].tag != CLASS) {
                        throw new ClassFormatError("invalid classIndex");
                    }
                    if(pool[cp.nameAndTypeIndex].tag != NAME_AND_TYPE) {
                        throw new ClassFormatError("invalid nameAndTypeIndex");
                    }
                    break;
                case NAME_AND_TYPE:
                    if(pool[cp.nameIndex].tag != UTF8) {
                        throw new ClassFormatError("invalid nameIndex");
                    }
                    if(pool[cp.descriptorIndex].tag != UTF8) {
                        throw new ClassFormatError("invalid descriptorIndex");
                    }
                    break;
                case METHOD_HANDLE:
                    validateMethodHandle(cp, majorVersion);
                    break;
                case METHOD_TYPE:
                    if(pool[cp.descriptorIndex].tag != UTF8) {
                        throw new ClassFormatError("invalid descriptorIndex");
                    }
                    break;
                case DYNAMIC:
                case INVOKE_DYNAMIC:
                    if(pool[cp.nameAndTypeIndex].tag != NAME_AND_TYPE) {
                        throw new ClassFormatError("invalid nameAndTypeIndex");
                    }
                    break;
            }
        }
    }

    private static void validateMethodHandle(CPInfo cp, int majorVersion) {
        switch (cp.referenceKind) {
            case REF_getField:
            case REF_getStatic:
            case REF_putField:
            case REF_putStatic:
                if(cp.pool[cp.referenceIndex].tag != FIELD_REF) {
                    throw new ClassFormatError("invalid field reference");
                }
                break;
            case REF_invokeVirtual:
            case REF_newInvokeSpecial:
                if(cp.pool[cp.referenceIndex].tag != METHOD_REF) {
                    throw new ClassFormatError("invalid method reference");
                }
                break;
            case REF_invokeStatic:
            case REF_invokeSpecial:
                if (majorVersion < 52) {
                    if(cp.pool[cp.referenceIndex].tag != METHOD_REF) {
                        throw new ClassFormatError("invalid method reference");
                    }
                } else {
                    int tag = cp.pool[cp.referenceIndex].tag;
                    if(tag != METHOD_REF && tag != INTERFACE_METHOD_REF) {
                        throw new ClassFormatError("invalid method or interface-method reference");
                    }
                }
                break;
            case REF_invokeInterface:
                if(cp.pool[cp.referenceIndex].tag != INTERFACE_METHOD_REF) {
                    throw new ClassFormatError("invalid interface-method reference");
                }
                break;
            default:
                throw new ClassFormatError("invalid reference kind");
        }
        switch (cp.referenceKind) {
            case REF_invokeVirtual:
            case REF_invokeStatic:
            case REF_invokeSpecial:
            case REF_newInvokeSpecial:
            case REF_invokeInterface:
                CPInfo ref = cp.pool[cp.referenceIndex];
                ref = cp.pool[ref.nameAndTypeIndex];
                ref = cp.pool[ref.nameIndex];
                String name = ref.stringBytes;
                if (cp.referenceKind == REF_newInvokeSpecial) {
                    if(!"<init>".equals(name)) {
                        throw new ClassFormatError("REF_newInvokeSpecial is applicable to constructors only");
                    }
                } else {
                    if("<init>".equals(name) || "<clinit>".equals(name)) {
                        throw new ClassFormatError("constructors or static initializer cannot have reference kind " + cp.referenceKind);
                    }
                }
        }
    }

    @Override
    public String toString() {
        switch (tag) {
            case UTF8:
                return stringBytes;
            case INTEGER:
                return String.valueOf(intBytes);
            case FLOAT:
                return String.valueOf(floatBytes);
            case LONG:
                return String.valueOf(longBytes);
            case DOUBLE:
                return String.valueOf(doubleBytes);
            case STRING:
                return String.valueOf(stringIndex);
            case CLASS:
            case MODULE:
            case PACKAGE:
                return pool[nameIndex].toString();
            case FIELD_REF:
            case METHOD_REF:
            case INTERFACE_METHOD_REF:
                String cls = pool[classIndex].toString();
                String nameAndType = pool[nameAndTypeIndex].toString();
                return cls + nameAndType;
            case NAME_AND_TYPE:
                String name = pool[nameIndex].toString();
                String description = pool[descriptorIndex].toString();
                return Utils.getSignature(name, description);
            case METHOD_HANDLE:
                return pool[referenceIndex].toString();
            case METHOD_TYPE:
                return pool[descriptorIndex].toString();
            case DYNAMIC:
            case INVOKE_DYNAMIC:
                String bootstrapMethodAttribute = pool[bootstrapMethodAttrIndex].toString();
                nameAndType = pool[nameAndTypeIndex].toString();
                return bootstrapMethodAttribute + nameAndType;
            default:
                return String.valueOf(tag);
        }
    }
}
