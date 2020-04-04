package com.hurryyu.asm_gradle_plugin

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter

public class LifecycleTransform extends Transform {

    @Override
    String getName() {
        return "LifecycleTransform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.PROJECT_ONLY
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def outputProvider = transformInvocation.outputProvider
        transformInvocation.inputs.each {
            input ->
                input.directoryInputs.each {
                    DirectoryInput directoryInput ->
                        File dir = directoryInput.file
                        if (dir) {
                            dir.traverse(type: FileType.FILES, nameFilter: ~/.*\.class/) {
                                File file ->
                                    ClassReader classReader = new ClassReader(file.bytes)
                                    ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS)
                                    ClassVisitor classVisitor = new LifecycleClassVisitor(classWriter)
                                    classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES)
                                    byte[] bytes = classWriter.toByteArray()
                                    FileOutputStream fos = new FileOutputStream(file.path)
                                    fos.write(bytes)
                                    fos.close()
                            }
                        }
                        File dest = outputProvider.getContentLocation(directoryInput.name,
                                directoryInput.contentTypes, directoryInput.scopes, Format.DIRECTORY)
                        FileUtils.copyDirectory(directoryInput.file, dest)
                }
        }
    }
}