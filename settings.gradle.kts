pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SiyuCloud"
include(":app")
include(":ui")
include(":ui:tv")
include(":ui:phone")
include(":ui:tablet")
include(":media")
include(":media:video")
include(":media:audio")
include(":media:image")
include(":core")
include(":core:network")
include(":core:storage")
include(":utils")
include(":utils:common")
include(":utils:extensions")
