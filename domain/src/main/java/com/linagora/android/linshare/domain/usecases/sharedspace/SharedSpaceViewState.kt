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

package com.linagora.android.linshare.domain.usecases.sharedspace

import com.linagora.android.linshare.domain.model.OperatorType
import com.linagora.android.linshare.domain.model.order.OrderListConfigurationType
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpace
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpaceId
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpaceNodeNested
import com.linagora.android.linshare.domain.model.workgroup.NewNameRequest
import com.linagora.android.linshare.domain.usecases.utils.Failure
import com.linagora.android.linshare.domain.usecases.utils.Failure.FeatureFailure
import com.linagora.android.linshare.domain.usecases.utils.Success

data class SharedSpaceViewState(val sharedSpace: List<SharedSpaceNodeNested>) : Success.ViewState()
data class SharedSpaceFailure(val throwable: Throwable) : FeatureFailure()
data class SearchSharedSpaceViewState(val sharedSpace: List<SharedSpaceNodeNested>) : Success.ViewState()
object EmptySharedSpaceState : Failure.FeatureFailure()
data class SharedSpaceItemClick(val sharedSpaceNodeNested: SharedSpaceNodeNested) : Success.OnlineViewEvent(OperatorType.OnItemClick)
data class SharedSpaceContextMenuClick(val sharedSpaceNodeNested: SharedSpaceNodeNested) : Success.OfflineViewEvent(OperatorType.OpenContextMenu)
data class GetSharedSpaceSuccess(val sharedSpace: SharedSpace) : Success.ViewState()
data class GetSharedSpaceFailed(val throwable: Throwable) : FeatureFailure()
object NoResultsSearchSharedSpace : Failure.FeatureFailure()
object SearchSharedSpaceInitial : Success.ViewState()
data class DetailsSharedSpaceItem(val sharedSpaceNodeNested: SharedSpaceNodeNested) : Success.OnlineViewEvent(OperatorType.ViewDetails)
data class OpenAddMembers(val sharedSpaceId: SharedSpaceId) : Success.ViewEvent()
data class CreateWorkGroupSuccess(val sharedSpace: SharedSpace) : Success.ViewState()
data class CreateWorkGroupFailed(val throwable: Throwable) : FeatureFailure()
object CreateWorkGroupButtonBottomBarClick : Success.OnlineViewEvent(OperatorType.CreateWorkGroup)
data class CreateWorkGroupViewState(val nameWorkGroup: NewNameRequest) : Success.ViewEvent()
object BlankNameError : Failure.FeatureFailure()
object DuplicatedNameError : Failure.FeatureFailure()
data class NotDuplicatedName(val verifiedName: NewNameRequest) : Success.ViewState()
object NameContainSpecialCharacter : Failure.FeatureFailure()
data class ValidName(val nameWorkGroup: String) : Success.ViewState()
data class DeletedSharedSpaceSuccess(val sharedSpace: SharedSpace) : Success.ViewState()
data class DeleteSharedSpaceFailure(val throwable: Throwable) : FeatureFailure()
data class DeleteSharedSpaceClick(val sharedSpaceNodeNested: SharedSpaceNodeNested) : Success.OnlineViewEvent(OperatorType.DeleteSharedSpace)
data class OnShowConfirmDeleteSharedSpaceClick(val sharedSpaceNodeNested: SharedSpaceNodeNested) : Success.OfflineViewEvent(OperatorType.ShowConfirmDialogClick)
object OpenOrderByDialog : Success.ViewEvent()
data class OnOrderByRowItemClick(val orderListConfigurationType: OrderListConfigurationType) : Success.OfflineViewEvent(OperatorType.OrderBy)
