package com.linagora.android.linshare.view.upload

import androidx.lifecycle.ViewModel
import com.linagora.android.linshare.inject.annotation.FragmentScoped
import com.linagora.android.linshare.inject.annotation.ViewModelKey
import com.linagora.android.linshare.view.dialog.UploadProgressDialog
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
internal abstract class UploadFragmentModule {

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeUploadFragment(): UploadFragment

    @Binds
    @IntoMap
    @ViewModelKey(UploadFragmentViewModel::class)
    internal abstract fun bindUploadFragmentViewModel(viewModel: UploadFragmentViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeProgressUploadDialog(): UploadProgressDialog

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributePickDestinationDialog(): PickDestinationDialog
}
