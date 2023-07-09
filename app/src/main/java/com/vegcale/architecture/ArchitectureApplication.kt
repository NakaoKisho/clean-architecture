package com.vegcale.architecture

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * @HiltAndroidApp でアノテーションをすることで、Hiltのコード生成を可能にします。
 * Application オブジェクトのライフサイクルにアタッチされ、依存関係を提供します。
 */
@HiltAndroidApp
class ArchitectureApplication : Application()