### Algoritm greedy for the knappsack problem
```
function GREEDY-ALGORITHM(problem) returns a solution
    knappsacks <- m knappsacs with capacity W
    items <- n items with value p and weight w 
    state <- packlist, description of an item packed in a knappsack
    repeat
        items <-UPDATE-STATE(state, knappsacks, items)
        if items not empty 
            for each knappsack in random order do
                if knappsack kan hold item then
                    knappsack <- add
                    exit for            
    until all items picked or no items fits in knappsack   
```
****
```
function UPDATE-STATE(state,knappsacks,items) returns a list of items
    order items by value (p/w)
    return list
```



