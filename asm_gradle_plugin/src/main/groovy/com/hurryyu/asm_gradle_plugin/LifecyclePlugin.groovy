package com.hurryyu.asm_gradle_plugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class LifecyclePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        LifecycleTransform lifecycleTransform = new LifecycleTransform()
        android.registerTransform(lifecycleTransform)
    }
}