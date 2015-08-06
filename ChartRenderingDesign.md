# Introduction #
We need an effective yet easy-to-manage and extensible way to render charts from KnitML. There may be specification changes involved to support chart round-tripping. The preliminary design will work from a simple increase/decrease pattern with dynamic repeating sections.

# Details #

Pass 1: Walk through instruction's elements to determine if it is chartable.
  * Save the original state of the engine so that we can revert if the instruction is unchartable. (Requires an engine enhancement)
  * Expand context-sensitive instructions and repeats (i.e. make all repeats have the 'times' option and expand inline-instruction-refs)
  * Gather a maximum chart width.
  * For now, make any instruction with turns unchartable.
  * For now, any instruction which ends with a different number of stitches than it started with cannot be repeated with a chart. The only reason this would occur is if context-sensitive repeats were used in the instruction. If the starting number of stitches is not equal to the starting number of stitches when the chart was rendered, the result must be rendered textually.
  * If unchartable, revert to basic text renderer and revert engine back to original state.

Pass 2:
Build an in-memory chart
  * Chart should start "centered" for the first row and then shrink according to the way each decrease leans
  * Chart should use "logical" representations of operations
  * Then produce a translation from internal syntax to a character syntax which is the target. By default provide one of the Unicode symbols from the Knitting Universe font set.
  * The engine is used to determine whether the chart represents knitting flat or in-the-round, unless it's from the instruction definition section, in which case it must be specified on the instruction object itself
  * Use a separate ChartWriter with a symbol set to actually produce the real chart