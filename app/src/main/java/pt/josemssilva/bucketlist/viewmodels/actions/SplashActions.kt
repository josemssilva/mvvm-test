package pt.josemssilva.bucketlist.viewmodels.actions

sealed class SplashActions {
    object GoDoLogin: SplashActions()
    object GoHome: SplashActions()
}