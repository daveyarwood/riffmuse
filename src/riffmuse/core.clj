(ns riffmuse.core
  (:require [clojure.string :as str]
            [riffmuse.parser :refer (parse-scale)]
            [riffmuse.scales :refer (scale-name notes)]
            [riffmuse.riff :refer (generate-riff)]
            [riffmuse.ascii :as ascii])
  (:gen-class :main true))


(def ^:private help
"riffmuse is a command-line tool that algorithmically generates notes within a given scale, hopefully inspiring you to create a riff using those notes.

Simply pass the scale you'd like your riff in as command-line arguments. Input is not case-sensitive, and can be abbreviated in a variety of ways.

Examples:

    riffmuse a      (A major)
    riffmuse Db     (D-flat major)
    riffmuse Df     (D-flat major -- same as Db)
    riffmuse cs min (C-sharp minor)
    riffmuse o      (octatonic 1)
    riffmuse o2     (octatonic 2)
    riffmuse B pent (B major pentatonic)
    riffmuse em p   (E minor pentatonic)
    riffmuse x      (chromatic)
    riffmuse r      (scale chosen at random)
    
Or, if you'd prefer, you can type out the full name of the scale, for instance,

    riffmuse Bb minor
    riffmuse bb minor
    riffmuse B-flat minor
    riffmuse octatonic
    riffmuse f sharp blues
    riffmuse A pentatonic minor
    riffmuse g major pentatonic
    riffmuse random
    etc.
    
Running 'riffmuse help' or 'riffmuse h' will display this help text.")


(def ^:private scale-choices
  (let [letters (map str "abcdefg")
        notes   (concat letters
                        (map #(str % "-flat") letters)
                        (map #(str % "-sharp") letters))
        root    [" major" " minor" " major pentatonic" " minor pentatonic" " blues"]
        no-root ["octatonic 1" "octatonic 2" "chromatic"]]
    (concat (for [note notes, scale root] (str note scale))
            no-root)))

(defn- indent [s]
  "Indents each line of a string with a tab character."
  (->> s str/split-lines (map (partial str \tab)) (str/join \newline)))

(defn -main
  "Parses command-line args and dispatches the appropriate functions.
   If no arguments are given, displays the help text."
  ([] (-main "help"))
  ([& args]
    (let [args-string (str/join args)]
      (cond
        (re-find #"(?i)help|^h" args-string)       (println help)
        (re-find #"(?i)rand(om)?|^r$" args-string) (-main (rand-nth scale-choices))
        :else (try 
                (let [scale (parse-scale args-string)
                      riff  (generate-riff (notes scale))]
                  (printf "\nScale:\n\n%s\n\n" (indent (scale-name scale)))
                  (printf "Notes:\n\n%s\n\n" (indent (ascii/notes riff)))
                  (printf "Guitar:\n\n%s\n\n" (indent (ascii/guitar riff))))
                (catch IllegalArgumentException e
                  (printf "\nInvalid scale specified.\n\n%s\n\n" help)))))))
