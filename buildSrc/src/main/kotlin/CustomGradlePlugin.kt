import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpConventionPlugin : MainGradlePlugin() {
    override fun applyPlugin(project: Project) {
        with(project.pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
            apply("org.jetbrains.kotlin.plugin.serialization")
        }
    }
}
class AndroidAppConventionPlugin : MainGradlePlugin() {
    override fun applyPlugin(project: Project) {
        with(project.pluginManager) {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
        }
    }
}
class GoogleServicesConventionPlugin : MainGradlePlugin() {
    override fun applyPlugin(project: Project) {
        project.pluginManager.apply("com.google.gms.google-services")
    }
}