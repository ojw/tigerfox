(ns tigerfox.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]
            [tigerfox.entities :refer :all]
            [clojure.data.priority-map :as pm]
            [clojure.algo.generic.functor :refer :all]))

(defn render-entity-map! [screen entity-map]
  (let [entities (vals entity-map)]
    (render! screen entities)
    nil))

;(defn map-entities [f [entity-map & stuff]]
; (

(defscreen main-screen

  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    [(pm/priority-map-keyfn :y :player (player))])
  
  :on-render
  (fn [screen [entities]]
    (let [new-entities (fmap tick entities)]
      (clear!)
      (render-entity-map! screen new-entities)
      [new-entities]))

  :on-touch-down
  (fn [{:keys [input-x input-y] :as screen} [entities]]
    (let [{:keys [x y]} (input->screen screen input-x input-y)
          new-entities (assoc-in entities [:player :destination] {:x x :y y})]
      [new-entities])))

(defgame tigerfox
  :on-create
  (fn [this]
    (set-screen! this main-screen)))
