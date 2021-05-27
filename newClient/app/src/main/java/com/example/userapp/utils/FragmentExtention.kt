package com.example.userapp.utils

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController


/*
inline fun <reified VM : ViewModel> Fragment.sharedGraphViewModel(
    @IdRes navGraphId: Int,
    bundle: Bundle = Bundle(),
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy {
    getKoin().getViewModel(ViewModelParameter(VM::class,  qualifier, parameters, bundle, findNavController().getViewModelStoreOwner(navGraphId).viewModelStore))
}
*/
