(defproject tigerfox "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  
  :dependencies [[com.badlogicgames.gdx/gdx "1.2.0"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.2.0"]
                 [com.badlogicgames.gdx/gdx-box2d "1.2.0"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.2.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.2.0"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.2.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.2.0"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/data.priority-map "0.0.5"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [org.clojure/algo.generic "0.1.2"]
                 [play-clj "0.3.8"]]
  
  :source-paths ["src" "src-common"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [tigerfox.core.desktop-launcher]
  :main tigerfox.core.desktop-launcher)
