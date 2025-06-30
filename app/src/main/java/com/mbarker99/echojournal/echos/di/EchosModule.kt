package com.mbarker99.echojournal.echos.di

import com.mbarker99.echojournal.echos.data.recording.VoiceRecorderImpl
import com.mbarker99.echojournal.echos.domain.recording.VoiceRecorder
import com.mbarker99.echojournal.echos.presentation.create_echo.CreateEchoViewModel
import com.mbarker99.echojournal.echos.presentation.echos.EchosViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoModule = module {
    single {
        VoiceRecorderImpl(
            context = androidApplication(),
            applicationScope = get()
        )
    } bind VoiceRecorder::class

   viewModelOf(::EchosViewModel)
   viewModelOf(::CreateEchoViewModel)
}