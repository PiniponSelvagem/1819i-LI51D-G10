package pt.isel.pdm.li51d.g10.yama.utils

import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import pt.isel.pdm.li51d.g10.yama.YamaApplication
import pt.isel.pdm.li51d.g10.yama.data.Repository

val Application.repository: Repository
    get() = (this as YamaApplication).repository

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): T =
        ViewModelProviders.of(this, ViewModelFactory(this.application.repository)).get(T::class.java)

class ViewModelFactory(val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository::class.java).newInstance(repository)
    }
}