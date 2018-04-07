*Project Kronslott*
# Kotlin+DropWizard+Dagger JDBI2 configuration
DropWizard+Dagger bundle to setup JDBI version 2 for use in a Kotlin project.

## Usage

### Instructions

1. Add Jdbi2Module as a dependency to you Dagger @Component.
1. Make sure to @Provide DataSourceFactory and Environment from a @Module in your project.

### Example

See IntegrationTest.kt for an example.

### Extras

An extension function for the DBI class is provided: DBI.onDemand() without parameters.
Use it like this in a Dagger @Module:

```kotlin
@Provides
fun provideTestDao(dbi: DBI): TestDao = dbi.onDemand()
```

## Adding this library to your project

Follow the instructions on https://jitpack.io/#se.activout/kronslott-jdbi2.
