package com.zj.preview.images.image.easing

@Suppress("unused")
sealed class ScaleEffect(val easing: Easing) {
    object BACK : ScaleEffect(Back())
    object BOUNCE : ScaleEffect(Bounce())
    object CUBIC : ScaleEffect(Cubic())
    object Circle : ScaleEffect(Circle())
    object ELASTIC : ScaleEffect(Elastic())
    object EXPO : ScaleEffect(Expo())
    object LINEAR : ScaleEffect(Linear())
    object QUAD : ScaleEffect(Quad())
    object QUART : ScaleEffect(Quart())
    object QUINT : ScaleEffect(Quint())
    object SINE : ScaleEffect(Sine())
}
