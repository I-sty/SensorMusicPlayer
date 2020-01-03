package com.szollosi.sensormusicplayer

class Counter {

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
