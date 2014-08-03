(ns riffmuse.riff)

(defn- generate-melody [notes]
  "Generates a sequence of 16 notes selected randomly from a set of notes."
  (repeatedly 16 #(rand-nth (seq notes))))

(defn- generate-rhythm []
  "Generates a sequence of 16 :on's and :off's, representing whether or not
   a note is present on that 1/16th beat of a measure. The probability of a
   note being present in a given slot is weighted, to increase the likelihood
   of a less syncopated rhythm."
  (let [probabilities '(0.85 0.30 0.35 0.30, 0.60 0.30 0.35 0.30, 
                        0.70 0.30 0.35 0.30, 0.60 0.30 0.35 0.30)]
    (map #(if (>= % (rand)) :on :off) probabilities)))

(defn generate-riff [notes]
  "Takes a set of notes and algorithmically generates a riff.
   Returns a riff in the form of a list of 16 things that are either 
   a note (as a string like 'Bb') or nil."
  (map #(when (= % :on) (rand-nth (seq notes))) (generate-rhythm)))
