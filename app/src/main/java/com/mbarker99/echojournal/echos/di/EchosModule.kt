package com.mbarker99.echojournal.echos.di

import com.mbarker99.echojournal.echos.data.audio.AudioPlayerImpl
import com.mbarker99.echojournal.echos.data.echo.EchoDataSourceImpl
import com.mbarker99.echojournal.echos.data.recording.RecordingStorageImpl
import com.mbarker99.echojournal.echos.data.recording.VoiceRecorderImpl
import com.mbarker99.echojournal.echos.domain.audio.AudioPlayer
import com.mbarker99.echojournal.echos.domain.echo.EchoDataSource
import com.mbarker99.echojournal.echos.domain.recording.RecordingStorage
import com.mbarker99.echojournal.echos.domain.recording.VoiceRecorder
import com.mbarker99.echojournal.echos.presentation.create_echo.CreateEchoViewModel
import com.mbarker99.echojournal.echos.presentation.echos.EchosViewModel
import com.mbarker99.echojournal.echos.presentation.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val echoModule = module {
    singleOf(::VoiceRecorderImpl) bind VoiceRecorder::class
    singleOf(::RecordingStorageImpl) bind RecordingStorage::class
    singleOf(::AudioPlayerImpl) bind AudioPlayer::class
    singleOf(::EchoDataSourceImpl) bind EchoDataSource::class

    viewModelOf(::EchosViewModel)
    viewModelOf(::CreateEchoViewModel)
    viewModelOf(::SettingsViewModel)
}