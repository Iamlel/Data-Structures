10 constant DEFAULT_SIZE

: vector ( size -- addr )
    dup 1 <= if drop DEFAULT_SIZE then \ check if given size is valid
    dup 2 + cells allocate throw \ allocate size + 2 of cells 
    2dup ! \ set index 0 of arr address to size
    cell + dup cell + -rot \ get index 2 and leave it for the return address
    swap 1 + cells erase ; 

: v+ ( element addr -- addr )
    tuck cell - dup @ 1 + \ get length and add 1
    2dup swap ! \ set the new length
    2dup swap cell - @ \ get max length
    > if
        over cell - dup @ \ get max length again
        dup 1 rshift + \ get new max length
        2dup swap ! \ set the new max length
        tuck 2 + cells resize throw cell + \ resize the array
        dup 2over over - 1 + cells -rot cells + swap erase \ erase empty cells 
        nip dup rot cells + \ get next free index
        2swap drop swap ! \ append the element
        nip cell + \ get the return address
    else cells + ! then ;

: length ( addr -- length ) cell - @ ;
: v@ ( index addr -- value ) tuck length tuck + swap mod cells + @ ;
: max-length ( addr -- max-length) 2 cells - @ ;