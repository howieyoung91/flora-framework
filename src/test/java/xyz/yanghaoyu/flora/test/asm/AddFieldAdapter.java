package xyz.yanghaoyu.flora.test.asm;

import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
import com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor;
import com.sun.xml.internal.ws.org.objectweb.asm.FieldVisitor;

public class AddFieldAdapter extends ClassAdapter {
    private int accessModifier;
    private String name;
    private String desc;
    private boolean isFieldPresent;

    public AddFieldAdapter(ClassVisitor cv, int accessModifier, String name, String desc) {
        super(cv);
        this.accessModifier = accessModifier;
        this.name = name;
        this.desc = desc;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        if (name.equals(this.name)) {
            isFieldPresent = true;
        }
        return cv.visitField(access, name, desc, signature, value);
    }

    @Override
    public void visitEnd() {
        if (!isFieldPresent) {
            // 若属性不存在则写入
            FieldVisitor fv = cv.visitField(accessModifier, name, desc, null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        cv.visitEnd();
    }
}
