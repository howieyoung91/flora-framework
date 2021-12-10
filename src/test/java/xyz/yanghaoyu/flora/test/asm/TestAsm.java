package xyz.yanghaoyu.flora.test.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassReader;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
import org.junit.Test;

import java.io.IOException;

/**
 * @author <a href="https://yanghaoyu.xyz">Howie Young</a><i>on 2021/12/9 13:50<i/>
 * @version 1.0
 */


public class TestAsm {
    public TestAsm() {
    }

    @Test
    public void t() {
        try {
            ClassReader cr = new ClassReader(Student.class.getCanonicalName());
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            AddSecurityCheckClassAdapter ca = new AddSecurityCheckClassAdapter(cw);
            // ClassAdapter ca = new AddFieldAdapter(cw, Opcodes.ACC_PUBLIC, "fieldName", "fieldDesc");
            cr.accept(ca, ClassReader.SKIP_DEBUG);
            byte[] data = cw.toByteArray();
            new AccountGeneratorClassLoader().defineClassFromClassFile("Account$EnhancedByASM", data);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
