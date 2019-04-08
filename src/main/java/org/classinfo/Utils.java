package org.classinfo;

import java.util.ArrayList;
import java.util.List;

class Utils {

    private static final int ACC_PUBLIC = 0x0001;
    private static final int ACC_PRIVATE = 0x0002;
    private static final int ACC_PROTECTED = 0x0004;
    private static final int ACC_STATIC = 0x0008;
    private static final int ACC_FINAL = 0x0010;
    private static final int ACC_SUPER = 0x0020;
    private static final int ACC_SYNCHRONIZED = 0x0020;
    private static final int ACC_VOLATILE = 0x0040;
    private static final int ACC_BRIDGE = 0x0040;
    private static final int ACC_TRANSIENT = 0x0080;
    private static final int ACC_VARARGS = 0x0080;
    private static final int ACC_NATIVE = 0x0100;
    private static final int ACC_INTERFACE = 0x0200;
    private static final int ACC_ABSTRACT = 0x0400;
    private static final int ACC_STRICT = 0x0800;
    private static final int ACC_SYNTHETIC = 0x01000;
    private static final int ACC_ANNOTATION = 0x02000;
    private static final int ACC_ENUM = 0x04000;
    private static final int ACC_MODULE = 0x08000;

    private static boolean isSet(int flag, int filter) {
        return (flag & filter) == filter;
    }

    static String getClassFlags(int flag) {
        StringBuilder sb = new StringBuilder();
        if (isSet(flag, ACC_PUBLIC)) {
            sb.append(" public");
        }
        if (isSet(flag, ACC_FINAL)) {
            sb.append(" final");
        }
        if (isSet(flag, ACC_SUPER)) {
            sb.append(" super");
        }
        if (isSet(flag, ACC_INTERFACE)) {
            sb.append(" interface");
        }
        if (isSet(flag, ACC_ABSTRACT)) {
            sb.append(" abstract");
        }
        if (isSet(flag, ACC_SYNTHETIC)) {
            sb.append(" synthetic");
        }
        if (isSet(flag, ACC_ANNOTATION)) {
            sb.append(" annotation");
        }
        if (isSet(flag, ACC_ENUM)) {
            sb.append(" enum");
        }
        if (isSet(flag, ACC_MODULE)) {
            sb.append(" module");
        }
        return sb.substring(1);
    }

    static String getFieldFlags(int flag) {
        StringBuilder sb = new StringBuilder();
        if (isSet(flag, ACC_PUBLIC)) {
            sb.append(" public");
        }
        if (isSet(flag, ACC_PRIVATE)) {
            sb.append(" private");
        }
        if (isSet(flag, ACC_PROTECTED)) {
            sb.append(" protected");
        }
        if (isSet(flag, ACC_STATIC)) {
            sb.append(" static");
        }
        if (isSet(flag, ACC_FINAL)) {
            sb.append(" final");
        }
        if (isSet(flag, ACC_VOLATILE)) {
            sb.append(" volatile");
        }
        if (isSet(flag, ACC_TRANSIENT)) {
            sb.append(" transient");
        }
        if (isSet(flag, ACC_ABSTRACT)) {
            sb.append(" abstract");
        }
        if (isSet(flag, ACC_SYNTHETIC)) {
            sb.append(" synthetic");
        }
        if (isSet(flag, ACC_ENUM)) {
            sb.append(" enum");
        }
        return sb.substring(1);
    }

    static String getMethodFlags(int flag) {
        StringBuilder sb = new StringBuilder();
        if (isSet(flag, ACC_PUBLIC)) {
            sb.append(" public");
        }
        if (isSet(flag, ACC_PRIVATE)) {
            sb.append(" private");
        }
        if (isSet(flag, ACC_PROTECTED)) {
            sb.append(" protected");
        }
        if (isSet(flag, ACC_STATIC)) {
            sb.append(" static");
        }
        if (isSet(flag, ACC_FINAL)) {
            sb.append(" final");
        }
        if (isSet(flag, ACC_SYNCHRONIZED)) {
            sb.append(" synchronized");
        }
        if (isSet(flag, ACC_BRIDGE)) {
            sb.append(" bridge");
        }
        if (isSet(flag, ACC_VARARGS)) {
            sb.append(" varargs");
        }
        if (isSet(flag, ACC_NATIVE)) {
            sb.append(" native");
        }
        if (isSet(flag, ACC_ABSTRACT)) {
            sb.append(" abstract");
        }
        if (isSet(flag, ACC_STRICT)) {
            sb.append(" strict");
        }
        if (isSet(flag, ACC_SYNTHETIC)) {
            sb.append(" synthetic");
        }
        return sb.substring(1);
    }

    static String getSignature(String name, String descriptor) {
        if (descriptor.charAt(0) != '(') {
            return expand(descriptor) + ' ' + name;
        }
        int index = descriptor.indexOf(')');
        String parameters = expand(descriptor.substring(1, index));
        String returnValue = expand(descriptor.substring(index + 1));
        return returnValue + ' ' + name + '(' + parameters + ')';

    }

    private static String expand(String descriptor) {
        List<String> list = new ArrayList<>();
        String array = "";
        for (int i = 0; i < descriptor.length(); i++) {
            String param = null;
            char ch = descriptor.charAt(i);
            switch (ch) {
                case '[':
                    array += "[]";
                    break;
                case 'L':
                    int index = descriptor.indexOf(';', i + 1);
                    param = descriptor.substring(i + 1, index).replace('/', '.');
                    i = index;
                    break;
                case 'B':
                    param = "byte";
                    break;
                case 'C':
                    param = "char";
                    break;
                case 'D':
                    param = "double";
                    break;
                case 'F':
                    param = "float";
                    break;
                case 'I':
                    param = "int";
                    break;
                case 'J':
                    param = "long";
                    break;
                case 'S':
                    param = "short";
                    break;
                case 'Z':
                    param = "boolean";
                    break;
                case 'V':
                    param = "void";
                    break;
                default:
                    throw new RuntimeException("Invalid Description: " + ch);
            }
            if (ch != '[') {
                list.add(param + array);
                array = "";
            }
        }
        return String.join(", ", list);
    }

}
