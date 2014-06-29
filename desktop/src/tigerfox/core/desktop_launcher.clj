(ns tigerfox.core.desktop-launcher
  (:require [tigerfox.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. tigerfox "tigerfox" 800 600)
  (Keyboard/enableRepeatEvents true))
