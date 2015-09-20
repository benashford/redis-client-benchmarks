(ns car-test.core
  (:require [taoensso.carmine :as car])
  (:gen-class))

(def ^:private N 1000000)

(defmacro wcar* [& body] `(car/wcar {:pool {} :spec {}} ~@body))

(defn -main []
  (dotimes [j 10]
    (let [start-time (System/nanoTime)]
      (wcar*
       (last (map (fn [_]
                    (car/set "foo" "bar"))
                  (range N))))
      (println (format "Took: %.2fms"
                       (/ (- (System/nanoTime)
                             start-time)
                          1000000.0))))))
