package pt.josemssilva.bucketlist.viewmodels.actions

sealed class BLSplashActions {
    object GoDoLogin: BLSplashActions()
    object GoHome: BLSplashActions()
}