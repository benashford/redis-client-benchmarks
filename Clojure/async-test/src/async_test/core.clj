(ns async-test.core
  (:require [redis-async.core :as ra]
            [redis-async.client :as client])
  (:gen-class))

(def ^:private N 1000000)

(defn -main []
  (let [pool (ra/make-pool {})]
    (dotimes [j 10]
      (let [start-time (System/nanoTime)
            last-c (last (map (fn [_]
                                (client/set pool "foo" "bar"))
                              (range N)))]
        (client/<!! last-c)
        (println (format "Took: %.2fms"
                         (/ (- (System/nanoTime)
                               start-time)
                            1000000.0)))))))
