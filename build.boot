#!/usr/bin/env boot

(set-env!
 :source-paths #{"src" "test"}
 :dependencies '[[org.clojure/clojure "1.6.0"]
                 [adzerk/bootlaces "0.1.9" :scope "test"]
                 [adzerk/boot-test "1.0.3" :scope "test"]
                 [pandeiro/boot-http "0.6.2"]
                 [instaparse "1.3.5"]
                 [trptcolin/versioneer "0.1.1"]
  							 [compojure               "1.3.1"]
                 [ring/ring-devel         "1.3.2" :scope "test"]
                 [ring/ring-jetty-adapter "1.3.2" :scope "test"]
                 [clj-http                "1.0.1"]])

(require '[adzerk.bootlaces :refer :all]
         '[adzerk.boot-test :refer :all]
         '[pandeiro.boot-http :refer :all])

(def +version+ "1.0.0")
(bootlaces! +version+)

(task-options!
  aot {:namespace '#{riffmuse.core}}
  pom {:project 'riffmuse
       :version +version+
       :description "A simple CLI tool to inspire sweet riffs"
       :url "https://github.com/daveyarwood/riffmuse"
       :scm {:url "https://github.com/daveyarwood/riffmuse"}
       :license {"name" "Eclipse Public License"
                 "url" "http://www.eclipse.org/legal/epl-v10.html"}}
  jar {:main 'riffmuse.core}
  test {:namespaces '#{}})

(deftask dev
  "Serve the web interface and dev REPL locally."
  []
  (comp (serve :handler 'riffmuse.web/app :reload true)
        (repl :server true :port 4005)
        (wait)))

(deftask build
  "Builds uberjar.
   TODO: be able to build an executable à la lein bin"
  []
  (comp (aot) (pom) (uber) (jar)))

(defn -main [& args]
  (require 'riffmuse.core)
  (apply (resolve 'riffmuse.core/-main) args))
