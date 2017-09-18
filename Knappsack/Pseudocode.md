### Algorithm greedy for the knapsack problem
```
function GREEDY-ALGORITHM(problem) returns a solution
    knappsacks <- m knapsacks with capacity W
    items <- n items with value p and weight w 
    state <- packlist, description of an item packed in a knapsack
    repeat
        items <-UPDATE-STATE(state, knapsacks, items)
        if items not empty 
            for each knapsack in random order do
                if knapsack kan hold item then
                    knapsack <- add
                    exit for            
    until all items picked or no items fits in knapsack   
```
****
```
function UPDATE-STATE(state,knapsacks,items) returns a list of items
    order items by value (p/w)
    return list
```



