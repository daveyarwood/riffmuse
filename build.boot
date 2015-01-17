#!/usr/bin/env boot

(set-env!
 :source-paths #{"src" "test"}
 :dependencies '[[org.clojure/clojure "1.6.0"]
                 [adzerk/bootlaces "0.1.9" :scope "test"]
                 [adzerk/boot-test "1.0.3" :scope "test"]
                 [instaparse "1.3.5"]
                 [trptcolin/versioneer "0.1.1"]
  							 [compojure               "1.3.1"]
                 [ring/ring-devel         "1.3.2" :scope "test"]
                 [ring/ring-jetty-adapter "1.3.2" :scope "test"]
                 [clj-http                "1.0.1"]])

(require '[adzerk.bootlaces :refer :all]
         '[adzerk.boot-test :refer :all]
         '[riffmuse.core])

(def +version+ "1.0.0")
(bootlaces! +version+)

(task-options!
  pom {:project 'riffmuse
       :version +version+
       :description "A simple CLI tool to inspire sweet riffs"
       :url "https://github.com/daveyarwood/riffmuse"
       :scm {:url "https://github.com/daveyarwood/riffmuse"}
       :license {:name "Eclipse Public License"
                 :url "http://www.eclipse.org/legal/epl-v10.html"}}
  test {:namespaces '#{}})

(deftask run
  "Run the riffmuse CLI client with args as a single string.
   This is analogous to running 'riffmuse args' with the executable."
  [a args ARGS str "The command-line arguments."]
  (riffmuse.core/-main args))

(deftask serve
  "Serve the web interface and dev REPL locally."
  []
  (comp
    (clojure.core/eval
      '(do (require
             '[riffmuse.web           :refer (app)]
             '[ring.adapter.jetty     :refer (run-jetty)]
             '[ring.middleware.reload :refer (wrap-reload)])
           (comp
             (with-pre-wrap fileset
               (run-jetty (wrap-reload #'app) {:port 3000 :join? false})
               fileset)
             (repl :server true :port 4005))))
    (wait)))

(defn -main [& args]
  (println "to do"))