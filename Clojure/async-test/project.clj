(defproject async-test "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [redis-async "0.3.1"]]
  :main async-test.core
  :aot [async-test.core])
