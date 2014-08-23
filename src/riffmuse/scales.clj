(ns riffmuse.scales
  (:require [clojure.string :as str]))

(defn sanitize [pitch scale]
  "For the sake of simplicity, Riffmuse does not support the use of more than
   one accidental; e.g., double sharps and double flats. For this reason,
   scales like E# major (E# Fx Gx A# B# Cx Dx) are not supported. This function
   is used to ensure that such scales will be spelled enharmonically, e.g.
   E# major -> F major (F G A Bb C D E)."
  (get (condp contains? scale
         #{:major :major-pentatonic}
         {:d-sharp :e-flat, :e-sharp :f, :f-flat :e,
          :g-sharp :a-flat, :a-sharp :b-flat, :b-sharp :c}

         #{:minor :minor-pentatonic :blues}
         {:c-flat :b, :d-flat :c-sharp, :e-sharp :f, :f-flat :e,
          :g-flat :f-sharp, :a-flat :g-sharp, :b-sharp :c})
       pitch
       pitch))

(defn scale-name [{:keys [pitch scale]}]
  "Converts a scale (a map) to a human-readable string version of its name."
  (let [pitch (when pitch
                (str/replace (name (sanitize pitch scale)) #"-flat|-sharp"
                             {"-flat" "b" "-sharp" "#"}))
        scale (str/replace (name scale) #"-" " ")]
    (-> (str pitch \space scale) str/trim str/capitalize)))

(defmulti notes
  "Takes the scale (a map) output by parse-cli-args and returns a set of
   notes representing the appropriate scale."
   :scale)

(defmethod notes :chromatic [_]
  (set (mapcat #(map str (repeat (str/upper-case %)) [\b nil \#])
               [\A \B \C \D \E \F \G])))

(defmethod notes :octatonic-1 [_]
  #{"E" "F" "G" "Ab" "Bb" "B" "C#" "D"})

(defmethod notes :octatonic-2 [_]
  #{"E" "F#" "G" "A" "Bb" "C" "C#" "D#"})

(defn- circle-of-fifths [major-or-minor]
  (zipmap (case major-or-minor
            :major [:c-flat :g-flat :d-flat :a-flat :e-flat :b-flat :f
                    :c :g :d :a :e :b :f-sharp :c-sharp]
            :minor [:a-flat :e-flat :b-flat :f :c :g :d
                    :a :e :b :f-sharp :c-sharp :g-sharp :d-sharp :a-sharp])
          (range -7 8)))

(defn key-signature [pitch major-or-minor]
  (let [n ((circle-of-fifths major-or-minor) pitch)]
    (conj {}
          (when (pos? n) {:sharps (set (take n "FCGDAEB"))})
          (when (neg? n) {:flats  (set (take (Math/abs n) "BEADGCF"))}))))

(defn- diatonic-scale [pitch major-or-minor]
  (let [pitch          (sanitize pitch major-or-minor)
        letter->number (zipmap "abcdefg" (range 8))
        number->letter (zipmap (range 8) "ABCDEFG")
        base-number    (->> pitch name first letter->number)
        base-notes     (take 7 (map number->letter
                                    (iterate #(rem (inc %) 7) base-number)))
        {:keys [sharps flats]} (key-signature pitch major-or-minor)]
    (map (fn [letter]
           (str letter (when (and sharps (sharps letter)) \#)
                       (when (and flats (flats letter)) \b)))
         base-notes)))

(defmethod notes :major [{:keys [pitch]}]
  (set (diatonic-scale pitch :major)))

(defmethod notes :minor [{:keys [pitch]}]
  (set (diatonic-scale pitch :minor)))

(defn- pentatonic-scale [pitch major-or-minor]
  (let [pitch          (sanitize pitch major-or-minor)
        diatonic-scale (diatonic-scale pitch major-or-minor)
        scale-degree   (set (case major-or-minor
                              :major [0 1 2 4 5]
                              :minor [0 2 3 4 6]))]
    (keep-indexed #(when (scale-degree %1) %2) diatonic-scale)))

(defmethod notes :major-pentatonic [{:keys [pitch]}]
  (set (pentatonic-scale pitch :major)))

(defmethod notes :minor-pentatonic [{:keys [pitch]}]
  (set (pentatonic-scale pitch :minor)))

(defn- augment [pitch]
  "Takes a pitch in string form, e.g. 'A#', and returns the string form of the
   pitch one half-step higher, e.g. 'B.'"
  (let [letter->number (zipmap "ABCDEFG" (range 8))
        number->letter (zipmap (range 8) "ABCDEFG")
        base-number    (-> pitch first letter->number)
        next-letter    (number->letter (rem (inc base-number) 7))]
    (case (last pitch)
      \b (str (first pitch))
      \# (str next-letter)
      (str pitch \#))))

(defmethod notes :blues [{:keys [pitch]}]
  (let [minor-pentatonic (pentatonic-scale pitch :minor)
        extra-note       (augment (nth minor-pentatonic 2))]
    (set (concat (take 3 minor-pentatonic)
                 [extra-note]
                 (drop 3 minor-pentatonic)))))
