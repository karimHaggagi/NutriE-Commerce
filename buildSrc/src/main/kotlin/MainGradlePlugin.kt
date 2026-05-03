import org.gradle.api.Plugin
import org.gradle.api.Project

abstract class MainGradlePlugin: Plugin<Project> {
    abstract fun applyPlugin(project: Project)

    override fun apply(project: Project) {
        applyPlugin(project)
    }

//    private fun Project.android():LibraryExtension {
//
//    }
}
