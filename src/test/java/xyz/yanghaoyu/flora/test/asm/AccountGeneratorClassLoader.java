package xyz.yanghaoyu.flora.test.asm;

class AccountGeneratorClassLoader extends ClassLoader {
    public Class defineClassFromClassFile(String className, byte[] classFile) throws ClassFormatError {
        return defineClass(className, classFile, 0, classFile.length);
    }
}
