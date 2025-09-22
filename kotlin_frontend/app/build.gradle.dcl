androidApplication {
    namespace = "org.example.app"

    dependencies {
        // AndroidX core UI dependencies (explicit versions per rules)
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.fragment:fragment-ktx:1.8.3")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")

        // SQLite dependency as requested (using Android framework SQLite via androidx.sqlite)
        implementation("androidx.sqlite:sqlite:2.4.0")

        // Remove sample deps that are no longer needed
        // implementation("org.apache.commons:commons-text:1.11.0")
        // implementation(project(":utilities"))
    }
}
