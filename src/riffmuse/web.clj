(ns riffmuse.web
	(:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [riffmuse.core :as riffmuse]))

(defn- htmlify
  [stuff]
  (str "<html><body><pre><code>" stuff "</code></pre></body></html>"))

(defroutes app-routes
  (GET "/" []
    (htmlify
     (with-out-str (riffmuse/-main "E blues"))))
  (route/not-found "404 - page not found"))

(def app
  (handler/site app-routes))
