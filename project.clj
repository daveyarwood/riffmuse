(defproject riffmuse "1.0.0"
  :description "A simple CLI tool to inspire sweet riffs"
  :url "http://www.github.com/daveyarwood/riffmuse"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [instaparse "1.3.2"]]
  :main riffmuse.core
  :aot [riffmuse.core])
