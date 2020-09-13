package com.digitaldetox.aww.di

import com.digitaldetox.aww.di.auth.AuthComponent
import com.digitaldetox.aww.di.main.MainComponent
import dagger.Module

@Module(
    subcomponents = [
        AuthComponent::class,
        MainComponent::class
    ]
)
class SubComponentsModule