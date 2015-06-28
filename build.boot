#!/usr/bin/env boot

(set-env!
 :source-paths #{"src" "test"}
 :resource-paths #{"resources"}
 :dependencies '[[org.clojure/clojure       "1.7.0-RC1"]
                 [adzerk/bootlaces          "0.1.11" :scope "test"]
                 [adzerk/boot-test          "1.0.4"  :scope "test"]
                 [adzerk/boot-reload        "0.3.1"]
                 [org.clojure/clojurescript "0.0-3308"]
                 [adzerk/boot-cljs          "0.0-3308-0"]
                 [pandeiro/boot-http        "0.6.2"]
                 [instaparse                "1.4.1"]
                 [trptcolin/versioneer      "0.2.0"]
                 [compojure                 "1.3.4"]
                 [ring/ring-core            "1.3.2"  :scope "test"]
                 [clj-http                  "1.1.2"]])

(require '[adzerk.bootlaces   :refer :all]
         '[adzerk.boot-test   :refer :all]
         '[adzerk.boot-cljs   :refer [cljs]]
         '[adzerk.boot-reload :refer [reload]]
         '[pandeiro.boot-http :refer :all]
         '[riffmuse.core])

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
  jar {:main 'riffmuse.core})

(deftask dev
  "Serve the web interface and dev REPL locally.
   Reload on changes."
  []
  (comp (watch)
        (speak)
        (cljs :optimizations :none 
              :source-map true
              :compiler-options {:output-to  "public/main.js"
                                 :asset-path "out"})
        (reload :asset-path "public")
        (repl :server true :port 4005)
        (serve :handler 'riffmuse.web/app)))

(deftask build
  "Builds uberjar.
   TODO: be able to build an executable à la lein bin"
  []
  (comp (aot) (pom) (uber) (jar)))

(defn -main [& args]
  (binding [riffmuse.core/*version* +version+] 
    (apply (resolve 'riffmuse.core/-main) args)))
