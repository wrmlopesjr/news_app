# Worldwide News App

A simple app using NewsAPI (https://newsapi.org)

## CI

For every push in the master, a job will run in bitrise. For more information: https://app.bitrise.io/app/9e6b8e00f32ae244

Master: [![Build Status](https://app.bitrise.io/app/9e6b8e00f32ae244/status.svg?token=C9g-V4d20TWL2ibnmnCM6g&branch=master)](https://app.bitrise.io/app/9e6b8e00f32ae244)

## Tools and Frameworks used

- MVVM
- Retrofit
- Koin (DI) 
- RxAndroid
- Picasso (Image loading) 
- Architecture Components (LiveData and ViewModel)
- JUnit
- JaCoCo (code coverage)
- Espresso

## Tests

To run Unit tests with coverage:

    ./gradlew jacocoTestDebugUnitTestReport


To run Instrumentation tests:

    ./gradlew connectedDebugAndroidTest
    
All data for Unit and Instrumentation Tests are mocked with static JSON (check [MockedEndpointService.kt](/news_app/app/src/testCommon/java/dev/wilsonjr/newsapp/base/MockedEndpointService.kt) for implementation)


## Questions

For any questions, send an email to <wrmlopesjr@gmail.com>