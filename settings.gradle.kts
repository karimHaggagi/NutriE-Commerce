rootProject.name = "ECommerce"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

include(":composeApp")
include(":feature:auth")
include(":core:designsystem")
include(":core:model")
include(":core:data")
include(":core:di")
include(":core:domain")
include(":core:datasource:network")
include(":feature:home")
include(":feature:profile")
include(":feature:adminPanel")
include(":feature:manageProduct")
include(":feature:productOverview")
include(":feature:details")
include(":feature:cart")
include(":feature:category")
include(":feature:categoryProducts")
include(":feature:checkout")
include(":feature:paymentStatus")
include(":core:datasource:local")

include(":core:navigation")
