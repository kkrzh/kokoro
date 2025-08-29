package cn.xj.kokoro.mobile.album

import android.util.Log

data class AlbumConfig(val folderName: String, val fileProviderAuthorities: String){
    // 构建器模式确保配置不可变
    class Builder {
        var folderName: String = ""
        var fileProviderAuthorities: String = ""

         fun build(): AlbumConfig {
            require(folderName.isNotBlank()) { "Folder name must not be blank" }
            require(fileProviderAuthorities.isNotBlank()) { "File provider authorities must not be blank" }
            return AlbumConfig(folderName, fileProviderAuthorities)
        }
    }
}


object ConfigManager {
    const val TAG = "kokoro.album"
    private var config: AlbumConfig? = null
    // 初始化配置
    fun initialize(config: AlbumConfig) {
        require(this.config == null) { "AlbumConfig has already been initialized" }
        this.config = config

        Log.d(TAG, """
            AlbumConfig Initialized:
            FolderName: ${config.folderName}
            FileProviderAuthorities: ${config.fileProviderAuthorities}
        """.trimIndent())
    }

    // 获取配置（安全访问）
    fun getConfig(): AlbumConfig {
        return config ?: throw IllegalStateException("AlbumConfig not initialized. Call initialize() first.")
    }
}

fun albumConfig(block: AlbumConfig.Builder.()-> Unit){
    val builder = AlbumConfig.Builder().apply(block)
    ConfigManager.initialize(builder.build())
}