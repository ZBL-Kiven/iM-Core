package com.zj.preview.mod

data class DownloadInfo(var cur: Long, var total: Long, val downloadId: Int, var downloading: Boolean = false)