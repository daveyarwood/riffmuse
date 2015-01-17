(defproject riffmuse "1.0.0"
  :description "A simple CLI tool to inspire sweet riffs"
  :url "http://www.github.com/daveyarwood/riffmuse"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [instaparse "1.3.5"]
                 [trptcolin/versioneer "0.1.1"]]
  :aot [riffmuse.core]
  :main riffmuse.core)
