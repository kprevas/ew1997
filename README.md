This script solves the problem discussed on 
[Effectively Wild episode 1997](https://blogs.fangraphs.com/effectively-wild-episode-1997-this-time-it-pitch-counts/):
what's the longest streak of games where two teams experienced the same sequence
of wins and losses?

The script reads [Retrosheet game logs](https://www.retrosheet.org/gamelogs/glfields.txt) data,
and accepts two arguments to limit the minimum and maximum size of spans to consider,
although this does not prove necessary in practice: on the full Retrosheet data set, it completes
in about 19 seconds on a Mac M1 Pro.

### Results

```
35218567 total spans
Longest match: 36 games - [[1890 PIT games 78-113, 1875 BR2 games 1-36]]
Total execution time 18924ms
```