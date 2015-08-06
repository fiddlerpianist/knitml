# General #
  * IDs follow the XML format for ID / IDREFs
    * Notably this means no spaces in IDs, and IDs cannot start with a number
  * Patterns are processed "top down", so IDREFs cannot refer to IDs that have not already been processed
  * Design pattern using KEL as much as possible, then publish the "validated XML"

# Needles #
  * How needle definitions affect the pattern
  * "Use Needle(s)" vs. "Using Needle"
  * "Arrange Stitches On Needles"

# Yarn #
  * How to define yarn
  * Multi-color knitting

# Row management #
  * Leaving row numbers blank
  * Specifying row number groupings
    * Can only be done within an instruction

# Instructions (a.k.a. "Block" instructions) #
  * Types (global vs local)
  * Why you should group your rows in instructions whenever possible
  * Guidelines for defining instructions
    * Smallest practical unit (leave out unnecessary repeats, defining those in a for-each-row-in-instruction later)
    * Specify the knitting shape (i.e. flat vs round) in any instructions defined in the header
    * Global instructions must have a label (a way to identify the instruction in the pattern)
  * Merged instructions

# Inline Instructions #

# Stitches #
  * A "Western" stitch mount is assumed for all stitches
    * Means that k2togs lean right, ssks lean left
    * "front" loop is leading loop, "back" is trailing loop
    * slip knitwise twists the stitch

# For Each Row in Instruction / Apply Next Row #