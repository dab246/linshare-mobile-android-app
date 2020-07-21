/*
 * LinShare is an open source filesharing software, part of the LinPKI software
 * suite, developed by Linagora.
 *
 * Copyright (C) 2020 LINAGORA
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Affero General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version,
 * provided you comply with the Additional Terms applicable for LinShare software by
 * Linagora pursuant to Section 7 of the GNU Affero General Public License,
 * subsections (b), (c), and (e), pursuant to which you must notably (i) retain the
 * display in the interface of the “LinShare™” trademark/logo, the "Libre & Free" mention,
 * the words “You are using the Free and Open Source version of LinShare™, powered by
 * Linagora © 2009–2020. Contribute to Linshare R&D by subscribing to an Enterprise
 * offer!”. You must also retain the latter notice in all asynchronous messages such as
 * e-mails sent with the Program, (ii) retain all hypertext links between LinShare and
 * http://www.linshare.org, between linagora.com and Linagora, and (iii) refrain from
 * infringing Linagora intellectual property rights over its trademarks and commercial
 * brands. Other Additional Terms apply, see
 * <http://www.linshare.org/licenses/LinShare-License_AfferoGPL-v3.pdf>
 * for more details.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for
 * more details.
 * You should have received a copy of the GNU Affero General Public License and its
 * applicable Additional Terms for LinShare along with this program. If not, see
 * <http://www.gnu.org/licenses/> for the GNU Affero General Public License version
 *  3 and <http://www.linshare.org/licenses/LinShare-License_AfferoGPL-v3.pdf> for
 *  the Additional Terms applicable to LinShare software.
 */

package com.linagora.android.linshare.data.network.handler

import com.linagora.android.linshare.data.network.parseLinShareErrorResponse
import com.linagora.android.linshare.domain.usecases.upload.UploadException
import com.linagora.android.linshare.domain.utils.ErrorResponseConstant.CANCEL_UPLOAD_WORKER
import com.linagora.android.linshare.domain.utils.ErrorResponseConstant.INTERNET_NOT_AVAILABLE
import com.linagora.android.linshare.domain.utils.ErrorResponseConstant.UNKNOWN_RESPONSE
import com.linagora.android.linshare.domain.utils.OnCatch
import org.slf4j.LoggerFactory
import retrofit2.HttpException
import retrofit2.Retrofit
import java.net.SocketException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UploadNetworkRequestHandler @Inject constructor(
    private val retrofit: Retrofit
) : OnCatch {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(UploadNetworkRequestHandler::class.java)
    }

    override fun invoke(throwable: Throwable) {
        LOGGER.error("invoke(): ${throwable.message} - ${throwable.printStackTrace()}")
        when (throwable) {
            is HttpException -> reactToHttpErrorResponse(throwable)
            is SocketException -> throw UploadException(INTERNET_NOT_AVAILABLE)
            is UnknownHostException -> throw UploadException(INTERNET_NOT_AVAILABLE)
            is CancellationException -> throw UploadException(CANCEL_UPLOAD_WORKER)
            else -> throw UploadException(UNKNOWN_RESPONSE)
        }
    }

    private fun reactToHttpErrorResponse(httpException: HttpException) {
        val errorResponse = retrofit.parseLinShareErrorResponse(httpException)
        throw UploadException(errorResponse)
    }
}
