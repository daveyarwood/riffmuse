(ns riffmuse.web
  (:require cljsjs.jquery)) 

(defn get-output [input]
  (doto (.post js/jQuery 
               "/riff" 
               (str "scale=" input))
    (.success #(doto (js/jQuery ".output code")
                 (.html (str "> riffmuse " input "\n\n" %))))))

(js/jQuery 
  (fn []
    (get-output "help")
    (doto (js/jQuery ".controls input[type=submit]")
      (.click (fn [e] 
                (let [input (.val (js/jQuery "#scale"))]
                  (.preventDefault e)
                  (get-output input)))))))
