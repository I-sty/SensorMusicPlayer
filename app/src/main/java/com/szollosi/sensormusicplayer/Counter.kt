package com.szollosi.sensormusicplayer

class MyCounter {

  @get:Synchronized
  var counter: Long = 0
    private set

  init {
    this.counter = 0
  }

  @Synchronized
  fun increment() {
    ++counter
  }
}
