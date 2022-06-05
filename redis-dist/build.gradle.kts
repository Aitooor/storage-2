plugins {
    id("storage.publishing-conventions")
}

dependencies {
    api(project(":api"))
    api(libs.jedis)
}