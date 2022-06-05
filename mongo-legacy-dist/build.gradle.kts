plugins {
    id("storage.publishing-conventions")
}

dependencies {
    api(project(":api-codec"))
    api(libs.mongo.driver)

    testImplementation(project(":mongo-legacy-dist"))
}