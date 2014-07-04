(ns tigerfox.entities
  (:require [play-clj.g2d :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [clojure.math.numeric-tower :as math]))

(defn player []
  (assoc (texture "player.png")
    :x 100
    :y 100
    :destination {:x 100 :y 100}
    :speed 8))

(defn move-vector [{:keys [x y destination speed] :as entity}]
  (let [dest-x (:x destination)
        dest-y (:y destination)
        dif-x (- dest-x x)
        dif-y (- dest-y y)
        hypotenuse (math/sqrt (+ (math/expt dif-x 2)
                                 (math/expt dif-y 2)))]
    (if (< speed hypotenuse)
      (let [dx (double (* speed (/ dif-x hypotenuse)))
            dy (double (* speed (/ dif-y hypotenuse)))]
        [dx dy])
      [0 0])))

(defn test-position [screen entity]
  (if-let [cell  (-> screen
                     (tiled-map-layer "Tile Layer 1")
                     (tiled-map-cell (:x (screen->isometric screen entity)) (:y (screen->isometric screen entity))))]
    (let [tile (.getTile cell)
          props (.getProperties tile)
          prop (.get props "blocking")]
      (if prop
        (zero? (Integer/parseInt prop))
        true))
    true))

(defn move [screen entity]
  (let [[dx dy]  (move-vector entity)
        new-entity (-> entity
                       (update-in [:x] (partial + dx))
                       (update-in [:y] (partial + dy)))]
    (if (test-position screen new-entity)
      new-entity
      entity)))

(defn tick [screen entity]
  (move screen entity))
