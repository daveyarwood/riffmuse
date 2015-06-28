(ns riffmuse.web
  (:require cljsjs.jquery)) 

(defn get-output [scale-input]
  (doto (.post js/jQuery 
               "/riff" 
               (str "scale=" scale-input))
    (.success #(doto (js/jQuery ".output code")
                 (.html %)))))

(js/jQuery 
  (fn []
    (get-output "help")
    (doto (js/jQuery ".controls input[type=submit]")
      (.click (fn [e] 
                (let [scale-input (.val (js/jQuery "#scale"))]
                  (.preventDefault e)
                  (get-output scale-input)))))))
