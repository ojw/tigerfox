(ns tigerfox.core
  (:require [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.math :refer :all]
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

(defn add-down-key [screen key]
  (let [down-keys (:down-keys screen)
        new-down-keys (conj down-keys key)]
    (update! screen :down-keys new-down-keys)))

(defn remove-down-key [screen key]
  (let [down-keys (:down-keys screen)
        new-down-keys (disj down-keys key)]
    (update! screen :down-keys new-down-keys)))

(defn process-keys [screen]
  (let [keys (:down-keys screen)]
    (let [position (position screen)
          old-x (x screen)
          old-y (y screen)
          old-z (z screen)]
      (if (contains? keys (key-code :w)) (y! screen (+ old-y 10)))
      (if (contains? keys (key-code :a)) (x! screen (- old-x 10)))
      (if (contains? keys (key-code :s)) (y! screen (- old-y 10)))
      (if (contains? keys (key-code :d)) (x! screen (+ old-x 10)))
      nil)))

(defscreen main-screen

  :on-show
  (fn [screen entities]
    (update! screen 
             :renderer (isometric-tiled-map "map.tmx" 1.5)
             :camera (orthographic :translate (vector-3 100 100 0))
             :down-keys #{})
    [(pm/priority-map-keyfn :y :player (player))])
  
  :on-render
  (fn [screen [entities]]
    (let [new-entities (fmap (partial tick screen) entities)
          player (:player entities)]
      (clear!)
      (render-entity-map! screen new-entities)
      (run! informational :on-update-position :entity player)
      (process-keys screen)
      [new-entities]))

  :on-resize
  (fn [screen entities]
    (height! screen 600))

  :on-touch-down
  (fn [{:keys [input-x input-y] :as screen} [entities]]
    (let [{:keys [x y]} (input->screen screen input-x input-y)
          new-entities (assoc-in entities [:player :destination] {:x x :y y})]
      [new-entities]))

  :on-key-down
  (fn [screen entities]
    (add-down-key screen (:key screen))
    nil)

  :on-key-up
  (fn [screen entities]
    (remove-down-key screen (:key screen))
    nil)
)


(defgame tigerfox
  :on-create
  (fn [this]
    (set-screen! this main-screen informational)))
