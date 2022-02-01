package com.udacity.loadapp.view


sealed class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}