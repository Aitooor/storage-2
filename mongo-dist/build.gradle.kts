plugins {
    id("storage.publishing-conventions")
}

dependencies {
    api(project(":api"))
    api(libs.mongo.jack)
}