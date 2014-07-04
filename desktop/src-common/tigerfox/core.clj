(ns tigerfox.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]
            [tigerfox.entities :refer :all]
            [clojure.data.priority-map :as pm]
            [clojure.algo.generic.functor :refer :all]))

(declare main-screen informational)

(defn render-entity-map! [screen entity-map]
  (->> entity-map
       vals
       (render-sorted! screen ["Tile Layer 1"])))

(defscreen informational

  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    [(label "FOO" (color :white))])
  
  :on-render
  (fn [screen entities]
    (render! screen entities))
  
  :on-resize
  (fn [screen entities]
    (height! screen 600))

  :on-update-position
  (fn [{:keys [entity] :as screen} entities]
    (label (str (int (:x entity)) ", " (int (:y entity))) (color :blue))))

(defscreen main-screen

  :on-show
  (fn [screen entities]
    (update! screen 
             :renderer (isometric-tiled-map "map.tmx" 1)
             :camera (orthographic))
    [(pm/priority-map-keyfn :y :player (player))])
  
  :on-render
  (fn [screen [entities]]
    (let [new-entities (fmap (partial tick screen) entities)
          player (:player entities)]
      (clear!)
      (render-entity-map! screen new-entities)
      (run! informational :on-update-position :entity player)
      [new-entities]))

  :on-resize
  (fn [screen entities]
    (height! screen 600))

  :on-touch-down
  (fn [{:keys [input-x input-y] :as screen} [entities]]
    (let [{:keys [x y]} (input->screen screen input-x input-y)
          new-entities (assoc-in entities [:player :destination] {:x x :y y})]
      [new-entities])))

(defgame tigerfox
  :on-create
  (fn [this]
    (set-screen! this main-screen informational)))
