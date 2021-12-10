package xyz.yanghaoyu.flora.test.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassReader;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;
import xyz.yanghaoyu.flora.test.testCglib.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;

public class AddField {
    private Class<?> clazz;
    private ClassReader cr;
    private ClassWriter cw;
    private ClassAdapter ca;
    private File classFile;

    private final static String CLASS_FILE_SUFFIX = ".class";

    public AddField(Class<?> clazz) {
        this.clazz = clazz;
    }

    public void addPublicField(String fieldName, String fieldDesc) {
        if (cr == null) {
            try {
                cr = new ClassReader(clazz.getCanonicalName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        }
        if (ca == null) {
            ca = new AddFieldAdapter(cw, Opcodes.ACC_PUBLIC, fieldName, fieldDesc);
        } else {
            ca = new AddFieldAdapter(ca, Opcodes.ACC_PUBLIC, fieldName, fieldDesc);
        }
    }

    public void writeByteCode() {
        cr.accept(ca, ClassReader.SKIP_DEBUG);
        byte[] bys = cw.toByteArray();
        OutputStream os = null;
        try {
            os = new FileOutputStream(getFile());

            os.write(bys);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private File getFile() {
        if (classFile == null) {
            StringBuffer sb = new StringBuffer();
            sb.append(clazz.getResource("/"))
                    .append(clazz.getCanonicalName().replace(".", File.separator))
                    .append(CLASS_FILE_SUFFIX);
            classFile = new File(sb.substring(6));
        }
        return classFile;
    }




    public static void main(String[] args) {
        // 为 Student 添加字段
        AddField add = new AddField(Student.class);
        // 添加一个名为 address，类型为 java.lang.String 的 public 字段
        // add.addPublicField("address", "Ljava/lang/String;");
        // 再增加一个名为 tel，类型为 int 的 public 方法
        // add.addPublicField("tel", "I");
        // 重新生成 .class 文件
        // add.writeByteCode();
        AddField adder = new AddField(Target.class);
        adder.addPublicField("test", "Ljava/lang/String;");
        adder.writeByteCode();
        Field[] declaredFields = Target.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            System.out.println(declaredField.getName());
        }
    }
}
