rootProject.name = "emporixapi"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("./libs.versions.toml"))
        }
    }
}
