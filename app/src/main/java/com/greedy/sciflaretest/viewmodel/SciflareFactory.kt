package com.greedy.sciflaretest.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greedy.sciflaretest.repository.SciflareRepo

class SciflareFactory(val app: Application, val sciflareRepo: SciflareRepo) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SciflareViewModel(app, sciflareRepo) as T
    }
}