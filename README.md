# riffmuse

Riffmuse is a simple CLI tool designed to inspire sweet riffs by algorithmically generating notes within a given scale.

## Usage

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
    
Running 'riffmuse help' or 'riffmuse h' will display these examples.

## License

Copyright © 2014 Dave Yarwood

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
