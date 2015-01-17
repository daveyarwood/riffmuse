(ns riffmuse.parser
  (:require [clojure.string :as str]
            [instaparse.core :as insta]))

(defn parse-scale [input]
  "Takes CLI arg string and returns a map representing a specific scale,
   e.g. {:type :root, :pitch :e-flat, :scale :blues}
        {:type :no-root, :scale :chromatic}"
  (->> input
       (insta/parse (insta/parser "
         scale      = root | no-root

         root       = pitch <ows> r-scale?
         <r-scale>  = (major | minor)
                    | pentatonic
                    | (major | minor) <ows> pentatonic
                    | pentatonic <ows> (major | minor)
                    | blues

         major      = #'(?i)maj(or)?'
         minor      = #'(?i)m(in(or)?)?'
         pentatonic = #'(?i)p(ent(a(tonic)?)?)?'
         blues      = #'(?i)blues'
         pitch      = letter <ows> (sharp | flat | <natural>)?
         letter     = #'[A-Ga-g]'
         sharp      = #'(?i)s(harp)?|#'
         flat       = #'(?i)f(lat)?' | #'[Bb]'
         <natural>  = #'(?i)n(at(ural)?)?'

         no-root    = octatonic | chromatic
         octatonic  = #'(?i)o(ct(a(tonic)?)?)?' <ows> #'[1-2]'?
         chromatic  = #'(?i)chr(om(atic)?)?' | #'[Xx]'

         <ows>     = #'[\\s-_]*'
         "))
       (insta/transform
         {:flat (constantly "-flat")
          :sharp (constantly "-sharp")
          :major (constantly :major)
          :minor (constantly :minor)
          :pentatonic (constantly :pentatonic)
          :blues (constantly :blues)
          :chromatic (constantly :chromatic)
          :octatonic (fn ([_] :octatonic-1)
                         ([_ num] (case num
                                    "1" :octatonic-1
                                    "2" :octatonic-2)))
          :letter str/lower-case
          :pitch (fn ([letter]
                       {:pitch (keyword letter)})
                     ([letter accidental]
                       {:pitch (keyword (str letter accidental))}))
          :scale (fn [[type & attrs]]
                   (conj {:type type}
                         (if-let [pitch (:pitch (first attrs))]
                           (let [attrs (rest attrs)]
                             {:pitch pitch
                              :scale (case (count attrs)
                                       0 :major
                                       1 (if (= (first attrs) :pentatonic)
                                           :major-pentatonic
                                           (first attrs))
                                       2 (case (set attrs)
                                           #{:minor :pentatonic} :minor-pentatonic
                                           #{:major :pentatonic} :major-pentatonic))})
                           {:scale (first attrs)})))})))
