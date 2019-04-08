package org.classinfo.test;

import org.classinfo.ClassInfo;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.lang.module.ModuleReader;
import java.lang.module.ResolvedModule;
import java.util.AbstractCollection;

public class ClassTest {

    @Test
    void testClass() throws IOException {
    	ClassInfo classInfo = new ClassInfo(AbstractCollection.class);
        classInfo.validate();
    	System.out.println(classInfo);
    }

    @Test
    void testAllClasses() throws IOException {
        for (ResolvedModule module : ModuleLayer.boot().configuration().modules()) {
            try (ModuleReader reader = module.reference().open()) {
                reader.list().filter(c -> c.endsWith(".class")).forEach(c -> {
                    try (InputStream in = getClass().getResourceAsStream('/' + c)) {
                        ClassInfo classInfo = new ClassInfo(in);
                        System.out.println(classInfo);
                    } catch (IOException | ClassFormatError e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

}
