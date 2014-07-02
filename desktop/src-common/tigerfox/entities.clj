(ns tigerfox.entities
  (:require [play-clj.g2d :refer :all]
            [clojure.math.numeric-tower :as math]))

(defn player []
  (assoc (texture "player.png")
    :x 100
    :y 100
    :destination {:x 200 :y 400}
    :speed 5))

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

(defn move [entity]
  (let [[dx dy]  (move-vector entity)]
    (-> entity
        (update-in [:x] (partial + dx))
        (update-in [:y] (partial + dy)))))

(defn tick [entity]
  (move entity))
