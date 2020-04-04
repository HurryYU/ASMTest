package com.hurryyu.asm_gradle_plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * ===================================================================
 * Author: HurryYu http://www.hurryyu.com & https://github.com/HurryYU
 * Email: cqbbyzh@gmial.com or 1037914505@qq.com
 * Time: 2020/4/3
 * Version: 1.0
 * Description:
 * ===================================================================
 */
public class LifecycleMethodVisitor extends MethodVisitor {
    private String className;
    private String methodName;

    public LifecycleMethodVisitor(MethodVisitor methodVisitor, String className, String methodName) {
        super(Opcodes.ASM5, methodVisitor);
        this.className = className;
        this.methodName = methodName;
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + " ------> " + methodName);
        mv.visitMethodInsn(
                Opcodes.INVOKESTATIC,
                "android/util/Log",
                "i",
                "(Ljava/lang/String;Ljava/lang/String;)I",
                false
        );
        mv.visitInsn(Opcodes.POP);
    }
}
