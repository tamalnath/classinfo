package org.classinfo;

import java.io.DataInputStream;
import java.io.IOException;

public class AttributeInfo {

    private static final String CONSTANT_VALUE = "ConstantValue";
    private static final String CODE = "Code";
    private static final String STACK_MAP_TABLE = "StackMapTable";
    private static final String EXCEPTIONS = "Exceptions";
    private static final String INNER_CLASSES = "InnerClasses";
    private static final String ENCLOSING_METHOD = "EnclosingMethod";
    private static final String SYNTHETIC = "Synthetic";
    private static final String SIGNATURE = "Signature";
    private static final String SOURCE_FILE = "SourceFile";
    private static final String SOURCE_DEBUG_EXTENSION = "SourceDebugExtension";
    private static final String LINE_NUMBER_TABLE = "LineNumberTable";
    private static final String LOCAL_VARIABLE_TABLE = "LocalVariableTable";
    private static final String LOCAL_VARIABLE_TYPE_TABLE = "LocalVariableTypeTable";
    private static final String DEPRECATED = "Deprecated";
    private static final String RUNTIME_VISIBLE_ANNOTATIONS = "RuntimeVisibleAnnotations";
    private static final String RUNTIME_INVISIBLE_ANNOTATIONS = "RuntimeInvisibleAnnotations";
    private static final String RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS = "RuntimeVisibleParameterAnnotations";
    private static final String RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS = "RuntimeInvisibleParameterAnnotations";
    private static final String RUNTIME_VISIBLE_TYPE_ANNOTATIONS = "RuntimeVisibleTypeAnnotations";
    private static final String RUNTIME_INVISIBLE_TYPE_ANNOTATIONS = "RuntimeInvisibleTypeAnnotations";
    private static final String ANNOTATION_DEFAULT = "AnnotationDefault";
    private static final String BOOTSTRAP_METHODS = "BootstrapMethods";
    private static final String METHOD_PARAMETERS = "MethodParameters";
    private static final String MODULE = "Module";
    private static final String MODULE_PACKAGE = "ModulePackages";
    private static final String MODULE_MAIN_CLASS = "ModuleMainClass";
    private static final String NEST_HOST = "NestHost";
    private static final String NEST_MEMBERS = "NestMembers";

    private int attributeNameIndex;
    private int attributeLength;
    private int constantValueIndex;
    private Code code;
    private StackMapFrame[] stackMapTable;
    private int[] exceptions;
    private InnerClass[] innerClasses;
    private CPInfo[] pool;
    private int enclosingMethodClassIndex;
    private int enclosingMethodMethodIndex;
    private int signatureIndex;
    private int sourceFileIndex;
    private String sourceDebugExtension;
    private LineNumberTable[] lineNumbers;
    private LocalVariableTable[] localVariables;
    private LocalVariableTypeTable[] localVariableTypes;
    private Annotation[] annotations;
    private ElementValue defaultValue;
    private BootstrapMethods[] bootstrapMethods;
    private MethodParameters[] methodParameters;
    private Module module;
    private int[] packageIndex;
    private int mainClassIndex;
    private int hostClassIndex;
    private int[] classes;

    AttributeInfo(DataInputStream stream, CPInfo[] pool) throws IOException {
        this.pool = pool;
        attributeNameIndex = stream.readUnsignedShort();
        String attributeName = pool[attributeNameIndex].stringBytes;
        attributeLength = stream.readInt();
        switch (attributeName) {
            case CONSTANT_VALUE:
                constantValueIndex = stream.readUnsignedShort();
                break;
            case CODE:
                code = new Code(stream, pool);
                break;
            case STACK_MAP_TABLE:
                stackMapTable = new StackMapFrame[stream.readUnsignedShort()];
                for (int i = 0; i < stackMapTable.length; i++) {
                    stackMapTable[i] = new StackMapFrame(stream);
                }
                break;
            case EXCEPTIONS:
                exceptions = new int[stream.readUnsignedShort()];
                for (int i = 0; i < exceptions.length; i++) {
                    exceptions[i] = stream.readUnsignedShort();
                }
                break;
            case INNER_CLASSES:
                innerClasses = new InnerClass[stream.readUnsignedShort()];
                for (int i = 0; i < innerClasses.length; i++) {
                    innerClasses[i] = new InnerClass(stream);
                }
                break;
            case ENCLOSING_METHOD:
                enclosingMethodClassIndex = stream.readUnsignedShort();
                enclosingMethodMethodIndex = stream.readUnsignedShort();
                break;
            case SYNTHETIC:
                break;
            case SIGNATURE:
                signatureIndex = stream.readUnsignedShort();
                break;
            case SOURCE_FILE:
                sourceFileIndex = stream.readUnsignedShort();
                break;
            case SOURCE_DEBUG_EXTENSION:
                sourceDebugExtension = stream.readUTF();
                break;
            case LINE_NUMBER_TABLE:
                lineNumbers = new LineNumberTable[stream.readUnsignedShort()];
                for (int i = 0; i < lineNumbers.length; i++) {
                    lineNumbers[i] = new LineNumberTable(stream);
                }
                break;
            case LOCAL_VARIABLE_TABLE:
                localVariables = new LocalVariableTable[stream.readUnsignedShort()];
                for (int i = 0; i < localVariables.length; i++) {
                    localVariables[i] = new LocalVariableTable(stream);
                }
                break;
            case LOCAL_VARIABLE_TYPE_TABLE:
                localVariableTypes = new LocalVariableTypeTable[stream.readUnsignedShort()];
                for (int i = 0; i < localVariableTypes.length; i++) {
                    localVariableTypes[i] = new LocalVariableTypeTable(stream);
                }
                break;
            case DEPRECATED:
                break;
            case RUNTIME_VISIBLE_ANNOTATIONS:
            case RUNTIME_INVISIBLE_ANNOTATIONS:
            case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
            case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
            case RUNTIME_VISIBLE_TYPE_ANNOTATIONS:
            case RUNTIME_INVISIBLE_TYPE_ANNOTATIONS:
                annotations = new Annotation[stream.readUnsignedShort()];
                for (int i = 0; i < annotations.length; i++) {
                    annotations[i] = new Annotation(stream);
                }
                break;
            case ANNOTATION_DEFAULT:
                defaultValue = new ElementValue(stream);
                break;
            case BOOTSTRAP_METHODS:
                bootstrapMethods = new BootstrapMethods[stream.readUnsignedShort()];
                for (int i = 0; i < bootstrapMethods.length; i++) {
                    bootstrapMethods[i] = new BootstrapMethods(stream);
                }
                break;
            case METHOD_PARAMETERS:
                methodParameters = new MethodParameters[stream.readUnsignedShort()];
                for (int i = 0; i < methodParameters.length; i++) {
                    methodParameters[i] = new MethodParameters(stream);
                }
                break;
            case MODULE:
                module = new Module(stream);
                break;
            case MODULE_PACKAGE:
                packageIndex = new int[stream.readUnsignedShort()];
                for (int i = 0; i < packageIndex.length; i++) {
                    packageIndex[i] = stream.readUnsignedShort();
                }
                break;
            case MODULE_MAIN_CLASS:
                mainClassIndex = stream.readUnsignedShort();
                break;
            case NEST_HOST:
                hostClassIndex = stream.readUnsignedShort();
                break;
            case NEST_MEMBERS:
                classes = new int[stream.readUnsignedShort()];
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = stream.readUnsignedShort();
                }
                break;
            default:
                byte[] info = new byte[attributeLength];
                if (stream.read(info) != attributeLength) {
                    throw new ClassFormatError("Could not read attribute: " + attributeName);
                }
                break;
        }
    }

    @Override
    public String toString() {
        return pool[attributeNameIndex].toString();
    }

    void validate() {
        if (pool[attributeNameIndex].tag != CPInfo.UTF8) {
            throw new ClassFormatError("invalid attribute-name index");
        }
        switch (pool[attributeNameIndex].stringBytes) {
            case CONSTANT_VALUE:
                switch (pool[constantValueIndex].tag) {
                    case CPInfo.INTEGER:
                    case CPInfo.FLOAT:
                    case CPInfo.LONG:
                    case CPInfo.DOUBLE:
                    case CPInfo.STRING:
                        break;
                    default:
                        throw new ClassFormatError("invalid constant-value index");
                }
                break;
            case CODE:
                code.validate();
                break;
            case STACK_MAP_TABLE:
                for (StackMapFrame stackMapFrame : stackMapTable) {
                    stackMapFrame.validate();
                }
                break;
            case EXCEPTIONS:
                for (int exceptionIndex : exceptions) {
                    if (pool[exceptionIndex].tag != CPInfo.CLASS) {
                        throw new ClassFormatError("invalid exception class index");
                    }
                }
                break;
            case INNER_CLASSES:
                for (InnerClass innerClass : innerClasses) {
                    innerClass.validate();
                }
                break;
            case ENCLOSING_METHOD:
                if (pool[enclosingMethodClassIndex].tag != CPInfo.CLASS) {
                    throw new ClassFormatError("invalid enclosing method class index");
                }
                if (enclosingMethodMethodIndex != 0 && pool[enclosingMethodMethodIndex].tag != CPInfo.NAME_AND_TYPE) {
                    throw new ClassFormatError("invalid enclosing method method index");
                }
                break;
            case SYNTHETIC:
                if (attributeLength != 0) {
                    throw new ClassFormatError("invalid length for synthetic attribute: " + attributeLength);
                }
                break;
            case SIGNATURE:
                if (pool[signatureIndex].tag != CPInfo.UTF8) {
                    throw new ClassFormatError("invalid signature");
                }
                break;
            case SOURCE_FILE:
                if (pool[sourceFileIndex].tag != CPInfo.UTF8) {
                    throw new ClassFormatError("invalid source file");
                }
                break;
            case SOURCE_DEBUG_EXTENSION:
                if (sourceDebugExtension == null) {
                    throw new ClassFormatError("invalid source debug extension");
                }
                break;
            case LINE_NUMBER_TABLE:
                // TODO add validations
                break;
            case LOCAL_VARIABLE_TABLE:
                for (LocalVariableTable table : localVariables) {
                    if (pool[table.nameIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("invalid name index for local variable");
                    }
                    if (pool[table.descriptorIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("invalid descriptor index for local variable");
                    }
                }
                break;
            case LOCAL_VARIABLE_TYPE_TABLE:
                for (LocalVariableTypeTable table : localVariableTypes) {
                    if (pool[table.nameIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("invalid name index for local variable type");
                    }
                    if (pool[table.signatureIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("invalid signature index for local variable type");
                    }
                }
                break;
            case DEPRECATED:
                if (attributeLength != 0) {
                    throw new ClassFormatError("non-zero length of Deprecated attribute");
                }
                break;
            case RUNTIME_VISIBLE_ANNOTATIONS:
            case RUNTIME_INVISIBLE_ANNOTATIONS:
            case RUNTIME_VISIBLE_PARAMETER_ANNOTATIONS:
            case RUNTIME_INVISIBLE_PARAMETER_ANNOTATIONS:
            case RUNTIME_VISIBLE_TYPE_ANNOTATIONS:
            case RUNTIME_INVISIBLE_TYPE_ANNOTATIONS:
                for (Annotation annotation : annotations) {
                    annotation.validate();
                }
                break;
            case ANNOTATION_DEFAULT:
                defaultValue.validate();
                break;
            case BOOTSTRAP_METHODS:
                for (BootstrapMethods bootstrapMethod : bootstrapMethods) {
                    if (pool[bootstrapMethod.bootstrapMethodRef].tag != CPInfo.METHOD_HANDLE) {
                        throw new ClassFormatError("invalid bootstrap method reference");
                    }
                    for (int index : bootstrapMethod.bootstrapArguments) {
                        if (pool[index] == null) {
                            throw new ClassFormatError("invalid bootstrap arguments");
                        }
                    }
                }
                break;
            case METHOD_PARAMETERS:
                for (MethodParameters methodParameter : methodParameters) {
                    if (methodParameter.nameIndex != 0 && pool[methodParameter.nameIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("invalid name index");
                    }
                }
                break;
            case MODULE:
                module.validate();
                break;
            case MODULE_PACKAGE:
                for (int index : packageIndex) {
                    if (pool[index].tag != CPInfo.PACKAGE) {
                        throw new ClassFormatError("invalid package index");
                    }
                }
                break;
            case MODULE_MAIN_CLASS:
                if (pool[mainClassIndex].tag != CPInfo.CLASS) {
                    throw new ClassFormatError("invalid main class index");
                }
                break;
            case NEST_HOST:
                if (pool[hostClassIndex].tag != CPInfo.CLASS) {
                    throw new ClassFormatError("invalid main class index");
                }
                break;
            case NEST_MEMBERS:
                for (int index : classes) {
                    if (pool[index].tag != CPInfo.CLASS) {
                        throw new ClassFormatError("invalid nest member class index");
                    }
                }
                break;
        }
    }

    class StackMapFrame {

        private static final int SAME_MIN = 0;
        private static final int SAME_MAX = 63;
        private static final int SAME_LOCALS_1_STACK_ITEM_MIN = 64;
        private static final int SAME_LOCALS_1_STACK_ITEM_MAX = 127;
        private static final int SAME_LOCALS_1_STACK_ITEM_EXTENDED = 247;
        private static final int CHOP_MIN = 248;
        private static final int CHOP_MAX = 250;
        private static final int SAME_FRAME_EXTENDED = 251;
        private static final int APPEND_MIN = 252;
        private static final int APPEND_MAX = 254;
        private static final int FULL_FRAME = 255;

        private int frameType;
        private int offsetDelta;
        private VerificationTypeInfo[] stack;
        private VerificationTypeInfo[] locals;

        StackMapFrame(DataInputStream stream) throws IOException {
            frameType = stream.readUnsignedByte();
            if (frameType >= SAME_MIN && frameType <= SAME_MAX) {
                offsetDelta = frameType;
                stack = new VerificationTypeInfo[0];
            } else if (frameType >= SAME_LOCALS_1_STACK_ITEM_MIN && frameType <= SAME_LOCALS_1_STACK_ITEM_MAX) {
                offsetDelta = frameType - 64;
                stack = new VerificationTypeInfo[1];
                stack[0] = new VerificationTypeInfo(stream);
            } else if (frameType == SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
                offsetDelta = stream.readUnsignedShort();
                stack = new VerificationTypeInfo[1];
                stack[0] = new VerificationTypeInfo(stream);
            } else if ((frameType >= CHOP_MIN && frameType <= CHOP_MAX) || frameType == SAME_FRAME_EXTENDED) {
                offsetDelta = stream.readUnsignedShort();
                stack = new VerificationTypeInfo[0];
            } else if (frameType >= APPEND_MIN && frameType <= APPEND_MAX) {
                offsetDelta = stream.readUnsignedShort();
                locals = new VerificationTypeInfo[frameType - APPEND_MIN + 1];
                for (int i = 0; i < locals.length; i++) {
                    locals[i] = new VerificationTypeInfo(stream);
                }
                stack = new VerificationTypeInfo[0];
            } else if (frameType == FULL_FRAME) {
                offsetDelta = stream.readUnsignedShort();
                locals = new VerificationTypeInfo[stream.readUnsignedShort()];
                for (int i = 0; i < locals.length; i++) {
                    locals[i] = new VerificationTypeInfo(stream);
                }
                stack = new VerificationTypeInfo[stream.readUnsignedShort()];
                for (int i = 0; i < stack.length; i++) {
                    stack[i] = new VerificationTypeInfo(stream);
                }
            } else {
                throw new ClassFormatError("invalid stack_map_frame");
            }
        }

        void validate() {
            for (VerificationTypeInfo verificationTypeInfo : stack) {
                verificationTypeInfo.validate();
            }
            if (locals != null) {
                for (VerificationTypeInfo verificationTypeInfo : locals) {
                    verificationTypeInfo.validate();
                }
            }
        }

        class VerificationTypeInfo {
            private static final int ITEM_Top = 0;
            private static final int ITEM_Integer = 1;
            private static final int ITEM_Float = 2;
            private static final int ITEM_Double = 3;
            private static final int ITEM_Long = 4;
            private static final int ITEM_Null = 5;
            private static final int ITEM_UninitializedThis = 6;
            private static final int ITEM_Object = 7;
            private static final int ITEM_Uninitialized = 8;

            private int tag;
            private int cpoolIndex;
            private int offset;

            VerificationTypeInfo(DataInputStream stream) throws IOException {
                tag = stream.readUnsignedByte();
                switch (tag) {
                    case ITEM_Top:
                    case ITEM_Integer:
                    case ITEM_Float:
                    case ITEM_Double:
                    case ITEM_Long:
                    case ITEM_Null:
                    case ITEM_UninitializedThis:
                        break;
                    case ITEM_Object:
                        cpoolIndex = stream.readUnsignedShort();
                        break;
                    case ITEM_Uninitialized:
                        offset = stream.readUnsignedShort();
                        break;
                    default:
                        throw new ClassFormatError("invalid verification_type_info");
                }
            }

            void validate() {
                if (tag == ITEM_Object && pool[cpoolIndex].tag != CPInfo.CLASS) {
                    throw new ClassFormatError("invalid class object index");
                }
            }
        }
    }

    class InnerClass {
        private final int innerClassInfoIndex;
        private final int outerClassInfoIndex;
        private final int innerNameIndex;
        private final int innerClassAccessFlags;

        InnerClass(DataInputStream stream) throws IOException {
            innerClassInfoIndex = stream.readUnsignedShort();
            outerClassInfoIndex = stream.readUnsignedShort();
            innerNameIndex = stream.readUnsignedShort();
            innerClassAccessFlags = stream.readUnsignedShort();
        }

        void validate() {
            if (pool[innerClassInfoIndex].tag != CPInfo.CLASS) {
                throw new ClassFormatError("invalid inner class info index");
            }
            if (outerClassInfoIndex != 0 && pool[outerClassInfoIndex].tag != CPInfo.CLASS) {
                throw new ClassFormatError("invalid outer class info index");
            }
            if (innerNameIndex != 0 && pool[innerNameIndex].tag != CPInfo.UTF8) {
                throw new ClassFormatError("invalid inner name index");
            }
        }
    }

    static class LineNumberTable {
        private final int startPc;
        private final int lineNumber;

        LineNumberTable(DataInputStream stream) throws IOException {
            startPc = stream.readUnsignedShort();
            lineNumber = stream.readUnsignedShort();
        }
    }

    static class LocalVariableTable {
        private final int startPc;
        private final int length;
        private final int nameIndex;
        private final int descriptorIndex;
        private final int index;

        LocalVariableTable(DataInputStream stream) throws IOException {
            startPc = stream.readUnsignedShort();
            length = stream.readUnsignedShort();
            nameIndex = stream.readUnsignedShort();
            descriptorIndex = stream.readUnsignedShort();
            index = stream.readUnsignedShort();
        }
    }

    static class LocalVariableTypeTable {
        private final int startPc;
        private final int length;
        private final int nameIndex;
        private final int signatureIndex;
        private final int index;

        LocalVariableTypeTable(DataInputStream stream) throws IOException {
            startPc = stream.readUnsignedShort();
            length = stream.readUnsignedShort();
            nameIndex = stream.readUnsignedShort();
            signatureIndex = stream.readUnsignedShort();
            index = stream.readUnsignedShort();
        }

    }

    class Annotation {

        private final int typeIndex;
        private final ElementValuePair elementValuePairs[];

        Annotation(DataInputStream stream) throws IOException {
            typeIndex = stream.readUnsignedShort();
            elementValuePairs = new ElementValuePair[stream.readUnsignedShort()];
            for (int i = 0; i < elementValuePairs.length; i++) {
                elementValuePairs[i] = new ElementValuePair(stream);

            }
        }

        void validate() {
            if (pool[typeIndex].tag != CPInfo.UTF8) {
                throw new ClassFormatError("Invalid annotation type index");
            }
            for (ElementValuePair elementValuePair : elementValuePairs) {
                if (pool[elementValuePair.elementNameIndex].tag != CPInfo.UTF8) {
                    throw new ClassFormatError("Invalid element name index");
                }
                elementValuePair.value.validate();
            }
        }
    }

    class ElementValuePair {

        private final int elementNameIndex;
        private final ElementValue value;

        ElementValuePair(DataInputStream stream) throws IOException {
            elementNameIndex = stream.readUnsignedShort();
            value = new ElementValue(stream);
        }

    }

    class ElementValue {
        private int tag;
        private int constantValueIndex;
        private int typeNameIndex;
        private int constNameIndex;
        private int classInfoIndex;
        private Annotation annotationValue;
        private ElementValue[] values;

        ElementValue(DataInputStream stream) throws IOException {
            tag = stream.readUnsignedByte();
            switch (tag) {
                case 'B':
                case 'C':
                case 'D':
                case 'F':
                case 'I':
                case 'J':
                case 'S':
                case 'Z':
                case 's':
                    constantValueIndex = stream.readUnsignedShort();
                    break;
                case 'e':
                    typeNameIndex = stream.readUnsignedShort();
                    constNameIndex = stream.readUnsignedShort();
                    break;
                case 'c':
                    classInfoIndex = stream.readUnsignedShort();
                    break;
                case '@':
                    annotationValue = new Annotation(stream);
                    break;
                case '[':
                    values = new ElementValue[stream.readUnsignedShort()];
                    for (int i = 0; i < values.length; i++) {
                        values[i] = new ElementValue(stream);
                    }
                    break;
                default:
                    throw new IOException("Invalid Tag: " + tag);
            }
        }

        void validate() {
            switch (tag) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    if (pool[constantValueIndex].tag != CPInfo.INTEGER) {
                        throw new ClassFormatError("Expected Integer for element value tag " + tag);
                    }
                    break;
                case 'D':
                    if (pool[constantValueIndex].tag != CPInfo.DOUBLE) {
                        throw new ClassFormatError("Unexpected Double value");
                    }
                    break;
                case 'F':
                    if (pool[constantValueIndex].tag != CPInfo.FLOAT) {
                        throw new ClassFormatError("Unexpected Float value");
                    }
                    break;
                case 'J':
                    if (pool[constantValueIndex].tag != CPInfo.LONG) {
                        throw new ClassFormatError("Unexpected Long value");
                    }
                    break;
                case 's':
                    if (pool[constantValueIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("Unexpected UTF8 value");
                    }
                    break;
                case 'e':
                    if (pool[typeNameIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("Unexpected type name index");
                    }
                    if (pool[constNameIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("Unexpected const name index");
                    }
                    break;
                case 'c':
                    if (pool[classInfoIndex].tag != CPInfo.UTF8) {
                        throw new ClassFormatError("Unexpected class info index");
                    }
                    break;
                case '@':
                    annotationValue.validate();
                    break;
                case '[':
                    for (ElementValue value : values) {
                        value.validate();
                    }
                    break;
            }
        }
    }

    static class BootstrapMethods {

        private int bootstrapMethodRef;
        private int[] bootstrapArguments;

        BootstrapMethods(DataInputStream stream) throws IOException {
            bootstrapMethodRef = stream.readUnsignedShort();
            bootstrapArguments = new int[stream.readUnsignedShort()];
            for (int i = 0; i < bootstrapArguments.length; i++) {
                bootstrapArguments[i] = stream.readUnsignedShort();
            }
        }
    }

    static class MethodParameters {
        private int nameIndex;
        private int accessFlags;

        MethodParameters(DataInputStream stream) throws IOException {
            nameIndex = stream.readUnsignedShort();
            accessFlags = stream.readUnsignedShort();
        }
    }

    class Module {
        private int moduleNameIndex;
        private int moduleFlags;
        private int moduleVersionIndex;
        private Requires[] requires;
        private Exports[] exports;
        private Opens[] opens;
        private int[] usesIndex;
        private Provides[] provides;

        Module(DataInputStream stream) throws IOException {
            moduleNameIndex = stream.readUnsignedShort();
            moduleFlags = stream.readUnsignedShort();
            moduleVersionIndex = stream.readUnsignedShort();
            requires = new Requires[stream.readUnsignedShort()];
            for (int i = 0; i < requires.length; i++) {
                requires[i] =new Requires(stream);
            }
            exports = new Exports[stream.readUnsignedShort()];
            for (int i = 0; i < exports.length; i++) {
                exports[i] = new Exports(stream);

            }
            opens = new Opens[stream.readUnsignedShort()];
            for (int i = 0; i < opens.length; i++) {
                opens[i] = new Opens(stream);
            }
            usesIndex = new int[stream.readUnsignedShort()];
            for (int i = 0; i < usesIndex.length; i++) {
                usesIndex[i] = stream.readUnsignedShort();
            }
            provides = new Provides[stream.readUnsignedShort()];
            for (int i = 0; i < provides.length; i++) {
                provides[i] = new Provides(stream);
            }
        }

        void validate() {
            if (pool[moduleNameIndex].tag != CPInfo.MODULE) {
                throw new ClassFormatError("invalid module name index");
            }
            if (moduleVersionIndex != 0 && pool[moduleVersionIndex].tag != CPInfo.UTF8) {
                throw new ClassFormatError("invalid module version index");
            }
            for (Requires require : requires) {
                if (pool[require.requiresIndex].tag != CPInfo.MODULE) {
                    throw new ClassFormatError("invalid requires index");
                }
                if (require.requiresVersionIndex != 0 && pool[require.requiresVersionIndex].tag != CPInfo.UTF8) {
                    throw new ClassFormatError("invalid requires version index");
                }
            }
            for (Exports export : exports) {
                if (pool[export.exportsIndex].tag != CPInfo.PACKAGE) {
                    throw new ClassFormatError("invalid exports index");
                }
                for (int index : export.exportsToIndex) {
                    if (pool[index].tag != CPInfo.MODULE) {
                        throw new ClassFormatError("invalid exports to index");
                    }
                }
            }
            for (Opens open : opens) {
                if (pool[open.opensIndex].tag != CPInfo.PACKAGE) {
                    throw new ClassFormatError("invalid opens index");
                }
                for (int index : open.opensToIndex) {
                    if (pool[index].tag != CPInfo.MODULE) {
                        throw new ClassFormatError("invalid opens to index");
                    }
                }
            }
            for (int uses : usesIndex) {
                if (pool[uses].tag != CPInfo.CLASS) {
                    throw new ClassFormatError("invalid uses index");
                }
            }
            for (Provides provide : provides) {
                if (pool[provide.providesIndex].tag != CPInfo.CLASS) {
                    throw new ClassFormatError("invalid provides index");
                }
                for (int index : provide.providesWithIndex) {
                    if (pool[index].tag != CPInfo.CLASS) {
                        throw new ClassFormatError("invalid provides with index");
                    }
                }
            }
        }

        class Requires {
            private int requiresIndex;
            private int requiresFlags;
            private int requiresVersionIndex;

            Requires(DataInputStream stream) throws IOException {
                requiresIndex = stream.readUnsignedShort();
                requiresFlags = stream.readUnsignedShort();
                requiresVersionIndex = stream.readUnsignedShort();
            }
        }

        class Exports {
            private int exportsIndex;
            private int exportsFlags;
            private int[] exportsToIndex;

            Exports(DataInputStream stream) throws IOException {
                exportsIndex = stream.readUnsignedShort();
                exportsFlags = stream.readUnsignedShort();
                exportsToIndex = new int[stream.readUnsignedShort()];
                for (int i = 0; i < exportsToIndex.length; i++) {
                    exportsToIndex[i] = stream.readUnsignedShort();
                }
            }
        }

        class Opens {
            private int opensIndex;
            private int opensFlags;
            private int[] opensToIndex;

            Opens(DataInputStream stream) throws IOException {
                opensIndex = stream.readUnsignedShort();
                opensFlags = stream.readUnsignedShort();
                opensToIndex = new int[stream.readUnsignedShort()];
                for (int i = 0; i < opensToIndex.length; i++) {
                    opensToIndex[i] = stream.readUnsignedShort();
                }
            }
        }

        class Provides {
            private int providesIndex;
            private int[] providesWithIndex;

            Provides(DataInputStream stream) throws IOException {
                providesIndex = stream.readUnsignedShort();
                providesWithIndex = new int[stream.readUnsignedShort()];
                for (int i = 0; i < providesWithIndex.length; i++) {
                    providesWithIndex[i] = stream.readUnsignedShort();
                }
            }
        }
    }
}
