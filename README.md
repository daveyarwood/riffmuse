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

## Installation

Riffmuse is intended to be installed as an executable file in the user's `$PATH` and run from the command line.

### Prerequisites

* [Leiningen](http://www.leiningen.org)
* [lein-bin](https://github.com/Raynes/lein-bin) to create the executable

*Alternatively, you can clone the source, cd to the project directory and use `lein run [scale name]`.*

1. Clone the source.

    git clone git@github.com:daveyarwood/riffmuse.git

2. Create the executable using lein-bin.

    cd riffmuse
    lein bin

3. This creates an executable file in the `target` directory called something like `riffmuse-1.0.0`. Move this file to `/usr/bin` or any other directory on your `$PATH`, and rename it to simply `riffmuse`.

    sudo mv target/riffmuse-1.0.0 /usr/bin/riffmuse

You should now be able to run `riffmuse` from the command line.

    > riffmuse e blues

    Riffmuse v1.0.0
    ---------------

    Scale:

        E blues

    Notes:

        1               2               3               4
        | D |   |   | E |   | D | D | E |   |   | G |   | D |   | G | G |

    Guitar:

        1               2               3               4
    A|--5-----------7-------5-------7----------10-------5-------------
    E|-------------------------10-------------------------------3---3-

## License

Copyright © 2014 Dave Yarwood

Distributed under the Eclipse Public License version 1.0
