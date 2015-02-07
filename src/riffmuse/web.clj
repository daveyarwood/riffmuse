(ns riffmuse.web
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer (file-response content-type)]
            [riffmuse.core :refer (riff-output invalid-scale
                                   help scale-choices)]))

(defroutes app-routes
  (GET "/" []
    (-> (file-response "index.html" {:root "public"})
        (content-type "text/html")))
  (POST "/riff" {:keys [params] :as request}
    (cond
      (re-find #"(?i)help|^h" (:scale params))
        help
      (re-find #"(?i)rand(om)?|^r$" (:scale params))
        (riff-output (rand-nth scale-choices))
      :else
        (try
          (riff-output (:scale params))
          (catch IllegalArgumentException e invalid-scale))))
  (route/not-found "404 - page not found"))

(def app
  (handler/site app-routes))
