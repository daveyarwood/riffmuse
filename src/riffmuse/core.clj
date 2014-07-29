(ns riffmuse.core
  (:require [clojure.string :as s]
            [riffmuse.parser :refer (parse-scale)]
            [riffmuse.scales :refer (notes)]
            [riffmuse.riff :as riff])
  (:gen-class :main true))

(def ^:private help
"riffmuse is a command-line tool that algorithmically generates notes within a given scale, hopefully inspiring you to create a riff using those notes.

It takes only one command-line argument, representing the scale you'd like your riff in.

Examples:

    riffmuse a      (A minor)
    riffmuse A      (A major)
    riffmuse Db     (D-flat major)
    riffmuse Df     (D-flat major -- same as Db)
    riffmuse cs     (C-sharp minor)
    riffmuse o      (octatonic 1)
    riffmuse o2     (octatonic 2)
    riffmuse B pent (B major pentatonic)
    riffmuse em p   (E minor pentatonic)
    riffmuse x      (chromatic)
    
Or, if you'd prefer, you can type out the full name of the scale, for instance,

    riffmuse Bb minor
    riffmuse bb minor
    riffmuse B-flat minor
    riffmuse octatonic
    riffmuse f sharp blues
    riffmuse A pentatonic minor
    riffmuse g major pentatonic
    etc.
    
Running 'riffmuse help' or 'riffmuse h' will bring up this help text.")

(defn -main
  "Parses command-line args and dispatches the appropriate functions.
   If no arguments are given, displays the help text."
  ([] (-main "help"))
  ([& args]
    (let [args-string (s/join args)]
      (if (re-find #"(?i)help|^h" args-string)
        (println help)
        (notes (parse-scale args-string))))))
