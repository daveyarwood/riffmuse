(ns riffmuse.ascii
  (:require [clojure.string :as str]
            [riffmuse.seq-utils :refer (positions)]))

(defn notes [riff]
  "Returns ASCII representation of riff, giving note names like 'C#'."
  (let [result (str "| " (str/join " | " (map #(or % \space) riff)) " |")
        beat-indices (drop-last 
                       (keep-indexed #(when (zero? (rem (+ %1 4) 4)) %2)
                                      (positions #{\|} result)))
        index->beat (zipmap beat-indices (iterate inc 1))
        beat-line (apply str (map-indexed #(if ((set beat-indices) %1) 
                                             (index->beat %1) 
                                             %2)
                                          (repeat (inc (last beat-indices)) 
                                                  \space)))]
    (str/join \newline beat-line result)))

(defn guitar [riff]
  "Returns ASCII representation of riff, giving notes in guitar tab format.
   Each note is represented as a root note on the E or A string, with 
   equal likelihood of either string being chosen.")
