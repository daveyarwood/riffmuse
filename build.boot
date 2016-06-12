#!/usr/bin/env boot

(set-env!
 :source-paths   #{"src" "test"}
 :resource-paths #{"resources"}
 :dependencies   '[[org.clojure/clojure "1.7.0"]
                   [adzerk/bootlaces    "0.1.13" :scope "test"]
                   [adzerk/boot-jar2bin "1.0.0"  :scope "test"]
                   [adzerk/boot-test    "1.1.0"  :scope "test"]
                   [instaparse          "1.4.1"]])

(def web-deps
  '[[adzerk/boot-reload        "0.4.8"]
    [org.clojure/clojurescript "0.0-3308"]
    [adzerk/boot-cljs          "0.0-3308-0"]
    [pandeiro/boot-http        "0.7.0"]
    [compojure                 "1.4.0"]
    [ring/ring-core            "1.4.0"  :scope "test"]
    [clj-http                  "2.0.0"]
    [cljsjs/jquery             "2.1.4-0"]])

(require '[adzerk.bootlaces    :refer :all]
         '[adzerk.boot-jar2bin :refer :all]
         '[adzerk.boot-test    :refer :all]
         '[riffmuse.core]
         '[riffmuse.version    :refer (-version-)])

(bootlaces! -version-)

(task-options!
  aot    {:namespace '#{riffmuse.core}}
  pom    {:project 'riffmuse
          :version -version-
          :description "A simple CLI tool to inspire sweet riffs"
          :url "https://github.com/daveyarwood/riffmuse"
          :scm {:url "https://github.com/daveyarwood/riffmuse"}
          :license {"name" "Eclipse Public License"
                    "url" "http://www.eclipse.org/legal/epl-v10.html"}}
  jar    {:file "riffmuse.jar"
          :main 'riffmuse.core}
  target {:dir #{"target"}})

(defn add-web-deps!
  []
  (merge-env! :dependencies web-deps))

(deftask dev
  "Serve the web interface and dev REPL locally.
   Reload on changes."
  []
  (add-web-deps!)
  (require '[adzerk.boot-cljs]
           '[adzerk.boot-reload]
           '[pandeiro.boot-http])
  (let [cljs   (resolve 'adzerk.boot-cljs/cljs)
        reload (resolve 'adzerk.boot-reload/reload)
        serve  (resolve 'pandeiro.boot-http/serve)]
    (comp
      (watch)
      (speak)
      (cljs :optimizations :none
            :source-map true
            :compiler-options {:output-to  "public/main.js"
                               :asset-path "out"})
      (reload :asset-path "public")
      (repl :server true :port 4005)
      (serve :handler 'riffmuse.web/app))))

(deftask build
  "Builds uberjar and executables."
  []
  (comp
    (aot)
    (pom)
    (uber)
    (jar)
    (bin :output-dir "bin")))

(defn -main [& args]
  (apply (resolve 'riffmuse.core/-main) args))
