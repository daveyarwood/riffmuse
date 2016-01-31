(ns riffmuse.core
  (:require [clojure.string   :as    str]
            [riffmuse.parser  :refer (parse-scale)]
            [riffmuse.scales  :refer (scale-name notes)]
            [riffmuse.riff    :refer (generate-riff)]
            [riffmuse.ascii   :as    ascii]
            [riffmuse.version :refer (-version-)])
  (:gen-class))

(def header
  (let [ver-string  (str "Riffmuse v" -version-)
        dashes      (apply str (repeat (count ver-string) \-))]
    (str \newline ver-string \newline dashes \newline)))

(def help
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

Running 'riffmuse help' or 'riffmuse h' will display this help text.
")

(def scale-choices
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

(defn riff-output
  "Parses text argument, determines the scale and returns formatted output.
   Throws an IllegalArgumentException if the input is not valid."
  [scale-input]
  (let [scale (parse-scale scale-input)
        riff  (generate-riff (notes scale))]
    (format "Scale:\n\n%s\n\nNotes:\n\n%s\n\nGuitar:\n\n%s\n"
           (indent (scale-name scale))
           (indent (ascii/notes riff))
           (indent (ascii/guitar riff)))))

(def invalid-scale
  (format "\n***Invalid scale specified.***\n%s" help))

(defn -main
  "Parses command-line args and dispatches the appropriate functions.
   If no arguments are given, displays the help text."
  ([] (-main "help"))
  ([& args]
    (let [args-string (str/join \space args)]
      (cond
        (empty? (.trim args-string))               (-main "help")
        (re-find #"(?i)help|^h" args-string)       (println
                                                     (str header \newline help))
        (re-find #"(?i)rand(om)?|^r$" args-string) (-main (rand-nth scale-choices))
        (re-find #"(?i)version|^v" args-string)    (println
                                                     (str "Riffmuse v" -version-))
        :else (try
                (let [riff-output (riff-output args-string)]
                  (println header)
                  (println riff-output))
                (catch IllegalArgumentException e
                  (println invalid-scale)))))))
