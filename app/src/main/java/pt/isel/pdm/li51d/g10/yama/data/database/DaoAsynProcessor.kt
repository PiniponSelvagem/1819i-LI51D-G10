package pt.isel.pdm.li51d.g10.yama.data.database

import android.os.AsyncTask

abstract class DaoAsyncProcessor<T>(private val daoProcessCallback: DaoProcessCallback<T>?) {

    interface DaoProcessCallback<T> {
        fun onResult(result: T)
    }

    protected abstract fun doAsync(): T

    fun start() {
        DaoProcessAsyncTask().execute()
    }

    private inner class DaoProcessAsyncTask : AsyncTask<Void, Void, T>() {

        override fun doInBackground(vararg params: Void): T {
            return doAsync()
        }

        override fun onPostExecute(t: T) {
            daoProcessCallback?.onResult(t)
        }
    }
}