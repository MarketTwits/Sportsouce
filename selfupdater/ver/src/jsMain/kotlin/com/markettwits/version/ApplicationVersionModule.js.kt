package com.markettwits.version

import org.koin.core.module.Module
import org.koin.dsl.module

actual val applicationVersionModule: Module = module {
    single<ApplicationVersionManager> {
       object : ApplicationVersionManager{
           override fun currentDistribution(): ApplicationVersion =
               ApplicationVersion("",null)
       }
    }
}