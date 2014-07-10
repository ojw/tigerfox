(ns tigerfox.entities
  (:require [play-clj.g2d :refer :all]
            [play-clj.core :refer :all]
            [play-clj.ui :refer :all]
            [clojure.math.numeric-tower :as math]))

(defn player []
  (assoc (texture "player.png")
    :x 600
    :y 200
    :destination {:x 600 :y 200}
    :speed 8))

(defn shift-x [{:keys [x] :as entity}]
  (assoc entity :x (- x 32)))
  

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

(defn test-layer [screen entity layer]
  (let [x (:x (screen->isometric screen entity))
        y (:y (screen->isometric screen entity))]
    (if-let [cell  (-> screen
                       (tiled-map-layer layer)
                       (tiled-map-cell x y))]
      (let [tile (.getTile cell)
            props (.getProperties tile)
            prop (.get props "blocking")]
        (if prop
          (zero? (Integer/parseInt prop))
          true))
      true)))

(defn test-position [screen entity]
  (every? (partial test-layer screen entity) (tiled-map! screen :get-layers)))
  ;(test-layer screen entity "walls"))

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
