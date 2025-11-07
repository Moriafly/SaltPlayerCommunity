/*
 * Salt Player Community
 * Copyright (C) 2025 Moriafly and Contributions
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301
 * USA
 */

@file:Suppress("unused")

package com.moriafly.sp.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import com.moriafly.sp.player.internal.InternalCommand

@UnstableSpPlayerApi
class SaltMediaPlayer(
    private val context: Context
) : SaltPlayer() {
    private lateinit var mediaPlayer: MediaPlayer

    override fun getIsPlaying(): Boolean = mediaPlayer.isPlaying

    override fun getDuration(): Long = mediaPlayer.duration.toLong()

    override fun getPosition(): Long = mediaPlayer.currentPosition.toLong()

    override suspend fun processInit() {
        mediaPlayer =
            MediaPlayer().apply {
                setOnCompletionListener {
                    sendCommand(InternalCommand.OnEnded)
                }
            }
    }

    override suspend fun processLoad(mediaItem: Any?) {
        mediaPlayer.reset()

        when (mediaItem) {
            is Uri -> mediaPlayer.setDataSource(context, mediaItem)
            else -> throw UnSupportedMediaItemException()
        }
    }

    /**
     * **Thread Unsafe**
     */
    override suspend fun processPrepare() {
        mediaPlayer.prepare()
    }

    override suspend fun processPrepareCompleted() {
    }

    /**
     * **Thread Unsafe**
     */
    override suspend fun processPrepareCanceled(targetMediaItem: Any?) {
    }

    override suspend fun processPlay() {
        mediaPlayer.start()
    }

    override suspend fun processPause() {
        mediaPlayer.pause()
    }

    override suspend fun processOnLoop() {
        mediaPlayer.seekTo(0)
    }

    override suspend fun processOnGapless() {
        next()
    }

    override suspend fun processStop() {
        mediaPlayer.stop()
    }

    override suspend fun processRelease() {
        super.processRelease()
        mediaPlayer.release()
    }

    override suspend fun processSeekTo(position: Long) {
        mediaPlayer.seekTo(position.toInt())
    }

    override suspend fun processSeekToCompleted() {
    }

    override suspend fun processSetConfig(config: Config) {
    }

    override suspend fun processCustomCommand(command: Command) {
    }
}
