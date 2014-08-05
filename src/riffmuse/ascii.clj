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
    (str/join \newline [beat-line result])))

(def note->number
  {"Cb" 11, "C" 0, "C#" 1, "Db" 1, "D" 2, "D#" 3, "Eb" 3, "E" 4, "E#" 5,
   "Fb" 4, "F" 5, "F#" 6, "Gb" 6, "G" 7, "G#" 8, "Ab" 8, "A" 9, "A#" 10,
   "Bb" 10, "B" 11, "B#" 0})

(defn- fret [note string]
  "Returns the fret number of the given note on the given string (:a or :e),
   as a string with hyphen-padding for an ASCII tab, e.g. '--7-', '-11-'"
  (let [note-number (note->number note)
        fret-number (rem (+ note-number (case string :a 3 :e 8)) 12)
        octave-up?  (and (= fret-number 0) (< (rand) 0.5))]
    (cond
      octave-up?          "-12-"
      (>= fret-number 10) (format "-%d-" fret-number)
      :else               (format "--%d-" fret-number))))

(defn guitar [riff]
  "Returns ASCII representation of riff, giving notes in guitar tab format.
   Each note is represented as a root note on the E or A string, with 
   equal likelihood of either string being chosen."
  (let [positions  (map #(when % 
                          (let [string (rand-nth [:e :a])]
                            {:string string, :fret (fret % string)}))
                        riff)
        tab-note   (fn [note string] 
                     (if (= (:string note) string) (:fret note) "----"))
        tab-string (fn [notes string] 
                     (apply str (map #(tab-note % string) notes)))
        a-string   (str "A|" (tab-string positions :a))
        e-string   (str "E|" (tab-string positions :e))
        beat-line  "to do..."]
    ,,,))
