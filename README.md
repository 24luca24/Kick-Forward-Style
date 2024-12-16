# Kick-Forward-Style
Program that count frequency of the word, given a text and the number of most frequency word to retrieve

Variation of the Pipeline style, with the following additional constraints:
    - Each function takes an additional parameter, usually the last, which is another function.
    - The function parameter is applied at the end of the current function.
    - The function parameter is given, as input, what would be the output of the current function.
    - The larger problem is solved as a pipeline of functions, but where the next function to be applied is given as a parameter to the current function.

