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

package com.linagora.android.linshare.view.sharedspace.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.linagora.android.linshare.R
import com.linagora.android.linshare.databinding.FragmentSharedSpaceMemberBinding
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpace
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpaceId
import com.linagora.android.linshare.domain.model.sharedspace.SharedSpaceRole
import com.linagora.android.linshare.domain.model.sharedspace.member.SharedSpaceMember
import com.linagora.android.linshare.domain.usecases.sharedspace.OpenAddMembers
import com.linagora.android.linshare.domain.usecases.sharedspace.role.OnSelectRoleClickForUpdate
import com.linagora.android.linshare.domain.usecases.utils.Success
import com.linagora.android.linshare.model.parcelable.toParcelable
import com.linagora.android.linshare.util.dismissDialogFragmentByTag
import com.linagora.android.linshare.util.filterNetworkViewEvent
import com.linagora.android.linshare.util.getParentViewModel
import com.linagora.android.linshare.util.getViewModel
import com.linagora.android.linshare.view.dialog.SelectRoleForUpdateDialog
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class SharedSpaceMembersFragment(private val sharedSpace: SharedSpace) : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var sharedSpaceDetailsViewModel: SharedSpaceDetailsViewModel

    private lateinit var sharedSpaceMemberViewModel: SharedSpaceMemberViewModel

    private lateinit var binding: FragmentSharedSpaceMemberBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSharedSpaceMemberBinding.inflate(inflater, container, false)
            .apply { lifecycleOwner = viewLifecycleOwner }
        initViewModel(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSwipeRefreshLayout()
        sharedSpaceMemberViewModel.initData(sharedSpace.sharedSpaceId)
    }

    private fun initViewModel(binding: FragmentSharedSpaceMemberBinding) {
        sharedSpaceDetailsViewModel = getParentViewModel(viewModelFactory)
        sharedSpaceMemberViewModel = getViewModel(viewModelFactory)
        binding.viewModel = sharedSpaceMemberViewModel
        binding.ownRoleName = sharedSpace.role.name
        observeViewState()
    }

    private fun observeViewState() {
        sharedSpaceDetailsViewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            state.map { success -> when (success) {
                is Success.ViewEvent -> reactToViewEvent(success)
            } }
        })

        sharedSpaceMemberViewModel.viewState.observe(viewLifecycleOwner, Observer { state ->
            state.map { success -> when (success) {
                is Success.ViewEvent -> reactToViewEventMemberFragment(success)
            } }
        })
    }

    private fun reactToViewEvent(viewEvent: Success.ViewEvent) {
        when (viewEvent) {
            is OpenAddMembers -> navigateToAddMembersFragment(viewEvent.sharedSpaceId)
        }
        sharedSpaceDetailsViewModel.dispatchResetState()
    }

    private fun handleViewEvent(viewEvent: Success.ViewEvent) {
        when (viewEvent) {
            is OnSelectRoleClickForUpdate -> showSelectRoleForUpdateDialog(viewEvent.lastSelectedRole, viewEvent.sharedSpaceMember)
        }
        sharedSpaceMemberViewModel.dispatchResetState()
    }

    private fun reactToViewEventMemberFragment(viewEvent: Success.ViewEvent) {
        when (val filteredViewEvent = viewEvent.filterNetworkViewEvent(sharedSpaceMemberViewModel.internetAvailable.value)) {
            is Success.CancelViewEvent -> {}
            else -> handleViewEvent(filteredViewEvent)
        }
    }

    private fun dismissSelectRoleForUpdateDialog() {
        childFragmentManager.dismissDialogFragmentByTag(SelectRoleForUpdateDialog.TAG)
    }

    private fun showSelectRoleForUpdateDialog(
        lastSelectedRole: SharedSpaceRole,
        sharedSpaceMember: SharedSpaceMember
    ) {
        dismissSelectRoleForUpdateDialog()
        sharedSpaceMemberViewModel.listSharedSpaceRoles.value?.let {
            SelectRoleForUpdateDialog(
                it,
                lastSelectedRole,
                sharedSpaceMember,
                sharedSpaceMemberViewModel.onSelectRoleForUpdateBehavior
            ).show(childFragmentManager, SelectRoleForUpdateDialog.TAG)
        }
    }

    private fun setUpSwipeRefreshLayout() {
        binding.swipeLayoutMember.setColorSchemeResources(R.color.colorPrimary)
    }

    private fun navigateToAddMembersFragment(sharedSpaceId: SharedSpaceId) {
        val action = SharedSpaceDetailsFragmentDirections
            .actionNavigationSharedSpaceToSharedSpaceAddMemberFragment(sharedSpaceId.toParcelable())
        findNavController().navigate(action)
    }
}
